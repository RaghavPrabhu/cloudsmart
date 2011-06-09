/**
 * 
 */
package com.css.labs.cloudsmart;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import com.amazonaws.ec2.AmazonEC2;
import com.amazonaws.ec2.AmazonEC2Client;
import com.amazonaws.ec2.AmazonEC2Exception;
import com.amazonaws.ec2.model.CreateSecurityGroupRequest;
import com.amazonaws.ec2.model.CreateSecurityGroupResponse;
import com.amazonaws.ec2.model.DescribeSecurityGroupsRequest;
import com.amazonaws.ec2.model.DescribeSecurityGroupsResponse;
import com.amazonaws.ec2.model.DescribeSecurityGroupsResult;
import com.amazonaws.ec2.model.ResponseMetadata;
import com.amazonaws.ec2.model.SecurityGroup;

/**
 * @author Prabhu KuppurajBabu
 * 
 */
public class CreateSecurityGroup extends Task {

	private String accessKey;
	private String secretKey;
	private String groupName;
	private String groupDescription;

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
	}

	/**
	 * @return the groupDescription
	 */
	public String getGroupDescription() {
		return groupDescription;
	}

	/**
	 * @param groupDescription
	 *            the groupDescription to set
	 */
	public void setGroupDescription(String groupDescription) {
		this.groupDescription = groupDescription;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.tools.ant.Task#execute()
	 */
	@Override
	public void execute() throws BuildException {
		boolean success = true;
		AmazonEC2 amazonEC2Service = new AmazonEC2Client(this.accessKey,
				this.secretKey);

		DescribeSecurityGroupsRequest request = new DescribeSecurityGroupsRequest();
		DescribeSecurityGroupsResponse response;
		DescribeSecurityGroupsResult describeSecurityGroupsResult = null;
		try {
			response = amazonEC2Service.describeSecurityGroups(request);

			describeSecurityGroupsResult = response
					.getDescribeSecurityGroupsResult();
		} catch (AmazonEC2Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		java.util.List<SecurityGroup> securityGroupList = describeSecurityGroupsResult
				.getSecurityGroup();

		for (SecurityGroup securityGroup : securityGroupList) {
			if (this.groupName.equalsIgnoreCase(securityGroup.getGroupName())) {
				success = false;
			}
		}
		if (success == true) {
			try {
				CreateSecurityGroupRequest securityGrouprequest = new CreateSecurityGroupRequest();
				securityGrouprequest.setGroupName(this.groupName);
				securityGrouprequest.setGroupDescription(this.groupDescription);
				CreateSecurityGroupResponse securityGroupResponse = amazonEC2Service
						.createSecurityGroup(securityGrouprequest);
				ResponseMetadata responseMetadata = securityGroupResponse
						.getResponseMetadata();
			} catch (AmazonEC2Exception e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Security Group Already Exists on the Name "
					+ this.groupName);
		}
	}
}
