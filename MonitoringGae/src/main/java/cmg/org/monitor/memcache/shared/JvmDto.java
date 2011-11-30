package cmg.org.monitor.memcache.shared;

import java.io.Serializable;


public class JvmDto implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private double freeMemory;

	private double totalMemory;

	private double maxMemory;

	private double usedMemory;
	
	public JvmDto() {
	}

	public JvmDto(double freeMemory, double totalMemory, double maxMemory,
			double usedMemory) {
		super();
		this.setFreeMemory(freeMemory);
		this.setTotalMemory(totalMemory);
		this.setMaxMemory(maxMemory);
		this.setUsedMemory(usedMemory);
	}

	public double getFreeMemory() {
		return freeMemory;
	}

	public void setFreeMemory(double freeMemory) {
		this.freeMemory = freeMemory;
	}

	public double getTotalMemory() {
		return totalMemory;
	}

	public void setTotalMemory(double totalMemory) {
		this.totalMemory = totalMemory;
	}

	public double getMaxMemory() {
		return maxMemory;
	}

	public void setMaxMemory(double maxMemory) {
		this.maxMemory = maxMemory;
	}

	public double getUsedMemory() {
		return usedMemory;
	}

	public void setUsedMemory(double usedMemory) {
		this.usedMemory = usedMemory;
	}
	
	
	
}
