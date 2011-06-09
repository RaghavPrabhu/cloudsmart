/**
 * 
 */
package com.css.labs.cloudsmart.nagios;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.contrib.ssl.EasySSLProtocolSocketFactory;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

/**
 * @author Prabhu KuppurajBabu
 *
 */
public class DeregisterInstance extends Task {
	
	private String userName;
	private String password;
	private String instanceID;
	private String nagiosURI;
	
	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @param instanceID the instanceID to set
	 */
	public void setInstanceID(String instanceID) {
		this.instanceID = instanceID;
	}
	
	/**
	 * @param nagiosURI the nagiosURI to set
	 */
	public void setNagiosURI(String nagiosURI) {
		this.nagiosURI = nagiosURI;
	}
	
	/* (non-Javadoc)
	 * @see org.apache.tools.ant.Task#execute()
	 */
	@Override
	public void execute() throws BuildException {
		// TODO Auto-generated method stub
		HttpClient httpClient=new HttpClient();
		try {
			URI serverURI=new URI(this.nagiosURI);
			ProtocolSocketFactory socketFactory=new EasySSLProtocolSocketFactory( );
			Protocol https=new Protocol("https", socketFactory,serverURI.getPort() );
			Protocol.registerProtocol( "https", https );
			Credentials nagiosCredentials=new UsernamePasswordCredentials(this.userName,this.password);
			httpClient.getState().setCredentials(new AuthScope(serverURI.getHost(),serverURI.getPort(), AuthScope.ANY_REALM),nagiosCredentials);
			//PostMethod httpMethod=new PostMethod(this.nagiosURI);
			
			DeleteMethod httpMethod=new DeleteMethod(this.nagiosURI+this.instanceID);
			
			try {
				httpClient.executeMethod(httpMethod);
			} catch (HttpException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	
	
	

}
