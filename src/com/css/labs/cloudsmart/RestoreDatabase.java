package com.css.labs.cloudsmart;

import org.apache.log4j.Logger;

import com.css.labs.cloudsmart.test.ProcessService;

/**
 * @author Hajamaideen Abdul
 * 
 */

public class RestoreDatabase extends ProcessService {
	
	static Logger logger = Logger.getLogger(RestoreDatabase.class);
	
	private String CMD_SQL = "cmd /c SQLCMD -S 127.0.0.1 ";
	private String userName = null;
	private String password = null;
	private String restoreFileListLocation = null;
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}	
	
	public void setRestoreFileListLocation(String restoreFileListLocation) {
		this.restoreFileListLocation = restoreFileListLocation;
	}

	public boolean restoreDatabase() {
		boolean status = false;
		try {
			if(userName != null && password != null && restoreFileListLocation != null) {
				CMD_SQL += "-U "+userName+" -P "+password+" -i "+restoreFileListLocation;
				String queryString = processService(CMD_SQL);
		    	System.out.println(queryString);
		    	if((queryString != null) && (!queryString.equals(""))) {
		    		if(queryString.indexOf("RESTORE DATABASE SUCCESSFULLY PROCESSED") != -1) {
		    			status = true;
		    		}
		    	}
			}
		}
		catch(Exception exception) {
			System.err.println("Restore Database Exception : "+exception.getMessage());
		}
		return status;
	}
	
	
	public void execute() {
		if(restoreDatabase()) {
			logger.info("The SQL SERVER \"SalesLogix Database\" Restored Successfully");
		}
		else {
			logger.info("The SQL SERVER \"SalesLogix Database\" Restore process failed");
		}
	}
}
