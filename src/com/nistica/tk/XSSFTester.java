package com.nistica.tk;

import java.io.*;
import java.util.*;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

public class XSSFTester 
{
	private GregorianCalendar gc;
	private String dateString;
	private String fileString;
	private File file;
	
	public XSSFWorkbook workbook;
	public XSSFCreationHelper createHelper;
	public XSSFSheet sheet;
	public FileInputStream fileIn;
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
        sheet.getPrintSetup().setLandscape(true);
    }
    
	public void addHeader()  {
		Row headerRow = sheet.createRow(0);
        for (int i=0;i<9;i++) {
        	headerRow.createCell(i).setCellValue(headers[i]);
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
		int i = 0;
		Cell checkerCell;
		Workbook checker = null;
		try {
			fileIn = new FileInputStream(fileString);
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		}
		try {
			checker = WorkbookFactory.create(fileIn);
		} catch (InvalidFormatException | IOException e) {
			e.printStackTrace();
		}
		do {
			checkerCell = checker.getSheet("new sheet").getRow(0).getCell(i++);
		} while (checkerCell != null);
		Row newOrderRow = sheet.createRow(sheet.getLastRowNum()+1);
		for (int j=0;j<9;j++) {
			if ((j==2 || j==5 || j==6) && info[j] != "") {
				newOrderRow.createCell(j).setCellValue(Integer.parseInt(info[j]));
			} else if (j==8) {
				newOrderRow.createCell(j).setCellValue(Double.parseDouble(info[j]));
			} else {
				newOrderRow.createCell(j).setCellValue(info[j]);
			}
			newOrderRow.getCell(j).setCellStyle(SetCS());
        	sheet.autoSizeColumn(j);
        }
        try {
        	fileOut = new FileOutputStream(fileString);
        	workbook.write(fileOut);
            fileOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
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