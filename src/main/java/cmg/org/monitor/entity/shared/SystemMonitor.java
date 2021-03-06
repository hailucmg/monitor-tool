package cmg.org.monitor.entity.shared;

import java.util.Date;

import javax.annotation.Nullable;
import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.Unique;

import cmg.org.monitor.util.shared.Utility;

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
public class SystemMonitor implements IsSerializable  {
	
	public static final String STATUS_SMILE = "smile";
	public static final String STATUS_BORED = "bored";
	public static final String STATUS_DEAD = "dead";	

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	@Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
	private String encodedKey;

	@Persistent
	private String remoteUrl;

	@Persistent
	private String code;

	@Unique
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
	
	@Persistent
	private String healthStatus;
	
	@Persistent
	private Date timeStamp;

	@Persistent
	private int lastestCpuUsage;

	@Persistent
	private int lastestMemoryUsage;
	
	@NotPersistent
	private NotifyMonitor notify;
	
	@Persistent
	private
	Integer startHour;
	
	@Persistent
	private
	Integer startMinute;
	
	@Persistent
	private
	Integer endHour;
	
	@Persistent
	private
	Integer endMinute;	
	
	@Persistent
	private
	Integer endMinutes;
	
	@Persistent
	private
	Integer startMinutes;


	public NotifyMonitor getNotify() {
		if (notify == null) {
			notify = new NotifyMonitor();
		}
		return notify;
	}

	public void setNotify(NotifyMonitor notify) {
		this.notify = notify;
	}

	/**
	 * Default constructor.<br>
	 */
	public SystemMonitor() {
		this.healthStatus = STATUS_DEAD;
		this.isActive = false;
		this.isDeleted = false;
		this.lastestCpuUsage = -1;
		this.lastestMemoryUsage = -1;
		this.startHour = 0;
		this.startMinute = 0;
		this.endHour = 0;
		this.endMinute = 0;
		this.endMinutes = 0;
		this.startMinutes = 0;
	}

	public SystemMonitor(String code, String name, String url,
			String remoteUrl, boolean isActice) {
		this.code = code;
		this.name = name;
		this.url = url;
		this.remoteUrl = remoteUrl;
		this.isActive = isActice;
		this.healthStatus = STATUS_DEAD;
		this.isDeleted = false;
		this.lastestCpuUsage = -1;
		this.lastestMemoryUsage = -1;
	}
	
	@Override
	public String toString() {
		return code + " - " + name;
	}
	
	public void swapValue(SystemMonitor sys) {
		code = sys.getCode();
		name = sys.getName();
		url = sys.getUrl();
		remoteUrl = sys.getRemoteUrl();
		isActive = sys.isActive();
		isDeleted = sys.isDeleted();
		healthStatus = sys.getHealthStatus();
		ip = sys.getIp();
		groupEmail = sys.getGroupEmail();
		email = sys.getEmail();
		emailRevice = sys.getEmailRevice();
		status = sys.getStatus();
		protocol = sys.getProtocol();
		timeStamp = sys.getTimeStamp();
		lastestCpuUsage = sys.getLastestCpuUsage();
		lastestMemoryUsage = sys.getLastestMemoryUsage();
		startHour = sys.getStartHour();
		startMinute = sys.getStartMinute();
		endHour = sys.getEndHour();
		endMinute = sys.getEndMinute();
		endMinutes = sys.getEndMinutes();
		startMinutes = sys.getStartMinutes();
	}

	public String getId() {
		return encodedKey;
	}

	public void setId(String id) {
		this.encodedKey = id;
	}
	
	public String getName() {
		return name == null ? "N/A" : name;
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
		return url == null ? "N/A" : url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getIp() {
		return ip == null ? "N/A" : ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public boolean getStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
		if (!status) {
			this.healthStatus = STATUS_DEAD;
		}
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
	
	public int compareByCode(SystemMonitor sys) {
		return code.compareTo(sys.getCode());
	}

	/** 
	 * @return the startHour 
	 */
	public int getStartHour() {
		return Utility.getIntegerValue(startHour, 0);
	}

	/** 
	 * @param startHour the startHour to set 
	 */
	
	public void setStartHour(int startHour) {
		this.startHour = startHour;
	}

	/** 
	 * @return the startMinute 
	 */
	public int getStartMinute() {
		return Utility.getIntegerValue(startMinute, 0);
	}

	/** 
	 * @param startMinute the startMinute to set 
	 */
	
	public void setStartMinute(int startMinute) {
		
		this.startMinute = startMinute;
	}

	/** 
	 * @return the endHour 
	 */
	public int getEndHour() {		
		return Utility.getIntegerValue(endHour, 0);
	}

	/** 
	 * @param endHour the endHour to set 
	 */
	
	public void setEndHour(int endHour) {
		this.endHour = endHour;
	}

	/** 
	 * @return the endMinute 
	 */
	public int getEndMinute() {
		return Utility.getIntegerValue(endMinute, 0);
	}

	/** 
	 * @param endMinute the endMinute to set 
	 */
	
	public void setEndMinute(int endMinute) {
		this.endMinute = endMinute;
	}

	/** 
	 * @return the endMinutes 
	 */
	public Integer getEndMinutes() {
		return endMinutes == null ? 0 : endMinutes;
	}

	/** 
	 * @param endMinutes the endMinutes to set 
	 */
	
	public void setEndMinutes(Integer endMinutes) {
		this.endMinutes = endMinutes;
	}

	/** 
	 * @return the startMinutes 
	 */
	public Integer getStartMinutes() {
		return startMinutes == null ? 0 : startMinutes;
	}

	/** 
	 * @param startMinutes the startMinutes to set 
	 */
	
	public void setStartMinutes(Integer startMinutes) {
		this.startMinutes = startMinutes;
	}

}
