package cmg.org.monitor.ext.model.shared;

import java.io.Serializable;

/**
 * Please enter a short description for this class.
 * 
 * <p>
 * Optionally, enter a longer description.
 * </p>
 * 
 * @author
 * @version
 */
public class CpuPhysicalDto implements Serializable {

	// ~ Instance fields
	// --------------------------------------------------------

	/** Default UUID value */
	private static final long serialVersionUID = 1L;

	/** This attribute maps to the column cpu_usage_id in the cpuusage table. */
	protected String type;

	/** This attribute maps to the column cpu_usage_id in the cpuusage table. */
	protected Double total;

	/** This attribute maps to the column cpu_usage_id in the cpuusage table. */
	protected Double used;

	/** This attribute maps to the column cpu_usage_id in the cpuusage table. */
	protected Double free;

	/** This attribute maps to the column time_stamp in the cpuusage table. */
	protected String timeStamp;

	/**
	 * Method 'CpuUsageDto'
	 */
	public CpuPhysicalDto() {
	}

	// ~ Methods
	// ----------------------------------------------------------------

	/**
	 * Method 'getTimeStamp'
	 * 
	 * @return String
	 */
	public String getTimeStamp() {
		return timeStamp;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public Double getUsed() {
		return used;
	}

	public void setUsed(Double used) {
		this.used = used;
	}

	public Double getFree() {
		return free;
	}

	public void setFree(Double free) {
		this.free = free;
	}

	/**
	 * Method 'setTimeStamp'
	 * 
	 * @param timeStamp
	 */
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	/**
	 * Method 'equals'
	 * 
	 * @param _other
	 * 
	 * @return boolean
	 */
	public boolean equals(Object _other) {
		if (_other == null) {
			return false;
		}
		if (_other == this) {
			return true;
		}
		if (!(_other instanceof CpuPhysicalDto)) {
			return false;
		}
		final CpuPhysicalDto _cast = (CpuPhysicalDto) _other;

		if ((timeStamp == null) ? (_cast.timeStamp != timeStamp) : (!timeStamp
				.equals(_cast.timeStamp))) {
			return false;
		}

		return true;
	}

	/**
	 * Method 'hashCode'
	 * 
	 * @return int
	 */
	public int hashCode() {
		int _hashCode = 0;

		if (timeStamp != null) {
			_hashCode = (29 * _hashCode) + timeStamp.hashCode();
		}

		return _hashCode;
	}

	/**
	 * Method 'toString'
	 * 
	 * @return String
	 */
	public String toString() {
		StringBuffer ret = new StringBuffer();
		ret.append("org.cmg.monitor.dto.CpuUsageDto: ");
		ret.append(", timeStamp=" + timeStamp);

		return ret.toString();
	}
}
