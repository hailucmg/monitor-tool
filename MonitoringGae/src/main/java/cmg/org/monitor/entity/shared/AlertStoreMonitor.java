package cmg.org.monitor.entity.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "false")
public class AlertStoreMonitor implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	@Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
	private String id;

	@Persistent(mappedBy = "alertStore")
	private ArrayList<AlertMonitor> alerts = new ArrayList<AlertMonitor>();

	@Persistent
	private String sysId;
	
	@Persistent
	private String name;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Persistent
	private int cpuUsage;

	@Persistent
	private int memUsage;

	@Persistent
	private Date timeStamp;

	public String getId() {
		return id;
	}

	public boolean addAlert(AlertMonitor alert) {		
		if (alerts == null) {
			alerts = new ArrayList<AlertMonitor>();
		}
		alert.setAlertStore(this);		
		
		alerts.add(alert);
		return true;
	}
	
	public String getErrors() {
		StringBuffer sb = new StringBuffer();
		if (alerts != null && alerts.size() > 0) {
			sb.append("<ol>");
			for (int i = 0; i < alerts.size(); i++) {
				sb.append("<li><h5>Alert #" + i + " : " + alerts.get(i).getTimeStamp() +"</h5><ul>");
				sb.append("<li>Error: " + alerts.get(i).getError() + "</li>");
				sb.append("<li>Desciption: " + alerts.get(i).getDescription() + "</li></ul></li>");
			}
			sb.append("</ol>");
		}
		return sb.toString();
	}
	
	@Override
	public String toString() {
		StringBuffer sf = new StringBuffer();
		sf.append("\r\nSystem ID: " + sysId);
		sf.append("\r\nCPU Usage: " + cpuUsage);
		sf.append("\r\nMemory Usage: " + memUsage);
		sf.append("\r\nTimestamp: " + timeStamp + "\r\n");
		return sf.toString();
	}


	public ArrayList<AlertMonitor> getAlerts() {
		return alerts;
	}

	public void setAlerts(ArrayList<AlertMonitor> alerts) {
		this.alerts = alerts;
	}

	public int getCpuUsage() {
		return cpuUsage;
	}

	public void setCpuUsage(int cpuUsage) {
		this.cpuUsage = cpuUsage;
	}

	public int getMemUsage() {
		return memUsage;
	}

	public void setMemUsage(int memUsage) {
		this.memUsage = memUsage;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getSysId() {
		return sysId;
	}

	public void setSysId(String sysId) {
		this.sysId = sysId;
	}



}
