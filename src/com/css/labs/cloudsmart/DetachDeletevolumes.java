package com.css.labs.cloudsmart;

import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.BuildException;

import com.amazonaws.ec2.AmazonEC2Client;
import com.amazonaws.ec2.AmazonEC2Exception;
import com.amazonaws.ec2.model.DescribeInstancesRequest;
import com.amazonaws.ec2.model.DescribeInstancesResponse;
import com.amazonaws.ec2.model.DescribeInstancesResult;
import com.amazonaws.ec2.model.InstanceBlockDeviceMapping;
import com.amazonaws.ec2.model.InstanceEbsBlockDevice;
import com.amazonaws.ec2.model.Reservation;
import com.amazonaws.ec2.model.RunningInstance;

public class DetachDeletevolumes extends VolumeOperations {
	
	private String accessKey = "YOUR_AWS_ACCESS_KEY";
	private String secretKey = "YOUR_AWS_SECRET_KEY";	
	private String osDevice = "";
	private List<String> instanceVolume = null;
	
	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}
	
	public void setOsDevice(String osDevice) {
		this.osDevice = osDevice;
	}
	
	public void addVolume() {
		try {
			DescribeInstancesRequest request = new DescribeInstancesRequest();
	        List<String> instance = new ArrayList<String>();
	        instance.add(instanceId);
	        request.setInstanceId(instance);
			 
	        DescribeInstancesResponse response = amazonEC2Service.describeInstances(request);
            
            if (response.isSetDescribeInstancesResult()) {
                DescribeInstancesResult  describeInstancesResult = response.getDescribeInstancesResult();
                List<Reservation> reservationList = describeInstancesResult.getReservation();
                for (Reservation reservation : reservationList) {
                    List<RunningInstance> runningInstanceList = reservation.getRunningInstance();
                    for (RunningInstance runningInstance : runningInstanceList) {                    	
                        List<InstanceBlockDeviceMapping> blockDeviceMappingList = runningInstance.getBlockDeviceMapping();
                        for (InstanceBlockDeviceMapping blockDeviceMapping : blockDeviceMappingList) {
                            if (blockDeviceMapping.isSetDeviceName()) {
                                System.out.println("DeviceName : " + blockDeviceMapping.getDeviceName());                                
                            }
                            if (blockDeviceMapping.isSetEbs()) {              
                                InstanceEbsBlockDevice  ebs = blockDeviceMapping.getEbs();                                
                                if (ebs.isSetVolumeId()) {
                                    System.out.println("VolumeId : " + ebs.getVolumeId());                                    
                                }
                                if (ebs.isSetStatus()) {
                                    System.out.println("Status : " + ebs.getStatus());                                    
                                }  
	                            if(!blockDeviceMapping.getDeviceName().equalsIgnoreCase(osDevice)) {	                            	
	                            	instanceVolume.add(ebs.getVolumeId());
	                            	System.out.println("****************** VolumeId : " + ebs.getVolumeId());
                            	}
                            } 
                        }
                    }
                }
            }             
           
        } catch (AmazonEC2Exception ex) {
                        
        }
	}
	
	public void execute() throws BuildException {
		try {
    		amazonEC2Service = new AmazonEC2Client(accessKey, secretKey);
    		instanceVolume = new ArrayList<String>(0);
    		addVolume();
    		detachVolume(instanceVolume);
    		volumeStatus(instanceVolume);
    		deleteVolume(instanceVolume);
    	}
    	catch(Exception exception){
    		System.out.println("EBS Volume Delete Exception: "+exception);
    	}
	}
}

