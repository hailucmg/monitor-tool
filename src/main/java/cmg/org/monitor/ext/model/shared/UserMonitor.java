package cmg.org.monitor.ext.model.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cmg.org.monitor.entity.shared.AlertStoreMonitor;
import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.entity.shared.SystemUser;
import cmg.org.monitor.util.shared.MonitorConstant;

public class UserMonitor implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -517786869646667636L;
	private String id;
	private List<String> groupIds;
	private SystemUser user;
	private int role;
	private ArrayList<GroupMonitor> groups;
	private ArrayList<SystemMonitor> systems;
	
	private ArrayList<AlertStoreMonitor> stores;
	
	
	public ArrayList<AlertStoreMonitor> getStores() {
		return stores;
	}

	public void setStores(ArrayList<AlertStoreMonitor> stores) {
		this.stores = stores;
	}

	public ArrayList<SystemMonitor> getSystems() {
		return systems;
	}

	public void setSystems(ArrayList<SystemMonitor> systems) {
		this.systems = systems;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}
	
	public String getRoleName() {
		StringBuffer sb = new StringBuffer();
		sb.append(role == MonitorConstant.ROLE_ADMIN ? "Administator" : "Normal user");
		return sb.toString();
	}

	public String getGroupsName() {
		StringBuffer sb = new StringBuffer();
		if (groups != null && groups.size() > 0) {
			for (int i = 0; i < groups.size(); i++) {
				sb.append(groups.get(i));
				if (i != groups.size() - 1) {
					sb.append(", ");
				}
			}
		} else {
			sb.append("null");
		}
		return sb.toString();
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("\r\nID: " + id);
		sb.append("\r\nRole: "
				+ (role == MonitorConstant.ROLE_ADMIN ? "Admin" : "User"));
		sb.append("\r\nGroup: ");
		if (groups != null && groups.size() > 0) {
			for (GroupMonitor g : groups) {
				sb.append(g + ", ");
			}
		} else {
			sb.append("null");
		}
		return sb.toString();
	}

	public ArrayList<GroupMonitor> getGroups() {
		return groups;
	}

	public void setGroups(ArrayList<GroupMonitor> groups) {
		this.groups = groups;
	}

	public void addGroup(GroupMonitor g) {
		if (g != null) {
			if (groups == null) {
				groups = new ArrayList<GroupMonitor>();
				groups.add(g);
			} else {
				boolean check = false;
				for (GroupMonitor group : groups) {
					if (group.getName().equals(g.getName())) {
						check = true;
						break;
					}
				}
				if (!check) {
					groups.add(g);
				}
			}
			if (g.getName().toLowerCase().contains(MonitorConstant.ADMIN_MONITOR_GROUP)) {
				role = MonitorConstant.ROLE_ADMIN;
			}
		}
	}
	
	public void addSystem(SystemMonitor system) {
		if (system != null) {
			if (systems == null) {
				systems = new ArrayList<SystemMonitor>();
				systems.add(system);
			} else {
				boolean check = false;
				for (SystemMonitor sys : systems) {
					if (sys.getId().equals(system.getId())) {
						check = true;
						break;
					}
				}
				if (!check) {
					systems.add(system);
				}
			}
			
		}
	}
	
	public void addAlertStore(AlertStoreMonitor store) {
		if (store != null) {
			if (stores == null) {
				stores = new ArrayList<AlertStoreMonitor>();
			}
			stores.add(store);
		}
	}
	
	public int compareByName(UserMonitor c) {
		return id.trim().compareTo(c.getId().trim());
	}

	public boolean checkGroup(String groupId) {
		if (groupIds != null && groupIds.size() > 0) {
			for (String gid: groupIds) {
				if (gid.equalsIgnoreCase(groupId)) {
					return true;
				}
			}
		}
		return false;
	}

	/** 
	 * @return the groupIds 
	 */
	public List<String> getGroupIds() {
		return groupIds;
	}

	/** 
	 * @param groupIds the groupIds to set 
	 */
	
	public void setGroupIds(List<String> groupIds) {
		this.groupIds = groupIds;
	}

	/** 
	 * @return the user 
	 */
	public SystemUser getUser() {
		return user;
	}

	/** 
	 * @param user the user to set 
	 */
	
	public void setUser(SystemUser user) {
		this.user = user;
	}
}