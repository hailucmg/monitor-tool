package cmg.org.monitor.entity.shared;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;


public class CpuMonitor implements IsSerializable {
	private int cpuUsage;

	private int totalCpu;

	private String vendor;

	private String model;

	private Date timeStamp;
	
	public CpuMonitor() {
		cpuUsage = 0;
	}
	
	@Override
	public String toString() {
		StringBuffer sf = new StringBuffer();
		sf.append("\r\nUsage: " + cpuUsage);
		sf.append("\r\nTotal CPU: " + totalCpu);
		sf.append("\r\nVendor: " + vendor);
		sf.append("\r\nModel: " + model);
		sf.append("\r\nTimestamp: " + timeStamp + "\r\n");
		return sf.toString();
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
