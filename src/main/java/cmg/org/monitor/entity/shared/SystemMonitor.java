
package cmg.org.monitor.entity.shared;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import javax.jdo.annotations.Element;
import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import cmg.org.monitor.ext.model.shared.SystemDto;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author lamphan
 * @version 1.0
 */

@SuppressWarnings("serial")
@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class SystemMonitor implements Model, IsSerializable {

	@Persistent(mappedBy = "systemMonitor")
	@Element(dependent = "true")
	private Set<AlertMonitor> alerts = new HashSet<AlertMonitor>();

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	@Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
	private String encodedKey;

	@Persistent(mappedBy = "systemMonitor")
	private List<ServiceMonitor> services = new ArrayList<ServiceMonitor>();

	@Persistent(mappedBy = "systemMonitor")
	private List<FileSystem> fileSystems = new ArrayList<FileSystem>();
	
	@Persistent(mappedBy = "systemMonitor")
	private List<CpuMemory> cpuMems = new ArrayList<CpuMemory>();
	
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
	
	@NotPersistent
	private
	CpuMemory lastCpuMemory;
	
	@NotPersistent
	private
	String healthStatus;
	@NotPersistent
	private
	CpuMemory[] listHistoryCpuMemory;
	
	@NotPersistent
	private
	FileSystem[] lastestFileSystems;
	
	@NotPersistent
	private
	ServiceMonitor[] lastestServiceMonitors;
	
	/**
	 * Default constructor.<br>
	 */
	public SystemMonitor() {

	}

	/**
	 * @param name
	 * @param url
	 * @param ip
	 * @param isActive
	 * @param systemStatus
	 * @param isDeleted
	 */
	public SystemMonitor(String name, String url, String ip,
			boolean isActive) {
		super();
		
		this.name = name;
		this.url = url;
		this.ip = ip;
		this.isActive = isActive;
		this.isDeleted = false;
		this.status = true;
	}
	
	
	public SystemMonitor(String code,String name, String url, String ip,
			boolean isActive) {
		super();
		this.code = code;
		this.name = name;
		this.url = url;
		this.ip = ip;
		this.isActive = isActive;
		this.isDeleted = false;
		this.status = true;
	}
	/**
	 * 
	 * Convert an Entity to Data transfer object.
	 * 
	 * @return an DTO object.
	 */
	public SystemDto toDTO() {
		SystemDto entityDTO = new SystemDto(this.getName(),
			 this.getIp(), this.getUrl(), this.getRemoteUrl(), this.isActive(),
				this.getStatus(), this.isDeleted(), this.getProtocol(), this.getGroupEmail());
		entityDTO.setId(this.getId());

		return entityDTO;
	}
	
	@Override
    public String getId() {
        return encodedKey;
    }
	
	/**
	 * @return
	 */
	public List<ServiceMonitor> getServices() {
		return services;
	}

	/**
	 * @param services
	 */
	public void setServices(List<ServiceMonitor> services) {
		this.services = services;
	}
	
	/**
	 * Add a Service to list.<br>
	 * @param service
	 */
	public void addService(ServiceMonitor service) {
		service.setSystemMonitor(this);
        this.services.add(service);
    }

	/**
	 * Add a new Cpu Memory information to list.<br>
	 * @param service
	 */
	public void addCpuMemory(CpuMemory cpuMemory) {
		cpuMemory.setSystemMonitor(this);
		this.cpuMems.add(cpuMemory);
	}
	/**
	 * Add a new File system information to list.<br>
	 * @param service
	 */
	
	public void addFileSystem(FileSystem fileSystem) {
		fileSystem.setSystemMonitor(this);
		this.fileSystems.add(fileSystem);
	}
	
	
	/**
	 * Get a name of system
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Get a services object.<br>
	 * @param index
	 * @return a ServiceMonitor object.
	 */
	public ServiceMonitor getServiceMonitorAt(int index) {
        if (index < 0 || index >= this.services.size()) {
            return null;
        }

        return this.services.get(index);
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

	public CpuMemory getLastCpuMemory() {
		return lastCpuMemory;
	}

	public void setLastCpuMemory(CpuMemory lastCpuMemory) {
		this.lastCpuMemory = lastCpuMemory;
	}
	
	public String getGroupEmail() {
		return groupEmail;
	}

	public void setGroupEmail(String groupEmail) {
		this.groupEmail = groupEmail;
	}
	
	/**
	 * Get list of alerts.
	 * 
	 * @return set of alerts
	 */
	public Set<AlertMonitor> getAlerts() {
		return alerts;
	}

	public String getHealthStatus() {
		return healthStatus;
	}

	public void setHealthStatus(String healthStatus) {
		this.healthStatus = healthStatus;
	}

	public CpuMemory[] getListHistoryCpuMemory() {
		return listHistoryCpuMemory;
	}

	public void setListHistoryCpuMemory(CpuMemory[] listHistoryCpuMemory) {
		this.listHistoryCpuMemory = listHistoryCpuMemory;
	}

	public FileSystem[] getLastestFileSystems() {
		return lastestFileSystems;
	}

	public void setLastestFileSystems(FileSystem[] lastestFileSystems) {
		this.lastestFileSystems = lastestFileSystems;
	}

	public ServiceMonitor[] getLastestServiceMonitors() {
		return lastestServiceMonitors;
	}

	public void setLastestServiceMonitors(ServiceMonitor[] lastestServiceMonitors) {
		this.lastestServiceMonitors = lastestServiceMonitors;
	}
	
}
