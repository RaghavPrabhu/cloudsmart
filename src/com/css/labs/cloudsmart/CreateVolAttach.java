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
import com.amazonaws.ec2.model.CreateVolumeRequest;
import com.amazonaws.ec2.model.CreateVolumeResponse;
import com.amazonaws.ec2.model.CreateVolumeResult;
import com.amazonaws.ec2.model.Volume;

/**
 * @author Prabhu KuppurajBabu
 * 
 */
public class CreateVolAttach extends Task {

	private String size;
	private String availabilityZone;
	private String snapshotId;
	private String accessKey;
	private String secretKey;
	private String instanceID;
	private String device;

	/**
	 * @return the device
	 */
	public String getDevice() {
		return device;
	}

	/**
	 * @param device
	 *            the device to set
	 */
	public void setDevice(String device) {
		this.device = device;
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
	 * @return the size
	 */
	public String getSize() {
		return size;
	}

	/**
	 * @param size
	 *            the size to set
	 */
	public void setSize(String size) {
		this.size = size;
	}

	/**
	 * @return the availabilityZone
	 */
	public String getAvailabilityZone() {
		return availabilityZone;
	}

	/**
	 * @param availabilityZone
	 *            the availabilityZone to set
	 */
	public void setAvailabilityZone(String availabilityZone) {
		this.availabilityZone = availabilityZone;
	}

	/**
	 * @return the snapshotId
	 */
	public String getSnapshotId() {
		return snapshotId;
	}

	/**
	 * @param snapshotId
	 *            the snapshotId to set
	 */
	public void setSnapshotId(String snapshotId) {
		this.snapshotId = snapshotId;
	}

	/**
	 * @return the accessKey
	 */
	public String getAccessKey() {
		return accessKey;
	}

	/**
	 * @param accessKey
	 *            the accessKey to set
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
	 * @param secretKey
	 *            the secretKey to set
	 */
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.tools.ant.Task#execute()
	 */
	@Override
	public void execute() throws BuildException {
		// TODO Auto-generated method stub
		try {
			AmazonEC2 service = new AmazonEC2Client(this.accessKey,
					this.secretKey);			

			CreateVolumeRequest createVolRequest = new CreateVolumeRequest(
					this.size, this.snapshotId, this.availabilityZone);

			CreateVolumeResponse createVolResponse = service
					.createVolume(createVolRequest);

			CreateVolumeResult createVolumeResult = createVolResponse
					.getCreateVolumeResult();

			Volume volume = createVolumeResult.getVolume();

			System.out.println(" Volume ID " + volume.getVolumeId());

			String instance_id = this.instanceID;

			AttachVolumeRequest AttachVolRequest = new AttachVolumeRequest(
					volume.getVolumeId(), instance_id, this.device);

			AttachVolumeResponse attachVolResponse = service
					.attachVolume(AttachVolRequest);
			AttachVolumeResult attachVolumeResult = attachVolResponse
					.getAttachVolumeResult();

			Attachment attachment = attachVolumeResult.getAttachment();

			System.out
					.println(" Volume Sucessfully attached to the Instance : "
							+ attachment.getInstanceId());
			System.out.println(" Device Type :" + attachment.getDevice());
			System.out.println(" Time : " + attachment.getAttachTime());

		} catch (AmazonEC2Exception ex) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			System.out.println("Caught Exception: " + ex.getMessage());
			System.out.println("Response Status Code: " + ex.getStatusCode());
			System.out.println("Error Code: " + ex.getErrorCode());
			System.out.println("Error Type: " + ex.getErrorType());
			System.out.println("Request ID: " + ex.getRequestId());
			System.out.print("XML: " + ex.getXML());
		}

	}

	
}
