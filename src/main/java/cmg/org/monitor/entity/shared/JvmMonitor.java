package cmg.org.monitor.entity.shared;

import java.io.Serializable;
import java.util.Date;

public class JvmMonitor implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5027602085427688515L;

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
	
	@Override
	public String toString() {
		StringBuffer sf = new StringBuffer();
		sf.append("\r\nFree memory: " + freeMemory);
		sf.append("\r\nTotal memory: " + totalMemory);
		sf.append("\r\nMax memory: " + maxMemory);
		sf.append("\r\nUsed memory: " + usedMemory);
		sf.append("\r\nTimestamp: " + timeStamp + "\r\n");
		return sf.toString();
	}
	
	public int getPercentUsage() {
		if (totalMemory == 0)
			return 0;
		return (int) ((usedMemory  / totalMemory ) * 100);
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
