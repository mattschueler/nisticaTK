package com.nistica.panels;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.nistica.tk.OrderGUI;


@SuppressWarnings("serial")
public class HelpDialog extends JDialog{
	
	public HelpDialog(){
		this.setTitle("Help");
		this.setModal(true);
		
		JPanel helpPanel = new JPanel();
		JTextArea helpInfo = new JTextArea(
				"Help on using the Ordering System:\nTo add an item to your cart (right side), double click an item on the menu (left side).\nYou can " +
	    		"adjust the parameters of your order at any time, on either side of the screen, but the parameters in your cart are the ones that will " +
	    		"be saved.\nTo remove an item from your cart, simply click the x button on the right of each item.\nIf you add an item on the menu that is already in " + 
	    		"your cart, the item in the cart will be overwritten.\nWhen you are ready to submit your order, hit the order button to review your order.\n" +
	    		"If you are happy with your order, click the next order button to submit your order.  If you are not, simply close the window and make any changes " +
	    		"you wish to make.\nYou can also hit the clear button to clear out your entire cart and start from the beginning.\n\nThank you for using the " +
	    		"Nistica Thai Kitchen Ordering System!"
	    		);
		helpInfo.setLineWrap(true);
		helpInfo.setWrapStyleWord(true);
		helpInfo.setEditable(false);
		helpInfo.setPreferredSize(new Dimension(395,290));
		helpInfo.setBackground(OrderGUI.MENUCOLOR);
		helpInfo.setForeground(Color.BLACK);
		helpInfo.setVisible(true);
	 	helpPanel.add(helpInfo);
	 	helpPanel.setPreferredSize(new Dimension(405,300));
	 	helpPanel.setBackground(OrderGUI.MENUCOLOR);
	 	helpPanel.setVisible(true);
	 	
	 	this.add(helpPanel);
	}

}
