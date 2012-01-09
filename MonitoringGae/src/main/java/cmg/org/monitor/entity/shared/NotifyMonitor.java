package cmg.org.monitor.entity.shared;

import java.io.Serializable;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class NotifyMonitor implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	@Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
	private String id;

	@Persistent
	private String sid;

	@Persistent
	private boolean isNotifyCpu;

	@Persistent
	private boolean isNotifyMemory;

	@Persistent
	private boolean isJVM;

	@Persistent
	private boolean isNotifyServices;

	@Persistent
	private boolean isNotifyServicesConnection;

	public NotifyMonitor() {
	}

	
	public NotifyMonitor(String sid, boolean isNotifyCpu,
			boolean isNotifyMemory,
			boolean isNotifyServices, boolean isNotifyServicesConnection, boolean isJVM) {
		this.sid = sid;
		this.isNotifyCpu = isNotifyCpu;
		this.isNotifyMemory = isNotifyMemory;
		this.isNotifyServices = isNotifyServices;
		this.isNotifyServicesConnection = isNotifyServicesConnection;
		this.isJVM = isJVM;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public boolean isNotifyCpu() {
		return isNotifyCpu;
	}

	public void setNotifyCpu(boolean isNotifyCpu) {
		this.isNotifyCpu = isNotifyCpu;
	}

	public boolean isNotifyMemory() {
		return isNotifyMemory;
	}

	public void setNotifyMemory(boolean isNotifyMemory) {
		this.isNotifyMemory = isNotifyMemory;
	}

	public boolean isNotifyServices() {
		return isNotifyServices;
	}

	public void setNotifyServices(boolean isNotifyServices) {
		this.isNotifyServices = isNotifyServices;
	}

	public boolean isNotifyServicesConnection() {
		return isNotifyServicesConnection;
	}

	public void setNotifyServicesConnection(boolean isNotifyServicesConnection) {
		this.isNotifyServicesConnection = isNotifyServicesConnection;
	}

	public String getId() {
		return id;
	}
	public boolean isJVM() {
		return isJVM;
	}

	public void setJVM(boolean isJVM) {
		this.isJVM = isJVM;
	}
	public void swapValue(NotifyMonitor nf){
		this.isJVM = nf.isJVM;
		this.isNotifyCpu = nf.isNotifyCpu;
		this.isNotifyMemory = nf.isNotifyMemory;
		this.isNotifyServices = nf.isNotifyServices;
		this.isNotifyServicesConnection = nf.isNotifyServicesConnection;
		this.sid = nf.getSid();
		this.id = nf.getId();
	}
}