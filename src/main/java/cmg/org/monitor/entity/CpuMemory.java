package cmg.org.monitor.entity;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

/**
 * @author admin
 * @version 1.0
 */
@PersistenceCapable
public class CpuMemory {
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;

	@Persistent
	private double totalMemory;

	@Persistent
	private double usedMemory;

	@Persistent
	private double cpuUsage;

	@Persistent
	private int totalCpu;

	@Persistent
	private String vendor;

	@Persistent
	private String model;

	@Persistent
	private Date timeStamp;

	public CpuMemory() {
	}

	private CpuMemory(double totalMemory, double usedMemory, double cpuUsage,
			int totalCpu, String vendor, String model, Date timeStamp) {
		super();

		this.totalMemory = totalMemory;
		this.usedMemory = usedMemory;
		this.cpuUsage = cpuUsage;
		this.totalCpu = totalCpu;
		this.vendor = vendor;
		this.model = model;
		this.timeStamp = timeStamp;
	}

	public Key getKey() {
		return key;
	}

	public double getTotalMemory() {
		return totalMemory;
	}

	public void setTotalMemory(double totalMemory) {
		this.totalMemory = totalMemory;
	}

	public double getUsedMemory() {
		return usedMemory;
	}

	public void setUsedMemory(double usedMemory) {
		this.usedMemory = usedMemory;
	}

	public double getCpuUsage() {
		return cpuUsage;
	}

	public void setCpuUsage(double cpuUsage) {
		this.cpuUsage = cpuUsage;
	}

	public int getTotalCpu() {
		return totalCpu;
	}

	public void setTotalCpu(int totalCpu) {
		this.totalCpu = totalCpu;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

}
