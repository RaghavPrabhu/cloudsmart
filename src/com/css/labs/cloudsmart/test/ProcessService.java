package com.css.labs.cloudsmart.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;
import org.apache.tools.ant.Task;

/**
 * @author Hajamaideen Abdul
 * 
 */

public class ProcessService extends Task {
	
	static Logger logger = Logger.getLogger(ProcessService.class);
	
	public String processService(String cmdLine) {
    	StringBuilder stringBuilder = new StringBuilder();
    	BufferedReader bufferedReader = null;
    	if(cmdLine != null && (!cmdLine.equals(""))) {
    		try {
		    	Process process = Runtime.getRuntime().exec(cmdLine);
		    	bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));	    	
		    	String line = null;
		    	while((line = bufferedReader.readLine()) != null){
		    		stringBuilder.append(line);		    		
		    	}
		    	process.exitValue();
    		}
    		catch(Exception exception) {
    			logger.error("Windows Service Exception : "+exception.getMessage());
    		}
    		finally {
    			if(bufferedReader != null){
    				try {
						bufferedReader.close();						
					} catch (IOException e) {
						logger.error("Windows Service BufferedReader Exception");
					}
    			}
    		}
    	}
    	return stringBuilder.toString().toUpperCase(); 
    }

}
