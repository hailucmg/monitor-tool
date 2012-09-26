
package cmg.org.monitor.entity.shared;

import java.util.ArrayList;
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
@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "false")
public class AlertStoreMonitor implements IsSerializable {

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

	public void fixAlertList(NotifyMonitor notify) {
		if (alerts != null && alerts.size() > 0) {
			ArrayList<AlertMonitor> temp = new ArrayList<AlertMonitor>();
			for (int i = 0; i < alerts.size(); i++) {
				boolean check = false;
				if (!notify.isJVM() && alerts.get(i).getType() == AlertMonitor.HIGH_USAGE_LEVEL_JVM) {
					check = true;
				} 
				if (!notify.isNotifyCpu() && alerts.get(i).getType() == AlertMonitor.HIGH_USAGE_LEVEL_CPU) {
					check = true;
				} 
				if (!notify.isNotifyMemory() && alerts.get(i).getType() == AlertMonitor.HIGH_USAGE_LEVEL_MEMORY) {
					check = true;
				} 
				if (!notify.isNotifyServices() && alerts.get(i).getType() == AlertMonitor.SERVICE_ERROR_STATUS) {
					check = true;
				} 
				if (!notify.isNotifyServicesConnection() && alerts.get(i).getType() == AlertMonitor.SERVICE_HIGH_LEVEL_PING_TIME) {
					check = true;
				} 					
				if (check) {
					temp.add(alerts.get(i));
				}
			}
			if (temp.size() > 0) {
				for (AlertMonitor alert : temp) {
					alerts.remove(alert);
				}
			}
		}
	}

	public boolean addAlert(AlertMonitor alert) {
		if (alerts == null) {
			alerts = new ArrayList<AlertMonitor>();
		}
		//alert.setAlertStore(this);

		alerts.add(alert);
		return true;
	}

	public String getErrors() {
		StringBuffer sb = new StringBuffer();
		if (alerts != null && alerts.size() > 0) {
			sb.append("<ol>");
			for (int i = 0; i < alerts.size(); i++) {
				sb.append("<li><h5>Alert #" + i + " : "
						+ alerts.get(i).getTimeStamp() + "</h5><ul>");
				sb.append("<li>Error: " + alerts.get(i).getError() + "</li>");
				sb.append("<li>Desciption: " + alerts.get(i).getDescription()
						+ "</li></ul></li>");
			}
			sb.append("</ol>");
		}
		return sb.toString();
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
