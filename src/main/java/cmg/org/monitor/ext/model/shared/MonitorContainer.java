package cmg.org.monitor.ext.model.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cmg.org.monitor.entity.shared.ChangeLogMonitor;
import cmg.org.monitor.entity.shared.CpuMonitor;
import cmg.org.monitor.entity.shared.MemoryMonitor;
import cmg.org.monitor.entity.shared.NotifyMonitor;
import cmg.org.monitor.entity.shared.SystemGroup;
import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.entity.shared.SystemUser;

public class MonitorContainer implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7534556803682802817L;

	private GroupMonitor[] groups;
	
	private String[] emails;

	private String[] remoteUrls;
	
	private SystemMonitor sys;
	
	private MemoryMonitor[] rams;
	
	private MemoryMonitor[] swaps;
	
	private ChangeLogMonitor[] changelogs;
	
	private int changelogCount;
	
	private NotifyMonitor notify;
	
	private ChangeLogMonitor changelog;
	
	private List<SystemGroup> listSystemGroup;
	private List<SystemUser> listSystemUsers;

	public List<SystemGroup> getListSystemGroup() {
		return listSystemGroup;
	}

	public void setListSystemGroup(List<SystemGroup> listSystemGroup) {
		this.listSystemGroup = listSystemGroup;
	}

	public List<SystemUser> getListSystemUsers() {
		return listSystemUsers;
	}

	public void setListSystemUsers(List<SystemUser> listSystemUsers) {
		this.listSystemUsers = listSystemUsers;
	}

	public GroupMonitor[] getGroups() {
		return groups;
	}

	public void setGroups(GroupMonitor[] groups) {
		this.groups = groups;
	}

	public String[] getEmails() {
		return emails;
	}

	public void setEmails(String[] emails) {
		this.emails = emails;
	}

	public String[] getRemoteUrls() {
		return remoteUrls;
	}

	public void setRemoteUrls(String[] remoteUrls) {
		this.remoteUrls = remoteUrls;
	}

	public SystemMonitor getSys() {
		return sys;
	}

	public void setSys(SystemMonitor sys) {
		this.sys = sys;
	}

	public MemoryMonitor[] getRams() {
		return rams;
	}

	public void setRams(MemoryMonitor[] rams) {
		this.rams = rams;
	}

	public MemoryMonitor[] getSwaps() {
		return swaps;
	}

	public void setSwaps(MemoryMonitor[] swaps) {
		this.swaps = swaps;
	}

	public ChangeLogMonitor[] getChangelogs() {
		return changelogs;
	}

	public void setChangelogs(ChangeLogMonitor[] changelogs) {
		this.changelogs = changelogs;
	}

	public int getChangelogCount() {
		return changelogCount;
	}

	public void setChangelogCount(int changelogCount) {
		this.changelogCount = changelogCount;
	}


	public NotifyMonitor getNotify() {
		return notify;
	}

	public void setNotify(NotifyMonitor notify) {
		this.notify = notify;
	}

	public ChangeLogMonitor getChangelog() {
		return changelog;
	}

	public void setChangelog(ChangeLogMonitor changelog) {
		this.changelog = changelog;
	}	
	
}
