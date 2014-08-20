package com.nistica.panels;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class HelpDialog extends JDialog{
	
	public HelpDialog(){
		this.setTitle("Help");
		this.setModal(true);
		
		JPanel helpPanel = new JPanel();
		JTextArea helpInfo = new JTextArea(
				"Help on using the Ordering System:\nTo add an item to your cart (right side), click on the arrow of the item on the menu (left side).\nYou can " +
	    		"adjust the parameters of your order at any time, on either side of the screen, but the parameters in your cart are the ones that will " +
	    		"be saved.\nTo remove an item from your cart, simply click the x button on the right of each item.\nIf you add an item on the menu that is already in " + 
	    		"your cart, the item in the cart will be overwritten.\nWhen you are ready to submit your order, hit the order button to review your order.  The subtotal, tax, "+
	    		"and total are all written at the bottom of the order popup\nIf you are happy with your order, click the Submit Order button to submit your order.  If you are not, " +
	    		"simply close the window and make any changes you wish to make.\nYou can also hit the clear button to clear out your entire cart and start from the beginning.\n" +
	    		"Hover over an item to see its description.\n\n"+
	    		"NOTE: There is the standard 7% sales tax on orders, as well as a (2.9% + $0.30) online transaction fee."+
	    		"\n\n\nThank you for using the Nistica Thai Kitchen Ordering System!"
	    		);
		helpInfo.setLineWrap(true);
		helpInfo.setWrapStyleWord(true);
		helpInfo.setEditable(false);
		helpInfo.setPreferredSize(new Dimension(415,390));
		helpInfo.setBackground(new Color(245, 223, 231));
		helpInfo.setForeground(Color.BLACK);
		helpInfo.setVisible(true);
	 	helpPanel.add(helpInfo);
	 	helpPanel.setPreferredSize(new Dimension(425,440));
	 	helpPanel.setBackground(new Color(245, 223, 231));
	 	helpPanel.setVisible(true);
	 	
	 	this.add(helpPanel);
	}

}
