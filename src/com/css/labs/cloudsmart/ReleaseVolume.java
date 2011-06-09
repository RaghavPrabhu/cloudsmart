package com.css.labs.cloudsmart;

import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.BuildException;

import com.amazonaws.ec2.AmazonEC2Client;

/**
 * @author Hajamaideen Abdul
 * 
 */
public class ReleaseVolume extends VolumeOperations {
	private String accessKey;
	private String secretKey;
	private String instanceID;
	private String snapshotId;
	
	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}
	
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
	
	public void setInstanceID(String instanceID) {
		this.instanceID = instanceID;
	}
	
	public void setSnapshotId(String snapshotId) {
		this.snapshotId = snapshotId;
	}	
	
	public void execute() throws BuildException {
		EBSSnapshot snapshot = new EBSSnapshot();
		snapshot.setAccessKey(accessKey);
		snapshot.setSecretKey(secretKey);
		snapshot.setSnapshotId(snapshotId);
		snapshot.setInstanceId(instanceID);
		snapshot.addVolume();
		snapshot.selectVolume();
		String volumeId = snapshot.getVolumeId();
		if(volumeId != null) {
			List<String> volume = new ArrayList<String>(0);
			volume.add(volumeId);
			
			amazonEC2Service = new AmazonEC2Client(accessKey, secretKey);
			detachVolume(volume);
			volumeStatus(volume);
			deleteVolume(volume);
		}
	}
}
