/**
 * 
 */
package com.css.labs.cloudsmart;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import com.amazonaws.ec2.AmazonEC2;
import com.amazonaws.ec2.AmazonEC2Client;
import com.amazonaws.ec2.AmazonEC2Exception;
import com.amazonaws.ec2.model.Placement;
import com.amazonaws.ec2.model.ResponseMetadata;
import com.amazonaws.ec2.model.RunInstancesRequest;
import com.amazonaws.ec2.model.RunInstancesResponse;

/**
 * @author Prabhu KuppurajBabu
 * 
 */
public class LaunchInstance extends Task {

	private String accessKey;
	private String secretKey;
	private String imageID;
	private String zone;
	private String instanceType;
	private String keyName;
	private List<String> securityGroup = new ArrayList<String>();
	private String groupName;
	private int minCount=1;
	private int maxCount;
	private String userDataFile;
	private String additionalUserData;
	private byte[] encodedUserData;
	private String userData;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.tools.ant.Task#execute()
	 */
	/**
	 * @return the imageID
	 */
	public String getImageID() {
		return imageID;
	}

	/**
	 * @param imageID
	 *            the imageID to set
	 */
	public void setImageID(String imageID) {
		this.imageID = imageID;
	}

	/**
	 * @return the zone
	 */
	public String getZone() {
		return zone;
	}

	/**
	 * @param zone
	 *            the zone to set
	 */
	public void setZone(String zone) {
		this.zone = zone;
	}

	/**
	 * @return the instanceType
	 */
	public String getInstanceType() {
		return instanceType;
	}

	/**
	 * @param instanceType
	 *            the instanceType to set
	 */
	public void setInstanceType(String instanceType) {
		this.instanceType = instanceType;
	}

	/**
	 * @return the keyName
	 */
	public String getKeyName() {
		return keyName;
	}

	/**
	 * @param keyName
	 *            the keyName to set
	 */
	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

	/**
	 * @return the groupName
	 */
	public String getGroupName() {
		return groupName;
	}

	/**
	 * @param groupName
	 *            the groupName to set
	 */
	public void setGroupName(String groupName) {
		this.groupName = groupName;
		setSecurityGroup(this.groupName);
	}

	/**
	 * @return the securityGroup
	 */
	public List<String> getSecurityGroup() {
		return securityGroup;
	}

	/**
	 * @param securityGroup
	 *            the securityGroup to set
	 */
	public void setSecurityGroup(String securityGroup) {
		if (null != securityGroup) {
			if (getGroupName().indexOf(",") != -1) {
				String[] groups = getGroupName().split(",");
				for (String group : groups) {
					this.securityGroup.add(group);
				}
			} else {
				this.securityGroup.add(securityGroup);
			}
		}
		
	}

	/**
	 * @return the minCount
	 */
	public int getMinCount() {
		return minCount;
	}

	/**
	 * @param minCount
	 *            the minCount to set
	 */
	public void setMinCount(int minCount) {

		// Atleast One Instance to be launched
		if (minCount <= 0) {
			this.minCount = 1;
		}
		this.minCount = minCount;
	}

	/**
	 * @return the maxCount
	 */
	public int getMaxCount() {
		return maxCount;
	}

	/**
	 * @param maxCount
	 *            the maxCount to set
	 */
	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}

	/**
	 * @param accessKey
	 *            the accessKey to set
	 */
	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	/**
	 * @param secretKey
	 *            the secretKey to set
	 */
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	/**
	 * @return the userDataFile
	 */
	public String getUserDataFile() {
		return userDataFile;
	}

	/**
	 * @param userDataFile
	 *            the userDataFile to set
	 */
	public void setUserDataFile(String userDataFile) {
		this.userDataFile = userDataFile;
	}

	/**
	 * @return the additionalUserData
	 */
	public String getAdditionalUserData() {
		return additionalUserData;
	}

	/**
	 * @param additionalUserData
	 *            the additionalUserData to set
	 */
	public void setAdditionalUserData(String additionalUserData) {
		this.additionalUserData = additionalUserData;
	}

	/**
	 * @return the encodedUserData
	 */
	public byte[] getEncodedUserData() {
		return encodedUserData;
	}

	
	
	/**
	 * @return the userData
	 */
	public String getUserData() {
		return userData;
	}

	/**
	 * @param userData the userData to set
	 */
	public void setUserData(String userData) {
		this.userData = userData;
		setEncodedUserData(this.userData);
	}

	/**
	 * @param encodedUserData
	 *            the encodedUserData to set
	 *            
	 *   Consolidating user-data from various inputs           
	 */
	public void setEncodedUserData(String encodedUserData) {
		
		/*FileWriter out=null;
		
		 try {
			out=new FileWriter(this.userDataFile,true);
			 out.write("\n");
			 if(null!=encodedUserData){
				 encodedUserData=encodedUserData.replaceAll(",", "\n");
				 out.write(encodedUserData);
			 }
			 out.write("\n");
	         out.close();
	         File userDataFile = new File(getUserDataFile());
	         FileInputStream fileInputStream = new FileInputStream(userDataFile);
				BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
				byte fileData[] = new byte[bufferedInputStream.available()];
				bufferedInputStream.read(fileData, 0, fileData.length);
				this.encodedUserData = Base64.encodeBase64(fileData, false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}*/
		
		encodedUserData=encodedUserData.replaceAll(",", "\n");
		System.out.println(encodedUserData);
		byte[] encodeByte=encodedUserData.getBytes();
		this.encodedUserData=Base64.encodeBase64(encodeByte,false);
		
	}

	@Override
	public void execute() throws BuildException {
		// TODO Auto-generated method stub
		AmazonEC2 amazonEC2Service=new AmazonEC2Client(this.accessKey, this.secretKey);
		RunInstancesRequest runInstancesRequest = new RunInstancesRequest();

		runInstancesRequest.setImageId(this.imageID);
		runInstancesRequest.setInstanceType(this.instanceType);

		Placement placement = new Placement();
		placement.setAvailabilityZone(this.zone);

		runInstancesRequest.setPlacement(placement);
		runInstancesRequest.setKeyName(this.keyName);
		runInstancesRequest.setSecurityGroup(this.securityGroup);
		runInstancesRequest.setMinCount(this.minCount);
		runInstancesRequest.setMaxCount(this.maxCount);
		runInstancesRequest.setUserData(new String(getEncodedUserData()));
		
		try {
			RunInstancesResponse runInstancesResponse=amazonEC2Service.runInstances(runInstancesRequest);
			ResponseMetadata responseMetadata = runInstancesResponse.getResponseMetadata();
			System.out.println(responseMetadata.getRequestId());
			
		} catch (AmazonEC2Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}

	}

}
