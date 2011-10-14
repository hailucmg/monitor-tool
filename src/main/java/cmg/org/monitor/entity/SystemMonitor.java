/**
 * 
 */
package cmg.org.monitor.entity;

import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

/**
 * @author lamphan
 * @version 1.0
 */
@PersistenceCapable
public class SystemMonitor {

	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;

	@Persistent
	private List<Long> networks;

	@Persistent
	private String name;

	@Persistent
	private String address;

	@Persistent
	private String ip;

	@Persistent
	private Boolean isActive;
	
	@Persistent
	private Boolean systemStatus;
	
	@Persistent
	private Boolean isDeleted;

	public SystemMonitor() {

	}

	private SystemMonitor(String name, String address, String ip,
			Boolean isActive, Boolean systemStatus, Boolean isDeleted) {
		super();
		this.name = name;
		this.address = address;
		this.ip = ip;
		this.isActive = isActive;
		this.systemStatus = systemStatus;
		this.isDeleted = isDeleted;
	}
	
	public Long getId() {
		return id;
	}

	public List<Long> getNetworks() {
		return networks;
	}

	public void setNetworks(List<Long> networks) {
		this.networks = networks;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
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

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

}
