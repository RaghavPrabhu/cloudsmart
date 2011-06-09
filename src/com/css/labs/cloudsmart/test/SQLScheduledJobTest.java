package com.css.labs.cloudsmart.test;

import org.apache.log4j.Logger;

/**
 * @author Hajamaideen Abdul
 * 
 */

public class SQLScheduledJobTest extends ProcessService {
	
	static Logger logger = Logger.getLogger(SQLScheduledJobTest.class);
	
	private String CMD_SQL = "cmd /c SQLCMD -S 127.0.0.1 ";
	private String userName = null;
	private String password = null;
	private String jobFileListLocation = null;
	private String createJobFileLocation = null;
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}	
	
	public void setJobFileListLocation(String jobFileListLocation) {
		this.jobFileListLocation = jobFileListLocation;
	}

	public void setCreateJobFileLocation(String createJobFileLocation) {
		this.createJobFileLocation = createJobFileLocation;
	}

	public boolean checkBackupJob() {
		boolean status = false;
		try {
			if(userName != null && password != null && jobFileListLocation != null) {
				CMD_SQL += "-U "+userName+" -P "+password+" -i "+jobFileListLocation;
				String queryString = processService(CMD_SQL);
		    	System.out.println(queryString);
		    	if((queryString != null) && (!queryString.equals(""))) {
		    		if((queryString.indexOf("MASTER") != -1) && (queryString.indexOf("SALESLOGIX") != -1)) {
		    			status = true;
		    		}
		    	}
			}
		}
		catch(Exception exception) {
			System.err.println("SQLScheduledJobTest CheckBackupJob Exception : "+exception.getMessage());
		}
		return status;
	}
	
	public boolean createBackupJob() {
		boolean status = false;
		try {
			if(userName != null && password != null && createJobFileLocation != null) {
				CMD_SQL += "-U "+userName+" -P "+password+" -i "+createJobFileLocation;
				String queryString = processService(CMD_SQL);
		    	System.out.println(queryString);
		    	if((queryString != null) && (!queryString.equals(""))) {
		    		if(!(queryString.indexOf("Database Backup Job Creation Failed".toUpperCase()) != -1)) {
		    			status = true;
		    		}		    		
		    	}
			}
		}
		catch(Exception exception) {
			System.err.println("SQLScheduledJobTest CreateBackupJob Exception : "+exception.getMessage());
		}
		return status;
	}
	
	public void execute() {
		if(checkBackupJob()) {
			logger.info("The SQL SERVER AGENT \"Database Backup\" Job is already created");
		}
		else {
			logger.info("The SQL SERVER AGENT \"Database Backup\" Job is not created");
			if(createBackupJob()) {
				logger.info("The \"Database Backup\" Job Created Successfully");
			}
			else {
				logger.info("The \"Database Backup\" Job Creation Failed");
			}
		}
	}
}
