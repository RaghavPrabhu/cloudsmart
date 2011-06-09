package com.css.labs.cloudsmart.test;

import org.apache.log4j.Logger;

/**
 * @author Hajamaideen Abdul
 * 
 */

public class SQLUsersTest extends ProcessService {
	
	static Logger logger = Logger.getLogger(SQLUsersTest.class);
	
	private String CMD_SQL = "cmd /c SQLCMD -S 127.0.0.1 ";
	private String userName = null;
	private String password = null;
	private String fileLocation = null;
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setFileLocation(String fileLocation) {
		this.fileLocation = fileLocation;
	}
	
	public boolean checkUser() {
		boolean status = false;
		try {
			if(userName != null && password != null && fileLocation != null) {
				CMD_SQL += "-U "+userName+" -P "+password+" -i "+fileLocation;
				String queryString = processService(CMD_SQL);
		    	System.out.println(queryString);
		    	if((queryString != null) && (!queryString.equals(""))) {
		    		if(queryString.indexOf("USER EXISTS") != -1) {
		    			status = true;
		    		}
		    	}
			}
		}
		catch(Exception exception) {
			System.err.println("SQLUsersTest Exception : "+exception.getMessage());
		}
		return status;
	}
	
	public void execute() {
		if(checkUser()) {
			logger.info("The SQL User account is already created in the Database");
		}
		else {
			logger.info("The SQL User account is not available in the Database its being created");
		}
	}
}
