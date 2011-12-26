package cmg.org.monitor.entity.shared;

import java.util.Date;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * @author lamphan
 * @version 1.0
 */

@SuppressWarnings("serial")
@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class SystemMonitor implements Model {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	@Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
	private String encodedKey;

	@Persistent
	private String remoteUrl;

	@Persistent
	private String code;

	@Persistent
	private String name;

	@Persistent
	private String url;

	@Persistent
	private String ip;

	@Persistent
	private boolean isActive;

	@Persistent
	private boolean status;

	@Persistent
	private boolean isDeleted;

	@Persistent
	private String protocol;

	@Persistent
	private String groupEmail;

	@Persistent
	private String email;

	@Persistent
	private String emailRevice;
	
	@NotPersistent
	private Date timeStamp;

	@NotPersistent
	private int lastestCpuUsage;

	@NotPersistent
	private int lastestMemoryUsage;

	@NotPersistent
	private String healthStatus;

	/**
	 * Default constructor.<br>
	 */
	public SystemMonitor() {

	}

	public SystemMonitor(String code, String name, String url,
			String remoteUrl, boolean isActice) {
		this.code = code;
		this.name = name;
		this.url = url;
		this.remoteUrl = remoteUrl;
		this.isActive = isActice;
	}

	@Override
	public String getId() {
		return encodedKey;
	}

	public String getName() {
		return name;
	}

	public String getRemoteUrl() {
		return remoteUrl;
	}

	public void setRemoteUrl(String remoteUrl) {
		this.remoteUrl = remoteUrl;
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

	public boolean getStatus() {
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getGroupEmail() {
		return groupEmail;
	}

	public void setGroupEmail(String groupEmail) {
		this.groupEmail = groupEmail;
	}

	public String getHealthStatus() {
		return healthStatus;
	}

	public void setHealthStatus(String healthStatus) {
		this.healthStatus = healthStatus;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	@Override
	public String toString() {
		return code + " - " + name;
	}

	public int getLastestCpuUsage() {
		return lastestCpuUsage;
	}

	public void setLastestCpuUsage(int lastestCpuUsage) {
		this.lastestCpuUsage = lastestCpuUsage;
	}

	public int getLastestMemoryUsage() {
		return lastestMemoryUsage;
	}

	public void setLastestMemoryUsage(int lastestMemoryUsage) {
		this.lastestMemoryUsage = lastestMemoryUsage;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getEmailRevice() {
		return emailRevice;
	}

	public void setEmailRevice(String emailRevice) {
		this.emailRevice = emailRevice;
	}

}
