/**
 * 
 */
package com.css.labs.cloudsmart;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import com.amazonaws.ec2.AmazonEC2;
import com.amazonaws.ec2.AmazonEC2Client;
import com.amazonaws.ec2.AmazonEC2Exception;
import com.amazonaws.ec2.model.Attachment;
import com.amazonaws.ec2.model.DetachVolumeRequest;
import com.amazonaws.ec2.model.DetachVolumeResponse;
import com.amazonaws.ec2.model.DetachVolumeResult;

/**
 * @author Prabhu KuppurajBabu
 *
 */
public class DetachVolume extends Task {

	private String accessKey;
	private String secretKey;
	private String volumeID;	
	private String instanceID;
	private String device;
	private boolean force;
	

	
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
	 * @return the volumeID
	 */
	public String getVolumeID() {
		return volumeID;
	}

	/**
	 * @param volumeID the volumeID to set
	 */
	public void setVolumeID(String volumeID) {
		this.volumeID = volumeID;
	}

	/**
	 * @return the instanceID
	 */
	public String getInstanceID() {
		return instanceID;
	}

	/**
	 * @param instanceID the instanceID to set
	 */
	public void setInstanceID(String instanceID) {
		this.instanceID = instanceID;
	}

	/**
	 * @return the device
	 */
	public String getDevice() {
		return device;
	}

	/**
	 * @param device the device to set
	 */
	public void setDevice(String device) {
		this.device = device;
	}
	
	/**
	 * @return the force
	 */
	public boolean isForce() {
		return force;
	}

	/**
	 * @param force the force to set
	 */
	public void setForce(boolean force) {
		this.force = force;
	}
	
	/* (non-Javadoc)
	 * @see org.apache.tools.ant.Task#execute()
	 */
	@Override
	public void execute() throws BuildException {
		// TODO Auto-generated method stub
		
		
		try {
			AmazonEC2 service = new AmazonEC2Client(this.accessKey,this.secretKey);
			DetachVolumeRequest detachVolRequest= new DetachVolumeRequest(this.volumeID,this.instanceID,this.device,force);
			DetachVolumeResponse detachVolResponse = service.detachVolume(detachVolRequest);
			DetachVolumeResult  detachVolumeResult = detachVolResponse.getDetachVolumeResult();
			Attachment  attachment = detachVolumeResult.getAttachment();
			
			System.out.println(" Volume Deattached Sucessfully: "+attachment.getVolumeId());
			
			
		} catch (AmazonEC2Exception e) {
			// TODO Auto-generated catch block
			e.getMessage();
		} 
		
		
		
	}

	
	

}
