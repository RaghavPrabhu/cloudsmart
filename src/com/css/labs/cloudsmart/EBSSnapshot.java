package com.css.labs.cloudsmart;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.TreeSet;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import com.amazonaws.ec2.AmazonEC2;
import com.amazonaws.ec2.AmazonEC2Client;
import com.amazonaws.ec2.AmazonEC2Exception;
import com.amazonaws.ec2.model.CreateSnapshotRequest;
import com.amazonaws.ec2.model.CreateSnapshotResponse;
import com.amazonaws.ec2.model.CreateSnapshotResult;
import com.amazonaws.ec2.model.DeleteSnapshotRequest;
import com.amazonaws.ec2.model.DeleteSnapshotResponse;
import com.amazonaws.ec2.model.DescribeInstancesRequest;
import com.amazonaws.ec2.model.DescribeInstancesResponse;
import com.amazonaws.ec2.model.DescribeInstancesResult;
import com.amazonaws.ec2.model.DescribeSnapshotsRequest;
import com.amazonaws.ec2.model.DescribeSnapshotsResponse;
import com.amazonaws.ec2.model.DescribeSnapshotsResult;
import com.amazonaws.ec2.model.DescribeVolumesRequest;
import com.amazonaws.ec2.model.DescribeVolumesResponse;
import com.amazonaws.ec2.model.DescribeVolumesResult;
import com.amazonaws.ec2.model.InstanceBlockDeviceMapping;
import com.amazonaws.ec2.model.InstanceEbsBlockDevice;
import com.amazonaws.ec2.model.Reservation;
import com.amazonaws.ec2.model.ResponseMetadata;
import com.amazonaws.ec2.model.RunningInstance;
import com.amazonaws.ec2.model.Snapshot;
import com.amazonaws.ec2.model.Volume;
import com.css.labs.cloudsmart.eventlog.WindowsEventLog;

/**
 * @author Hajamaideen Abdul
 * 
 */

public class EBSSnapshot extends Task {

	private String accessKey = "YOUR_AWS_ACCESS_KEY";
	private String secretKey = "YOUR_AWS_SECRET_KEY";
	private String instanceId = "test";
	private String snapshotId = "";
	private String volumeId = "";
	private List<String> instanceVolume = null;
	private AmazonEC2 amazonEC2Service = null;
	private int snapLimit = 0;
	private static int LOG_LEVEL_ERROR=1;
	private static int LOG_LEVEL_WARN=2;
	private static int LOG_LEVEL_INFO=3;

