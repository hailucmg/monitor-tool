package cmg.org.monitor.entity.shared;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@SuppressWarnings("serial")
@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class AlertStoreMonitor implements Model {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	@Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
	private String id;

	@Persistent(mappedBy = "alertStore")
	private ArrayList<AlertMonitor> alerts = new ArrayList<AlertMonitor>();

	@Persistent
	private SystemMonitor system;

	@Persistent
	private int cpuUsage;

	@Persistent
	private int memUsage;

	@Persistent
	private Date timeStamp;

	@Override
	public String getId() {
		return id;
	}

	public boolean addAlert(AlertMonitor alert) {
		boolean check = false;
		if (alerts == null) {
			alerts = new ArrayList<AlertMonitor>();
		}
		alert.setAlertStore(this);
		if (alerts.size() > 0) {
			for (AlertMonitor al : alerts) {
				if (al.getType() == alert.getType()) {
					check = true;
					break;
				}
			}
		}
		if (!check) {
			alerts.add(alert);
			check = true;
		}
		return check;
	}

	public List<AlertMonitor> getAlerts() {
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

	
	public SystemMonitor getSystem() {
		return system;
	}

	public void setSystem(SystemMonitor system) {
		this.system = system;
	}

}
