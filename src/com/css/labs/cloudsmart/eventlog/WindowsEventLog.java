/**
 * 
 */
package com.css.labs.cloudsmart.eventlog;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Ponmarimuth Natesan 
 *
 */

public class WindowsEventLog {
	public void setEventLog( String snapshotId, int logLevel) {
		String userName = System.getProperty("user.name");
		String eventType = "";
		String error_id = "";
		String sourceMessage = "";
		String eventMessage="";
		Properties prop = new Properties(); 
		try {
	        	prop.load(new FileInputStream("WindowsEvent.properties"));
	    } catch (IOException e) {
	    	System.out.println("Error" +e.getMessage());
	    }
		switch(logLevel){
		case 1:
			eventType = "ERROR";
			error_id=prop.getProperty("error");
			eventMessage = "~~"+prop.getProperty("errormessage");
			break;
		case 2:
			eventType = "WARNING";
			error_id=prop.getProperty("warning");
			eventMessage = "~~"+prop.getProperty("warningmessage");
			break;
		case 3:
			eventType = "INFORMATION";
			error_id=prop.getProperty("success");
			eventMessage = "~~"+prop.getProperty("sucessmessage");
			break;
		}
		
		sourceMessage = prop.getProperty("source");
		Runtime run = Runtime.getRuntime();
	    try {

	        run.exec("cmd start /c start C:/event.bat "+userName +" "+eventType +" "+error_id +" "+sourceMessage +" "+snapshotId+eventMessage);
	        run.exec("cmd start exit");
	    } catch (Exception e) {

	        e.printStackTrace();

	    }
	}
}
