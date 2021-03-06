package cmg.org.monitor.ext.model.shared;

import cmg.org.monitor.entity.shared.ChangeLogMonitor;
import cmg.org.monitor.entity.shared.InvitedUser;
import cmg.org.monitor.entity.shared.MemoryMonitor;
import cmg.org.monitor.entity.shared.NotifyMonitor;
import cmg.org.monitor.entity.shared.SystemGroup;
import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.entity.shared.SystemUser;

import com.google.gwt.user.client.rpc.IsSerializable;

/** 
	* DOCME
	* 
	* @Creator Hai Lu
	* @author $Author$
	* @version $Revision$
	* @Last changed: $LastChangedDate$
*/
public class MonitorContainer implements IsSerializable {

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
	
	private SystemGroup[] listSystemGroup;
	private SystemUser[] listSystemUsers;
	
	private InvitedUser[] listInvitedUsers;
	
	private String[] listTimeZone;
	
	private String currentTimeZone;
	

	public InvitedUser[] getListInvitedUsers() {
		return listInvitedUsers;
	}

	public void setListInvitedUsers(InvitedUser[] listInvitedUsers) {
		this.listInvitedUsers = listInvitedUsers;
	}

	public SystemGroup[] getListSystemGroup() {
		return listSystemGroup;
	}

	public void setListSystemGroup(SystemGroup[] listSystemGroup) {
		this.listSystemGroup = listSystemGroup;
	}

	public SystemUser[] getListSystemUsers() {
		return listSystemUsers;
	}

	public void setListSystemUsers(SystemUser[] listSystemUsers) {
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

	/** 
	 * @return the listTimeZone 
	 */
	public String[] getListTimeZone() {
		return listTimeZone;
	}

	/** 
	 * @param listTimeZone the listTimeZone to set 
	 */
	
	public void setListTimeZone(String[] listTimeZone) {
		this.listTimeZone = listTimeZone;
	}

	/** 
	 * @return the currentTimeZone 
	 */
	public String getCurrentTimeZone() {
		return currentTimeZone;
	}

	/** 
	 * @param currentTimeZone the currentTimeZone to set 
	 */
	
	public void setCurrentTimeZone(String currentTimeZone) {
		this.currentTimeZone = currentTimeZone;
	}	
	
}
