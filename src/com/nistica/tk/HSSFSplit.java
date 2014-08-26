package com.nistica.tk;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.util.*;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.hssf.usermodel.*;

@SuppressWarnings ("unused")
public class HSSFSplit
{
	private GregorianCalendar gc;
	private String dateString;
	public static String fileString;
	private String templateLocation;
	private String finalString;
	private File file;
	
	public HSSFWorkbook workbook;
	public HSSFCreationHelper createHelper;
	public HSSFSheet sheet;
	public FileInputStream fileIn;
	public FileOutputStream fileOut;
	private HSSFCellStyle rowCS;
	private HSSFFont rowFont;
	FileChannel channel;
	FileLock lock;
	private boolean init;
	
	public HSSFSplit() {
    	gc = new GregorianCalendar();
    	dateString = "" + gc.get(Calendar.YEAR) + String.format("%02d", (gc.get(Calendar.MONTH)+1)) + gc.get(Calendar.DAY_OF_MONTH);
		templateLocation = "/ordersTemplate/TEMPLATE.xls";
		finalString = "orders/thaiorder" + dateString + ".xls";
		//init();
		init = false;
    }
    
    public boolean init() {
    	file = new File(fileString);
		
		int state = 2;
		boolean created = false; 
    	    	                
		if (!file.exists()) {
    		try {
    			InputStream tempIn = HSSFTester.class.getResourceAsStream(templateLocation);
    			HSSFWorkbook temp = new HSSFWorkbook(tempIn);
    			FileOutputStream tempOut = new FileOutputStream(finalString);
    			temp.write(tempOut);
    			tempOut.close();
    			HSSFWorkbook indiv = new HSSFWorkbook();
    			indiv.createSheet("new sheet");
    			tempOut = new FileOutputStream(fileString);
    			indiv.write(tempOut);
    			tempOut.close();
    			created = true;
    			  			
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
				e.printStackTrace();
			}  
		} catch (FileNotFoundException e2) {
			e2.printStackTrace();
			return false;
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
					e1.printStackTrace();
				}
			return false;
		}
    	
    	sheet = workbook.getSheet("new sheet");
    	rowCS = workbook.createCellStyle();
		rowFont = workbook.createFont();
		rowFont.setFontHeightInPoints((short)11);
		rowCS.setFont(rowFont);
	    return true;
    }
    
	public boolean addOrder(String[] info, String name) {
		int i = 0;
		boolean successful = true;
		HSSFWorkbook checker = null;
		HSSFSheet checkerSheet;
		fileString = "orders/indivOrders/" + name.toUpperCase() + dateString + ".xls";
		if (!init) {
			init = init();
		}
		try {
			
			channel = new RandomAccessFile(file, "rw").getChannel();

	        // Use the file channel to create a lock on the file.
	        // This method blocks until it can retrieve the lock.
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
		newOrderRow.getCell(startRow).setCellStyle(SetCS());
		/*if (info[1] != "") {
			newOrderRow.createCell(startRow+1).setCellValue(Integer.parseInt(info[1])+" " +info[7]);
		} else {*/
			newOrderRow.createCell(startRow+1).setCellValue(info[1]+" " +info[7]);
		//}
		newOrderRow.getCell(startRow+1).setCellStyle(SetCS());
		newOrderRow.createCell(startRow+2).setCellValue(info[2]);
		newOrderRow.getCell(startRow+2).setCellStyle(SetCS());
		/*if (info[3] != "") {
			newOrderRow.createCell(startRow+3).setCellValue(Integer.parseInt(info[3]));
		} else {*/
			newOrderRow.createCell(startRow+3).setCellValue(info[3]);	
		//}
		newOrderRow.getCell(startRow+3).setCellStyle(SetCS());
		newOrderRow.createCell(startRow+4).setCellValue(info[4]);
		newOrderRow.getCell(startRow+4).setCellStyle(SetCS());
		newOrderRow.createCell(startRow+5).setCellValue(info[5]);
		newOrderRow.getCell(startRow+5).setCellStyle(SetCS());
		newOrderRow.createCell(startRow+6).setCellValue(info[6]);
		newOrderRow.getCell(startRow+6).setCellStyle(SetCS());		
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
					e.printStackTrace();
				}
        	
        }
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
        style.setAlignment(CellStyle.ALIGN_CENTER);
        return style;
	}
}