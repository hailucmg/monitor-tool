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
 * 
 */
@PersistenceCapable
public class SystemMonitor {

	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;

//	@Persistent
//	private List<Key> networks;

	@Persistent
	private String name;

	@Persistent
	private String address;

	@Persistent
	private String ip;

	@Persistent
	private Boolean isActive;

	public SystemMonitor() {

	}

	private SystemMonitor(String name, String address, String ip,
			Boolean isActive) {
		super();
		this.name = name;
		this.address = address;
		this.ip = ip;
		this.isActive = isActive;
	}

//	public List<Key> getNetworks() {
//		return networks;
//	}
//
//	public void setNetworks(List<Key> networks) {
//		this.networks = networks;
//	}

	public Key getKey() {
		return key;
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

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

}
