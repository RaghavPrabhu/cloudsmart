/**
 * 
 */
package com.css.labs.cloudsmart;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import com.amazonaws.ec2.AmazonEC2;
import com.amazonaws.ec2.AmazonEC2Client;
import com.amazonaws.ec2.AmazonEC2Exception;
import com.amazonaws.ec2.model.AttachVolumeRequest;
import com.amazonaws.ec2.model.AttachVolumeResponse;
import com.amazonaws.ec2.model.AttachVolumeResult;
import com.amazonaws.ec2.model.Attachment;

/**
 * @author Prabhu KuppurajBabu
 *
 */
public class AttachVolume extends Task {

	
	private String accessKey;
	private String secretKey;
	private String volumeID;	
	private String instanceID;
	private String device;	
	
	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public void setVolumeID(String volumeID) {
		this.volumeID = volumeID;
	}

	public void setInstanceID(String instanceID) {
		this.instanceID = instanceID;
	}

	public void setDevice(String device) {
		this.device = device;
	}
	
	public boolean attachVolume() {
		boolean status = false;
		try {
			AmazonEC2 service = new AmazonEC2Client(this.accessKey,this.secretKey);
			AttachVolumeRequest AttachVolRequest = new AttachVolumeRequest();
			AttachVolRequest.setInstanceId(instanceID);
			AttachVolRequest.setVolumeId(volumeID);
			AttachVolRequest.setDevice(device);
			
		    AttachVolumeResponse attachVolResponse = service.attachVolume(AttachVolRequest);
	        AttachVolumeResult  attachVolumeResult = attachVolResponse.getAttachVolumeResult();
	        Attachment  attachment = attachVolumeResult.getAttachment();
	        
	        System.out.println(" Device Type :"+attachment.getDevice());
	        System.out.println(" Time : "+attachment.getAttachTime());
	        status = true;
	        
		} catch (AmazonEC2Exception e) {
			System.err.println("Attach Volume Exception :"+e.getErrorCode());
		}
		return status;
	}

	@Override
	public void execute() throws BuildException {
		if(attachVolume()) {
			System.out.println(" Volume sucessfully attached to the Instance");	        
		}
		else {
			System.out.println(" Volume are not successfully attached to the Instance");	
		}
	}

}
