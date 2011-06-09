package com.css.labs.cloudsmart.test;

import org.apache.log4j.Logger;

/**
 * @author Hajamaideen Abdul
 * 
 */

public class IISWebSiteTest extends ProcessService {

	static Logger logger = Logger.getLogger(IISWebSiteTest.class);
	
	private String webSiteListScriptFile = null;
	private String CMD_SCRIPT = "cmd /c cscript ";		
	
	public void setWebSiteListScriptFile(String webSiteListScriptFile) {
		this.webSiteListScriptFile = webSiteListScriptFile;
	}

	public boolean checkWebsites(){
		boolean status = false;
		try {
			if(webSiteListScriptFile != null) {				
				String cmdLine = CMD_SCRIPT + webSiteListScriptFile;
		    	String queryString = processService(cmdLine);
		    	System.out.println(queryString);			    	
		    	if((queryString != null) && (!queryString.equals(""))) {		    		
		    		if((queryString.indexOf("DEFAULT WEB SITE") != -1) && (queryString.indexOf("SALESLOGIX") != -1)) {
			    		status = true;
			    	}
		    	}				
			}
		}
		catch(Exception exception) {
			System.err.println("IISWebSiteTest Exception : "+exception.getMessage());
		}
		return status;
	}
	
	public void execute() {
		try {
			// Check the Default Web Site and SalesLogix services running in IIS
			if(checkWebsites()) {
				logger.info("The Sites \"Default Web Site\" and \"SalesLogix\" are running in the IIS Server");
			}
			else {
				logger.info("The Sites \"Default Web Site\" and \"SalesLogix\" are not running in the IIS Server");				
			}
		}
		catch(Exception exception) {
			logger.error("IISWebSiteTest Exception : "+exception.getMessage());
		}
	}
}
