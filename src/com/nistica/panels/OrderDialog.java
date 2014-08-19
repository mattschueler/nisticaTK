package com.nistica.panels;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
//import java.awt.FlowLayout;
import java.awt.Font;
//import java.awt.GridBagConstraints;
//import java.awt.GridBagLayout;
import java.awt.GridLayout;
//import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.text.DecimalFormat;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.metal.MetalComboBoxButton;

import com.nistica.tk.CartPanel;
import com.nistica.tk.MenuItem;
import com.nistica.tk.OrderGUI;
import com.nistica.tk.SpringUtilities;
import com.nistica.tk.StripeOrder;

@SuppressWarnings("serial")
public class OrderDialog extends JDialog {
	JPanel checkoutPanel;
	OrderPanel orderItemHolder;
	double orderTotalPrice;
	CartPanel cartItemHolder;
	
	public static JLabel fnameLabel;
	public static JTextField fnameField;
	public static JLabel lnameLabel;
	public static JTextField lnameField;
	public static JLabel creditcardLabel;
	public static JTextField creditcardField;
	public static JLabel cvcLabel;
	public static JTextField cvcField;
	public static JLabel expMonthLabel;
	public static JTextField expMonthField;
	public static JLabel expYearLabel;
	public static JTextField expYearField;
	public static JTextArea totalsText;
	
	public static JButton submitButton;
	public static JLabel errorLabel;
	public static JLabel tipLabel;
	public static JTextField tipText;
	
	public static double tip;
	
