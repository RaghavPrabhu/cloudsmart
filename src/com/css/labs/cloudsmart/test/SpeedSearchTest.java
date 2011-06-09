package com.css.labs.cloudsmart.test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;

import org.apache.log4j.Logger;
import org.apache.tools.ant.Task;

/**
 * @author Hajamaideen Abdul
 * 
 */

public class SpeedSearchTest extends Task {
	
	static Logger logger = Logger.getLogger(SpeedSearchTest.class);
	
	private String fileLocation;	
	
	public void setFileLocation(String fileLocation) {
		this.fileLocation = fileLocation;
	}

	public boolean checkHostName() {
		boolean status = false;
		File file = null;
		FileInputStream fileInputStream = null;
		BufferedInputStream bufferedInputStream = null;		
		try {
			file = new File(fileLocation);
			fileInputStream = new FileInputStream(file);
			bufferedInputStream = new BufferedInputStream(fileInputStream);
			byte fileData[] = new byte[bufferedInputStream.available()];
			bufferedInputStream.read(fileData, 0, fileData.length);
			InetAddress address = InetAddress.getLocalHost();
			String searchURL = "Server="+address.getHostName();
			String fileContent = new String(fileData);
			if(fileContent.indexOf(searchURL) != -1) {
				status = true;
			}			
		}
		catch(IOException exception) {
			System.err.println("SLX Speed Search Exception : "+exception.getMessage());
		}
		finally {
			if(file != null) {
				try {
					bufferedInputStream.close();
			        fileInputStream.close();
				}
				catch (IOException exception) {
					System.err.println("SLX Speed Search Exception : "+exception.getMessage());
				}
			}
		}
		return status;
	}
	
	public void execute() {
		if(checkHostName()) {
			logger.info("The SLX Speed Search Server property is updated with local host name");
		}
		else {
			logger.info("The SLX Speed Search Server property is not updated with local host name");
		}
	}
}
