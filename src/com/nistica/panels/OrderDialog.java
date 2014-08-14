package com.nistica.panels;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

import javax.swing.*;
import javax.swing.plaf.metal.MetalComboBoxButton;

import com.nistica.tk.CartPanel;
import com.nistica.tk.MenuItem;
import com.nistica.tk.OrderGUI;
import com.nistica.tk.SpringUtilities;
import com.nistica.tk.StripeOrder;

public class OrderDialog extends JDialog {
	JPanel checkoutPanel;
	OrderPanel orderItemHolder;
	
	public OrderDialog(CartPanel cartItemHolder){
		
		this.setTitle("Your Order");
		this.setModal(true);
		//this.setResizable(false);
		
		JPanel bigPanel = new JPanel();
		bigPanel.setLayout(new GridLayout(2, 1));
		
		
		//this.getContentPane().setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		//this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		orderItemHolder = new OrderPanel();
		orderItemHolder.getNumComp(cartItemHolder.getComponents().length);
		
		orderSender(cartItemHolder);
		orderItemHolder.setVisible(true);
		orderItemHolder.setLayout(new BoxLayout(orderItemHolder, BoxLayout.PAGE_AXIS));
		orderItemHolder.setBackground(OrderGUI.MENUCOLOR);
		
		JScrollPane orderPane = new JScrollPane(orderItemHolder,  javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS , javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		orderPane.setPreferredSize(new Dimension(525,631));		
	 	orderPane.setVisible(true);
	 	
	 	//Setup the checkout panel
	 	JPanel bigCheckoutPanel = new JPanel();
	 	bigCheckoutPanel.setLayout(new GridLayout(3, 1));
	 	bigCheckoutPanel.setBorder(BorderFactory.createLineBorder(Color.BLUE));
	 	
	 	
	 	
	 	//components inside checkout panel
	 	JLabel nameLabel = new JLabel("Name:", JLabel.TRAILING);
	 	JTextField nameField = new JTextField("", 15);
	 	JLabel creditcardLabel = new JLabel("Credit card number:", JLabel.TRAILING);
	 	JTextField creditcardField = new JTextField("", 20);
	 	JLabel cvcLabel = new JLabel("CVC:", JLabel.TRAILING);
	 	JTextField cvcField = new JTextField("", 5);
	 	JLabel expMonthLabel = new JLabel("Card expiration month:", JLabel.TRAILING);
	 	JTextField expMonthField = new JTextField("", 7);
	 	JLabel expYearLabel = new JLabel("Card expiration year:", JLabel.TRAILING);
	 	JTextField expYearField = new JTextField("", 9);
	 	
	 	
	 	JButton submitButton = new JButton("Submit order");
	 	JLabel errorLabel = new JLabel("");
	 	errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
	 	errorLabel.setFont(new Font("Serif", Font.BOLD, 24));
	 	errorLabel.setForeground(Color.RED);
	 	
	 	submitButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				StripeOrder stripeOrder = new StripeOrder();
				
				String cardCheckMessage = stripeOrder.setCreditCard(
						creditcardField.getText(), nameField.getText(), Integer.valueOf(expMonthField.getText()), Integer.valueOf(expYearField.getText()));
				
				System.out.println("order passed?: " + cardCheckMessage);
				if(cardCheckMessage.equals("Card Valid"))
					stripeOrder.sendPayment(120, "<name> is ordering <food>");
				else
					errorLabel.setText(cardCheckMessage);
				
			}
	 		
	 	});
	 	
	 	
	 	SpringLayout springLayout = new SpringLayout();
	 	checkoutPanel = new JPanel();
	 	checkoutPanel.setLayout(springLayout);
	 	checkoutPanel.add(nameLabel);
	 	checkoutPanel.add(nameField);
	 	checkoutPanel.add(creditcardLabel);
	 	checkoutPanel.add(creditcardField);
	 	checkoutPanel.add(cvcLabel);
	 	checkoutPanel.add(cvcField);
	 	checkoutPanel.add(expMonthLabel);
	 	checkoutPanel.add(expMonthField);
	 	checkoutPanel.add(expYearLabel);
	 	checkoutPanel.add(expYearField);
	 	
	 	SpringUtilities.makeCompactGrid(checkoutPanel, 5, 2, //rows, cols
	 									6, 6, //initx, initx
	 									6, 6); //xpad, ypad
	 	
	 	bigCheckoutPanel.add(checkoutPanel);
	 	bigCheckoutPanel.add(submitButton);
	 	bigCheckoutPanel.add(errorLabel);
	 	
	 	
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
	
	
 	
 	@SuppressWarnings("serial")
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
 	
 	//This method is almost identical to the one in MenuPanel that sends the MenuItem to the cart, except that this method also locks
	//the components so that they cannot be edited.  This makes it so the user can see exactly what they are going to order as a
	//confirmation window.  If the user wants to make changes, he/she just needs to close/cancel the window and go back to the cart panel
	//to make changes, which they can then resubmit to the OrderPanel
	public void orderSender(CartPanel cartItemHolder) {
		MenuItem itemToSend;
		String[] sendingInfo = new String[7];
		int numComponents = cartItemHolder.getComponents().length;
		int i, j;
		if (numComponents>0) {
			for (i=0;i<numComponents;i++) {
				//Make a "copy"
				sendingInfo = ((MenuItem)(cartItemHolder.getComponents()[i])).info;
				itemToSend = new MenuItem();
				itemToSend.info = sendingInfo;
				itemToSend.hasMeats = (sendingInfo[3] != "");
				itemToSend.hasSpice = (sendingInfo[4] != "");
				itemToSend.createComponents();
				//rewrite the old values onto the new item
				j=3;
				//Price info
				Component[] componentArray = ((JPanel)(itemToSend.getComponents()[0])).getComponents();
				((JLabel)componentArray[0]).setText(sendingInfo[0]);
				((JLabel)componentArray[1]).setText(sendingInfo[1]);
				((JLabel)componentArray[2]).setText("$" + sendingInfo[2]);
				//Meat info
				if (itemToSend.hasMeats) {
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
				if (itemToSend.hasSpice) {
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
}
	