	public void setSnapLimit(int snapLimit) {
		this.snapLimit = snapLimit;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public void setSnapshotId(String snapshotId) {
		this.snapshotId = snapshotId;
	}

	public String getVolumeId() {
		return this.volumeId;
	}

	public void addVolume() {
		System.out
				.println("******************************* ADD VOLUME **********************************");
		instanceVolume = new ArrayList<String>(0);
		try {
			DescribeInstancesRequest request = new DescribeInstancesRequest();
			List<String> instance = new ArrayList<String>();
			instance.add(instanceId);
			request.setInstanceId(instance);

			DescribeInstancesResponse response = amazonEC2Service
					.describeInstances(request);

			if (response.isSetDescribeInstancesResult()) {
				DescribeInstancesResult describeInstancesResult = response
						.getDescribeInstancesResult();
				List<Reservation> reservationList = describeInstancesResult
						.getReservation();
				for (Reservation reservation : reservationList) {
					List<RunningInstance> runningInstanceList = reservation
							.getRunningInstance();
					for (RunningInstance runningInstance : runningInstanceList) {
						List<InstanceBlockDeviceMapping> blockDeviceMappingList = runningInstance
								.getBlockDeviceMapping();
						for (InstanceBlockDeviceMapping blockDeviceMapping : blockDeviceMappingList) {
							if (blockDeviceMapping.isSetEbs()) {
								InstanceEbsBlockDevice ebs = blockDeviceMapping
										.getEbs();
								if (ebs.isSetVolumeId()) {
									System.out.println("VolumeId : "
											+ ebs.getVolumeId());
									instanceVolume.add(ebs.getVolumeId());
								}
							}
						}
					}
				}
			}

		} catch (AmazonEC2Exception ex) {
			System.out.println("Caught Exception: " + ex.getMessage());
			System.out.println("Response Status Code: " + ex.getStatusCode());
			System.out.println("Error Code: " + ex.getErrorCode());
			System.out.println("Error Type: " + ex.getErrorType());
			System.out.println("Request ID: " + ex.getRequestId());
		}
	}

	public void selectVolume() {
		System.out
				.println("******************************* SELECT VOLUME **********************************");
		try {
			DescribeVolumesRequest request = new DescribeVolumesRequest();
			request.setVolumeId(instanceVolume);
			DescribeVolumesResponse response = amazonEC2Service
					.describeVolumes(request);
			if (response.isSetDescribeVolumesResult()) {
				DescribeVolumesResult describeVolumesResult = response
						.getDescribeVolumesResult();
				List<Volume> volumeList = describeVolumesResult.getVolume();
				for (Volume volume : volumeList) {
					if (volume.isSetSnapshotId()) {
						System.out.println("SnapshotId : "
								+ volume.getSnapshotId());
						if (snapshotId.trim().equalsIgnoreCase(
								volume.getSnapshotId().trim())) {
							volumeId = volume.getVolumeId();
							System.out.println("Selected Volume : " + volumeId);
							break;
						}
					}
				}
			}
		} catch (AmazonEC2Exception ex) {
			System.out.println("Caught Exception: " + ex.getMessage());
			System.out.println("Response Status Code: " + ex.getStatusCode());
			System.out.println("Error Code: " + ex.getErrorCode());
			System.out.println("Error Type: " + ex.getErrorType());
			System.out.println("Request ID: " + ex.getRequestId());
		}
	}

	public TreeSet<String> getSnapshotList() {
		System.out
				.println("******************************* SNAPSHOT LIST **********************************");
		TreeSet<String> snapList = new TreeSet<String>();
		try {
			DescribeSnapshotsRequest request = new DescribeSnapshotsRequest();
			DescribeSnapshotsResponse response = amazonEC2Service
					.describeSnapshots(request);
			if (response.isSetDescribeSnapshotsResult()) {
				DescribeSnapshotsResult describeSnapshotsResult = response
						.getDescribeSnapshotsResult();
				List<Snapshot> snapshotList = describeSnapshotsResult
						.getSnapshot();
				for (Snapshot snapshot : snapshotList) {
					if(snapshot.isSetVolumeId() && snapshot.isSetStartTime()){
						if (snapshot.getVolumeId().equalsIgnoreCase(volumeId)) {
							snapList.add(snapshot.getStartTime() + ","
									+ snapshot.getSnapshotId());
						}
					}
				}
			}
		} catch (AmazonEC2Exception ex) {
			System.out.println("Caught Exception: " + ex.getMessage());
			System.out.println("Response Status Code: " + ex.getStatusCode());
			System.out.println("Error Code: " + ex.getErrorCode());
			System.out.println("Error Type: " + ex.getErrorType());
			System.out.println("Request ID: " + ex.getRequestId());
		}
		return snapList;
	}

	public void createSnapShot() {
		WindowsEventLog eventLog = new WindowsEventLog();
		Snapshot snapshot = null;
		
		try {
			TreeSet<String> snapshotList = getSnapshotList();

			String snapId = "", createdTime = "";
			if (snapshotList.size() >= snapLimit && snapshotList.size() > 0) {
				while (snapshotList.size() >= snapLimit && snapshotList.size() > 0) {
					String snapData[] = snapshotList.first().split(",");
					createdTime = snapData[0];
					snapId = snapData[1];
					System.out.println("Snapshot Id = " + snapId + " and Time = " + createdTime);
					deleteSnapShot(snapId);
					snapshotList = getSnapshotList();
				}
			}
			System.out
			.println("******************************* CREATE SNAPSHOT **********************************");
			CreateSnapshotRequest createSnapshotRequest = new CreateSnapshotRequest();
			System.out.println("volume id" +volumeId);
			createSnapshotRequest.setVolumeId(volumeId);
			CreateSnapshotResponse response = amazonEC2Service
					.createSnapshot(createSnapshotRequest);

			System.out
					.println("************************** CreateSnapshot Action Response ******************************");

			if (response.isSetCreateSnapshotResult()) {
				CreateSnapshotResult createSnapshotResult = response
						.getCreateSnapshotResult();
				if (createSnapshotResult.isSetSnapshot()) {
					snapshot = createSnapshotResult.getSnapshot();
					if (snapshot.isSetSnapshotId()) {
					//	System.out.println("\tSnapshotId : "
						//		+ snapshot.getSnapshotId());
						System.out.println(snapshot.getSnapshotId() +"  Snapshot created successfully");
						//eventLog.setEventLog("SnapshotId : "+snapshot.getSnapshotId());
						eventLog.setEventLog(snapshot.getSnapshotId(),LOG_LEVEL_INFO);
					createPropertyFile(snapshot.getSnapshotId());
					
					}
				}
			}
		} catch (AmazonEC2Exception ex) {
			System.out.println("Caught Exception: " + ex.getMessage());
			System.out.println("Response Status Code: " + ex.getStatusCode());
			System.out.println("Error Code: " + ex.getErrorCode());
			System.out.println("Error Type: " + ex.getErrorType());
			System.out.println("Request ID: " + ex.getRequestId());
			System.out.println("XML: " + ex.getXML());
			//eventLog.setEventLog("CloudSnapshotId : "+snapshotId +"Error during operation");
			eventLog.setEventLog(snapshotId,LOG_LEVEL_ERROR);
		}
	}

	public void createPropertyFile(String snapShot) {
		try {
		    Properties snapProperties = new Properties();
			String propertyFile = "snapshot.properties";
			try {
				snapProperties.load(new FileInputStream(propertyFile));
			} catch (FileNotFoundException e) {				
				snapProperties.store(new FileOutputStream(propertyFile), null);
			}
			
			snapProperties.setProperty("SnapShotId", snapShot);
			snapProperties.store(new FileOutputStream(propertyFile), null);
	    }
	    catch(Exception exception) {
	    	System.err.println("Snapshot Property File Exception : "+exception.getMessage());
	    }		
	}
	public void deleteSnapShot(String deleteSnapShot) {
		System.out
				.println("******************************* DELETE SNAPSHOT **********************************");
		try {
			DeleteSnapshotRequest deleteSnapshotRequest = new DeleteSnapshotRequest();
			deleteSnapshotRequest.setSnapshotId(deleteSnapShot);

			DeleteSnapshotResponse response = amazonEC2Service
					.deleteSnapshot(deleteSnapshotRequest);

			System.out
					.println("************************** DeleteSnapshot Action Response ******************************");

			if (response.isSetResponseMetadata()) {
				System.out
						.println("********************************** ResponseMetadata ************************************");

				ResponseMetadata responseMetadata = response
						.getResponseMetadata();
				if (responseMetadata.isSetRequestId()) {
					System.out.println("\tRequestId : "
							+ responseMetadata.getRequestId());
				}
			}

		} catch (AmazonEC2Exception ex) {

			System.out.println("Caught Exception: " + ex.getMessage());
			System.out.println("Response Status Code: " + ex.getStatusCode());
			System.out.println("Error Code: " + ex.getErrorCode());
			System.out.println("Error Type: " + ex.getErrorType());
			System.out.println("Request ID: " + ex.getRequestId());
			System.out.println("XML: " + ex.getXML());
		}
	}

	public void execute() throws BuildException {
		try {
			amazonEC2Service = new AmazonEC2Client(accessKey, secretKey);			
			addVolume();
			selectVolume();
			createSnapShot();
		} catch (Exception exception) {
			System.out.println("EBS Volume SnapShot Creation Exception: "
					+ exception);
		}
	}
}
