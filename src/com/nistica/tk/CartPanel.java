package com.nistica.tk;

import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class CartPanel extends JPanel implements MouseListener{
	
	public int numberOfItems;
	public MenuPanel menuPanel;
	
	public CartPanel(int items) {
		this.numberOfItems = items;
		this.addMouseListener(this);
	}
	
	//To ensure that the menu and cart panels are linked together
	public void getPartner (MenuPanel menuPanel) {
		this.menuPanel = menuPanel;
	}
	
	//To ensure that there is space for every item in the array (in OrderGUI.java)
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(506, (numberOfItems * 110));
	}
	
	public void itemIDGetter (int id) {
		JPanel itemToRemove;
		int itemToRemoveID=0;
		int numComponents = this.getComponents().length;
		for (int i=0;i<numComponents;i++) {
			//This checks to see if the JLabel at the beginning of every menu item matches the one that passed the id from the mouseClicked event.
			if (Integer.parseInt(((JLabel)((JPanel)((JPanel)(this.getComponents()[i])).getComponents()[0]).getComponents()[0]).getText()) == id) {
				itemToRemoveID = i;
				break;
			}
		}
		itemToRemove = (JPanel)(this.getComponents()[itemToRemoveID]);
		this.remove(itemToRemove);
		repaint();
		revalidate();
	}
	
	public void newItem(JPanel item) {
		int numComponents = this.getComponents().length;
		int itemLabel, thisLabel;
		for (int i=0;i<numComponents;i++) {
			//This first line checks for an instance of JLabel inside nested Components of the JPanel
			if ((((JPanel)item.getComponents()[0]).getComponents()[0]) instanceof JLabel) {
				//This line checks to see if the menu item the user is trying to add is already in the list.
				//If it is, then the first on is deleted and the new one moved into place
				itemLabel = Integer.parseInt(((JLabel)(((JPanel)item.getComponents()[0]).getComponents()[0])).getText());
				//These two were split up from the if-statement to make the lines a bit shorter
				thisLabel = Integer.parseInt(((JLabel)((JPanel)((JPanel)(this.getComponents()[i])).getComponents()[0]).getComponents()[0]).getText());
				if (itemLabel == thisLabel) {
					//This removes the current JPanel that is being checked if it passes the above if-statement
					this.remove(this.getComponents()[i]);
					break;
				}
			}	
			
				
		}
		//The last item in each MenuItem is the transfer button, so the text of the transferbutton
		//should turn into an X to show that it will be deleted if it is pressed
		((JButton)(((JPanel)item.getComponents()[0]).getComponents()[((JPanel)item.getComponents()[0]).getComponents().length-1])).setText("X");
		this.add(item);
		item.setToolTipText(((MenuItem)item).info[8]);
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
