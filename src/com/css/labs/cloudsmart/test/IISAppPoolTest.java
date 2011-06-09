package com.css.labs.cloudsmart.test;

import org.apache.log4j.Logger;

/**
 * @author Hajamaideen Abdul
 * 
 */

public class IISAppPoolTest extends ProcessService {
	
	static Logger logger = Logger.getLogger(IISAppPoolTest.class);
	
	private String appPoolListScriptFile = null;
	private String CMD_SCRIPT = "cmd /c cscript ";		
	
	public void setAppPoolListScriptFile(String appPoolListScriptFile) {
		this.appPoolListScriptFile = appPoolListScriptFile;
	}

	public boolean checkAppPool(){
		boolean status = false;
		try {
			if(appPoolListScriptFile != null) {
				String cmdLine = CMD_SCRIPT + appPoolListScriptFile;
		    	String queryString = processService(cmdLine);
		    	System.out.println(queryString);			    	
		    	if((queryString != null) && (!queryString.equals(""))) {
		    		if((queryString.indexOf("DEFAULTAPPPOOL") != -1) && (queryString.indexOf("SALESLOGIX") != -1)
		    				&& (queryString.indexOf("SLX INTELLISYNC") != -1) && (queryString.indexOf("SLX WEB REPORTING") != -1)) {
			    		status = true;
			    	}
		    	}
			}
		}
		catch(Exception exception) {
			logger.error("IISAppPoolTest Exception : "+exception.getMessage());
		}
		return status;
	}
	
	public void execute() {
		try {
			// Check the DefaultAppPool, SalesLogix, SLX Intellisync and SLX Web Reporting application pool exists in IIS
			if(checkAppPool()) {
				logger.info("The IIS Application Pool \"DefaultAppPool\", \"SalesLogix\", \"SLX Intellisync\" and \"SLX Web Reporting\" are exists in the IIS Server");
			}
			else {
				logger.info("The IIS Application Pool \"DefaultAppPool\", \"SalesLogix\", \"SLX Intellisync\" and \"SLX Web Reporting\" are not exists in the IIS Server");
			}
		}
		catch(Exception exception) {
			logger.error("IISAppPoolTest Exception : "+exception.getMessage());
		}
	}
}
