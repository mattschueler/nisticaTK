package com.nistica.tk;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.constraint.*;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

public class CSVTester {
	
	public static ArrayList<OrderBean> orders;
	
	public CSVTester() {
		orders = new ArrayList<OrderBean>();
	}
	
	private static CellProcessor[] getProcessors() {
		final CellProcessor[] processors = new CellProcessor[] {
				//new UniqueHashCode(), //customer number
				new NotNull(), //first name of customer
				new NotNull(), //last name of customer
				new NotNull(), //Food number
				new NotNull(), //food name
				new Optional(), //meat type
				new Optional(), //spice number
				new NotNull(), //qty
				new NotNull(), //comments
				new NotNull() //total price of item
		};
		return processors;
	}
	
	//OrderBean will represent individual item in each order, so each will
	//be sent out separately from the others to a LOCAL csv file
	//After each is completed, then the entire file is sent to the server
	//and the order is charged to the client user
	//For info on how to compile the list of csv orders, see this website
	//http://www.solveyourtech.com/merge-csv-files/
	//The most likely solution is to use a batch file to execute the command
	//prompt code, or just run it from inside of the java program, if that
	//is possible
	public void addOrder(final String firstName, final String lastName, final String foodNum, final String foodName, 
			final String meatType, final String spiceNum, final String qty, final String comments, final String price) throws Exception {
		final OrderBean foodOrder = new OrderBean(firstName, lastName, foodNum, foodName, meatType, spiceNum, qty, comments, price);
		orders.add(foodOrder);
	}
	public void writeWithCsvBeanWriter() {
		ICsvBeanWriter beanWriter = null;
        try {
        	GregorianCalendar gc = new GregorianCalendar();
        	String fileName = "orders/thaiorder" + gc.get(Calendar.YEAR) + String.format("%0"+2+"d",(gc.get(Calendar.MONTH)+1)) + gc.get(Calendar.DATE) + ".csv";
            File f = new File(fileName);
            // the header elements are used to map the bean values to each column (names must match)
            final String[] header = new String[] {"firstName", "lastName", "foodNum", "foodName", "meatType", "spiceNum", "qty", "comments", "price"};
            final CellProcessor[] processors = getProcessors();
        	if (f.exists()) {
        	beanWriter = new CsvBeanWriter(new FileWriter(fileName, true),
            		CsvPreference.STANDARD_PREFERENCE);
        	} else {
        		beanWriter = new CsvBeanWriter(new FileWriter(fileName),
                		CsvPreference.STANDARD_PREFERENCE);
        		//write the header
        		beanWriter.writeHeader(header);
        	}
            // write the beans
            for( final OrderBean order : orders ) {
                    beanWriter.write(order, header, processors);
            }
            
        } catch (IOException e) {
			e.printStackTrace();
		}
        finally {
                if( beanWriter != null ) {
                        try {
							beanWriter.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
                }
        }
        orders = new ArrayList<OrderBean>();
	}
}
