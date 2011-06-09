/**
 * 
 */
package com.css.labs.cloudsmart.logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;

import org.apache.log4j.Appender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.helpers.NullEnumeration;
import org.apache.tools.ant.BuildEvent;
import org.apache.tools.ant.BuildListener;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Target;
import org.apache.tools.ant.Task;

/**
 * @author Ponmarimuth Natesan
 *
 */

public class CustomLogger extends FileAppender implements BuildListener {
	private boolean initialized = false;
	public static final String LOG_ANT = "org.apache.tools.ant";
    Properties prop = new Properties();
    String format = "%1$-40s %2$-10s %3$-10s %4$-20s \n";

    public CustomLogger() {
		initialized = false;		
		Logger log = Logger.getLogger(LOG_ANT);
		Logger rootLog = Logger.getRootLogger();
		FileAppender fa = null;
		Date projDate = new Date();
		StringBuffer dateStr = new StringBuffer();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
		dateStr = sdf.format(projDate, dateStr,new FieldPosition(0));
		String logFileName = "build_"+ dateStr.toString() + ".log";
		Enumeration<?> appenders = rootLog.getAllAppenders();
		while(appenders.hasMoreElements()) {
			Appender currAppender = (Appender) appenders.nextElement();
				if(currAppender instanceof FileAppender){
					fa = (FileAppender) currAppender;
				}
		}
		if(fa != null){
			fa.setFile(logFileName);
			log.info("Log File Generated " + logFileName);
			fa.activateOptions();
		}
		if(!(rootLog.getAllAppenders() instanceof NullEnumeration)) {
			initialized = true;
		}else {
			log.error("No log4j.properties in build area");
		}
		 try {
		        prop.load(new FileInputStream("errordescription.properties"));
		    } catch (IOException e) {
	    }
	}
	public void messageLogged(BuildEvent event) {
		if (initialized) {
			Object categoryObject = event.getTask();
			if (categoryObject == null) {
				categoryObject = event.getTarget();
				if (categoryObject == null) {
					categoryObject = event.getProject();
				}
			}
			Logger log = Logger.getLogger(categoryObject.getClass().getName());
			switch (event.getPriority()) {
			case Project.MSG_ERR:
				log.error(String.format(format,prop.getProperty("014").toUpperCase() ,"002", prop.getProperty("002"),event.getMessage()));
				break;
			case Project.MSG_WARN:
				log.warn(String.format(format,prop.getProperty("014").toUpperCase() ,"002", prop.getProperty("002"),event.getMessage()));
				break;
			case Project.MSG_INFO:
				log.error(String.format(format,prop.getProperty("014").toUpperCase() ,"001", prop.getProperty("001"),event.getMessage()));
				break;
			case Project.MSG_VERBOSE:
				log.debug(String.format(format,prop.getProperty("014").toUpperCase() ,"001", prop.getProperty("001"),event.getMessage()));
				break;
			case Project.MSG_DEBUG:
				log.debug(String.format(format,prop.getProperty("014").toUpperCase() ,"002", prop.getProperty("002"),event.getMessage()));
				break;
			default:
				log.error(String.format(format,prop.getProperty("014").toUpperCase() ,"002", prop.getProperty("002"),event.getMessage()));
				break;
			}
		}
	}
	public void buildStarted(BuildEvent event) {
		if (initialized) {
			Logger log = Logger.getLogger(Project.class.getName());
			log.info(String.format(format,"Build Started".toUpperCase() ,"001", prop.getProperty("001"),prop.getProperty("003")));
		}
	}
	public void buildFinished(BuildEvent event) {
		if (initialized) {
			Logger log = Logger.getLogger(Project.class.getName());
			if (event.getException() == null) {
				log.info(String.format(format,prop.getProperty("013").toUpperCase() ,"001", prop.getProperty("001"),prop.getProperty("004")));
			} else {
				System.out.println("Build Finished without Exception");
				log.error(String.format(format,prop.getProperty("013").toUpperCase() ,"002", prop.getProperty("002"),prop.getProperty("005")+event.getException()));
			}
		}
	}
	public void targetStarted(BuildEvent event) {
		if (initialized) {
			Logger log = Logger.getLogger(Target.class.getName());
			log.info(String.format(format,event.getTarget().getName().toUpperCase() ,"001", prop.getProperty("001"),prop.getProperty("006")));
		}
	}
	public void targetFinished(BuildEvent event) {
		if (initialized) {
			String targetName = event.getTarget().getName();
			Logger cat = Logger.getLogger(Target.class.getName());
			if (event.getException() == null) {
				cat.error(String.format(format,targetName.toUpperCase() ,"001", prop.getProperty("001"),prop.getProperty("007")));
			} else {
				cat.error(String.format(format,targetName.toUpperCase() ,"002", prop.getProperty("002"),prop.getProperty("008")+event.getException()));
			}
		}
	}
	public void taskStarted(BuildEvent event) {
		if (initialized) {
			Task task = event.getTask();
			Logger log = Logger.getLogger(task.getTaskName());
			log.info(String.format(format,event.getTask().getTaskName().toUpperCase() ,"001", prop.getProperty("001"),prop.getProperty("009")+ event.getTask().getTaskName()));
		}
	}
	public void taskFinished(BuildEvent event) {
		if (initialized) {
			Task task = event.getTask();
			Logger log = Logger.getLogger(task.getTaskName());
			if (event.getException() == null) {
				log.info(String.format(format,event.getTask().getTaskName().toUpperCase() ,"001", prop.getProperty("001"),prop.getProperty("010")+ event.getTask().getTaskName()));
			} else {
				log.error(String.format(format,event.getTask().getTaskName().toUpperCase() ,"010", task.getTaskName()," finished with error."+event.getException()));
				log.error(String.format(format,event.getTask().getTaskName().toUpperCase() ,"010", task.getTaskName()," finished with error."+event.getException()));
			}
		}
	}
}