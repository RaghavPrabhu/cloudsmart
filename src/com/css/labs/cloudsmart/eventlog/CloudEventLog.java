package com.css.labs.cloudsmart.eventlog;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


public class CloudEventLog {
	public void setEventLog( String snapshotId, int logLevel) {
		String userName = System.getProperty("user.name");
		String eventType = "";
		String error_id = "";
		String sourceMessage = "";
		String eventMessage="";
		String eventFileLocation="";
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
		eventFileLocation= prop.getProperty("eventfilelocation");
		Runtime run = Runtime.getRuntime();
	    try {

	        run.exec("cmd start /c start "+eventFileLocation+"  "+userName +" "+eventType +" "+error_id +" "+sourceMessage +" "+snapshotId+eventMessage);
	        run.exec("cmd start exit");
	    } catch (Exception e) {

	        e.printStackTrace();

	    }
	}
}

//EVENTCREATE /S HDC0001269 /U SL052235 /T ERROR /ID 200 /L Application /SO CloudSmartSnapshotID /D "snap-2a31eb42"
//EVENTCREATE /S %computername% /U %1 /T %2 /ID %3 /L Application /SO %4 /D "%5"
//EVENTCREATE /S %computername% /U %1 /T %2 /ID %3 /L %4 /D %5

/*Logger myLogger = Logger.getLogger("Log4JWindowsNTEvent");
PatternLayout layout = new PatternLayout();
NTEventLogAppender eventLogAppender = new NTEventLogAppender(message,
		layout);
ConsoleAppender consoleAppender = new ConsoleAppender(layout);
myLogger.addAppender(consoleAppender);
myLogger.addAppender(eventLogAppender);
switch(LogLevel){
case 1:
	myLogger.error(message+" "+snapshotId+" Not Created Successfully");
	break;
case 2:
	myLogger.warn(message+" "+snapshotId+" Created Successfully");
	break;
case 3:
	myLogger.setLevel(Level.INFO);
	myLogger.info(message+" "+snapshotId+" Created Successfully");
	break;
	
}



myLogger.isInfoEnabled();*/
