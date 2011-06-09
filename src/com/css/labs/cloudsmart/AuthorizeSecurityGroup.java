/**
 * 
 */
package com.css.labs.cloudsmart;

import org.apache.tools.ant.Task;

import com.amazonaws.ec2.AmazonEC2;
import com.amazonaws.ec2.AmazonEC2Client;
import com.amazonaws.ec2.AmazonEC2Exception;
import com.amazonaws.ec2.model.AuthorizeSecurityGroupIngressRequest;
import com.amazonaws.ec2.model.AuthorizeSecurityGroupIngressResponse;
import com.amazonaws.ec2.model.DescribeSecurityGroupsRequest;
import com.amazonaws.ec2.model.DescribeSecurityGroupsResponse;
import com.amazonaws.ec2.model.DescribeSecurityGroupsResult;
import com.amazonaws.ec2.model.IpPermission;
import com.amazonaws.ec2.model.ResponseMetadata;
import com.amazonaws.ec2.model.SecurityGroup;

/**
 * @author Prabhu KuppurajBabu
 * 
 */
public class AuthorizeSecurityGroup extends Task {

	private String accessKey;
	private String secretKey;
	private String groupName;
	private int fromPortNumber;
	private int toPortNumber;
	private String protocol;
	private String ipCIDR = null;

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
	 * @return the fromPortNumber
	 */
	public int getFromPortNumber() {
		return fromPortNumber;
	}

	/**
	 * @param fromPortNumber
	 *            the fromPortNumber to set
	 */
	public void setFromPortNumber(int fromPortNumber) {
		this.fromPortNumber = fromPortNumber;
	}

	/**
	 * @return the toPortNumber
	 */
	public int getToPortNumber() {
		return toPortNumber;
	}

	/**
	 * @param toPortNumber
	 *            the toPortNumber to set
	 */
	public void setToPortNumber(int toPortNumber) {
		this.toPortNumber = toPortNumber;
	}

	/**
	 * @return the protocol
	 */
	public String getProtocol() {
		return protocol;
	}

	/**
	 * @param protocol
	 *            the protocol to set
	 */
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	/**
	 * @return the ipCIDR
	 */
	public String getIpCIDR() {
		return ipCIDR;
	}

	/**
	 * @param ipCIDR
	 *            the ipCIDR to set
	 */
	public void setIpCIDR(String ipCIDR) {
		this.ipCIDR = ipCIDR;
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
	public void execute() {
		boolean success = true;
		// Establishing EC2 Service Connection
		AmazonEC2 amazonEC2Service = new AmazonEC2Client(this.accessKey,
				this.secretKey);
		DescribeSecurityGroupsRequest request = new DescribeSecurityGroupsRequest();
		DescribeSecurityGroupsResponse response = null;
		DescribeSecurityGroupsResult describeSecurityGroupsResult = null;
		try {
			response = amazonEC2Service.describeSecurityGroups(request);
			describeSecurityGroupsResult = response
					.getDescribeSecurityGroupsResult();
		} catch (AmazonEC2Exception e1) {
			e1.printStackTrace();
		}
		java.util.List<SecurityGroup> securityGroupList = describeSecurityGroupsResult
				.getSecurityGroup();
		int count = 0;
		for (SecurityGroup securityGroup : securityGroupList) {
			if (this.groupName.equalsIgnoreCase(securityGroup.getGroupName())) {
				java.util.List<IpPermission> ipPermissionList = securityGroup
						.getIpPermission();
				for (IpPermission ipPermission : ipPermissionList) {
					count = 0;
					if (this.protocol.equalsIgnoreCase(ipPermission
							.getIpProtocol())) {
						count = count + 1;
					}
					if (this.fromPortNumber == ipPermission.getFromPort()) {
						count = count + 1;
					}
					if (this.toPortNumber == ipPermission.getToPort()) {
						count = count + 1;
					}
					if (count == 3) {
						break;
					}
				}
			}
		}
		/*
		 * Authorizing Security Protocol Rule...
		 */

		if (count == 3) {
			success = false;
		} else {
			AuthorizeSecurityGroupIngressRequest authorizeSecurityGroupRequest = new AuthorizeSecurityGroupIngressRequest();
			authorizeSecurityGroupRequest.setGroupName(this.groupName);
			authorizeSecurityGroupRequest.setIpProtocol(this.protocol);
			authorizeSecurityGroupRequest.setFromPort(this.fromPortNumber);
			authorizeSecurityGroupRequest.setToPort(this.toPortNumber);
			authorizeSecurityGroupRequest.setCidrIp(this.ipCIDR);
			

			try {
				AuthorizeSecurityGroupIngressResponse authorizeSecurityGroupResponse = amazonEC2Service
						.authorizeSecurityGroupIngress(authorizeSecurityGroupRequest);
				ResponseMetadata responseMetadata = authorizeSecurityGroupResponse
						.getResponseMetadata();

			} catch (AmazonEC2Exception e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
				success = false;
			}
		}
		if (success == false) {
			System.out
					.println("Same port is already defined for this group "
							+ this.groupName + " From Port: "
							+ this.fromPortNumber + " To Port: "
							+ this.toPortNumber + " CIDR " + this.ipCIDR);
		}

	}

}
