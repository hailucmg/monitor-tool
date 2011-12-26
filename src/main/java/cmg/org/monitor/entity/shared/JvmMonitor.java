package cmg.org.monitor.entity.shared;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class JvmMonitor implements Serializable {
	
	private double freeMemory;

	private double totalMemory;

	private double maxMemory;

	private double usedMemory;

	private Date timeStamp;

	/**
	 * Default constructor.<br>
	 */
	public JvmMonitor() {
		freeMemory = 0;
		totalMemory = 0;
		maxMemory = 0;
		usedMemory = 0;
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

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

}
