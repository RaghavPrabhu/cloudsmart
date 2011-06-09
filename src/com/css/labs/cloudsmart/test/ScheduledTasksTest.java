package com.css.labs.cloudsmart.test;

import java.io.File;

import org.apache.log4j.Logger;

/**
 * @author Hajamaideen Abdul
 * 
 */

public class ScheduledTasksTest extends ProcessService {
	static Logger logger = Logger.getLogger(ScheduledTasksTest.class);
	
	private String CMD_TASKS = "cmd /c SCHTASKS /QUERY";	
	private String serverType = null;	
	
	public void setServerType(String serverType) {
		this.serverType = serverType;
	}

	public boolean checkTask() {
		boolean status = false;
		try {
	    	String queryString = processService(CMD_TASKS);
	    	System.out.println(queryString);
	    	if((queryString != null) && (!queryString.equals(""))) {
	    		if(serverType != null) {
	    			if(serverType.equalsIgnoreCase("AppDB")) {
	    				if((queryString.indexOf("Full SLX Filestore Backup".toUpperCase()) != -1) && 
				    			(queryString.indexOf("Incremental SLX Filestore Backup".toUpperCase()) != -1)){			    		  	
				    		status = true;
				    	}
	    			}
	    			else if (serverType.equalsIgnoreCase("WS")) {
	    				if(queryString.indexOf("IISLogsLite".toUpperCase()) != -1){			    		  	
				    		status = true;
				    	}
	    			}	    			
	    		}		    	
	    	}			
		}
		catch(Exception exception) {
			System.err.println("ScheduledTasksTest CheckTask Exception : "+exception.getMessage());			
		}
		return status;
	}
	
	public boolean checkBackupFiles() {
		boolean status = false;
		try {
			File fullBackupFile = new File("D:\\Filestore\\Full SLX Filestore Backup.bkf");
			File incrementalBackupFile = new File("D:\\Filestore\\Incremental SLX Filestore Backup.bkf");
			if(fullBackupFile.exists() && incrementalBackupFile.exists()) {
				status = true;
			}			
		}
		catch(Exception exception) {
			System.err.println("ScheduledTasksTest CheckBackupFiles Exception : "+exception.getMessage());
		}
		return status;
	}
	
	public void execute() {
		if(checkTask()) {
			if(serverType != null) {
				if(serverType.equalsIgnoreCase("AppDB")) {
					logger.info("The Scheduled Tasks \"Full SLX Filestore Backup\" and \"Incremental SLX Filestore Backup\" are available in the Instance");
				}
				else {
					logger.info("The Scheduled Tasks \"IISLogsLite\" are available in the Instance");
				}
			}			
		}
		else {
			if(serverType != null) {
				if(serverType.equalsIgnoreCase("AppDB")) {
					logger.info("The Scheduled Task \"Full SLX Filestore Backup\" and \"Incremental SLX Filestore Backup\" are not available in the Instance");
				}
				else {
					logger.info("The Scheduled Task \"IISLogsLite\" are not available in the Instance");
				}
			}			
		}
		
		if(serverType != null) {
			if(serverType.equalsIgnoreCase("AppDB")) {
				if(checkBackupFiles()) {
					logger.info("The Backup Files \"Full SLX Filestore Backup.bkf\" and \"Incremental SLX Filestore Backup.bkf\" are found in the D:/Filestore directory");
				}
				else {
					logger.info("The Backup Files \"Full SLX Filestore Backup.bkf\" and \"Incremental SLX Filestore Backup.bkf\" are not found in the D:/Filestore directory");
				}
			}
		}	
	}
}
