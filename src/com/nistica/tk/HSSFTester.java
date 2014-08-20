package com.nistica.tk;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.util.*;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.hssf.usermodel.*;

public class HSSFTester 
{
	private GregorianCalendar gc;
	private String dateString;
	public static String fileString;
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
	FileChannel channel;
	FileLock lock;
	
	private final String[] headers = {"Initials", "Special #", "Meat", "Spice #", "Quantity", "Comments", "Price"};
	
    public HSSFTester() {
    	gc = new GregorianCalendar();
    	dateString = "" + gc.get(Calendar.YEAR) + String.format("%02d", (gc.get(Calendar.MONTH)+1)) + gc.get(Calendar.DAY_OF_MONTH);
		fileString = "orders/thaiorder" + dateString + ".xls";
		templateLocation = "/ordersTemplate/TEMPLATE.xls";
    	//fileString = "orders/TEMPLATE.xls";
		init();
    }
    
    public boolean init(){

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
		try {
			channel = new RandomAccessFile(file, "rw").getChannel();
			try {
	            lock = channel.tryLock();
	        } catch (OverlappingFileLockException e) {
	            // File is already locked in this thread or virtual machine
	        	e.printStackTrace();
	        } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
		} catch (FileNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
    	if (state == 1)
    		System.out.println("File successfully created");
    	else if (state == 0)
    		System.out.println("Failed to create File");
    	else if (state == 2)
    		System.out.println("File already exists");
    	
    	try {
    		 // Release the lock
	        lock.release();

	        // Close the file
	        channel.close();
			fileIn = new FileInputStream(fileString);
			System.out.println(fileString);
			workbook = new HSSFWorkbook(fileIn);
			fileIn.close();
			
	       
			
		} catch (IOException e) {
			e.printStackTrace();
			if(fileIn!=null)
				try {
					fileIn.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			return false;
		}
    	/*try {
	        // Get a file channel for the file

			//file = new File(fileString);
	        channel = new RandomAccessFile(file, "rw").getChannel();

	        // Use the file channel to create a lock on the file.
	        // This method blocks until it can retrieve the lock.
	        //lock = channel.lock();

	        
	           use channel.lock OR channel.tryLock();
	        

	        // Try acquiring the lock without blocking. This method returns
	        // null or throws an exception if the file is already locked.
	        try {
	            lock = channel.tryLock();
	        } catch (OverlappingFileLockException e) {
	            // File is already locked in this thread or virtual machine
	        	e.printStackTrace();
	        }

	        // Release the lock
	        lock.release();

	        // Close the file
	        channel.close();
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }*/
    	sheet = workbook.getSheet("new sheet");
        
    	rowCS = workbook.createCellStyle();
		rowFont = workbook.createFont();
		rowFont.setFontHeightInPoints((short)11);
		rowCS.setFont(rowFont);
	    return true;
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
			//file = new File(fileString);
	        channel = new RandomAccessFile(file, "rw").getChannel();

	        // Use the file channel to create a lock on the file.
	        // This method blocks until it can retrieve the lock.
	        //lock = channel.lock();
	        try {
	            lock = channel.tryLock();
	        } catch (OverlappingFileLockException e) {
	            // File is already locked in this thread or virtual machine
	        	e.printStackTrace();
	        }
	        
			System.out.println(fileString);
			fileIn = new FileInputStream(fileString);
		} catch (IOException e) {
			e.printStackTrace();
			successful = false;
			return false;
		}
		try {
			  lock.release();

		        // Close the file
		        channel.close();
			checker =new HSSFWorkbook(fileIn);
			
			 channel = new RandomAccessFile(file, "rw").getChannel();

		        // Use the file channel to create a lock on the file.
		        // This method blocks until it can retrieve the lock.
		        //lock = channel.lock();
		        try {
		            lock = channel.tryLock();
		        } catch (OverlappingFileLockException e) {
		            // File is already locked in this thread or virtual machine
		        	e.printStackTrace();
		        }
		} catch (IOException e) {
			e.printStackTrace();
			successful = false;
			return false;
		}
		checkerSheet = checker.getSheet("new sheet");
		do {} while (checkerSheet.getRow(i++) != null);
		Row newOrderRow = sheet.createRow(--i);
		int startRow = 1;
		newOrderRow.createCell(startRow).setCellValue(info[0]);
		if (info[1] != "") {
			newOrderRow.createCell(startRow+1).setCellValue(Integer.parseInt(info[1]));
		} else {
			newOrderRow.createCell(startRow+1).setCellValue(info[1]);
		}
		newOrderRow.createCell(startRow+2).setCellValue(info[2]);
		if (info[3] != "") {
			newOrderRow.createCell(startRow+3).setCellValue(Integer.parseInt(info[3]));
		} else {
			newOrderRow.createCell(startRow+3).setCellValue(info[3]);	
		}
		newOrderRow.createCell(startRow+4).setCellValue(Integer.parseInt(info[4]));
		newOrderRow.createCell(startRow+5).setCellValue(info[5]);
		newOrderRow.createCell(startRow+6).setCellValue(Double.parseDouble(info[6]));	
		System.out.println("THIS IS #6---" + info[6]);
		HSSFFormulaEvaluator form = new HSSFFormulaEvaluator(workbook);
		sheet.getRow(3).getCell(9).setCellType(Cell.CELL_TYPE_FORMULA);
		sheet.getRow(3).getCell(9).setCellFormula("SUM(H:H)");
		sheet.getRow(5).getCell(9).setCellType(Cell.CELL_TYPE_FORMULA);;
		sheet.getRow(5).getCell(9).setCellFormula("J4*0.07");
		sheet.getRow(7).getCell(9).setCellType(Cell.CELL_TYPE_FORMULA);;
		sheet.getRow(7).getCell(9).setCellFormula("J4+J6");
        try {
        	lock.release();

	        // Close the file
	        channel.close();
        	fileOut = new FileOutputStream(fileString);
        	
        	
        	workbook.write(fileOut);
            
        } catch (Exception e) {
            e.printStackTrace(); 
            successful = false;
            return false;
        } finally{
        	if(fileOut!=null)
				try {
					fileOut.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	
        }
		/*if(!writeWorkbook())
			successful = false;*/
       
        return successful;
	}
	private boolean writeWorkbook(){
		 System.out.println("Trying to write to xls");
        try
	    {
        	fileOut = new FileOutputStream(fileString);
        	workbook.write(fileOut);
            fileOut.close();
			
	    }
	    catch (IOException err) // Can't access
	    {
	        try
	        {
	            Thread.sleep(200); // Sleep a bit
	            return writeWorkbook(); // Try again
	        }
	        catch (InterruptedException err2)
	        {
	           return writeWorkbook(); // Could not sleep, try again anyway
	        }
	    }
        return true;
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