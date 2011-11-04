package cmg.org.monitor.ext.model.shared;

import java.io.Serializable;
import java.util.Set;

/**
 * 
 * @author lamphan
 * @version 1.0 The 'detailed' Data Transfer Object for the SystemMonitor class
 * 
 */
@SuppressWarnings("serial")
public class SystemDto implements Serializable {

	/** Represent code of entity */
	private String code;

	/** Represent ID of entity */
	private String id;

	/** Represent name of entity */
	private String name;

	private String url;

	/** Represent ID of entity */
	private String ip;

	/** Represent status of entity */
	private Boolean isActive;

	/** Represent ID of entity */
	private Boolean systemStatus;

	/** Represent mark of entity */
	private Boolean isDeleted;

	/** Represent protocol of entity */
	private String protocol;

	/** Represent group of mail */
	private String groupEmail;

	/** **/
	private Set<AlertDto> alerts;

	/**
	 * Default constructor.<br>
	 */
	public SystemDto() {
	}

	/**
	 * Constructor with parameters.
	 * 
	 * @param name
	 * @param address
	 * @param ip
	 * @param isActive
	 * @param systemStatus
	 * @param isDeleted
	 * @param protocol
	 */
	public SystemDto(String name, String ip, String url, Boolean isActive,
			Boolean systemStatus, Boolean isDeleted, String protocol,
			 String groupEmail) {
		this();
		setBasicInfo(name, ip, url, isActive, systemStatus, isDeleted,
				protocol, alerts, groupEmail);
	}

	/**
	 * @param name
	 * @param address
	 * @param ip
	 * @param isActive
	 * @param systemStatus
	 * @param isDeleted
	 * @param protocol
	 */
	public void setBasicInfo(String name, String ip, String url,
			Boolean isActive, Boolean systemStatus, Boolean isDeleted,
			String protocol, Set<AlertDto> alerts, String groupEmail) {
		this.name = name;

		this.url = url;
		this.ip = ip;
		this.isActive = isActive;
		this.systemStatus = systemStatus;
		this.isDeleted = isDeleted;
		this.protocol = protocol;
		this.groupEmail = groupEmail;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Boolean getSystemStatus() {
		return systemStatus;
	}

	public void setSystemStatus(Boolean systemStatus) {
		this.systemStatus = systemStatus;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
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

	public Set<AlertDto> getAlerts() {
		return alerts;
	}

	public void setAlerts(Set<AlertDto> alerts) {
		this.alerts = alerts;
	}
}
