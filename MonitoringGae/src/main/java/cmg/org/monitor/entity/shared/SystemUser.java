package cmg.org.monitor.entity.shared;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import cmg.org.monitor.ext.model.shared.UserMonitor;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * DOCME
 * 
 * @Creator Hai Lu
 * @author $Author$
 * @version $Revision$
 * @Last changed: $LastChangedDate$
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class SystemUser implements IsSerializable {
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	@Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
	private String id;

	@Persistent
	private String username;

	@Persistent
	private String email;

	@Persistent
	private String domain;

	@Persistent
	private String firstName;

	@Persistent
	private String lastName;

	@Persistent
	private String[] roleIDs;

	@Persistent
	private String[] groupIDs;

	private List<SystemGroup> groups;

	@Persistent
	private boolean isSuspended;

	@Persistent
	private boolean isDomainAdmin;

	public void swap(SystemUser in) {
		this.username = in.username;
		this.domain = in.domain;
		this.email = in.email;
		this.firstName = in.firstName;
		this.lastName = in.lastName;
		this.isSuspended = in.isSuspended;
		this.isDomainAdmin = in.isDomainAdmin;
		this.roleIDs = in.roleIDs;
		this.groupIDs = in.groupIDs;
		this.groups = in.groups;
	}

	public void clear() {
		
		groups = new ArrayList<SystemGroup>();
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
	
	@Deprecated
	public void addToGroup(SystemGroup group) {
		boolean check = false;
		List<String> groups = getGroupIDs();
		if (groups != null && groups.size() > 0) {
			for (String groupId : groups) {
				if (groupId.equalsIgnoreCase(group.getId())) {
					check = true;
					break;
				}
			}
		}
		if (!check) {
			groups.add(group.getId());
		}
		setGroupIDs(groups);
		check = false;
		List<String> users = group.getUserIDs();
		if (users != null && users.size() > 0) {
			for (Object userId : users) {
				if (((String) userId).equalsIgnoreCase(getId())) {
					check = true;
					break;
				}
			}
		}
		if (!check) {
			users.add(getId());
		}
		group.setUserIDs(users);
	}

	@Deprecated
	public void removeFromGroup(SystemGroup group) {
		int index = -1;
		if (!getGroupIDs().isEmpty()) {
			for (int i = 0; i < getGroupIDs().size(); i++) {
				if (getGroupIDs().get(i).equalsIgnoreCase(group.getId())) {
					index = i;
					break;
				}
			}
		}
		if (index != -1) {
			getGroupIDs().remove(index);
		}
		index = -1;
		if (!group.getUserIDs().isEmpty()) {
			for (int i = 0; i < group.getUserIDs().size(); i++) {
				if (((String) group.getUserIDs().get(i))
						.equalsIgnoreCase(getId())) {
					index = i;
					break;
				}
			}
		}
		if (index != -1) {
			group.getUserIDs().remove(index);
		}
	}

	public void addUserRole(String roleId) {
		boolean check = false;
		List<String> roles = getRoleIDs();
		if (roles != null && !roles.isEmpty()) {
			for (String rId : roles) {
				if (rId.equalsIgnoreCase(roleId)) {
					check = true;
					break;
				}
			}
		}
		if (!check) {
			if (roles == null) {
				roles = new ArrayList<String>();
			}
			roles.add(roleId);
		}
		setRoleIDs(roles);
	}
	
	@Deprecated
	public void addRole(SystemRole role) {
		boolean check = false;
		if (!getRoleIDs().isEmpty()) {
			for (String roleId : roleIDs) {
				if (roleId.equalsIgnoreCase(role.getId())) {
					check = true;
					break;
				}
			}
		}
		if (!check) {
			getRoleIDs().add(role.getId());
		}
		check = false;
		if (!role.getUserIDs().isEmpty()) {
			for (Object userId : role.getUserIDs()) {
				if (((String) userId).equalsIgnoreCase(getId())) {
					check = true;
					break;
				}
			}
		}
		if (!check) {
			role.getUserIDs().add(getId());
		}
	}
	
	public void removeUserRole(String roleId) {
		int index = -1;
		List<String> roles = getRoleIDs();
		if (roles != null && !roles.isEmpty()) {
			for (int i = 0; i < roles.size(); i++) {
				if (roles.get(i).equalsIgnoreCase(roleId)) {
					index = i;
					break;
				}
			}
		}
		if (index != -1) {
			roles.remove(index);
		}
		setRoleIDs(roles);
	}

	@Deprecated
	public void removeRole(SystemRole role) {
		int index = -1;
		if (!getRoleIDs().isEmpty()) {
			for (int i = 0; i < getRoleIDs().size(); i++) {
				if (getRoleIDs().get(i).equalsIgnoreCase(role.getId())) {
					index = i;
					break;
				}
			}
		}
		if (index != -1) {
			getRoleIDs().remove(index);
		}
		index = -1;
		if (!role.getUserIDs().isEmpty()) {
			for (int i = 0; i < role.getUserIDs().size(); i++) {
				if (((String) role.getUserIDs().get(i))
						.equalsIgnoreCase(getId())) {
					index = i;
					break;
				}
			}
		}
		if (index != -1) {
			role.getUserIDs().remove(index);
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFullName() {
		if (firstName == null || lastName == null) {
			return "";
		}
		return firstName + " " + lastName;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */

	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */

	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the roles
	 */
	public List<SystemRole> getRoles() {
		if (roleIDs != null && roleIDs.length > 0) {
			List<SystemRole> list = new ArrayList<SystemRole>();
			for (String rid : roleIDs) {
				SystemRole role = new SystemRole();
				role.setName(rid);
				list.add(role);
			}
			return list;
		} else {
			return null;
		}
		
	}

	/**
	 * @return the groups
	 */
	public List<SystemGroup> getGroups() {
		return groups;
	}

	/**
	 * @param groups
	 *            the groups to set
	 */

	public void setGroups(List<SystemGroup> groups) {
		this.groups = groups;
	}

	/**
	 * @return the domain
	 */
	public String getDomain() {
		return domain;
	}

	/**
	 * @param domain
	 *            the domain to set
	 */

	public void setDomain(String domain) {
		this.domain = domain;
	}

	/**
	 * @return the isSuspended
	 */
	public boolean isSuspended() {
		return isSuspended;
	}

	/**
	 * @param isSuspended
	 *            the isSuspended to set
	 */

	public void setSuspended(boolean isSuspended) {
		this.isSuspended = isSuspended;
	}

	/**
	 * @return the isDomainAdmin
	 */
	public boolean isDomainAdmin() {
		return isDomainAdmin;
	}

	/**
	 * @param isDomainAdmin
	 *            the isDomainAdmin to set
	 */

	public void setDomainAdmin(boolean isDomainAdmin) {
		this.isDomainAdmin = isDomainAdmin;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName
	 *            the firstName to set
	 */

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName
	 *            the lastName to set
	 */

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the roleIDs
	 */
	public List<String> getRoleIDs() {
		if (roleIDs == null) {
			return null;
		} else {
			List<String> list = new ArrayList<String>();
			for (int i = 0; i < roleIDs.length; i++) {
				list.add(roleIDs[i]);
			}
			return list;
		}
	}

	/**
	 * @param roleIDs
	 *            the roleIDs to set
	 */

	public void setRoleIDs(List<String> listRoleIDs) {
		if (listRoleIDs != null && listRoleIDs.size() > 0) {
			roleIDs = new String[listRoleIDs.size()];
			listRoleIDs.toArray(roleIDs);
		}
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
		}
	}
	
	public int compareByName(SystemUser s) {
		return username.trim().compareTo(s.getUsername().trim());
	}
}
