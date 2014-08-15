package com.nistica.tk;

import javax.swing.*;
import javax.swing.plaf.metal.MetalComboBoxButton;

import com.nistica.panels.HelpDialog;
import com.nistica.panels.OrderDialog;
import com.nistica.tk.MenuItem.*;

import java.awt.*;
import java.awt.event.*;
import java.util.Calendar;
import java.util.GregorianCalendar;

@SuppressWarnings("unused")
public class OrderGUI implements ScrollPaneConstants {

	public static JFrame frame;
	public static JScrollPane menuPane, cartPane;
	public static JSplitPane menuCartPane, bigPane;
	public static JPanel controlsPanel, newMenuItem;
	public static MenuPanel menuItemHolder;
	public static CartPanel cartItemHolder;
	//public static OrderPanel orderItemHolder;
	
	public static int numberOfMenuItems;
	
	public static final Color MENUCOLOR = new Color(230,242,242);
	public static final Color CONTROLCOLOR = new Color(47,211,214);
	
	public static XMLTester xmltest;
	public static XSSFTester xssftest;

	public OrderGUI() {
	}

	private static void createAndShowGUI() {
		xmltest = new XMLTester();
		xssftest = new XSSFTester();
		numberOfMenuItems = xmltest.theMenu.size();
		// Create the new JFrame object
		frame = new JFrame("Nistica Thai Kitchen Ordering System");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension splitSize = new Dimension(519, 600);
		
		// Create all the menu item objects
		menuItemHolder = new MenuPanel(numberOfMenuItems);
		menuItemHolder.setLayout(new BoxLayout(menuItemHolder, BoxLayout.PAGE_AXIS));
		for (MenuItem menuItem : xmltest.theMenu) {
			//newMenuItem = new MenuItem();
			menuItemHolder.add(menuItem);
		}
		menuItemHolder.setBackground(MENUCOLOR);
		//Initialize the Cart Panel
		cartItemHolder = new CartPanel(numberOfMenuItems);
		cartItemHolder.setLayout(new BoxLayout(cartItemHolder, BoxLayout.PAGE_AXIS));
		cartItemHolder.setBackground(MENUCOLOR);
		// Create a new JSplitPane with the two halves as the menu and cart
		menuPane = new JScrollPane(menuItemHolder, VERTICAL_SCROLLBAR_ALWAYS, HORIZONTAL_SCROLLBAR_NEVER);
		menuPane.setPreferredSize(splitSize);
		menuPane.setBorder(BorderFactory.createLineBorder(Color.blue));
		cartPane = new JScrollPane(cartItemHolder, VERTICAL_SCROLLBAR_ALWAYS, HORIZONTAL_SCROLLBAR_NEVER);
		cartPane.setPreferredSize(splitSize);
		menuCartPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, menuPane, cartPane);
		menuCartPane.setDividerLocation(525);
		menuCartPane.setEnabled(false);
		menuPane.setBorder(BorderFactory.createLineBorder(Color.red));
		//Create the controls panel at the top of the GUI
		controlsPanel = new JPanel();
		SpringLayout springControls = new SpringLayout();
		controlsPanel.setLayout(springControls);
		controlsPanel.setBackground(CONTROLCOLOR);
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
				HelpDialog help = new HelpDialog();
			 	help.setLocation(new Point(325,200));
			 	help.pack();
			 	help.setVisible(true);
			}
		});
		springControls.putConstraint(SpringLayout.NORTH, helpButton, 5, SpringLayout.NORTH, controlsPanel);
		springControls.putConstraint(SpringLayout.WEST, helpButton, 5, SpringLayout.WEST, controlsPanel);
		controlsPanel.add(helpButton);
		JButton orderButton = new JButton("Order");
		orderButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				OrderDialog order = new OrderDialog(cartItemHolder);				
			 	order.setLocation(new Point(325,100));
			 	order.setVisible(true);
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
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}
	
}