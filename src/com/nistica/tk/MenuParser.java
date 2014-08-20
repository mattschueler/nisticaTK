package com.nistica.tk;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

@SuppressWarnings("unused")
public class MenuParser {
	static final String LUNCH = "lunch";
	static final String DINNER = "dinner";
	static final String SIDE = "side";
	static final String BEVERAGE = "beverage";
	static final String SOUP = "soup";
	static final String ENTREE = "entree";
	static final String APPETIZERS = "appetizer";
	//soup in dinner too
	static final String SALAD = "salad";
	static final String YUMYUM = "yum_yum";
	static final String SPECIAL = "chefs_special";
	static final String ENTREE_MAIN= "entree_main";
	static final String THAI_CURRY= "entree_thai_curry";
	static final String DUCK = "entree_duck";
	static final String NOODLES_FRIED= "entree_noodles_fried_rice";
	
	static final String FOOD = "food";
	static final String NUMBER = "number";
	static final String NAME = "name";
	static final String ORIGINALPRICE = "originalPrice";
	static final String HASMEATS = "hasMeats";
	static final String HASSPICE = "hasSpice";
	static final String DESC = "desc";
	
	public List<MenuItem> readMenu(String menuFile, String course, String category){
		List<MenuItem> items = new ArrayList<MenuItem>();
		boolean correctCourse = false;
		boolean correctCategory = false;
		
		try{
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			
			InputStream in = new FileInputStream(new File(menuFile));
			XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
			
			//read the file
			MenuItem item = null;
			while(eventReader.hasNext()){
				XMLEvent event = eventReader.nextEvent();
				if(event.isStartElement()){
					StartElement startElement = event.asStartElement();
					//If it's a food make a new menuitem
					
					if(startElement.getName().getLocalPart().equals(course)){
						correctCourse = true;
						//get all the attributes
						
					}
					
					if(correctCourse && startElement.getName().getLocalPart().equals(category))
						correctCategory = true;
					
				}
				if(correctCourse != true || correctCategory != true)
					continue;
				
				if(event.isStartElement()){
					if(event.asStartElement().getName().getLocalPart().equals(FOOD)){
						item = new MenuItem();
						continue;
					}					
				}
				if(event.isStartElement()){
					if(event.asStartElement().getName().getLocalPart().equals(NUMBER)){
						event = eventReader.nextEvent();
						item.setNumber(event.asCharacters().getData());
						continue;
					}					
				}
				if(event.isStartElement()){
					if(event.asStartElement().getName().getLocalPart().equals(NAME)){
						event = eventReader.nextEvent();
						item.setName(event.asCharacters().getData());
						continue;
					}					
				}
				if(event.isStartElement()){
					if(event.asStartElement().getName().getLocalPart().equals(ORIGINALPRICE)){
						event = eventReader.nextEvent();
						
						item.setOriginalPrice(event.asCharacters().getData());
						continue;
					}					
				}
				if(event.isStartElement()){
					if(event.asStartElement().getName().getLocalPart().equals(HASMEATS)){
						event = eventReader.nextEvent();
						item.setHasMeats(event.asCharacters().getData().equals("true") ? true : false);
						continue;
					}					
				}
				if(event.isStartElement()){
					if(event.asStartElement().getName().getLocalPart().equals(HASSPICE)){
						event = eventReader.nextEvent();
						item.setHasSpice(event.asCharacters().getData().equals("true") ? true : false);
						continue;
					}					
				}
				if(event.isStartElement()){
					if(event.asStartElement().getName().getLocalPart().equals(DESC)){
						event = eventReader.nextEvent();
						item.setDesc(event.asCharacters().getData());
						continue;
					}					
				}
				if(event.isEndElement()){
					EndElement endElement = event.asEndElement();
					if(endElement.getName().getLocalPart() == FOOD){
						item.setCourse(course);
						item.setInfo();
						item.createComponents();
						
						items.add(item);
					}
					
					if(endElement.getName().getLocalPart().equals(course))
					{
						correctCourse = false;
					}
					
					if(endElement.getName().getLocalPart().equals(category))
					{
						correctCategory = false;
						break;
					}
				}
				
			}
		}catch(XMLStreamException e){
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return items;
	}
}
