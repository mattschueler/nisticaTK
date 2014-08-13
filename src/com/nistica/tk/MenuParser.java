package com.nistica.tk;

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
	static final String FOOD = "food";
	static final String NUMBER = "number";
	static final String NAME = "name";
	static final String ORIGINALPRICE = "originalPrice";
	static final String HASMEATS = "hasMeats";
	static final String HASSPICE = "hasSpice";
	
	public List<MenuItem> readMenu(String menuFile){
		List<MenuItem> items = new ArrayList<MenuItem>();
		
		try{
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			
			InputStream in = new FileInputStream(menuFile);
			XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
			
			//read the file
			MenuItem item = null;
			while(eventReader.hasNext()){
				XMLEvent event = eventReader.nextEvent();
				if(event.isStartElement()){
					StartElement startElement = event.asStartElement();
					//If it's a food make a new menuitem
					if(startElement.getName().getLocalPart() == FOOD){
						item = new MenuItem();
						//get all the attributes
						
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
						//System.out.println(event.asCharacters().getData());
						continue;
					}					
				}
				if(event.isStartElement()){
					if(event.asStartElement().getName().getLocalPart().equals(ORIGINALPRICE)){
						event = eventReader.nextEvent();
						
						item.setOriginalPrice(event.asCharacters().getData());
						//System.out.println(event.asCharacters().getData());
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
				if(event.isEndElement()){
					EndElement endElement = event.asEndElement();
					if(endElement.getName().getLocalPart() == FOOD){
						item.setInfo();
						item.createComponents();
						items.add(item);
					}
				}
				
			}
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}catch(XMLStreamException e){
			e.printStackTrace();
		}
		return items;
	}
}
