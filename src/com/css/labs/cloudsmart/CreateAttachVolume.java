package com.css.labs.cloudsmart;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

/**
 * @author Hajamaideen Abdul
 * 
 */

public class CreateAttachVolume extends Task {
	
	private String accessKey;
	private String secretKey;
	private String instanceID;
	private String device;
	private String size;
	private String snapshotId;
	private String availabilityZone;
	
	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}
	
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
	
	public void setInstanceID(String instanceID) {
		this.instanceID = instanceID;
	}
	
	public void setDevice(String device) {
		this.device = device;
	}
	
	public void setSize(String size) {
		this.size = size;
	}
	
	public void setSnapshotId(String snapshotId) {
		this.snapshotId = snapshotId;
	}	
	
	public void setAvailabilityZone(String availabilityZone) {
		this.availabilityZone = availabilityZone;
	}

	public void execute() throws BuildException {
		// Create the EBS Volume
		CreateVolume createVolume = new CreateVolume();
		createVolume.setAccessKey(accessKey);
		createVolume.setSecretKey(secretKey);
		createVolume.setSnapshotId(snapshotId);
		createVolume.setSize(size);
		createVolume.setAvailabilityZone(availabilityZone);
		String volumeId = createVolume.createVolume();
		System.out.println("Created Volume ID : "+volumeId);
		
		//Attach the EBS Volume with the Instance
		AttachVolume attachVolume = new AttachVolume();
		attachVolume.setAccessKey(accessKey);
		attachVolume.setSecretKey(secretKey);
		attachVolume.setDevice(device);
		attachVolume.setVolumeID(volumeId);
		attachVolume.setInstanceID(instanceID);
		if(attachVolume.attachVolume()) {
			System.out.println(" Volume sucessfully attached to the Instance");	        
		}
		else {
			System.out.println(" Volume are not successfully attached to the Instance");	
		}
	}
	
	
}
