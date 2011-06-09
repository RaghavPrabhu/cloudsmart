/**
 * 
 */
package com.css.labs.cloudsmart;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import com.amazonaws.ec2.AmazonEC2;
import com.amazonaws.ec2.AmazonEC2Client;
import com.amazonaws.ec2.AmazonEC2Exception;
import com.amazonaws.ec2.model.CreateVolumeRequest;
import com.amazonaws.ec2.model.CreateVolumeResponse;
import com.amazonaws.ec2.model.CreateVolumeResult;
import com.amazonaws.ec2.model.Volume;

/**
 * @author Prabhu KuppurajBabu
 * 
 */
public class CreateVolume extends Task {

	private String size;
	private String snapshotId;
	private String accessKey;
	private String secretKey;	
	private String availabilityZone;
	
	public void setSize(String size) {
		this.size = size;
	}

	public void setSnapshotId(String snapshotId) {
		this.snapshotId = snapshotId;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
	
	public void setAvailabilityZone(String availabilityZone) {
		this.availabilityZone = availabilityZone;
	}

	public String createVolume() {
		String volumeId = null;
		try {			
			AmazonEC2 service = new AmazonEC2Client(this.accessKey,this.secretKey);
			CreateVolumeRequest request = new CreateVolumeRequest();
			request.setSize(size);
			request.setSnapshotId(snapshotId);
			request.setAvailabilityZone(availabilityZone);			
			CreateVolumeResponse response = service.createVolume(request);
			CreateVolumeResult createVolumeResult = response.getCreateVolumeResult();
			Volume volume = createVolumeResult.getVolume();
			volumeId = volume.getVolumeId();
		
		} catch (AmazonEC2Exception e) {
			System.err.println("Create Volume Exception :"+e.getErrorCode());
		}
		return volumeId;
	}

	@Override
	public void execute() throws BuildException {		
		System.out.println(" Volume ID "+createVolume());
	}
}
