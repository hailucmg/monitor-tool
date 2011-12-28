package cmg.org.monitor.entity.shared;

import java.io.Serializable;
import java.util.Date;

/**
 * Model/entity representation of a network.
 * 
 * @author Hai Lu
 * 
 */

@SuppressWarnings("serial")
public class ServiceMonitor implements Serializable {

	private String name;

	private Date systemDate;
	
	private String strSystemDate;

	private int ping;

	private boolean status;

	private String description;

	private Date timeStamp;

	/**
	 * Default constructor.<br>
	 */
	public ServiceMonitor() {

	}	

	public ServiceMonitor(String name, Date systemDate, int ping,
			boolean status, String description, Date timeStamp) {
		super();

		this.name = name;
		this.systemDate = systemDate;
		this.ping = ping;
		this.status = status;
		this.description = description;
		this.timeStamp = timeStamp;
	}
	
	@Override
	public String toString() {
		StringBuffer sf = new StringBuffer();
		sf.append("\r\nName: " + name);
		sf.append("\r\nSystem date: " + strSystemDate);
		sf.append("\r\nPing time: " + ping);
		sf.append("\r\nStatus: " + status);
		sf.append("\r\nDescription: " + description);
		sf.append("\r\nTimestamp: " + timeStamp + "\r\n");
		return sf.toString();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getSystemDate() {
		return systemDate;
	}

	public void setSystemDate(Date systemDate) {
		this.systemDate = systemDate;
	}

	public int getPing() {
		return ping;
	}

	public void setPing(int ping) {
		this.ping = ping;
	}

	public boolean getStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
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

	public String getStrSystemDate() {
		return strSystemDate;
	}

	public void setStrSystemDate(String strSystemDate) {
		this.strSystemDate = strSystemDate;
	}
}
