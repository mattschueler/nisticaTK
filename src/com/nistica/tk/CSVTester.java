package com.nistica.tk;

import java.io.FileWriter;
import java.util.List;

import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.constraint.*;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

public class CSVTester {
	
	public static List<OrderBean> orders;
	
	private static CellProcessor[] getProcessors() {
		final CellProcessor[] processors = new CellProcessor[] {
				//new UniqueHashCode(), //customer number
				new NotNull(), //first name of customer
				new NotNull(), //last name of customer
				new NotNull(), //Food number
				new NotNull(), //food name
				new NotNull(), //item price
				new Optional(), //meat type
				new Optional(), //spice number
				new NotNull(), //qty
				new NotNull(), //comments
				new NotNull() //total price of item
		};
		return processors;
	}
	
	public static void writeWithCsvBeanWriter(final String firstName, final String lastName, final String foodNum, final String foodName, final String unitPrice, 
			final String meatType, final String spiceNum, final String qty, final String comments, final String totalPrice) throws Exception {
		//OrderBean will represent individual item in each order, so each will
		//be sent out separately from the others to a LOCAL csv file
		//After each is completed, then the entire file is sent to the server
		//and the order is charged to the client user
		//For info on how to compile the list of csv orders, see this website
		//http://www.solveyourtech.com/merge-csv-files/
		//The most likely solution is to use a batch file to execute the command
		//prompt code, or just run it from inside of the java program, if that
		//is possible
		final OrderBean foodOrder = new OrderBean(firstName, lastName, foodNum, foodName, unitPrice, meatType, spiceNum, qty, comments, totalPrice);
		orders.add(foodOrder);
		ICsvBeanWriter beanWriter = null;
        try {
                beanWriter = new CsvBeanWriter(new FileWriter("target/writeWithCsvBeanWriter.csv"),
                        CsvPreference.STANDARD_PREFERENCE);
                
                // the header elements are used to map the bean values to each column (names must match)
                final String[] header = new String[] {"firstName", "lastName", "foodNum", "foodName", "unitPrice", "meatType", "spiceNum", "qty", "comments", "totalPrice"};
                final CellProcessor[] processors = getProcessors();
                
                // write the header
                beanWriter.writeHeader(header);
                
                // write the beans
                for( final OrderBean order : orders ) {
                        beanWriter.write(order, header, processors);
                }
                
        }
        finally {
                if( beanWriter != null ) {
                        beanWriter.close();
                }
        }
	}
}
