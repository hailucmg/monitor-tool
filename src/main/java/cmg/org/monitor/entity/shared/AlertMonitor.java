package cmg.org.monitor.entity.shared;

import java.util.Date;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * @author lamphan
 * @version 1.0
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class AlertMonitor implements Model {
	public static final int CANNOT_GATHER_DATA = 0x001;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
	private
	AlertStoreMonitor alertStore;	

	@Persistent
	private Date timeStamp;
	
	/**
	 * Default constructor.<br>
	 */
	public AlertMonitor() {

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
