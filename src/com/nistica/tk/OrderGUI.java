package com.nistica.tk;

import javax.swing.*;
import javax.swing.plaf.metal.MetalComboBoxButton;

import com.nistica.tk.MenuItem.*;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@SuppressWarnings("unused")
public class OrderGUI {

	public static JFrame frame;
	public static JScrollPane menuPane, cartPane;
	public static JSplitPane menuCartPane, bigPane;
	public static JPanel controlsPanel, newMenuItem;
	public static MenuPanel menuItemHolder;
	public static CartPanel cartItemHolder;
	public static OrderPanel orderItemHolder;
	
	public static int numberOfMenuItems;
	
	public static Color menuColor = new Color(230,242,242);
	public static Color controlColor = new Color(47,211,214);

	//Database variables
	public static final String framework = "embedded";
	public static final String driver = "org.apache.derby.jdbc.EmbeddedDriver";
	public static final String protocol = "jdbc:derby:";
	
	public OrderGUI() {
	}

	private static void createAndShowGUI() {
		// Create the new JFrame object
		frame = new JFrame("Nistica Thai Kitchen Ordering System");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension splitSize = new Dimension(519, 750);
		
		// Create all the menu item objects
		menuItemHolder = new MenuPanel(numberOfMenuItems);
		menuItemHolder.setLayout(new BoxLayout(menuItemHolder, BoxLayout.PAGE_AXIS));
		/*for (int i = 0; i < numberOfMenuItems; i++) {
			newMenuItem = new MenuItem(Item.values()[i]);
			menuItemHolder.add(newMenuItem);
		}*/
		menuItemHolder.setBackground(menuColor);
		//Initialize the Cart Panel
		cartItemHolder = new CartPanel(numberOfMenuItems);
		cartItemHolder.setLayout(new BoxLayout(cartItemHolder, BoxLayout.PAGE_AXIS));
		cartItemHolder.setBackground(menuColor);
		
		loadMenu();
		// Create a new JSplitPane with the two halves as the menu and cart
		menuPane = new JScrollPane(menuItemHolder, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		menuPane.setPreferredSize(splitSize);
		cartPane = new JScrollPane(cartItemHolder, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		cartPane.setPreferredSize(splitSize);
		menuCartPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, menuPane, cartPane);
		menuCartPane.setDividerLocation(525);
		menuCartPane.setEnabled(false);
		//Create the controls panel at the top of the GUI
		controlsPanel = new JPanel();
		SpringLayout springControls = new SpringLayout();
		controlsPanel.setLayout(springControls);
		controlsPanel.setBackground(controlColor);
		//add all of the control buttons here, such as order, clear cart, help, change order (maybe), etc
		//the help button will create a new popup dialog box telling the user all the controls for using the program
		//the order button will prompt the user (possibly with a dialog box, but maybe with JOptionPane) if they want to submit
		//the clear cart button will do the same as above but with a clear message
		//the change order button will probably be tricky.  if possible: this button would go to the database and access the order
		//that corresponds to the user that prompted the access.  If there is an order found, then the session is "reopened" so the
		//user can change the order and resubmit it.  If there is no saved order, then a dialog box will open up informing the user
		//of this.
		//any other buttons that I can think of will be added as necessary in the best possible location
		JButton helpButton = new JButton("Help");
		helpButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// display/center the jdialog when the button is pressed
				JDialog d = new JDialog(frame, "Help", true);
				JPanel helpPanel = new JPanel();
				JTextArea helpInfo = new JTextArea(
						"Help on using the Ordering System:\nTo add an item to your cart (right side), double click an item on the menu (left side).\nYou can " +
			    		"adjust the parameters of your order at any time, on either side of the screen, but the parameters in your cart are the ones that will " +
			    		"be saved.\nTo remove an item from your cart, simply double click on it.\nIf you double click an item on the menu that is already in " + 
			    		"your cart, the item in the cart will be overwritten.\nWhen you are ready to submit your order, hit the order button to review your order.\n" +
			    		"If you are happy with your order, click the send button to submit your order.  If you are not, simply close the window and make any changes " +
			    		"you wish to make.\nYou can also hit the clear button to clear out your entire cart and start from the beginning.\n\nThank you for using the " +
			    		"Nistica Thai Kitchen Ordering System!"
			    		);
				helpInfo.setLineWrap(true);
				helpInfo.setWrapStyleWord(true);
				helpInfo.setEditable(false);
				helpInfo.setPreferredSize(new Dimension(395,290));
				helpInfo.setBackground(menuColor);
				helpInfo.setForeground(Color.BLACK);
				helpInfo.setVisible(true);
			 	helpPanel.add(helpInfo);
			 	helpPanel.setPreferredSize(new Dimension(405,300));
			 	helpPanel.setBackground(menuColor);
			 	helpPanel.setVisible(true);
			 	d.add(helpPanel);
			 	d.setLocation(new Point(325,200));
			 	d.pack();
			 	d.setVisible(true);
			}
		});
		springControls.putConstraint(SpringLayout.NORTH, helpButton, 5, SpringLayout.NORTH, controlsPanel);
		springControls.putConstraint(SpringLayout.WEST, helpButton, 5, SpringLayout.WEST, controlsPanel);
		controlsPanel.add(helpButton);
		JButton orderButton = new JButton("Order");
		orderButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JDialog orderDialog = new JDialog(frame, "Your Order", true);
				orderItemHolder = new OrderPanel();
				orderItemHolder.getNumComp(cartItemHolder.getComponents().length);
				orderSender();
				orderItemHolder.setVisible(true);
				orderItemHolder.setLayout(new BoxLayout(orderItemHolder, BoxLayout.PAGE_AXIS));
				orderItemHolder.setBackground(menuColor);
				JScrollPane orderPane = new JScrollPane(orderItemHolder, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
				orderPane.setPreferredSize(new Dimension(525,661));
				orderDialog.add(orderPane);
			 	orderPane.setVisible(true);
			 	orderDialog.setLocation(new Point(325,100));
			 	orderDialog.pack();
			 	orderDialog.setVisible(true);
			}
		});
		springControls.putConstraint(SpringLayout.NORTH, orderButton, 5, SpringLayout.NORTH, controlsPanel);
		springControls.putConstraint(SpringLayout.WEST, orderButton, 15, SpringLayout.EAST, helpButton);
		controlsPanel.add(orderButton);
		JButton clearCart = new JButton("Clear");
		clearCart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				int answer = JOptionPane.showConfirmDialog(clearCart, "Are you sure you want to clear your cart?","Clear Cart?",JOptionPane.YES_NO_OPTION);
				if (answer == JOptionPane.YES_OPTION){
					int i;
					int numItems = cartItemHolder.getComponents().length;
					for (i=0;i<numItems;i++) {
						cartItemHolder.remove(cartItemHolder.getComponents()[0]);
						cartItemHolder.repaint();
						cartItemHolder.revalidate();
					}
				}
			}
		});
		springControls.putConstraint(SpringLayout.NORTH, clearCart, 5, SpringLayout.NORTH, controlsPanel);
		springControls.putConstraint(SpringLayout.WEST, clearCart, 15, SpringLayout.EAST, orderButton);
		controlsPanel.add(clearCart);
		JLabel menuLabel = new JLabel("MENU");
		springControls.putConstraint(SpringLayout.SOUTH, menuLabel, 5, SpringLayout.SOUTH, controlsPanel);
		springControls.putConstraint(SpringLayout.WEST, menuLabel, 200, SpringLayout.WEST, controlsPanel);
		menuLabel.setFont(new Font("Serif", Font.PLAIN, 32));
		menuLabel.setForeground(Color.BLACK);
		controlsPanel.add(menuLabel);
		JLabel cartLabel = new JLabel("CART");
		springControls.putConstraint(SpringLayout.SOUTH, cartLabel, 5, SpringLayout.SOUTH, controlsPanel);
		springControls.putConstraint(SpringLayout.EAST, cartLabel, -230, SpringLayout.EAST, controlsPanel);
		cartLabel.setFont(new Font("Serif", Font.PLAIN, 32));
		cartLabel.setForeground(Color.BLACK);
		controlsPanel.add(cartLabel);
		//Create the Large JSplitPane tha contains the menu, the cart, and the control panel
		bigPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, controlsPanel, menuCartPane);
		bigPane.setDividerLocation(100);
		bigPane.setEnabled(false);
		frame.getContentPane().add(bigPane, BorderLayout.CENTER);
		// Pack frame and set it to visible
		frame.pack();
		frame.setVisible(true);
		frame.setResizable(false);
		menuItemHolder.getPartner(cartItemHolder);
		cartItemHolder.getPartner(menuItemHolder);
	}

	public static void main(String[] args) {
		//Load the JDBC Driver
		try {
			Class.forName(driver).newInstance();
			System.out.println("Loaded the appropriate driver");
		} catch (ClassNotFoundException cnfe) {
			System.err.println("\nUnable to load the JDBC driver " + driver);
			System.err.println("Please check your CLASSPATH.");
			cnfe.printStackTrace(System.err);
		} catch (InstantiationException ie) {
			System.err.println("\nUnable to instantiate the JDBC driver " + driver);
			ie.printStackTrace(System.err);
		} catch (IllegalAccessException iae) {
			System.err.println("\nNot allowed to access the JDBC driver " + driver);
			iae.printStackTrace(System.err);
		}
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}
	//This method is almost identical to the one in MenuPanel that sends the MenuItem to the cart, except that this method also locks
	//the components so that they cannot be edited.  This makes it so the user can see exactly what they are going to order as a
	//confirmation window.  If the user wants to make changes, he/she just needs to close/cancel the window and go back to the cart panel
	//to make changes, which they can then resubmit to the OrderPanel
	@SuppressWarnings("static-access")
	public static void orderSender () {
		MenuItem itemToSend;
		String[] sendingInfo = new String[7];
		int numComponents = cartItemHolder.getComponents().length;
		int i, j;
		if (numComponents>0) {
			for (i=0;i<numComponents;i++) {
				//Make a "copy"
				sendingInfo = ((MenuItem)(cartItemHolder.getComponents()[i])).info;
				itemToSend = new MenuItem(sendingInfo);
				//rewrite the old values onto the new item
				j=3;
				//Price info
				Component[] componentArray = ((JPanel)(itemToSend.getComponents()[0])).getComponents();
				((JLabel)componentArray[2]).setText("$" + sendingInfo[2]);
				//Meat info
				if (sendingInfo[3] != "0") {
					for (;j<componentArray.length;j++) {
						if (componentArray[j] instanceof JComboBox<?>) {
							break;
						}
					}
					((JComboBox<?>)componentArray[j]).setSelectedItem(sendingInfo[3]);
					((JComboBox<?>)componentArray[j]).setEditable(false);
					((JTextField)((JComboBox<?>)componentArray[j]).getEditor().getEditorComponent()).setEditable(false);
					((MetalComboBoxButton)((JComboBox<?>)componentArray[j]).getComponents()[0]).setEnabled(false);
					int k;
					//This disables the mouse listeners on the JComboBox so that the user cannot interact with it
					MouseListener[] listeners = ((JComboBox<?>)componentArray[j]).getMouseListeners();
					for (k=0;k<listeners.length;k++) {
						((JComboBox<?>)componentArray[j]).removeMouseListener(listeners[k]);
					}
					listeners = ((MetalComboBoxButton)((JComboBox<?>)componentArray[j]).getComponents()[0]).getMouseListeners();
					for (k=0;k<listeners.length;k++) {
						((MetalComboBoxButton)((JComboBox<?>)componentArray[j]).getComponents()[0]).removeMouseListener(listeners[k]);
					}
				}
				//Spice info
				if (sendingInfo[4] != "0") {
					for (;j<componentArray.length;j++) {
						if (componentArray[j] instanceof JSpinner) {
							break;
						}
					}
					((JSpinner)componentArray[j]).setValue(Integer.parseInt(sendingInfo[4]));
					((JSpinner)componentArray[j]).setEnabled(false);
					JSpinner.DefaultEditor spiceeditor = (JSpinner.DefaultEditor) ((JSpinner)componentArray[j]).getEditor();
					spiceeditor.getTextField().setEnabled( true );
					spiceeditor.getTextField().setEditable( false );
				}
				//Quantity info
				for (j=j+1;j<componentArray.length;j++) {
					if (((JPanel)(itemToSend.getComponents()[0])).getComponents()[j] instanceof JSpinner) {
						break;
					}
				}
				((JSpinner)componentArray[j]).setValue(Integer.parseInt(sendingInfo[5]));
				((JSpinner)componentArray[j]).setEnabled(false);
				JSpinner.DefaultEditor qtyeditor = ( JSpinner.DefaultEditor ) ((JSpinner)componentArray[j]).getEditor();
				qtyeditor.getTextField().setEnabled( true );
				qtyeditor.getTextField().setEditable( false );
				//Comment info
				((JFormattedTextField)componentArray[j+2]).setText(sendingInfo[6]);
				((JFormattedTextField)componentArray[j+2]).setEditable(false);
				orderItemHolder.newItem(itemToSend);
			}
		} else {
			//In case there should be something done in the event that there is nothing in the cart
			//There may be a JPanel with a JLabel informing the user of this, but likely nothing more than that
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	static void loadMenu() {
		System.out.println("Accessing Menu from menuDB in " + framework + " mode");
		Connection conn = null;
		ArrayList statements = new ArrayList(); 
		java.sql.Statement s = null;
		ResultSet rs = null;
		try {
			String dbName = "menuSystem";
			//try {
			conn = DriverManager.getConnection(protocol + dbName);
			/*} catch (SQLException sqle){
				if (((sqle.getErrorCode() == 40000) && ("XJ004".equals(sqle.getSQLState())))) {
					conn = DriverManager.getConnection(protocol + dbName + ";create=true;");
				}
			}*/
			System.out.println("Connected to database " + dbName);
			conn.setAutoCommit(false);
			s = conn.createStatement();
			statements.add(s);
			//try {
			rs = s.executeQuery("select * from menuTable order by num");
			/*} catch (SQLException sqle){
				if (((sqle.getErrorCode() == 20000) && ("42X05".equals(sqle.getSQLState())))) {
					s.execute("create table menuTable(num int, name varchar(40))");
					rs = s.executeQuery("select * from menuTable order by num");
				}
			}*/
			String[] itemInfo = new String[8];
			while (rs.next()) {
				System.out.println(rs.getInt(1) + " " + rs.getString(2));
				itemInfo[0] = "" + rs.getInt(1);//num
				itemInfo[1] = rs.getString(2);//name
				itemInfo[2] = "" + rs.getInt(3);//price
				itemInfo[3] = "" + rs.getInt(4);//meats
				itemInfo[4] = "" + rs.getInt(5);//spice
				itemInfo[5] = "" + rs.getInt(6);//qty
				itemInfo[6] = rs.getString(7);//comment
				itemInfo[7] = "" + rs.getInt(3);//original price
				newMenuItem = new MenuItem(itemInfo);
				menuItemHolder.add(newMenuItem);
				numberOfMenuItems++;
			}
			s.execute("drop table menuTable");
			System.out.println("Dropped table menuTable");
			try {
				DriverManager.getConnection(protocol + ";shutdown=true");
			} catch (SQLException se) {
				if (((se.getErrorCode() == 50000) && ("XJ015".equals(se.getSQLState())))) {
					System.out.println("Derby shut down normally");
				} else {
					System.err.println("Derby did not shut down normally");
					printSQLException(se);
				}
			}
		} catch (SQLException sqle) {
			printSQLException(sqle);
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
			} catch (SQLException sqle) {
				printSQLException(sqle);
			}
			int i = 0;
			while (!statements.isEmpty()) {
				java.sql.Statement st = (java.sql.Statement) statements.remove(i);
				try {
					if (st != null) {
						st.close();
						st = null;
					}
				} catch (SQLException sqle) {
					printSQLException(sqle);
				}
			}
			try {
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException sqle) {
				printSQLException(sqle);
			}
		}
	}
	
	public static void printSQLException(SQLException e) {
		while (e != null) {
			System.err.println("\n----- SQLException -----");
			System.err.println("  SQL State:  " + e.getSQLState());
			System.err.println("  Error Code: " + e.getErrorCode());
			System.err.println("  Message:    " + e.getMessage());
			e = e.getNextException();
		}
	}
}