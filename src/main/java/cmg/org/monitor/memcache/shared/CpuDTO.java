package cmg.org.monitor.memcache.shared;

import java.io.Serializable;
import java.util.Date;

public class CpuDTO implements Serializable  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int totalCpu;

	private int cpuUsage;
	
	private String vendor;

	private String model;

	private Date timeStamp;
	
	

	/**
	 * 
	 */
	public CpuDTO() {
		
	}



	public CpuDTO(double totalMemory, double usedMemory, int cpuUsage,
			int totalCpu, String vendor, String model, Date timeStamp) {
		super();

		this.setCpuUsage(cpuUsage);
		this.setTotalCpu(totalCpu);
		this.setVendor(vendor);
		this.setModel(model);
		this.setTimeStamp(timeStamp);
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
