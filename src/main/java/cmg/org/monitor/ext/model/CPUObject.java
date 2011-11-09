package cmg.org.monitor.ext.model;

import java.io.Serializable;

/**
 * @author admin
 * 
 */
public class CPUObject implements Serializable {

	/** Default UUID value */
	private static final long serialVersionUID = 1L;

	/** Archive for used amount of memory value */
	private String usedCPU;
	
	/** Archive UUID value */
	private String idleCPU;
	
	/** Default UUID value */
	private String vendor;
	
	/** Default UUID value */
	private String model;
	
	/** Default UUID value */
	private int totalCPUs;

	/**
	 * Gets property.
	 * 
	 * @return the return value
	 */
	public String getVendor() {
		return vendor;
	}

	/**
	 * Sets property.
	 * 
	 * @param vendor
	 *            the parameter
	 */
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	/**
	 * Gets property.
	 * 
	 * @return the return value
	 */
	public String getModel() {
		return model;
	}

	/**
	 * Sets property.
	 * 
	 * @param model
	 *            the parameter
	 */
	public void setModel(String model) {
		this.model = model;
	}

	/**
	 * Gets property.
	 * 
	 * @return the return value
	 */
	public int getTotalCPUs() {
		return totalCPUs;
	}

	/**
	 * Sets property.
	 * 
	 * @param totalCPUs
	 *            the parameter
	 */
	public void setTotalCPUs(int totalCPUs) {
		this.totalCPUs = totalCPUs;
	}

	/**
	 * Gets description of <code>CPUObject</code> object.
	 * 
	 * @return the description of <code>CPUObject</code> object
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(" [");
		sb.append("CPU Used: " + usedCPU);
		sb.append(", CPU Idled: " + idleCPU);
		sb.append(", CPU Vendor: " + vendor);
		sb.append(", CPU Model: " + model);
		sb.append(", Total CPUs: " + totalCPUs);
		sb.append("] ");

		return sb.toString();
	}

	/**
	 * Gets the Used CPU property.
	 * 
	 * @return the used CPU
	 */
	public String getUsedCPU() {
		return usedCPU;
	}

	/**
	 * Sets used CPU property.
	 * 
	 * @param usedCPU
	 *            the used CPU
	 */
	public void setUsedCPU(String usedCPU) {
		this.usedCPU = usedCPU;
	}

	/**
	 * Gets idle CPU property.
	 * 
	 * @return the idle CPU
	 */
	public String getIdleCPU() {
		return idleCPU;
	}

	/**
	 * Sets idle CPU property.
	 * 
	 * @param idleCPU
	 *            the idle CPU
	 */
	public void setIdleCPU(String idleCPU) {
		this.idleCPU = idleCPU;
	}
}
