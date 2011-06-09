package com.css.labs.cloudsmart.test;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.tools.ant.Task;

import com.amazonaws.ec2.AmazonEC2;
import com.amazonaws.ec2.AmazonEC2Client;
import com.amazonaws.ec2.AmazonEC2Exception;
import com.amazonaws.ec2.model.Address;
import com.amazonaws.ec2.model.DescribeAddressesRequest;
import com.amazonaws.ec2.model.DescribeAddressesResponse;
import com.amazonaws.ec2.model.DescribeAddressesResult;
import com.amazonaws.ec2.model.DescribeInstancesRequest;
import com.amazonaws.ec2.model.DescribeInstancesResponse;
import com.amazonaws.ec2.model.DescribeInstancesResult;
import com.amazonaws.ec2.model.InstanceBlockDeviceMapping;
import com.amazonaws.ec2.model.InstanceEbsBlockDevice;
import com.amazonaws.ec2.model.Reservation;
import com.amazonaws.ec2.model.RunningInstance;
import com.css.labs.cloudsmart.CreateAttachIP;
import com.css.labs.cloudsmart.DetachDeletevolumes;

/**
 * @author Hajamaideen Abdul
 * 
 */

public class InstanceTest extends Task {
	
	static Logger logger = Logger.getLogger(InstanceTest.class);
	
	private String accessKey = "YOUR_AWS_ACCESS_KEY";
	private String secretKey = "YOUR_AWS_SECRET_KEY";
	private String instanceId = "instanceId";
	private String publicIp = "publicIp";
	private String serverType = null;
	private AmazonEC2 amazonEC2Service = null;		
	
	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}
	
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
	
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}	
	
	public void setPublicIp(String publicIp) {
		this.publicIp = publicIp;
	}	
	
	public void setServerType(String serverType) {
		this.serverType = serverType;
	}
	
	public List<String> listVolume() {
		List<String> instanceVolume = null;
		if(accessKey != null && secretKey != null && instanceId != null) {				
			instanceVolume = new ArrayList<String>(0);
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
	                    	if (runningInstance.isSetIpAddress()) {
	                            logger.info("IpAddress : " + runningInstance.getIpAddress());
	                            publicIp = runningInstance.getIpAddress();
	                        }
	                        List<InstanceBlockDeviceMapping> blockDeviceMappingList = runningInstance.getBlockDeviceMapping();
	                        for (InstanceBlockDeviceMapping blockDeviceMapping : blockDeviceMappingList) {
	                            if (blockDeviceMapping.isSetEbs()) {              
	                                InstanceEbsBlockDevice  ebs = blockDeviceMapping.getEbs();                                
	                                if (ebs.isSetVolumeId()) {
	                                    logger.info("VolumeId : " + ebs.getVolumeId());  
	                                    instanceVolume.add(ebs.getVolumeId());	                            	
	                                }	                         
	                            } 
	                        }
	                    }
	                }
	            }            
	           
	        } catch (AmazonEC2Exception ex) {
	        	logger.error("Caught Exception: " + ex.getMessage());
	            logger.error("Response Status Code: " + ex.getStatusCode());
	            logger.error("Error Code: " + ex.getErrorCode());
	            logger.error("Error Type: " + ex.getErrorType());
	            logger.error("Request ID: " + ex.getRequestId());
	        }
		}
        return instanceVolume;
	}
	
	public boolean checkInstanceVolumes() {
		boolean status = false;
		List<String> volumeList = null;
		if(serverType != null) {			
			volumeList = listVolume();			
			if(volumeList != null) {				
				if(serverType.equalsIgnoreCase("AppDB")) {
					if(volumeList.size() == 4){
						status = true;
					}
				}
				else if(serverType.equalsIgnoreCase("WS")){
					if(volumeList.size() == 1){
						status = true;
					}					
				}
			}
		}
		return status;
	}
	
	public boolean checkElasticIp() {
		boolean status = false;
		
		if(publicIp != null) {
			try {
				DescribeAddressesRequest request = new DescribeAddressesRequest();
				List<String> publicIpList = new ArrayList<String>(0);
				publicIpList.add(publicIp);
				DescribeAddressesResponse response = amazonEC2Service.describeAddresses(request);
				if (response.isSetDescribeAddressesResult()) {
	                DescribeAddressesResult  describeAddressesResult = response.getDescribeAddressesResult();
	                List<Address> addressList = describeAddressesResult.getAddress();
	                for (Address address : addressList) {
	                	if(instanceId.equalsIgnoreCase(address.getInstanceId()) && publicIp.equals(address.getPublicIp())) {
	                		status = true;
	                	}	                    
	                }
	            }
			}
			catch (AmazonEC2Exception ex) {            
	            logger.error("Caught Exception: " + ex.getMessage());
	            logger.error("Response Status Code: " + ex.getStatusCode());
	            logger.error("Error Code: " + ex.getErrorCode());
	            logger.error("Error Type: " + ex.getErrorType());
	            logger.error("Request ID: " + ex.getRequestId());
	            System.out.print("XML: " + ex.getXML());
	        }			
		}			
		return status;
	}

	public void execute() {
		
		amazonEC2Service = new AmazonEC2Client(accessKey, secretKey);
		
		if(checkInstanceVolumes()) {
			if(serverType.equalsIgnoreCase("AppDB")) {
				logger.info("The EBS Volumes are attached with the "+instanceId+" Instance");
			}
			else {
				logger.info("The EBS Volumes are detached with the "+instanceId+" Instance");
				
			}
		}
		else {
			if(serverType.equalsIgnoreCase("AppDB")) {
				logger.info("The EBS Volumes are not attached with the "+instanceId+" Instance");
			}
			else if(serverType.equalsIgnoreCase("WS")) {
				logger.info("The EBS Volumes are not detached with the "+instanceId+" Instance");
				DetachDeletevolumes deleteInstanceVolumes = new DetachDeletevolumes();
				deleteInstanceVolumes.setAccessKey(accessKey);
				deleteInstanceVolumes.setSecretKey(secretKey);
				deleteInstanceVolumes.setInstanceId(instanceId);
				deleteInstanceVolumes.setOsDevice("/dev/sda1");
				deleteInstanceVolumes.execute();
			}
		}
		
		if(serverType.equalsIgnoreCase("WS")) {
			if(checkElasticIp()) {
				logger.info("The Elastic Ip is attached to the Instance");
			}
			else {
				logger.info("The Elastic Ip is not attached to the Instance");
				CreateAttachIP associateIp = new CreateAttachIP();
				associateIp.setAccessKey(accessKey);
				associateIp.setSecretKey(secretKey);
				associateIp.setInstanceId(instanceId);
				associateIp.execute();
			}
		}
	}
}
