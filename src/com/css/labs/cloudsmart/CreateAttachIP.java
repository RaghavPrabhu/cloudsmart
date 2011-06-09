package com.css.labs.cloudsmart;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import com.amazonaws.ec2.AmazonEC2;
import com.amazonaws.ec2.AmazonEC2Client;
import com.amazonaws.ec2.AmazonEC2Exception;
import com.amazonaws.ec2.model.AllocateAddressRequest;
import com.amazonaws.ec2.model.AllocateAddressResponse;
import com.amazonaws.ec2.model.AllocateAddressResult;
import com.amazonaws.ec2.model.AssociateAddressRequest;
import com.amazonaws.ec2.model.AssociateAddressResponse;
import com.amazonaws.ec2.model.ResponseMetadata;

/**
 * @author Haja Maideen
 * 
 */
public class CreateAttachIP extends Task {
	
	private String accessKey;
	private String secretKey;
	private String elasticIP;
	private String instanceId;
	private AmazonEC2 service = null;
	
	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
	public void setElasticIP(String elasticIP) {
		this.elasticIP = elasticIP;
	}
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}
	
	public void allocateElasticIp() {
		try {
			AllocateAddressRequest allocateAddressRequest = new AllocateAddressRequest();
			AllocateAddressResponse allocateAddressResponse = service.allocateAddress(allocateAddressRequest);			
			
			if (allocateAddressResponse.isSetAllocateAddressResult()) {
                AllocateAddressResult  allocateAddressResult = allocateAddressResponse.getAllocateAddressResult();
                if (allocateAddressResult.isSetPublicIp()) {
                    System.out.println("\tPublicIp : " + allocateAddressResult.getPublicIp());   
                    setElasticIP(allocateAddressResult.getPublicIp());
                }
            } 			
			System.out.println("Elastic IP Sucessfully Created");
			
		} catch (AmazonEC2Exception ex) {
			System.out.println("Caught Exception: " + ex.getMessage());
            System.out.println("Response Status Code: " + ex.getStatusCode());
            System.out.println("Error Code: " + ex.getErrorCode());
            System.out.println("Error Type: " + ex.getErrorType());
            System.out.println("Request ID: " + ex.getRequestId());
            System.out.print("XML: " + ex.getXML());
		}
	}
	
	public void associateElasticIp() {
		try {
			if(elasticIP != null && instanceId != null) {
				AssociateAddressRequest associateAddressRequest = new AssociateAddressRequest();
				associateAddressRequest.setInstanceId(this.instanceId);
				associateAddressRequest.setPublicIp(this.elasticIP);
				AssociateAddressResponse associateAddressResponse = service.associateAddress(associateAddressRequest);
				
				if (associateAddressResponse.isSetResponseMetadata()) {
	                ResponseMetadata  responseMetadata = associateAddressResponse.getResponseMetadata();
	                if (responseMetadata.isSetRequestId()) {
	                    System.out.println("\tRequestId : " + responseMetadata.getRequestId());                    
	                }
	            } 
				
				System.out.println("Elastic IP is Successfully Associated with EC2 Instance");
			}
			
		} catch (AmazonEC2Exception ex) {
			System.out.println("Caught Exception: " + ex.getMessage());
            System.out.println("Response Status Code: " + ex.getStatusCode());
            System.out.println("Error Code: " + ex.getErrorCode());
            System.out.println("Error Type: " + ex.getErrorType());
            System.out.println("Request ID: " + ex.getRequestId());
            System.out.print("XML: " + ex.getXML());
		}
	}
	
	public void execute() throws BuildException {
		service = new AmazonEC2Client(this.accessKey, this.secretKey);
		allocateElasticIp();
		associateElasticIp();
	}
}
