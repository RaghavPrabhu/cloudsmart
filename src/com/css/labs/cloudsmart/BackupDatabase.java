package com.css.labs.cloudsmart;

import org.apache.log4j.Logger;

import com.css.labs.cloudsmart.test.ProcessService;

/**
 * @author Hajamaideen Abdul
 * 
 */

public class BackupDatabase extends ProcessService {
	
	static Logger logger = Logger.getLogger(BackupDatabase.class);
	
	private String CMD_SQL = "cmd /c SQLCMD -S 127.0.0.1 ";
	private String userName = null;
	private String password = null;
	private String backupFileListLocation = null;
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}		
	
	public void setBackupFileListLocation(String backupFileListLocation) {
		this.backupFileListLocation = backupFileListLocation;
	}

	public boolean restoreDatabase() {
		boolean status = false;
		try {
			if(userName != null && password != null && backupFileListLocation != null) {
				CMD_SQL += "-U "+userName+" -P "+password+" -i "+backupFileListLocation;
				String queryString = processService(CMD_SQL);
		    	System.out.println(queryString);
		    	if((queryString != null) && (!queryString.equals(""))) {
		    		if(queryString.indexOf("BACKUP DATABASE SUCCESSFULLY PROCESSED") != -1) {
		    			status = true;
		    		}
		    	}
			}
		}
		catch(Exception exception) {
			System.err.println("Backup Database Exception : "+exception.getMessage());
		}
		return status;
	}
	
	
	public void execute() {
		if(restoreDatabase()) {
			logger.info("The SQL SERVER \"SalesLogix Database\" Backuped Successfully");
		}
		else {
			logger.info("The SQL SERVER \"SalesLogix Database\" Backup process failed");
		}
	}
}
