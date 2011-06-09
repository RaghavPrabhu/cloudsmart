package com.css.labs.cloudsmart;

import java.util.ArrayList;
import java.util.List;

import com.amazonaws.ec2.AmazonEC2Client;

public class IncreaseEBSVolume extends VolumeOperations {
	
	private String accessKey = "YOUR_AWS_ACCESS_KEY";
	
	private String secretKey = "YOUR_AWS_SECRET_KEY";
	
	private String snapshotId = "";
	
	private String volumeId = "";
	
	private String size="";
	
	private String availabilityZone="";
	
	private String deviceName="";
	
	private List<String> instanceVolume = null;
	
	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
	public void setSnapshotId(String snapshotId) {
		this.snapshotId = snapshotId;
	}
	public void setVolumeId(String volumeId) {
		this.volumeId = volumeId;
	}
	public void setInstanceVolume(List<String> instanceVolume) {
		this.instanceVolume = instanceVolume;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public void setAvailabilityZone(String availabilityZone) {
		this.availabilityZone = availabilityZone;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public void execute(){
		EBSSnapshot snapShot = new EBSSnapshot();
		snapShot.setAccessKey(accessKey);
		snapShot.setSecretKey(secretKey);
		snapShot.setInstanceId(instanceId);
		snapShot.setSnapshotId(snapshotId);
		snapShot.execute();
		volumeId = snapShot.getVolumeId();
		instanceVolume = new ArrayList<String>(0);
		instanceVolume.add(volumeId);
		amazonEC2Service = new AmazonEC2Client(accessKey, secretKey);
		detachVolume(instanceVolume);
		volumeStatus(instanceVolume);
		deleteVolume(instanceVolume);
		CreateVolAttach createVolume = new CreateVolAttach();
		createVolume.setInstanceID(instanceId);
		createVolume.setAccessKey(accessKey);
		createVolume.setSecretKey(secretKey);
		createVolume.setSnapshotId(snapshotId);
		createVolume.setSize(size);
		createVolume.setAvailabilityZone(availabilityZone);
		createVolume.setDevice(deviceName);
		createVolume.execute();
	}
}
