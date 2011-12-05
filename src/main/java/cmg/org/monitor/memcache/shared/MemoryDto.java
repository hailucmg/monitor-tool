package cmg.org.monitor.memcache.shared;

import java.io.Serializable;

public class MemoryDto implements Serializable {
	
	/** This attribute maps to the column cpu_usage_id in the cpuusage table. */
	protected String type;
	
	private double totalMemory;

	private double usedMemory;

	public MemoryDto() {
		
		// TODO Auto-generated constructor stub
	}

	
	
	private MemoryDto(double totalMemory, double usedMemory) {
		super();
		this.totalMemory = totalMemory;
		this.usedMemory = usedMemory;
	}

	/**
	 * @return
	 */
	public int getPercentUsage() {
		return (int) ((usedMemory / totalMemory) * 100);
	}


	public String getType() {
		return type;
	}



	public void setType(String type) {
		this.type = type;
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
	
	
	
}
