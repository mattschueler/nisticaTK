package com.nistica.tk;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;

@SuppressWarnings("serial")
public class MenuItem extends JPanel {
	//These are all of the visible JComponents that will be shown in the GUI
	public JPanel internalPanel;
	public JLabel nameLabel, numberLabel, priceLabel, sizeLabel, meatLabel, spiceLabel, commentLabel, qtyLabel;
	public JComboBox<String> sizeList, meatBox;
	public JSpinner spiceBox, qtyBox;
	public JFormattedTextField commentBox;
	//These are all the variables that store the values to be shown on the JComponents
	public static String name, number, price, originalPrice, comment, qty;
	public static boolean hasMeats, hasSpice;
	public static String[] meats = {"Chicken", "Beef", "Pork", "Veggies/Tofu", "Shrimp", "Squid"};
	public String[] info;
	//Misc variables for use in the program
	public Item item;
	public static SpinnerNumberModel qtySpinModel, spiceSpinModel;
	public int id;
	public Color itemColor = new Color(82,191,109);
	public Color textColor = new Color(0,0,0);
	public Color boxColor = new Color(230,242,230);

	public MenuItem(Item item) {
		//Initialization of each MenuItem
		this.item = item;
		qtySpinModel = new SpinnerNumberModel(1, 1, 20, 1);
		spiceSpinModel = new SpinnerNumberModel(0, 0, 5, 1);
		internalPanel = new JPanel();
		item.setParams();
		info = new String[8];
		info[0] = number;
		info[1] = name;
		info[2] = price;
		if (hasMeats) {
			info[3] = meats[0];
		} else {
			info[3] = "";
		}
		if (hasSpice) {
			info[4] = "0"; //default spice of zero
		} else {
			info[4] = "";
		}
		info[5] = "1"; //default quantity of one
		info[6] = ""; //default comment of ""
		info[7] = originalPrice;
		//Setting up the layout of each MenuItem
		SpringLayout sl = new SpringLayout();
		internalPanel.setLayout(sl);
		internalPanel.setBackground(itemColor);
		//Adding and setting up all the components to be added to the MenuItem's internalPanel
		numberLabel = new JLabel(number);
		numberLabel.setOpaque(true);
		numberLabel.setBackground(itemColor);
		numberLabel.setForeground(textColor);
		sl.putConstraint(SpringLayout.NORTH, numberLabel, 5, SpringLayout.NORTH, internalPanel);
		sl.putConstraint(SpringLayout.WEST, numberLabel, 5, SpringLayout.WEST, internalPanel);
		internalPanel.add(numberLabel);
		
		nameLabel = new JLabel(name);
		nameLabel.setOpaque(true);
		nameLabel.setBackground(itemColor);
		nameLabel.setForeground(textColor);
		sl.putConstraint(SpringLayout.WEST, nameLabel, 25, SpringLayout.EAST, numberLabel);
		sl.putConstraint(SpringLayout.NORTH, nameLabel, 5, SpringLayout.NORTH, internalPanel);
		internalPanel.add(nameLabel);
		
		priceLabel = new JLabel("$" + price);
		priceLabel.setOpaque(true);
		priceLabel.setBackground(itemColor);
		priceLabel.setForeground(textColor);
		sl.putConstraint(SpringLayout.WEST, priceLabel, 25, SpringLayout.EAST, nameLabel);
		sl.putConstraint(SpringLayout.NORTH, priceLabel, 5, SpringLayout.NORTH, internalPanel);
		internalPanel.add(priceLabel);
				
		if (hasMeats) {
			meatLabel = new JLabel("Meat Type:");
			meatLabel.setOpaque(true);
			meatLabel.setBackground(itemColor);
			meatLabel.setForeground(textColor);
			sl.putConstraint(SpringLayout.NORTH, meatLabel, 15, SpringLayout.SOUTH, numberLabel);
			sl.putConstraint(SpringLayout.WEST, meatLabel, 5, SpringLayout.WEST, internalPanel);
			internalPanel.add(meatLabel);
			meatBox = new JComboBox<String>(meats);
			//This Action Listener allows the JComboBox's value to change what the price tag is on each MenuItem
			meatBox.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					//Check each component in the component list of internalPanel and attempt to find a match for the source
					//of the action event
					int i, j;
					Component child = (Component) ae.getSource();
					Component[] parent = ((JPanel)((JComponent)ae.getSource()).getParent()).getComponents();
					for (i = 0, j = 0;i<parent.length;i++, j++) {
						if (parent[i] == child) {
							break;
						}
						if (i > 2 && parent[i] instanceof JLabel) {
							j--;
						}
					}
					//This gets the selected item and compares it to a list of possible values to see what the new price is
					//The originalPrice (info[7]) is also checked to determine what the cost is because there are different levels of pricing
					//for the different meats/veggies/other.  i.e. the starting price determines what the other prices will be
					((MenuItem)((JComboBox<?>)(ae.getSource())).getParent().getParent()).info[j] = String.valueOf(((JComboBox<?>)child).getSelectedItem());
					switch (((MenuItem)((JComboBox<?>)(ae.getSource())).getParent().getParent()).info[7]) {
					case "6.95":
						switch (((MenuItem)((JComboBox<?>)(ae.getSource())).getParent().getParent()).info[j]) {
						case "Chicken":case "Beef":case "Pork":case "Veggies/Tofu":
							((MenuItem)((JComboBox<?>)(ae.getSource())).getParent().getParent()).info[2] = "6.95";
							((JLabel)(parent[2])).setText("$" + ((MenuItem)((JComboBox<?>)(ae.getSource())).getParent().getParent()).info[2]);							
							break;
						case "Squid":case "Shrimp":
							((MenuItem)((JComboBox<?>)(ae.getSource())).getParent().getParent()).info[2] = "7.95";
							((JLabel)(parent[2])).setText("$" + ((MenuItem)((JComboBox<?>)(ae.getSource())).getParent().getParent()).info[2]);
							break;
						}
						break;
					case "8.95":
						switch (((MenuItem)((JComboBox<?>)(ae.getSource())).getParent().getParent()).info[j]) {
						case "Chicken":case "Beef":case "Pork":case "Veggies/Tofu":
							((MenuItem)((JComboBox<?>)(ae.getSource())).getParent().getParent()).info[2] = "8.95";
							((JLabel)(parent[2])).setText("$" + ((MenuItem)((JComboBox<?>)(ae.getSource())).getParent().getParent()).info[2]);
							break;
						case "Squid":case "Shrimp":
							((MenuItem)((JComboBox<?>)(ae.getSource())).getParent().getParent()).info[2] = "9.95";
							((JLabel)(parent[2])).setText("$" + ((MenuItem)((JComboBox<?>)(ae.getSource())).getParent().getParent()).info[2]);
							break;
						}
						break;
					case "9.95":
						switch (((MenuItem)((JComboBox<?>)(ae.getSource())).getParent().getParent()).info[j]) {
						case "Chicken":case "Pork":case "Veggies/Tofu":
							((MenuItem)((JComboBox<?>)(ae.getSource())).getParent().getParent()).info[2] = "9.95";
							((JLabel)(parent[2])).setText("$" + ((MenuItem)((JComboBox<?>)(ae.getSource())).getParent().getParent()).info[2]);
							break;
						case "Beef":
							((MenuItem)((JComboBox<?>)(ae.getSource())).getParent().getParent()).info[2] = "10.95";
							((JLabel)(parent[2])).setText("$" + ((MenuItem)((JComboBox<?>)(ae.getSource())).getParent().getParent()).info[2]);
							break;
						case "Squid":case "Shrimp":
							((MenuItem)((JComboBox<?>)(ae.getSource())).getParent().getParent()).info[2] = "13.95";
							((JLabel)(parent[2])).setText("$" + ((MenuItem)((JComboBox<?>)(ae.getSource())).getParent().getParent()).info[2]);
							break;
						}
						break;
					case "10.95":
						switch (((MenuItem)((JComboBox<?>)(ae.getSource())).getParent().getParent()).info[j]) {
						case "Chicken":case "Pork":case "Veggies/Tofu":
							((MenuItem)((JComboBox<?>)(ae.getSource())).getParent().getParent()).info[2] = "10.95";
							((JLabel)(parent[2])).setText("$" + ((MenuItem)((JComboBox<?>)(ae.getSource())).getParent().getParent()).info[2]);
							break;
						case "Beef":
							((MenuItem)((JComboBox<?>)(ae.getSource())).getParent().getParent()).info[2] = "11.95";
							((JLabel)(parent[2])).setText("$" + ((MenuItem)((JComboBox<?>)(ae.getSource())).getParent().getParent()).info[2]);
							break;
						case "Squid":case "Shrimp":
							((MenuItem)((JComboBox<?>)(ae.getSource())).getParent().getParent()).info[2] = "13.95";
							((JLabel)(parent[2])).setText("$" + ((MenuItem)((JComboBox<?>)(ae.getSource())).getParent().getParent()).info[2]);
							break;
						}
						break;
					}
				}
			});
			meatBox.setOpaque(true);
			meatBox.setBackground(boxColor);
			meatBox.setForeground(textColor);
			sl.putConstraint(SpringLayout.WEST, meatBox, 10, SpringLayout.EAST, meatLabel);
			sl.putConstraint(SpringLayout.NORTH, meatBox, 13, SpringLayout.SOUTH, numberLabel);
			internalPanel.add(meatBox);
		}
		
		if (hasSpice) {
			spiceLabel = new JLabel("Spice Level:");
			spiceLabel.setOpaque(true);
			spiceLabel.setBackground(itemColor);
			spiceLabel.setForeground(textColor);
			sl.putConstraint(SpringLayout.NORTH, spiceLabel, 15, SpringLayout.SOUTH, numberLabel);
			if (hasMeats) {
				sl.putConstraint(SpringLayout.WEST, spiceLabel, 20, SpringLayout.EAST, meatBox);
			} else {
				sl.putConstraint(SpringLayout.WEST, spiceLabel, 5, SpringLayout.WEST, internalPanel);
			}
			internalPanel.add(spiceLabel);
			spiceBox = new JSpinner(spiceSpinModel);
			JSEventHandler(spiceBox);
			spiceBox.setOpaque(true);
			spiceBox.setBackground(boxColor);
			spiceBox.setForeground(textColor);
			sl.putConstraint(SpringLayout.WEST, spiceBox, 10, SpringLayout.EAST, spiceLabel);
			sl.putConstraint(SpringLayout.NORTH, spiceBox, 15, SpringLayout.SOUTH, numberLabel);
			internalPanel.add(spiceBox);
		}
		
		qtyLabel = new JLabel("Quantity:");
		qtyLabel.setOpaque(true);
		qtyLabel.setBackground(itemColor);
		qtyLabel.setForeground(textColor);
		sl.putConstraint(SpringLayout.NORTH, qtyLabel, 15, SpringLayout.SOUTH, numberLabel);
		if (hasSpice) {
			sl.putConstraint(SpringLayout.WEST, qtyLabel, 20, SpringLayout.EAST, spiceBox);
		} else if (hasMeats) {
			sl.putConstraint(SpringLayout.WEST, qtyLabel, 20, SpringLayout.EAST, meatBox);
		} else {
			sl.putConstraint(SpringLayout.WEST, qtyLabel, 5, SpringLayout.WEST, internalPanel);
		}
		internalPanel.add(qtyLabel);
		qtyBox = new JSpinner(qtySpinModel);
		JSEventHandler(qtyBox);
		qtyBox.setOpaque(true);
		qtyBox.setBackground(boxColor);
		qtyBox.setForeground(textColor);
		sl.putConstraint(SpringLayout.WEST, qtyBox, 10, SpringLayout.EAST, qtyLabel);
		sl.putConstraint(SpringLayout.NORTH, qtyBox, 15, SpringLayout.SOUTH, numberLabel);
		internalPanel.add(qtyBox);
		
		commentLabel = new JLabel("Comments:");
		commentLabel.setOpaque(true);
		commentLabel.setBackground(itemColor);
		commentLabel.setForeground(textColor);
		sl.putConstraint(SpringLayout.WEST, commentLabel, 5, SpringLayout.WEST, internalPanel);
		sl.putConstraint(SpringLayout.NORTH, commentLabel, 50, SpringLayout.SOUTH, numberLabel);
		internalPanel.add(commentLabel);
		DefaultFormatter dformatter = new DefaultFormatter();
		dformatter.setCommitsOnValidEdit(true);
		commentBox = new JFormattedTextField(dformatter);
		TFEventHandler(commentBox);
		commentBox.setPreferredSize(new Dimension(300, 20));
		commentBox.setOpaque(true);
		commentBox.setBackground(new Color(220,220,220));
		commentBox.setForeground(textColor);
		sl.putConstraint(SpringLayout.WEST, commentBox, 10, SpringLayout.EAST, commentLabel);
		sl.putConstraint(SpringLayout.NORTH, commentBox, 50, SpringLayout.SOUTH, numberLabel);
		internalPanel.add(commentBox);
		
		internalPanel.setPreferredSize(new Dimension(500, 100));
		internalPanel.setMinimumSize(new Dimension(500, 100));
		internalPanel.setMaximumSize(new Dimension(500, 100));
		internalPanel.addMouseListener(new MouseListener() {
			//This Mouse Listener allows the ID of the MenuItem what was clicked on to be sent to its parent.
			//If it is in the menu, it is added to the cart, if it is in the cart it is deleted from the cart
			public void mouseClicked(MouseEvent me) {
				if (me.getClickCount() == 2) {
					itemIDSender(me);
				}
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {}
			
			@Override
			public void mouseExited(MouseEvent e) {}
			
			@Override
			public void mousePressed(MouseEvent e) {}
			
			@Override
			public void mouseReleased(MouseEvent e) {}
		});
				
		this.setPreferredSize(new Dimension(506, 110));
		this.setMinimumSize(new Dimension(506, 110));
		this.setMaximumSize(new Dimension(506, 110));
		this.add(internalPanel);
		this.setBackground(Color.DARK_GRAY);
	}
	//This enum contains all the information to create each MenuItem.  Right now it is used to feed the info into
	//the program, but a database would allow users to change the menu without having to go in and change the program
	//This portion of the code will most likely be replaced with a database in the future, and anything that access it
	//then access the database itself
	public enum Item {
		FOOD1 {
			@Override
			public void setParams() {
				number = "1";
				name = "Tom Yum Koong";
				originalPrice = "4.95";
				price = originalPrice;
				hasMeats = false; // Does not have meat/veggie options
				hasSpice = true;  // Has the option to get spice
			}
		},
		FOOD2 {
			@Override
			public void setParams() {
				number ="2";
				name = "Tom Kha Gai";
				originalPrice = "4.95";
				price = originalPrice;
				hasMeats = false; // Does not have meat/veggie options
				hasSpice = true;  // Does have the option to get spice
			}
		},
		FOOD3 {
			@Override
			public void setParams() {
				number = "3";
				name = "Jurd Woon Sen";
				originalPrice = "4.95";
				price = originalPrice;
				hasMeats = false; // Does not have meat/veggie options
				hasSpice = false; // Does not have the option to get spice
			}
		},
		FOOD4 {
			@Override
			public void setParams() {
				number = "4";
				name = "Pad Kra Pao";
				originalPrice = "6.95";
				price = originalPrice;
				hasMeats = true;  // Does have meat/veggie options
				hasSpice = true;  // Has the option to get spice;
			}
		},
		FOOD5 {
			@Override
			public void setParams() {
				number = "5";
				name = "Pad Kra Tiem";
				originalPrice = "6.95";
				price = originalPrice;
				hasMeats = true;
				hasSpice = false;
			}
		},
		FOOD6 {
			@Override
			public void setParams() {
				number = "6";
				name = "Pad Tou Kak";
				originalPrice = "6.95";
				price = originalPrice;
				hasMeats = true;
				hasSpice = true;
			}
		},
		FOOD7 {
			@Override
			public void setParams() {
				number = "7";
				name = "Pad Khing";
				originalPrice = "6.95";
				price = originalPrice;
				hasMeats = true;
				hasSpice = true;
			}
		};
		public abstract void setParams(); // Dummy method for override
	}
	//This method sends the ID given by the mouse event along to the parent panel of the MenuItem (cart or menu)
	public void itemIDSender(MouseEvent me) {
		id = Integer.parseInt(((JLabel)((JPanel) me.getSource()).getComponents()[0]).getText());
		if (this.getParent() instanceof MenuPanel) {
			((MenuPanel)((JPanel)me.getSource()).getParent().getParent()).itemIDGetter(id);
		} else if (this.getParent() instanceof CartPanel) {
			((CartPanel)((JPanel)me.getSource()).getParent().getParent()).itemIDGetter(id);
		}
	}
	//This adds the event listeners to the JSpinners for spice level and quantity, if they exist in each of the MenuItems
	//When the state of the JSpinner is changed (on edit or on arrow click) an event is fired that changes the value
	//of the property in the info array
	public void JSEventHandler(JSpinner spinner) {
		((DefaultFormatter)((JFormattedTextField)spinner.getEditor().getComponents()[0]).getFormatter()).setCommitsOnValidEdit(true);
	    spinner.addChangeListener(new ChangeListener() {
	    	@Override
	    	public void stateChanged(ChangeEvent ce) {
	    		int i;
	    		Component child = (Component) ce.getSource();
	    		Component[] parent = ((JPanel)((JComponent)ce.getSource()).getParent()).getComponents();
	    		for (i = 0;i<parent.length;i++) {
	    			if (parent[i] == child) {
	    				break;
	    			}
	    		}
	    		switch (((JLabel)(parent[i-1])).getText()) {
	    		case "Spice Level:":
	    			((MenuItem)((JSpinner)(ce.getSource())).getParent().getParent()).info[4] = String.valueOf(((JSpinner) parent[i]).getValue());
	    			break;
	    		case "Quantity:":
	    			((MenuItem)((JSpinner)(ce.getSource())).getParent().getParent()).info[5] = String.valueOf(((JSpinner) parent[i]).getValue());
	    			break;
	    		}
	    	}
	    });
	}
	//This method has a similar purpose to the method above, except it is for a JFormattedTextField (the comment box)
	public void TFEventHandler(JFormattedTextField textField) {
		textField.addPropertyChangeListener("value", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent pce) {
				((MenuItem)((JPanel)((JFormattedTextField)pce.getSource()).getParent()).getParent()).info[6] =((JFormattedTextField)pce.getSource()).getText();
			}
		});
	}
}