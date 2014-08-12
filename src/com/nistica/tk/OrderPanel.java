package com.nistica.tk;

import java.awt.Dimension;

import javax.swing.*;

@SuppressWarnings("serial")
public class OrderPanel extends JPanel {

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
