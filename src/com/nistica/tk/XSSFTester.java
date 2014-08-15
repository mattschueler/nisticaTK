package com.nistica.tk;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class XSSFTester 
{
	private GregorianCalendar gc;
	private String dateString;
	private String fileString;
	private File file;
	
	public XSSFWorkbook workbook;
	public XSSFCreationHelper createHelper;
	public XSSFSheet sheet;
	public FileOutputStream fileOut;
	
	private final String[] headers = {"First name", "Last Name", "Special #", "Food Name", "Meat", "Spice #", "Quantity", "Comments", "Price"};;
	
    public XSSFTester() {
    	gc = new GregorianCalendar();
    	dateString = "" + gc.get(Calendar.YEAR) + String.format("%02d", (gc.get(Calendar.MONTH)+1)) + gc.get(Calendar.DAY_OF_MONTH);
		fileString = "orders/thaiorder" + dateString + ".xlsx";
		file = new File(fileString);
		int state = 2;
		boolean created = false; 
    	    	                
		if (!file.exists()) {
    		try {
    			created = file.createNewFile();
    		} catch (IOException ioe) {
    			ioe.printStackTrace();
    		}
    		if (created) 
    			state = 1;
    		else if (!created) 
    			state = 0;
    	}
    	if (state == 1)
    		System.out.println("File successfully created");
    	else if (state == 0)
    		System.out.println("Failed to create File");
    	else if (state == 2)
    		System.out.println("File already exists");
    	
    	workbook = new XSSFWorkbook();
    	createHelper = workbook.getCreationHelper();
    	sheet = workbook.createSheet("new sheet");
        try {      
    		fileOut = new FileOutputStream(fileString);
    	} catch (IOException ioe) {
    		ioe.printStackTrace();
    	}
    }
    
	public void addHeader()  {
		Row headerRow = sheet.createRow(0);
        for (int i=0;i<9;i++) {
        	headerRow.createCell(i).setCellValue(createHelper.createRichTextString(headers[i]));
        	sheet.autoSizeColumn(i);
        }
        try {
        	fileOut = new FileOutputStream(fileString);
        	workbook.write(fileOut);
            fileOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	public void addOrder(String[] info) {
		Row newOrderRow = sheet.createRow(sheet.getLastRowNum()+1);		
		for (int i=0;i<9;i++) {
        	newOrderRow.createCell(i).setCellValue(createHelper.createRichTextString(info[i]));
        	sheet.autoSizeColumn(i);
        }
        try {
        	fileOut = new FileOutputStream(fileString);
        	workbook.write(fileOut);
            fileOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
}