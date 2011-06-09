/**
 * 
 */
package com.css.labs.cloudsmart.nagios;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.contrib.ssl.EasySSLProtocolSocketFactory;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

/**
 * @author Prabhu KuppurajBabu
 * 
 */
public class RegisterInstance extends Task {

	private String userName;
	private String password;
	private String instanceID;
	private String customerID;
	private String instanceName;
	private String instanceType;
	private String publicDNS;
	private String privateDNS;
	private String publicIPV4;
	private String privateIPV4;
	private String nagiosURI;
	

	/**
	 * @return the nagiosURI
	 */
	public String getNagiosURI() {
		return nagiosURI;
	}

	/**
	 * @param nagiosURI
	 *            the nagiosURI to set
	 */
	public void setNagiosURI(String nagiosURI) {
		this.nagiosURI = nagiosURI;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the instanceID
	 */
	public String getInstanceID() {
		return instanceID;
	}

	/**
	 * @param instanceID
	 *            the instanceID to set
	 */
	public void setInstanceID(String instanceID) {
		this.instanceID = instanceID;
	}

	/**
	 * @return the customerID
	 */
	public String getCustomerID() {
		return customerID;
	}

	/**
	 * @param customerID
	 *            the customerID to set
	 */
	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}

	/**
	 * @return the instanceName
	 */
	public String getInstanceName() {
		return instanceName;
	}

	/**
	 * @param instanceName
	 *            the instanceName to set
	 */
	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}

	/**
	 * @return the instanceType
	 */
	public String getInstanceType() {
		return instanceType;
	}

	/**
	 * @param instanceType
	 *            the instanceType to set
	 */
	public void setInstanceType(String instanceType) {
		this.instanceType = instanceType;
	}

	/**
	 * @return the publicDNS
	 */
	public String getPublicDNS() {
		return publicDNS;
	}

	/**
	 * @param publicDNS
	 *            the publicDNS to set
	 */
	public void setPublicDNS(String publicDNS) {
		this.publicDNS = publicDNS;
	}

	/**
	 * @return the privateDNS
	 */
	public String getPrivateDNS() {
		return privateDNS;
	}

	/**
	 * @param privateDNS
	 *            the privateDNS to set
	 */
	public void setPrivateDNS(String privateDNS) {
		this.privateDNS = privateDNS;
	}

	/**
	 * @return the publicIPV4
	 */
	public String getPublicIPV4() {
		return publicIPV4;
	}

	/**
	 * @param publicIPV4
	 *            the publicIPV4 to set
	 */
	public void setPublicIPV4(String publicIPV4) {
		this.publicIPV4 = publicIPV4;
	}

	/**
	 * @return the privateIPV4
	 */
	public String getPrivateIPV4() {
		return privateIPV4;
	}

	/**
	 * @param privateIPV4
	 *            the privateIPV4 to set
	 */
	public void setPrivateIPV4(String privateIPV4) {
		this.privateIPV4 = privateIPV4;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.tools.ant.Task#execute()
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void execute() throws BuildException {
		// TODO Auto-generated method stub
		HttpClient httpClient=new HttpClient();
		try {
			URI serverURI=new URI(this.getNagiosURI());
			ProtocolSocketFactory socketFactory=new EasySSLProtocolSocketFactory( );
			Protocol https=new Protocol("https", socketFactory,serverURI.getPort() );
			Protocol.registerProtocol( "https", https );
			Credentials nagiosCredentials=new UsernamePasswordCredentials(this.userName,this.password);
			httpClient.getState().setCredentials(new AuthScope(serverURI.getHost(),serverURI.getPort(), AuthScope.ANY_REALM),nagiosCredentials);
			PostMethod httpMethod=new PostMethod(this.nagiosURI);
			httpMethod.setRequestBody(requestBody(this.customerID, this.instanceID, this.instanceName, this.instanceType, this.publicDNS, this.privateDNS, this.publicIPV4, this.privateIPV4));
			int statusCode=httpClient.executeMethod(httpMethod);
			System.out.println(statusCode);
			System.out.println(httpMethod.getResponseBodyAsString());
			 httpMethod.releaseConnection();
			
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}  
	}

	private static String requestBody(String customerID, String instanceID,
			String instanceName, String instanceType, String publicDNS,
			String privateDNS, String publicIPV4, String privateIPV4) {
		StringWriter writer = new StringWriter();
		writer.write("<Instance>");
		writer.write("<Customer_ID>" + customerID + "</Customer_ID>");
		writer.write("<Instance_ID>" + instanceID + "</Instance_ID>");
		writer.write("<Instance_Name>" + instanceName + "</Instance_Name>");
		writer.write("<Instance_Type>" + instanceType + "</Instance_Type>");
		writer.write("<Public_DNS>" + publicDNS + "</Public_DNS>");
		writer.write("<Private_DNS>" + privateDNS + "</Private_DNS>");
		writer.write("<Public_IP>" + publicIPV4 + "</Public_IP>");
		writer.write("<Private_IP>" + privateIPV4 + "</Private_IP>");
		writer.write("</Instance>");

		return writer.toString();

	}

}
