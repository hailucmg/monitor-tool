	package cmg.org.monitor.ext.model.shared;

import java.io.Serializable;

@SuppressWarnings("serial")
public class MonitorEditDto implements Serializable{
	String id;
	
	String name;
	
	String group;
	
	String[] groups;
	
	boolean isActive;
	
	String protocol;
	
	String ip;
	
	String url;
	
	String remoteURl;
	
	String email;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPasswordEmail() {
		return passwordEmail;
	}
	public void setPasswordEmail(String passwordEmail) {
		this.passwordEmail = passwordEmail;
	}
	String passwordEmail;
	
	public String getRemoteURl() {
		return remoteURl;
	}
	public void setRemoteURl(String remoteURl) {
		this.remoteURl = remoteURl;
	}
	int select;
	public int getSelect() {
		return select;
	}
	public void setSelect(int select) {
		this.select = select;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public String[] getGroups() {
		return groups;
	}
	public void setGroups(String[] groups) {
		this.groups = groups;
	}
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
}
