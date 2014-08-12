package com.nistica.tk;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.*;

//import com.nistica.tk.MenuItem.Item;
import com.nistica.tk.OrderGUI;

@SuppressWarnings({ "serial", "unused" })
public class MenuPanel extends JPanel implements MouseListener {
	public int numberOfItems;
	public CartPanel cartPanel;

	public MenuPanel(int items) {
		this.numberOfItems = 68;
		this.addMouseListener(this);
	}
	
	public void getPartner (CartPanel cartPanel) {
		this.cartPanel = cartPanel;
	}
	//This makes it so the MenuPanel is always created to be the correct size to hold all the MenuItems
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(506, (numberOfItems * 110));
	}
	//This method creates a clone of the MenuItem that is being sent to the cart panel.  It saves the values of each component, then creates a new
	//one from scratch and sets all the values of the new components in the "clone" panel.  Since SpringLayout is not serializable, the MenuItem cannot be
	//cloned through an Object Stream.  This allows any changes the user has make to menu items in the menu panel to be sent and saved in the cart panel,
	//unless they are deleted from the cart panel.  The changes remain in the menu Panel unless the user changes them there again
	@SuppressWarnings("static-access")
	public void itemIDGetter (int id) {
		MenuItem itemToSend;
		int itemToSendID=0;
		String[] sendingInfo = new String[7];
		int numComponents = this.getComponents().length;
		//This section finds the corresponding MenuItem component to the ID that was given to the method's parameter
		int i;
		for (i=0;i<numComponents;i++) {
			if (Integer.parseInt(((JLabel)((JPanel)((JPanel)(this.getComponents()[i])).getComponents()[0]).getComponents()[0]).getText()) == id) {
				itemToSendID = i;
				break;
			}
		}
		//Create a new MenuItem with the information of the selected food
		sendingInfo = ((MenuItem)(this.getComponents()[itemToSendID])).info;
		itemToSend = new MenuItem(sendingInfo);
		//rewrite the old values onto the new item
		//Meat info
		int j=3;
		//Component[] componentArray = ((JPanel)(itemToSend.getComponents()[0])).getComponents();
		((JLabel)((JPanel)(itemToSend.getComponents()[0])).getComponents()[2]).setText("$" + sendingInfo[2]);
		if (sendingInfo[3] != "0") {
			for (;j<(((JPanel)(itemToSend.getComponents()[0])).getComponents()).length;j++) {
				if (((JPanel)(itemToSend.getComponents()[0])).getComponents()[j] instanceof JComboBox<?>) {
					break;
				}
			}
			((JComboBox<?>)((JPanel)(itemToSend.getComponents()[0])).getComponents()[j]).setSelectedItem(sendingInfo[3]);
		}
		//Spice info
		if (sendingInfo[4] != "0") {
			for (;j<((JPanel)(itemToSend.getComponents()[0])).getComponents().length;j++) {
				if (((JPanel)(itemToSend.getComponents()[0])).getComponents()[j] instanceof JSpinner) {
					break;
				}
			}
			((JSpinner)((JPanel)(itemToSend.getComponents()[0])).getComponents()[j]).setValue(Integer.parseInt(sendingInfo[4]));
		}
		//Quantity info
		for (j=j+1;j<((JPanel)(itemToSend.getComponents()[0])).getComponents().length;j++) {
			if (((JPanel)(itemToSend.getComponents()[0])).getComponents()[j] instanceof JSpinner) {
				break;
			}
		}
		((JSpinner)((JPanel)(itemToSend.getComponents()[0])).getComponents()[j]).setValue(Integer.parseInt(sendingInfo[5]));
		//Comment info
		((JFormattedTextField)((JPanel)(itemToSend.getComponents()[0])).getComponents()[j+2]).setText(sendingInfo[6]);
		itemToSend = new MenuItem(sendingInfo);
		cartPanel.newItem(itemToSend);
		repaint();
		revalidate();
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {}
	
	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}

	@Override
	public void mousePressed(MouseEvent arg0) {}

	@Override
	public void mouseReleased(MouseEvent arg0) {}
}