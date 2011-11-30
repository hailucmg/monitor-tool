package cmg.org.monitor.memcache.shared;

import java.io.Serializable;

public class SystemMonitorDto implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;
	
	private String remoteUrl;

	private String code;

	private String name;

	private String url;

	private String ip;

	private boolean isActive;

	private boolean status;

	private boolean isDeleted;

	private String protocol;

	private String groupEmail;

	private String email;
	
	public SystemMonitorDto() {
	}

	public SystemMonitorDto(String remoteUrl, String code, String name,
			String url, String ip, boolean isActive, String protocol, String groupEmail, String email) {
		super();
		this.remoteUrl = remoteUrl;
		this.code = code;
		this.name = name;
		this.url = url;
		this.ip = ip;
		this.isActive = isActive;
		this.status = false;
		this.isDeleted = false;
		this.protocol = protocol;
		this.groupEmail = groupEmail;
		this.email = email;
	}



	public String getRemoteUrl() {
		return remoteUrl;
	}

	public void setRemoteUrl(String remoteUrl) {
		this.remoteUrl = remoteUrl;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getGroupEmail() {
		return groupEmail;
	}

	public void setGroupEmail(String groupEmail) {
		this.groupEmail = groupEmail;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}
