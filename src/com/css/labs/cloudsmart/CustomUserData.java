/**
 * 
 */
package com.css.labs.cloudsmart;

import org.apache.commons.codec.binary.Base64;
import org.apache.tools.ant.Task;
import org.w3c.dom.UserDataHandler;

/**
 * @author Prabhu KuppurajBabu
 *
 */
public class CustomUserData  {
	
	
	
	public static void main(String... args){
		String userData="URL=temp,bucketName=cloudsmart,buildFile=cloudsmartbuild.xml";
		userData=userData.replaceAll(",", "\n");
		System.out.println(userData);
		byte[] encodeData= userData.getBytes();
		System.out.println(new String(Base64.encodeBase64(encodeData)));
		
	}

}
