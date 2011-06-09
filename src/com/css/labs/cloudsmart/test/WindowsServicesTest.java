package com.css.labs.cloudsmart.test;

import org.apache.log4j.Logger;

/**
 * @author Hajamaideen Abdul
 * 
 */

public class WindowsServicesTest extends ProcessService {
	
	static Logger logger = Logger.getLogger(WindowsServicesTest.class);
	
    private String CMD_QUERY = "cmd /c sc query ";
    private String CMD_AUTO = "cmd /c sc config ";
    private String CMD_START = "cmd /c sc start ";
    private String serviceName = null;    
    
    public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
    	
    public void queryService() throws Exception {
    	if(serviceName != null && (!serviceName.equals(""))) {
	    	String cmdLine = CMD_QUERY + serviceName;
	    	String queryString = processService(cmdLine);
	    	logger.info(queryString);
	    	if((queryString != null) && (!queryString.equals(""))) {
		    	if(queryString.indexOf("RUNNING") != -1){
		    		logger.info("The Service "+serviceName+" is Running");  		
		    	}
		    	else {
		    		logger.info("The Service "+serviceName+" is not Running...Lets Enable & Start the Service");
		    		cmdLine = CMD_AUTO + serviceName + " start= auto";
		    		queryString = processService(cmdLine);
		    		System.out.println(queryString);
		    		if((queryString != null) && (!queryString.equals(""))) {
				    	if(queryString.indexOf("SUCCESS") != -1){
				    		logger.info("The Service "+serviceName+" is Enabled...");  
				    		cmdLine = CMD_START + serviceName;
				    		queryString = processService(cmdLine);
				    		System.out.println(queryString);
				    		if((queryString != null) && (!queryString.equals(""))) {
						    	if((queryString.indexOf("SUCCESS") != -1) || (queryString.indexOf("START_PENDING") != -1)){
						    		logger.info("The Service "+serviceName+" is Started...");  			    		
						    	}
				    		}
				    	}
		    		}
		    	}
	    	}
    	}
    }    
    
    public void execute() {
    	try {
			queryService();
		} catch (Exception e) {
			logger.error("Windows Service Check Exception : "+e.getMessage());
		}
    }
}