package com.nistica.tk;

import java.io.*;
import java.util.*;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.hssf.usermodel.*;

public class HSSFTester 
{
	private GregorianCalendar gc;
	private String dateString;
	private String fileString;
	private String templateLocation;
	private File file;
	
	public HSSFWorkbook workbook;
	public HSSFCreationHelper createHelper;
	public HSSFSheet sheet;
	public FileInputStream fileIn;
	public FileOutputStream fileOut;
	private HSSFCellStyle headerCS;
	private HSSFFont headerFont;
	private HSSFCellStyle rowCS;
	private HSSFFont rowFont;
	
	private final String[] headers = {"Initials", "Special #", "Meat", "Spice #", "Quantity", "Comments", "Price"};
	
    public HSSFTester() {
    	gc = new GregorianCalendar();
    	dateString = "" + gc.get(Calendar.YEAR) + String.format("%02d", (gc.get(Calendar.MONTH)+1)) + gc.get(Calendar.DAY_OF_MONTH);
		fileString = "orders/thaiorder" + dateString + ".xls";
		templateLocation = "/ordersTemplate/TEMPLATE.xls";
    	//fileString = "orders/TEMPLATE.xls";
		
		file = new File(fileString);
		int state = 2;
		boolean created = false; 
    	    	                
		if (!file.exists()) {
    		try {
    			//FileInputStream tempIn = new FileInputStream(templateLocation);
    			InputStream tempIn = HSSFTester.class.getResourceAsStream(templateLocation);
    			HSSFWorkbook temp = new HSSFWorkbook(tempIn);
    			FileOutputStream tempOut = new FileOutputStream(fileString);
    			temp.write(tempOut);
    			tempOut.close();
    			created=true;
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
    	
    	try {
			fileIn = new FileInputStream(fileString);
			System.out.println(fileString);
			workbook = new HSSFWorkbook(fileIn);
			fileIn.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	sheet = workbook.getSheet("new sheet");
        
    	rowCS = workbook.createCellStyle();
		rowFont = workbook.createFont();
		rowFont.setFontHeightInPoints((short)11);
		rowCS.setFont(rowFont);
	    
    }
	/*public void addHeader()  {
		//Put in "NISTICA" and "please send spring rolls" in really big font
		HSSFRow headerRow = sheet.createRow(0);		
		for (int i=5;i<14;i++) {
        	Cell c = headerRow.createCell(i);
        	c.setCellValue(headers[i-5]);
        	c.setCellStyle(headerCS);
        	//Cell c = headerRow.getCell(i);//.setCellStyle(headerCS);
        	//c.setCellStyle(headerCS);
        	sheet.autoSizeColumn(i);
        }
        try {
        	fileOut = new FileOutputStream(fileString);
        	workbook.write(fileOut);
            fileOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
	
	public boolean addOrder(String[] info) {
		int i = 0;
		boolean successful = true;
		HSSFWorkbook checker = null;
		HSSFSheet checkerSheet;
		try {
			System.out.println(fileString);
			fileIn = new FileInputStream(fileString);
		} catch (IOException e) {
			e.printStackTrace();
			successful = false;
		}
		try {
			checker =new HSSFWorkbook(fileIn);
		} catch (IOException e) {
			e.printStackTrace();
			successful = false;
		}
		checkerSheet = checker.getSheet("new sheet");
		do {} while (checkerSheet.getRow(i++) != null);
		Row newOrderRow = sheet.createRow(--i);
		int startRow = 1;
		newOrderRow.createCell(startRow).setCellValue(info[0]);
		newOrderRow.createCell(startRow+1).setCellValue(Integer.parseInt(info[1]));
		newOrderRow.createCell(startRow+2).setCellValue(info[2]);
		if (info[3] != "") {
			newOrderRow.createCell(startRow+3).setCellValue(Integer.parseInt(info[3]));
		} else {
			newOrderRow.createCell(startRow+3).setCellValue(info[3]);	
		}
		newOrderRow.createCell(startRow+4).setCellValue(Integer.parseInt(info[4]));
		newOrderRow.createCell(startRow+5).setCellValue(info[5]);
		newOrderRow.createCell(startRow+6).setCellValue(info[6]);		
        try {
        	fileOut = new FileOutputStream(fileString);
        	workbook.write(fileOut);
            fileOut.close();
        } catch (Exception e) {
            e.printStackTrace(); 
            successful = false;
        }
        return successful;
	}
	public CellStyle SetCS() {
		CellStyle style = workbook.createCellStyle();
		style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        return style;
	}
}