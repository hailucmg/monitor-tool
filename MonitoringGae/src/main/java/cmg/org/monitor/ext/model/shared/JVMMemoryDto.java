package cmg.org.monitor.ext.model.shared;

import java.io.Serializable;

public class JVMMemoryDto implements Serializable { 
	
	/** Default UUID value */
	private static final long serialVersionUID = 1L;

	private String id;
	
	private double freeMemory;
	
	private double totalMemory;
	
	private double maxMemory;
	
	private double usedMemory;
	
	
	/**
	 * Default constructor.<br>
	 */
	public JVMMemoryDto() {
		
	}

	public JVMMemoryDto( double freeMemory, double totalMemory,
			double maxMemory, double usedMemory) {
		super();
		
		this.freeMemory = freeMemory;
		this.totalMemory = totalMemory;
		this.maxMemory = maxMemory;
		this.usedMemory = usedMemory;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
