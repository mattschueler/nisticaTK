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
	private String standingString;
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
    	dateString = "" + gc.get(Calendar.YEAR) + String.format("%02d", (gc.get(Calendar.MONTH)+1)) + String.format("%02d", gc.get(Calendar.DAY_OF_MONTH));
		templateLocation = "/ordersTemplate/TEMPLATE.xls";
		finalString = "orders/thaiorder" + dateString + ".xls";
		standingString = "orders/weeklyOrders/WO_Orders.xls";
		//init();
		init = false;
    }
    
    public boolean init() {
    	file = new File(fileString);
		
		int state = 2;
		boolean created = false; 
    	    	                
		if (!file.exists()) {
    		try {
    			InputStream tempIn = HSSFSplit.class.getResourceAsStream(templateLocation);
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
    
	public boolean addOrder(String[] info, String name, int weeks) {
		int i = 0;
		boolean successful = true;
		HSSFWorkbook checker = null;
		HSSFSheet checkerSheet;
		
		if (weeks<=1) {
			fileString = "orders/indivOrders/" + name.toUpperCase() + dateString + ".xls";
		} else {
			fileString = standingString;
		}
			
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
		
		newOrderRow.createCell(0).setCellValue("" + weeks);
		newOrderRow.getCell(0).setCellStyle(SetCS());
		newOrderRow.createCell(1).setCellValue(info[0]);
		newOrderRow.getCell(1).setCellStyle(SetCS());
		newOrderRow.createCell(2).setCellValue(info[1]+ " " +info[7]);
		newOrderRow.getCell(2).setCellStyle(SetCS());
		newOrderRow.createCell(3).setCellValue(info[2]);
		newOrderRow.getCell(3).setCellStyle(SetCS());
		newOrderRow.createCell(4).setCellValue(info[3]);	
		newOrderRow.getCell(4).setCellStyle(SetCS());
		newOrderRow.createCell(5).setCellValue(info[4]);
		newOrderRow.getCell(5).setCellStyle(SetCS());
		newOrderRow.createCell(6).setCellValue(info[5]);
		newOrderRow.getCell(6).setCellStyle(SetCS());
		newOrderRow.createCell(7).setCellValue(info[6]);
		newOrderRow.getCell(7).setCellStyle(SetCS());		
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