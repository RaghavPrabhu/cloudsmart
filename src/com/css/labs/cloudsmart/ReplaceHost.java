package com.css.labs.cloudsmart;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;

import org.apache.tools.ant.Task;

/**
 * @author Hajamaideen Abdul
 * 
 */
public class ReplaceHost extends Task {

	private String fileLocation;	
	private String replaceString;
	private String searchString;
	private String hostName;

	/**
	 * @param hostName the hostName to set
	 */
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public void setFileLocation(String fileLocation) {
		this.fileLocation = fileLocation;
	}

	public void setReplaceString(String replaceString) {
		this.replaceString = replaceString;
	}

	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}

	public void execute() {
		File file = null;
		FileInputStream fileInputStream = null;
		BufferedInputStream bufferedInputStream = null;
		FileOutputStream fileOutputStream = null;
		BufferedOutputStream bufferedOutputStream = null;
		
		try {
			InetAddress addr = InetAddress.getLocalHost();
			file = new File(fileLocation);
			fileInputStream = new FileInputStream(file);
			bufferedInputStream = new BufferedInputStream(fileInputStream);
			byte fileData[] = new byte[bufferedInputStream.available()];
			bufferedInputStream.read(fileData, 0, fileData.length);
            
			String srcContent = new String(fileData);
			
			if(this.hostName!=null){
				replaceString=addr.getHostName();
			}
			
			
			String destContent = srcContent.replaceAll(searchString, replaceString);
	        System.out.println("Replaced the string...");        
	        
	        fileOutputStream = new FileOutputStream(file);
	        bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
	        bufferedOutputStream.write(destContent.getBytes());        
	        
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				bufferedInputStream.close();
		        fileInputStream.close();
			}
			catch (IOException exception) {
				exception.printStackTrace();
			}
			try {
				bufferedOutputStream.close();
		        fileOutputStream.close();
			}
			catch (IOException exception) {
				exception.printStackTrace();
			}
		}
	}
}
