package cmg.org.monitor.entity;

import java.util.Date;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * @author lamphan
 * @version 1.0
 */

@SuppressWarnings("serial")
@PersistenceCapable
public class CpuMemory implements Model {
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	@Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
	private String encodedKey;

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

	/**
	 * Default constructor.<br> 
	 */
	public CpuMemory() {
		
	}

	public CpuMemory(double totalMemory, double usedMemory, double cpuUsage,
			int totalCpu, String vendor, String model, Date timeStamp) {

		this.totalMemory = totalMemory;
		this.usedMemory = usedMemory;
		this.cpuUsage = cpuUsage;
		this.totalCpu = totalCpu;
		this.vendor = vendor;
		this.model = model;
		this.timeStamp = timeStamp;
	}

	

	@Override
    public String getId() {
        return encodedKey;
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
