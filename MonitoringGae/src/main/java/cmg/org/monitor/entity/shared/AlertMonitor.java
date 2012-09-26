package cmg.org.monitor.entity.shared;

import java.io.Serializable;
import java.util.Date;

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
public class AlertMonitor implements IsSerializable {
	
	
	public static final int CANNOT_GATHER_DATA = 0x001;
	public static final int HIGH_USAGE_LEVEL_CPU = 0x002;
	public static final int HIGH_USAGE_LEVEL_JVM = 0x003;
	public static final int HIGH_USAGE_LEVEL_MEMORY = 0x004;
	public static final int HIGH_USAGE_LEVEL_FILESYSTEM = 0x005;
	public static final int SERVICE_HIGH_LEVEL_PING_TIME = 0x006;
	public static final int SERVICE_ERROR_STATUS = 0x007;


	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	@Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
	private String id;

	@Persistent
	private int type;

	@Persistent
	private String error;

	@Persistent
	private String description;

	@Persistent
	private AlertStoreMonitor alertStore;

	@Persistent
	private Date timeStamp;

	/**
	 * Default constructor.<br>
	 */
	public AlertMonitor() {

	}

	public AlertMonitor(int type, String error, String description,
			Date timeStamp) {
		super();
		this.type = type;
		this.error = error;
		this.description = description;
		this.timeStamp = timeStamp;
	}
	@Override
	public String toString() {
		StringBuffer sf = new StringBuffer();
		sf.append("\r\nType: " + type);
		sf.append("\r\nError: " + error);
		sf.append("\r\nDescription: " + description);
		sf.append("\r\nTimestamp: " + timeStamp + "\r\n");
		return sf.toString();
	}
	public String getId() {
		return id;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public AlertStoreMonitor getAlertStore() {
		return alertStore;
	}

	public void setAlertStore(AlertStoreMonitor alertStore) {
		this.alertStore = alertStore;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}


} // End class
