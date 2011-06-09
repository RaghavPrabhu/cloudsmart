/**
 * 
 */
package com.css.labs.cloudsmart;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import com.amazonaws.ec2.AmazonEC2;
import com.amazonaws.ec2.AmazonEC2Client;
import com.amazonaws.ec2.AmazonEC2Exception;
import com.amazonaws.ec2.model.AssociateAddressRequest;
import com.amazonaws.ec2.model.AssociateAddressResponse;

/**
 * @author Prabhu KuppurajBabu
 * 
 */
public class AttachIP extends Task {

	
	private String accessKey;
	private String secretKey;
	private String elasticIP;
	private String instanceID;
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.tools.ant.Task#execute()
	 */
	/**
	 * @return the accessKey
	 */
	public String getAccessKey() {
		return accessKey;
	}

	/**
	 * @param accessKey the accessKey to set
	 */
	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	/**
	 * @return the secretKey
	 */
	public String getSecretKey() {
		return secretKey;
	}

	/**
	 * @param secretKey the secretKey to set
	 */
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	/**
	 * @return the elasticIP
	 */
	public String getElasticIP() {
		return elasticIP;
	}

	/**
	 * @param elasticIP the elasticIP to set
	 */
	public void setElasticIP(String elasticIP) {
		this.elasticIP = elasticIP;
	}

	/**
	 * @return the instanceIDURL
	 */
	public String getInstanceID() {
		return instanceID;
	}

	/**
	 * @param instanceIDURL the instanceIDURL to set
	 */
	public void setInstanceID(String instanceID) {
		this.instanceID = instanceID;
	}

	@Override
	public void execute() throws BuildException {
		// TODO Auto-generated method stub
		
		try {
			AmazonEC2 service = new AmazonEC2Client(this.accessKey, this.secretKey);
			String instanceID=getInstanceID(this.instanceID);
			
			AssociateAddressRequest request = new AssociateAddressRequest(instanceID,this.elasticIP);
			AssociateAddressResponse response = service.associateAddress(request);
						
			System.out.println("Elastic IP Sucessfully Mapped to Instance");
			
		} catch (AmazonEC2Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String getInstanceID(String url) {

		String instanceID = null;
		try {
			HttpClient httpClient = new HttpClient();
			HttpMethod httpMethod = new GetMethod(url);
			httpClient.executeMethod(httpMethod);
			instanceID = httpMethod.getResponseBodyAsString();
			System.out.println(" Your Instance ID : " + instanceID);
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return instanceID.trim();

	}

}
