package cmg.org.monitor.entity.shared;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.gwt.user.client.rpc.IsSerializable;

public class InvitedUser implements IsSerializable{
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	@Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
	private String id;
	
	@Persistent
	private String email;
	
	@Persistent
	private String firstName;

	@Persistent
	private String lastName;
	
	@Persistent
	private String[] groupIDs;
	
	@Persistent
	private String status;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

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
	 * @param groupIDs
	 *            the groupIDs to set
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
