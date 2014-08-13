package com.nistica.tk;

import java.util.List;

public class XMLTester {
	public static void main(String[] args){
		MenuParser read = new MenuParser();
		List<MenuItem> readMenu = read.readMenu("menu.xml");
		for(MenuItem menuItem : readMenu)
			System.out.println(menuItem);
	}

}
