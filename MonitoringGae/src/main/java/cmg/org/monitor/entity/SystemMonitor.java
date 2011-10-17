/**
 * 
 */
package cmg.org.monitor.entity;

import java.util.ArrayList;


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

	@NotPersistent
	private ArrayList<NetworkMonitor> networks;

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
	
	@Persistent
	private String protocol;

	/**
	 * Default constructor.<br>
	 */
	public SystemMonitor() {

	}

	/**
	 * @param name
	 * @param address
	 * @param ip
	 * @param isActive
	 * @param systemStatus
	 * @param isDeleted
	 */
	public SystemMonitor(String name, String address, String ip,
			Boolean isActive, Boolean systemStatus, Boolean isDeleted) {
		super();
		this.name = name;
		this.address = address;
		this.ip = ip;
		this.isActive = isActive;
		this.systemStatus = systemStatus;
		this.isDeleted = isDeleted;
	}
	
	@Override
    public String getId() {
        return encodedKey;
    }
	
	/**
	 * @return
	 */
	public ArrayList<NetworkMonitor> getNetworks() {
		return networks;
	}

	/**
	 * @param networks
	 */
	public void setNetworks(ArrayList<NetworkMonitor> networks) {
		this.networks = networks;
	}
	
	/**
	 * Add a Network to array list.<br>
	 * @param aNetwork
	 */
	public void addNetwork(NetworkMonitor aNetwork) {
        this.networks.add(aNetwork);
    }

	/**
	 * Get a name of system
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Get a network object.<br>
	 * @param index
	 * @return a NetworkMonitor object.
	 */
	public NetworkMonitor getNetworkMonitorAt(int index) {
        if (index < 0 || index >= this.networks.size()) {
            return null;
        }

        return this.networks.get(index);
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

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
}
