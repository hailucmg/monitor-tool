package cmg.org.monitor.ext.model.shared;

import java.io.Serializable;
import java.util.ArrayList;

import cmg.org.monitor.entity.shared.CpuMonitor;
import cmg.org.monitor.entity.shared.MemoryMonitor;
import cmg.org.monitor.entity.shared.SystemMonitor;

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
	
	
}
