/*
 * Copyright (c) CMG Ltd All rights reserved.
 *
 * This software is the confidential and proprietary information of CMG
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with CMG.
 */
package cmg.org.monitor.entity.shared;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * The Class InvitedUser.
 */
public class InvitedUser implements IsSerializable{
	public static final String STATUS_ACTIVE = "Active";
	
	public static final String STATUS_PENDING = "Pending";
	
	public static final String STATUS_REQUESTING = "Requesting";
	
	/** The id. */
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	@Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
	private String id;
	
	/** The email. */
	@Persistent
	private String email;
	
	/** The first name. */
	@Persistent
	private String firstName;

	/** The last name. */
	@Persistent
	private String lastName;
	
	/** The group i ds. */
	@Persistent
	private String[] groupIDs;
	
	/** The status. */
	@Persistent
	private String status;	
	
	public boolean checkStatus(String status) {
		return this.status.equalsIgnoreCase(status);
	}
	
	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Gets the email.
	 *
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Sets the email.
	 *
	 * @param email the new email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Gets the first name.
	 *
	 * @return the first name
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Sets the first name.
	 *
	 * @param firstName the new first name
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Gets the last name.
	 *
	 * @return the last name
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Sets the last name.
	 *
	 * @param lastName the new last name
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Gets the status.
	 *
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Sets the status.
	 *
	 * @param status the new status
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * Adds the group.
	 *
	 * @param groupId the group id
	 */
	public void addGroup(String groupId) {
		boolean check = false;
		List<String> groups = getGroupIDs();
		if (groups != null && groups.size() > 0) {
			for (String gId : groups) {
				if (gId.equalsIgnoreCase(groupId)) {
					check = true;
					break;
				}
			}
		}
		if (!check) {
			if (groups == null) {
				groups = new ArrayList<String>();
			}
			groups.add(groupId);
		}
		setGroupIDs(groups);
	}
	
	/**
	 * Removes the group.
	 *
	 * @param groupId the group id
	 */
	public void removeGroup(String groupId) {
		int index = -1;
		List<String> groups = getGroupIDs();
		if (groups != null && !groups.isEmpty()) {
			for (int i = 0; i < groups.size(); i++) {
				if (groups.get(i).equalsIgnoreCase(groupId)) {
					index = i;
					break;
				}
			}
		}
		if (index != -1) {
			groups.remove(index);
		}
		setGroupIDs(groups);		
	}
	
	/**
	 * Gets the group i ds.
	 *
	 * @return the groupIDs
	 */
	public List<String> getGroupIDs() {
		if (groupIDs == null) {
			return null;
		} else {
			List<String> list = new ArrayList<String>();
			for (int i = 0; i < groupIDs.length; i++) {
				list.add(groupIDs[i]);
			}
			return list;
		}
	}

	/**
	 * Sets the group i ds.
	 *
	 * @param listGroupIDs the new group i ds
	 */

	public void setGroupIDs(List<String> listGroupIDs) {
		if (listGroupIDs != null && listGroupIDs.size() > 0) {
			groupIDs = new String[listGroupIDs.size()];
			listGroupIDs.toArray(groupIDs);
		} else {
		    groupIDs = null;
		}
	}
}
