package cmg.org.monitor.ext.model.shared;

import java.io.Serializable;

@SuppressWarnings("serial")
public class MonitorContainer implements Serializable {

	private String[] groups;

	private String[] emails;

	private String[] remoteURLs;
	
	private int select;	

	public String[] getRemoteURLs() {
		return remoteURLs;
	}

	public void setRemoteURLs(String[] remoteURL) {
		this.remoteURLs = remoteURL;
	}

	public String[] getEmails() {
		return emails;
	}

	public void setEmails(String[] emails) {
		this.emails = emails;
	}

	public int getSelect() {
		return select;
	}

	public void setSelect(int select) {
		this.select = select;
	}

	public String[] getGroups() {
		return groups;
	}

	public void setGroups(String[] groups) {
		this.groups = groups;
	}


}
