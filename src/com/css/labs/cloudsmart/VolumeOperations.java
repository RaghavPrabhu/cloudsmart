package com.css.labs.cloudsmart;

import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.Task;

import com.amazonaws.ec2.AmazonEC2;
import com.amazonaws.ec2.AmazonEC2Exception;
import com.amazonaws.ec2.model.DeleteVolumeRequest;
import com.amazonaws.ec2.model.DeleteVolumeResponse;
import com.amazonaws.ec2.model.DescribeVolumesRequest;
import com.amazonaws.ec2.model.DescribeVolumesResponse;
import com.amazonaws.ec2.model.DescribeVolumesResult;
import com.amazonaws.ec2.model.DetachVolumeRequest;
import com.amazonaws.ec2.model.DetachVolumeResponse;
import com.amazonaws.ec2.model.ResponseMetadata;
import com.amazonaws.ec2.model.Volume;

public class VolumeOperations extends Task {
	
	protected AmazonEC2 amazonEC2Service = null;	
	protected String instanceId = "test";
	
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	/*
	 * The volumeStatus method pools the status of the EBS Volume
	 */
	public void volumeStatus(List<String> instanceVolume) {
		for(String volumeId : instanceVolume) {
			try {
				String volumeStatus = "";
				System.out.println("****************** VolumeId : " + volumeId);
				while(!volumeStatus.equalsIgnoreCase("available")) {
					
					List<String> volumeInfo = new ArrayList<String>(0);
					volumeInfo.add(volumeId);
					DescribeVolumesRequest request = new DescribeVolumesRequest();
					request.setVolumeId(volumeInfo);
					DescribeVolumesResponse response = amazonEC2Service.describeVolumes(request);
					if (response.isSetDescribeVolumesResult()) {
		                DescribeVolumesResult  describeVolumesResult = response.getDescribeVolumesResult();
		                List<Volume> volumeList = describeVolumesResult.getVolume();
		                for (Volume volume : volumeList) {
		                    if (volume.isSetVolumeId()) {
		                        System.out.println("VolumeId : " + volume.getVolumeId());	                        
		                    }
		                    if (volume.isSetSnapshotId()) {
		                        System.out.println("SnapshotId : " + volume.getSnapshotId());	                        
		                    }
		                    if (volume.isSetStatus()) {
		                        System.out.println("Status : " + volume.getStatus());
		                        volumeStatus = volume.getStatus();
		                    }		                    
		                }
					}
					Thread.sleep(2000);				
				}	
			} catch (AmazonEC2Exception ex) {
	            
	            System.out.println("Caught Exception: " + ex.getMessage());
	            System.out.println("Response Status Code: " + ex.getStatusCode());
	            System.out.println("Error Code: " + ex.getErrorCode());
	            System.out.println("Error Type: " + ex.getErrorType());
	            System.out.println("Request ID: " + ex.getRequestId());
	            System.out.println("XML: " + ex.getXML());
	        } catch (InterruptedException e) {
				System.err.println("InterruptedException : "+e.getMessage());
			}
		}
	}
	
	/*
	 * The detachVolumes method detached the EBS volume attached with the EC2 instance
	 */	
	public void detachVolume(List<String> instanceVolume) {
		for(String volumeId : instanceVolume) {
			try {
				System.out.println("****************** VolumeId : " + volumeId);
			
				DetachVolumeRequest request = new DetachVolumeRequest();
		        request.setInstanceId(instanceId);
		        request.setVolumeId(volumeId);
		        
		        DetachVolumeResponse response = amazonEC2Service.detachVolume(request);
		        
		        if (response.isSetResponseMetadata()) {
	                System.out.println("************** Detach Volume ResponseMetadata ***************");
	                ResponseMetadata  responseMetadata = response.getResponseMetadata();
	                if (responseMetadata.isSetRequestId()) {
	                    System.out.println("RequestId : " + responseMetadata.getRequestId());	                    
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
	}
	
	/*
	 * The deleteVolumes method delete the EBS volume after detached from the EC2 instance
	 */
	public void deleteVolume(List<String> instanceVolume) {
		for(String volumeId : instanceVolume) {
			try {
				System.out.println("****************** VolumeId : " + volumeId);
			
				DeleteVolumeRequest request = new DeleteVolumeRequest();		        
		        request.setVolumeId(volumeId);		       
		        
		        DeleteVolumeResponse response = amazonEC2Service.deleteVolume(request);
		        if (response.isSetResponseMetadata()) {
	                System.out.println("************** Delete Volume ResponseMetadata ***************");
	                ResponseMetadata  responseMetadata = response.getResponseMetadata();
	                if (responseMetadata.isSetRequestId()) {
	                    System.out.println("RequestId : " + responseMetadata.getRequestId());	                    
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
	}
}
