package cmg.org.monitor.memcache.shared;

import java.io.Serializable;
import java.util.Date;

public class ServiceMonitorDto implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name;

	private Date systemDate;

	private int ping;

	private boolean status;

	private String description;
	
	private String error;

	private Date timeStamp;

	
	
	public ServiceMonitorDto(String name, Date systemDate, int ping,
			boolean status, String description, Date timeStamp, String error) {
		super();
		this.name = name;
		this.setSystemDate(systemDate);
		this.setPing(ping);
		this.setStatus(status);
		this.setDescription(description);
		this.setTimeStamp(timeStamp);
		this.setError(error);
	}

	
	
	public ServiceMonitorDto() {
		super();
		// TODO Auto-generated constructor stub
	}



	public String getError() {
		return error;
	}



	public void setError(String error) {
		this.error = error;
	}



	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public int getPing() {
		return ping;
	}

	public void setPing(int ping) {
		this.ping = ping;
	}

	public Date getSystemDate() {
		return systemDate;
	}

	public void setSystemDate(Date systemDate) {
		this.systemDate = systemDate;
	}
	
}
