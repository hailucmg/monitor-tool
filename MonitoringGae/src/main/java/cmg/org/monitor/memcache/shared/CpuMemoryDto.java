package cmg.org.monitor.memcache.shared;

import java.io.Serializable;
import java.util.Date;

public class CpuMemoryDto implements Serializable  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private double totalMemory;

	private double usedMemory;

	private int cpuUsage;

	private int totalCpu;

	private String vendor;

	private String model;

	private Date timeStamp;

	public CpuMemoryDto(double totalMemory, double usedMemory, int cpuUsage,
			int totalCpu, String vendor, String model, Date timeStamp) {
		super();
		this.setTotalMemory(totalMemory);
		this.setUsedMemory(usedMemory);
		this.setCpuUsage(cpuUsage);
		this.setTotalCpu(totalCpu);
		this.setVendor(vendor);
		this.setModel(model);
		this.setTimeStamp(timeStamp);
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

	public int getCpuUsage() {
		return cpuUsage;
	}

	public void setCpuUsage(int cpuUsage) {
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
