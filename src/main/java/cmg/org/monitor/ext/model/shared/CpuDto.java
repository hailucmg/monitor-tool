package cmg.org.monitor.ext.model.shared;

import java.io.Serializable;
import java.util.Date;

public class CpuDto implements Serializable {
	
	/** Default UUID value */
	private static final long serialVersionUID = 1L;

	private String id;

	private double usedMemory;

	private double totalMemory;

	private String typeMemory;
	
	/**
	 * This attribute maps to the column cpu_name in the cpu table.
	 */
	protected String cpuName;

	/**
	 * This attribute maps to the column cpu_usage in the cpu table.
	 */
	protected int cpuUsage;

	/**
	 * This attribute maps to the column vendor in the cpu table.
	 */
	protected String vendor;

	/**
	 * This attribute maps to the column model in the cpu table.
	 */
	protected String model;

	/**
	 * This attribute maps to the column total_cpu in the cpu table.
	 */
	protected int totalCpu;

	/**
	 * This attribute maps to the column time_stamp in the cpu table.
	 */
	protected Date timeStamp;

	/**
	 * This attribute represents whether the attribute timeStamp has been
	 * modified since being read from the database.
	 */
	protected boolean timeStampModified = false;

	/**
	 * Default contructor 'Cpu'
	 * 
	 */
	public CpuDto() {
	}

	public CpuDto(double totalMemory, double usedMemory, int cpuUsage,
			int totalCpu, String vendor, String model, Date timeStamp) {
		super();
		this.usedMemory = usedMemory;
		this.cpuUsage = cpuUsage;
		this.vendor = vendor;
		this.model = model;
		this.totalCpu = totalCpu;
		this.timeStamp = timeStamp;
	}

	/**
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
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

	
	
	public void setCpuName(String cpuName) {
		this.cpuName = cpuName;
	}

	public void setCpuUsage(int cpuUsage) {
		this.cpuUsage = cpuUsage;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public void setTotalCpu(int totalCpu) {
		this.totalCpu = totalCpu;
	}

	/**
	 * Method 'getCpuName'
	 * 
	 * @return String
	 */
	public String getCpuName() {
		return cpuName;
	}

	/**
	 * Method 'getCpuUsage'
	 * 
	 * @return String
	 */
	public int getCpuUsage() {
		return cpuUsage;
	}

	/**
	 * Method 'getVendor'
	 * 
	 * @return String
	 */
	public String getVendor() {
		return vendor;
	}

	/**
	 * Method 'getModel'
	 * 
	 * @return String
	 */
	public String getModel() {
		return model;
	}

	/**
	 * Method 'getTotalCpu'
	 * 
	 * @return String
	 */
	public int getTotalCpu() {
		return totalCpu;
	}

	/**
	 * Method 'getTimeStamp'
	 * 
	 * @return Date
	 */
	public Date getTimeStamp() {
		return timeStamp;
	}

	/**
	 * Method 'setTimeStamp'
	 * 
	 * @param timeStamp
	 */
	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
		this.timeStampModified = true;
	}

	/**
	 * Sets the value of timeStampModified
	 */
	public void setTimeStampModified(boolean timeStampModified) {
		this.timeStampModified = timeStampModified;
	}

	/**
	 * Gets the value of timeStampModified
	 */
	public boolean isTimeStampModified() {
		return timeStampModified;
	}

	public String getTypeMemory() {
		return typeMemory;
	}

	public void setTypeMemory(String typeMemory) {
		this.typeMemory = typeMemory;
	}

	/**
	 * Method 'toString'
	 * 
	 * @return String
	 */
	public String toString() {
		StringBuffer ret = new StringBuffer();
		ret.append("com.cmg.monitor.history.dto.Cpu: ");
		ret.append("cpuName=" + cpuName);
		ret.append(", cpuUsage=" + cpuUsage);
		ret.append(", vendor=" + vendor);
		ret.append(", model=" + model);
		ret.append(", totalCpu=" + totalCpu);

		ret.append(", timeStamp=" + timeStamp);
		return ret.toString();
	}

}