	public OrderDialog(CartPanel cih){
		
		this.setTitle("Your Order");
		this.setModal(true);
		//this.setResizable(false);
		
		JPanel bigPanel = new JPanel();
		this.cartItemHolder = cih;
		
		//this.getContentPane().setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		//this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		orderItemHolder = new OrderPanel();
		orderItemHolder.getNumComp(cartItemHolder.getComponents().length);
		
		orderSender(cartItemHolder);//sets the orderTotalPrice and adds each food item
		System.out.println("price w/o tax " +orderTotalPrice);
		orderItemHolder.setVisible(true);
		orderItemHolder.setLayout(new BoxLayout(orderItemHolder, BoxLayout.PAGE_AXIS));
		orderItemHolder.setBackground(OrderGUI.MENUCOLOR);
		
		JScrollPane orderPane = new JScrollPane(orderItemHolder,  javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS , javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		orderPane.setPreferredSize(new Dimension(525,631));		
	 	orderPane.setVisible(true);
	 	orderPane.getVerticalScrollBar().setUnitIncrement(14);
	 	
	 	//Setup the checkout panel
	 	JPanel bigCheckoutPanel = new JPanel();
	 	SpringLayout bcplayout = new SpringLayout();
	 	bigCheckoutPanel.setLayout(bcplayout);
	 	bigCheckoutPanel.setBorder(BorderFactory.createLineBorder(Color.BLUE));
	 	bigCheckoutPanel.setPreferredSize(new Dimension(525,625));
	 	
	 	
	 	//components inside checkout panel
	 	fnameLabel = new JLabel("First Name:", JLabel.TRAILING);
	 	fnameField = new JTextField("", 15);
	 	lnameLabel = new JLabel("Last Name:", JLabel.TRAILING);
	 	lnameField = new JTextField("", 15);
	 	creditcardLabel = new JLabel("Credit card number:", JLabel.TRAILING);
	 	creditcardField = new JTextField("", 20);
	 	cvcLabel = new JLabel("CVC:", JLabel.TRAILING);
	 	cvcField = new JTextField("", 5);
	 	expMonthLabel = new JLabel("Card expiration month #:", JLabel.TRAILING);
	 	expMonthField = new JTextField("", 7);
	 	expMonthField.setInputVerifier(new CardInputVerifier());
	 	expYearLabel = new JLabel("Card expiration year #:", JLabel.TRAILING);
	 	expYearField = new JTextField("", 9);
	 	totalsText = new JTextArea();
	 	totalsText.setPreferredSize(new Dimension(200,70));
	 	
	 	submitButton = new JButton("Submit order");
	 	errorLabel = new JLabel("");
	 	errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
	 	errorLabel.setFont(new Font("Serif", Font.BOLD, 24));
	 	errorLabel.setForeground(Color.RED);
	 	
	 	submitButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("priceClicked " +orderTotalPrice);
				errorLabel.setText("");
				StripeOrder stripeOrder = new StripeOrder();
				
				if(fnameField.getText().equals("") || lnameField.getText().equals(""))
				{
					errorLabel.setText("Fill out both name fields");
					return;
				}
				
				String cardCheckMessage;
				try{
				cardCheckMessage = stripeOrder.setCreditCard(
						creditcardField.getText(), fnameField.getText() + lnameField.getText(), Integer.valueOf(expMonthField.getText()), Integer.valueOf(expYearField.getText()));
				} catch(NumberFormatException e){
					errorLabel.setText("Your fields are not filled out properly");
					e.printStackTrace();
					return;
				}
				
				
				//System.out.println("order passed?: " + cardCheckMessage);
				if(cardCheckMessage.equals("Card Valid")) {
					boolean successfulOrder = false;
					Component[] items = orderItemHolder.getComponents();
					String[] itemInfo = new String[7];
					System.out.println("ITEMS LENGTHHHHHHHHHH: " + items.length);
					/*try {
				        // Get a file channel for the file
				        File file = new File("filename");
				        FileChannel channel = new RandomAccessFile(file, "rw").getChannel();

				        // Use the file channel to create a lock on the file.
				        // This method blocks until it can retrieve the lock.
				        FileLock lock = channel.lock();

				        
				           use channel.lock OR channel.tryLock();
				        

				        // Try acquiring the lock without blocking. This method returns
				        // null or throws an exception if the file is already locked.
				        try {
				            lock = channel.tryLock();
				        } catch (OverlappingFileLockException e) {
				            // File is already locked in this thread or virtual machine
				        }

				        // Release the lock
				        lock.release();

				        // Close the file
				        channel.close();
				    } catch (Exception e) {
				    }*/
					if(!OrderGUI.hssftest.init())
						return;
					
					for(int i=0;i<items.length;i++) {
						
						if (Integer.parseInt(((MenuItem)items[i]).info[0]) < 200) {
							itemInfo[0] = "" + fnameField.getText().charAt(0) + lnameField.getText().charAt(0);
							itemInfo[1] = ((MenuItem)items[i]).info[0];
							itemInfo[2] = ((MenuItem)items[i]).info[3];
							itemInfo[3] = ((MenuItem)items[i]).info[4];
							itemInfo[4] = ((MenuItem)items[i]).info[5];
							itemInfo[5] = ((MenuItem)items[i]).info[6];
							itemInfo[6] = String.format("" + Double.parseDouble(((MenuItem)items[i]).info[2]) * Integer.parseInt(((MenuItem)items[i]).info[5]));
							itemInfo[6] = new DecimalFormat("##.##").format(Double.valueOf(itemInfo[6]));
							System.out.println("Indiv price:" + itemInfo[6]);
							//System.out.println ("Indiv price- "+(new DecimalFormat("##.##").format(Double.valueOf(itemInfo[6]))));
							//Math.round(
							orderTotalPrice += Double.parseDouble(itemInfo[6]);
						} else {
							itemInfo[0] = "" + fnameField.getText().charAt(0) + lnameField.getText().charAt(0);
							itemInfo[1] = "";
							itemInfo[2] = "";
							itemInfo[3] = "";
							itemInfo[4] = ((MenuItem)items[i]).info[5];
							itemInfo[5] = ((MenuItem)items[i]).info[1];
							itemInfo[6] = String.format("" + Double.parseDouble(((MenuItem)items[i]).info[2]) * Integer.parseInt(((MenuItem)items[i]).info[5]));
							itemInfo[6] = new DecimalFormat("##.##").format(Double.valueOf(itemInfo[6]));
						}
						
						
						
						
						
						
						if (!OrderGUI.hssftest.addOrder(itemInfo)) {
							successfulOrder = false;
							break;
						} else {
							successfulOrder = true;
						}
					}
					if (successfulOrder) {
						System.out.println("amount sent to striper " + orderTotalPrice);
						errorLabel.setForeground(new Color(0,127,0));
						errorLabel.setText("Order Successful. Thank you.");
						System.out.println(orderTotalPrice+tip);

						stripeOrder.sendPayment(orderTotalPrice, tip, fnameField.getText()+" " + lnameField.getText()+" has ordered");

					} else {
						errorLabel.setForeground(Color.RED);
						errorLabel.setText("Error in sending order.");
					}
				} else {
					errorLabel.setForeground(Color.RED);
					errorLabel.setText(cardCheckMessage);
				}
			}
	 		
	 	});
	 	

	 	double transactionFee = (orderTotalPrice*1.07*(.029)+.3)/(.971); //Explained in StripeOrder.java
	 	totalsText.append(String.format("Subtotal: %.2f\n", orderTotalPrice) + String.format("Tax: %.2f\n", (orderTotalPrice * 0.07))
	 			+ String.format("Transaction fee: %.2f\n",  transactionFee) + 
	 			String.format("Total: %.2f", (orderTotalPrice * 1.07)+tip+transactionFee));

	 	totalsText.setEditable(false);
	 	
	 	tipLabel = new JLabel("Tip:");
	 	tipText = new JTextField("");
	 	tipText.getDocument().addDocumentListener(new DocumentListener(){
	 		@Override
			
	 		public void changedUpdate(DocumentEvent e) {
	 			change();
	 		  }
	 		public void removeUpdate(DocumentEvent e) {
	 			change();
	 		}
	 		public void insertUpdate(DocumentEvent e) {
	 			change();
	 		}
	 		public void change() {
	 			tip = Double.parseDouble(tipText.getText());
				totalsText.setText("");
				//subtotal = orderTotalPrice + tip;
				totalsText.append(String.format("Subtotal: %.2f\n", orderTotalPrice) + String.format("Tax: %.2f\n", (orderTotalPrice * 0.07)) + String.format("Total: %.2f", (orderTotalPrice * 1.07 + tip)));
				System.out.println("action event triggered");
	 		}
	 	});
	 	tipText.setInputVerifier(new TipInputVerifier());
	 	
	 	SpringLayout springLayout = new SpringLayout();
	 	checkoutPanel = new JPanel();
	 	checkoutPanel.setLayout(springLayout);
	 	checkoutPanel.add(fnameLabel);
	 	checkoutPanel.add(fnameField);
	 	checkoutPanel.add(lnameLabel);
	 	checkoutPanel.add(lnameField);
	 	checkoutPanel.add(creditcardLabel);
	 	checkoutPanel.add(creditcardField);
	 	checkoutPanel.add(cvcLabel);
	 	checkoutPanel.add(cvcField);
	 	checkoutPanel.add(expMonthLabel);
	 	checkoutPanel.add(expMonthField);
	 	checkoutPanel.add(expYearLabel);
	 	checkoutPanel.add(expYearField);
	 	
	 	SpringUtilities.makeCompactGrid(checkoutPanel, 6, 2, //rows, cols
	 									4, 4, //initx, initx
	 									6, 6); //xpad, ypad
	 	bcplayout.putConstraint(SpringLayout.WEST, checkoutPanel, 15, SpringLayout.WEST, bigCheckoutPanel);
	 	bcplayout.putConstraint(SpringLayout.EAST, checkoutPanel, -15, SpringLayout.EAST, bigCheckoutPanel);
	 	bcplayout.putConstraint(SpringLayout.NORTH, checkoutPanel, 5, SpringLayout.NORTH, bigCheckoutPanel);
	 	bigCheckoutPanel.add(checkoutPanel);
	 	bcplayout.putConstraint(SpringLayout.WEST, submitButton, 15, SpringLayout.WEST, bigCheckoutPanel);
	 	bcplayout.putConstraint(SpringLayout.EAST, submitButton, -15, SpringLayout.EAST, bigCheckoutPanel);
	 	bcplayout.putConstraint(SpringLayout.NORTH, submitButton, 15, SpringLayout.SOUTH, checkoutPanel);
	 	bcplayout.putConstraint(SpringLayout.SOUTH, submitButton, 75, SpringLayout.NORTH, submitButton);
	 	bigCheckoutPanel.add(submitButton);
	 	bcplayout.putConstraint(SpringLayout.WEST, errorLabel, 15, SpringLayout.WEST, bigCheckoutPanel);
	 	bcplayout.putConstraint(SpringLayout.EAST, errorLabel, -15, SpringLayout.EAST, bigCheckoutPanel);
	 	bcplayout.putConstraint(SpringLayout.NORTH, errorLabel, 15, SpringLayout.SOUTH, submitButton);
	 	bcplayout.putConstraint(SpringLayout.SOUTH, errorLabel, 275, SpringLayout.NORTH, errorLabel);
	 	bigCheckoutPanel.add(errorLabel);
	 	bcplayout.putConstraint(SpringLayout.WEST, totalsText, 15, SpringLayout.WEST, bigCheckoutPanel);

	 	/*bcplayout.putConstraint(SpringLayout.EAST, totalsText, -15, SpringLayout.EAST, bigCheckoutPanel);
	 	bcplayout.putConstraint(SpringLayout.NORTH, totalsText, 5, SpringLayout.SOUTH, errorLabel);
	 	bigCheckoutPanel.add(totalsText);	 	*/

	 	bcplayout.putConstraint(SpringLayout.EAST, totalsText, 225, SpringLayout.WEST, totalsText);
	 	bcplayout.putConstraint(SpringLayout.NORTH, totalsText, 15, SpringLayout.SOUTH, errorLabel);
	 	bigCheckoutPanel.add(totalsText);
	 	bcplayout.putConstraint(SpringLayout.WEST, tipLabel, 15, SpringLayout.EAST, totalsText);
	 	bcplayout.putConstraint(SpringLayout.EAST, tipLabel, 25, SpringLayout.WEST, tipLabel);
	 	bcplayout.putConstraint(SpringLayout.NORTH, tipLabel, 25, SpringLayout.SOUTH, errorLabel);
	 	bigCheckoutPanel.add(tipLabel);
	 	bcplayout.putConstraint(SpringLayout.WEST, tipText, 15, SpringLayout.EAST, tipLabel);
	 	bcplayout.putConstraint(SpringLayout.EAST, tipText, 150, SpringLayout.WEST, tipText);
	 	bcplayout.putConstraint(SpringLayout.NORTH, tipText, 25, SpringLayout.SOUTH, errorLabel);
	 	bigCheckoutPanel.add(tipText);
	 	
	 	/*SpringUtilities.makeCompactGrid(bigCheckoutPanel, 3, 1, //rows, cols
				4, 4, //initx, initx
				6, 6); //xpad, ypad
*/	 	
	 	bigPanel.add(orderPane);
	 	bigPanel.add(bigCheckoutPanel);
	 	
	 	bigPanel.repaint();
	 	bigPanel.validate();
	 	bigPanel.revalidate();
	 	this.add(bigPanel);
	 	this.setSize(400, 600);
	 	this.pack();
	 	this.validate();
	}
	
	
 	
 	private class OrderPanel extends JPanel {

 		public int numberOfItems;
 		
 		public void newItem(MenuItem item) {
 			this.add(item);
 		}
 		//Gets the number of items that are being made into an order so that the Panel can be created with the correct size
 		public void getNumComp(int num) {
 			numberOfItems = num;
 		}
 		//Similar to method in MenuPanel and CartPanel to ensure it is always the correct size
 		@Override
 		public Dimension getPreferredSize() {
 			return new Dimension(506, (numberOfItems * 110));
 		}	
 	}
 	
 	private class CardInputVerifier extends InputVerifier{
 		
 		@Override
 		public boolean verify(JComponent input){
 			String text = ((JTextField)input).getText();
 			try {
 				Integer.valueOf(text); 	
 				if (!(Integer.valueOf(text) > 0 && Integer.valueOf(text) < 13)) {
 					JOptionPane.showMessageDialog(null,
 	 	                    "Error: Please enter a month between 1 and 12", "Error Message",
 	 	                    JOptionPane.ERROR_MESSAGE);
 					return false;
 				}
 			} catch(NumberFormatException e){
 				e.printStackTrace();
 				JOptionPane.showMessageDialog(null,
 	                    "Error: Please enter a valid number", "Error Message",
 	                    JOptionPane.ERROR_MESSAGE);
 				return false;
 			}
 			return true;
 		}
 	}
 	
