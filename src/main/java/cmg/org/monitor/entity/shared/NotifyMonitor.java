package cmg.org.monitor.entity.shared;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

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
public class NotifyMonitor implements IsSerializable {

	

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	@Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
	private String id;

	@Persistent
	private String sid;

	@Persistent
	private Boolean isNotifyCpu = false;

	@Persistent
	private Boolean isNotifyMemory = false;

	@Persistent
	private Boolean isJVM = false;

	@Persistent
	private Boolean isNotifyServices = false;

	@Persistent
	private Boolean isNotifyServicesConnection = false;
	
	@Persistent
	private Boolean isNotifyConnectionPool = false;

	public NotifyMonitor() {
		this.isNotifyCpu = false;
		this.isNotifyMemory = false;
		this.isNotifyServices = false;
		this.isNotifyServicesConnection = false;
		this.isJVM = false;
		this.isNotifyConnectionPool = false;
	}

	
	public NotifyMonitor(String sid, boolean isNotifyCpu,
			boolean isNotifyMemory,
			boolean isNotifyServices, boolean isNotifyServicesConnection, boolean isJVM, boolean isNotifyConnectionPool) {
		this.sid = sid;
		this.isNotifyCpu = isNotifyCpu;
		this.isNotifyMemory = isNotifyMemory;
		this.isNotifyServices = isNotifyServices;
		this.isNotifyServicesConnection = isNotifyServicesConnection;
		this.isJVM = isJVM;
		this.isNotifyConnectionPool = isNotifyConnectionPool;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public boolean isNotifyCpu() {
		if (isNotifyCpu == null) {
			return false;
		}
		return isNotifyCpu.booleanValue();
	}

	public void setNotifyCpu(boolean isNotifyCpu) {
		this.isNotifyCpu = isNotifyCpu;
	}

	public boolean isNotifyMemory() {
		if (isNotifyMemory == null){
			return false;
		}
		return isNotifyMemory.booleanValue();
	}

	public void setNotifyMemory(boolean isNotifyMemory) {
		this.isNotifyMemory = isNotifyMemory;
	}

	public boolean isNotifyServices() {
		if (isNotifyServices == null) {
			return false;
		}
		return isNotifyServices.booleanValue();
	}

	public void setNotifyServices(boolean isNotifyServices) {
		this.isNotifyServices = isNotifyServices;
	}

	public boolean isNotifyServicesConnection() {
		if (isNotifyServicesConnection == null) {
			return false;
		}
		return isNotifyServicesConnection.booleanValue();
	}

	public void setNotifyServicesConnection(boolean isNotifyServicesConnection) {
		this.isNotifyServicesConnection = isNotifyServicesConnection;
	}

	public String getId() {
		return id;
	}
	public boolean isJVM() {
		if (isJVM == null) {
			return false;
		}
		return isJVM.booleanValue();
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
		this.isNotifyConnectionPool = nf.isNotifyConnectionPool;
		this.sid = nf.getSid();
		this.id = nf.getId();
	}


	/** 
	 * @return the isNotifyConnectionPool 
	 */
	public boolean isNotifyConnectionPool() {
		if (isNotifyConnectionPool == null) {
			return false;
		}
		return isNotifyConnectionPool.booleanValue();
	}


	/** 
	 * @param isNotifyConnectionPool the isNotifyConnectionPool to set 
	 */
	
	public void setNotifyConnectionPool(boolean isNotifyConnectionPool) {
		this.isNotifyConnectionPool = isNotifyConnectionPool;
	}
}