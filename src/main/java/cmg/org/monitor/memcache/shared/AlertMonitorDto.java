package cmg.org.monitor.memcache.shared;

import java.io.Serializable;
import java.util.Date;


public class AlertMonitorDto implements Serializable  {	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String error;

	private String description;

	private Date timeStamp;
	
	public AlertMonitorDto() {		
	}

	public AlertMonitorDto(String error, String description, Date timeStamp) {
		super();
		this.setError(error);
		this.setDescription(description);
		this.setTimeStamp(timeStamp);
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