private class TipInputVerifier extends InputVerifier{
 		
 		@Override
 		public boolean verify(JComponent input){
 			String text = ((JTextField)input).getText();
 			try {
 				Double.valueOf(text); 				
 			} catch(NumberFormatException e){
 				((JTextField)input).setText("");
 				e.printStackTrace();
 				JOptionPane.showMessageDialog(null,
 	                    "Error: Please enter a valid tip of form ##.##", "Error Message",
 	                    JOptionPane.ERROR_MESSAGE);
 				return false;
 			}
 			return true;
 		}
 	}
 	
 	//This method is almost identical to the one in MenuPanel that sends the MenuItem to the cart, except that this method also locks
	//the components so that they cannot be edited.  This makes it so the user can see exactly what they are going to order as a
	//confirmation window.  If the user wants to make changes, he/she just needs to close/cancel the window and go back to the cart panel
	//to make changes, which they can then resubmit to the OrderPanel
	public void orderSender(CartPanel cartItemHolder) {
		
		//remove later
		int itemCount = cartItemHolder.getComponentCount();
		double orderTotalPrice=0;
		for(int i=0; i<itemCount; i++)
		{
			//price * quantity
			 orderTotalPrice+=
					 Double.valueOf( ( (MenuItem)cartItemHolder.getComponents()[i]).info[2])*
					 Double.valueOf( ((MenuItem)cartItemHolder.getComponents()[i]).info[5]) ;
		}
		
		//It's 107 because *100 to conver to cents and then the 7% tax
		System.out.println("order total price with tax (in cents) " + (int) (orderTotalPrice*107));
		this.orderTotalPrice = orderTotalPrice;
		
		
		MenuItem itemToSend;
		String[] sendingInfo = new String[9];
		int numComponents = cartItemHolder.getComponents().length;
		int i, j;
		if (numComponents>0) {
			for (i=0;i<numComponents;i++) {
				//Make a "copy"
				sendingInfo = ((MenuItem)(cartItemHolder.getComponents()[i])).info;
				itemToSend = new MenuItem();
				itemToSend.info = sendingInfo;
				itemToSend.setHasMeats((sendingInfo[3] != ""));
				itemToSend.setHasSpice((sendingInfo[4] != ""));
				itemToSend.createComponents();
				//rewrite the old values onto the new item
				j=3;
				//Price info
				Component[] componentArray = ((JPanel)(itemToSend.getComponents()[0])).getComponents();
				((JLabel)componentArray[0]).setText(sendingInfo[0]);
				((JLabel)componentArray[1]).setText(sendingInfo[1]);
				((JLabel)componentArray[2]).setText("$" + sendingInfo[2]);
				//Meat info
				if (itemToSend.isHasMeats()) {
					for (;j<componentArray.length;j++) {
						if (componentArray[j] instanceof JComboBox) {
							break;
						}
					}
					((JComboBox)componentArray[j]).setSelectedItem(sendingInfo[3]);
					((JComboBox)componentArray[j]).setEditable(false);
					((JTextField)((JComboBox)componentArray[j]).getEditor().getEditorComponent()).setEditable(false);
					((MetalComboBoxButton)((JComboBox)componentArray[j]).getComponents()[0]).setEnabled(false);
					int k;
					//This disables the mouse listeners on the JComboBox so that the user cannot interact with it
					MouseListener[] listeners = ((JComboBox)componentArray[j]).getMouseListeners();
					for (k=0;k<listeners.length;k++) {
						((JComboBox)componentArray[j]).removeMouseListener(listeners[k]);
					}
					listeners = ((MetalComboBoxButton)((JComboBox)componentArray[j]).getComponents()[0]).getMouseListeners();
					for (k=0;k<listeners.length;k++) {
						((MetalComboBoxButton)((JComboBox)componentArray[j]).getComponents()[0]).removeMouseListener(listeners[k]);
					}
				}
				//Spice info
				if (itemToSend.isHasSpice()) {
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
				
				//Transfer button				
				itemToSend.transferButton.setEnabled(false);
				itemToSend.setToolTipText(sendingInfo[8]);
				orderItemHolder.newItem(itemToSend);
			}
		} else {
			//In case there should be something done in the event that there is nothing in the cart
			//There may be a JPanel with a JLabel informing the user of this, but likely nothing more than that
		}
	}
}
	

