package com.nistica.tk;

import java.util.List;

public class XMLTester {
	public List<MenuItem> theMenu;
	public XMLTester(){
		MenuParser read = new MenuParser();
		theMenu = read.readMenu("menu.xml");
		/*for(MenuItem menuItem : theMenu)
			System.out.println(menuItem);
		*/
	}

}
