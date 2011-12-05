package cmg.org.monitor.memcache.shared;

import java.io.Serializable;
import java.util.Date;

import cmg.org.monitor.ext.model.Component;


public class AlertMonitorDto implements Serializable  {	
	
	public static final int UNABLE_DATA_READ = 0x001;
	public static final int DATA_ERROR = 0x002;
	public static final int SERVICE_ERROR = 0x003;
	public static final int SLOW_PING_RESPONSE = 0x004;
	public static final int OVER_USE_MEMORY = 0x005;
	public static final int DATA_NULL = 0x006;
	public static final int JVM_ERROR = 0x007;
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String error;
	
	private Component component;

	private String description;
	
	private int type;

	private Date timeStamp;
	
	public AlertMonitorDto() {		
	}

	

	/**
	 * @param error
	 * @param type
	 * @param description
	 * @param timeStamp
	 */
	public AlertMonitorDto(String error,int type, String description, Date timeStamp) {
		this();
		setBasicInfo(error,type, description, timeStamp);
	}

	
	/**
	 * @param error
	 * @param type
	 * @param description
	 * @param timeStamp
	 */
	public void setBasicInfo(String error, int type, String description, Date timeStamp) {
		this.error = error;
		this.setType(type);
		this.description = description;
		this.timeStamp = timeStamp;
	}
	
	
	
	public Component getComponent() {
		return component;
	}



	public void setComponent(Component component) {
		this.component = component;
	}



	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}	
	
}
