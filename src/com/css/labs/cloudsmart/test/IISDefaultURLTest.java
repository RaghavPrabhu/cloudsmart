package com.css.labs.cloudsmart.test;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.apache.log4j.Logger;
import org.apache.tools.ant.Task;

import com.css.labs.cloudsmart.ReplaceHost;

/**
 * @author Hajamaideen Abdul
 * 
 */

public class IISDefaultURLTest extends Task {

	static Logger logger = Logger.getLogger(IISDefaultURLTest.class);
	
	private String fileLocation;	
	private String replaceString;
	private String searchString;
	
	public void setFileLocation(String fileLocation) {
		this.fileLocation = fileLocation;
	}

	public void setReplaceString(String replaceString) {
		this.replaceString = replaceString;
	}

	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}

	public boolean checkURL() {
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
			
			String searchURL = "https://"+replaceString+"/SLXClient";
			String fileContent = new String(fileData);
			if(fileContent.indexOf(searchURL) != -1) {
				status = true;
			}			
		}
		catch(IOException exception) {
			System.err.println("IIS Check Default URL Exception : "+exception.getMessage());
		}
		finally {
			if(file != null) {
				try {
					bufferedInputStream.close();
			        fileInputStream.close();
				}
				catch (IOException exception) {
					System.err.println("IIS Check Default URL Exception : "+exception.getMessage());
				}
			}
		}
		return status;
	}
	
	public boolean runURL() {
		boolean status = false;
		try {
			URL myurl = new URL("https://"+replaceString+"/SLXClient");
		    HttpsURLConnection connection = (HttpsURLConnection)myurl.openConnection();
		    InputStream inputStream = connection.getInputStream();
		    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
		    BufferedReader bufferedReader =new BufferedReader(inputStreamReader);

		    String inputLine;

		    while ((inputLine = bufferedReader.readLine()) != null) {
		        System.out.println(inputLine);
		        break;
		    }

		    bufferedReader.close();
		    
		    status = true;
		}
		catch(IOException exception) {
			System.err.println("IIS Run Default URL Exception : "+exception.getMessage());
		}
		
		return status;
	}
	
	public void execute() {
		// Check "https://Host_Name/SLXClient" URL in default.asp
		if(checkURL()) {
			logger.info("The URL is updated in the default web page");
			if(runURL()) {
				logger.info("The default page URL redirected");
			}
			else {
				logger.info("The redirected URL default page was not found");
			}
		}
		else {
			logger.info("The URL is not updated in the default web page");
			ReplaceHost replaceHost = new ReplaceHost();
			replaceHost.setFileLocation(fileLocation);
			replaceHost.setSearchString(searchString);
			replaceHost.setReplaceString(replaceString);
			replaceHost.execute();
		}
	}
}
