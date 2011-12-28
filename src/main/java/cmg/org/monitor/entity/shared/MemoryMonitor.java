package cmg.org.monitor.entity.shared;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lamphan, Hong Hai
 * @version 1.0
 */

@SuppressWarnings("serial")
public class MemoryMonitor implements Serializable {
	public static final int MEM = 0x001;
	public static final int SWAP = 0x002;

	private int type;

	private double totalMemory;

	private double usedMemory;
		
	private Date timeStamp;

	/**
	 * Default constructor.<br>
	 */
	public MemoryMonitor() {
		totalMemory = 0;
		usedMemory = 0;
	}
	
	public MemoryMonitor(int type) {
		this.type = type;
		totalMemory = 0;
		usedMemory = 0;
	}
	
	@Override
	public String toString() {
		StringBuffer sf = new StringBuffer();
		sf.append("\r\nType: " + (type == SWAP ? "Swap" : "Ram"));
		sf.append("\r\nTotal memory: " + totalMemory);
		sf.append("\r\nUsed memory: " + usedMemory);
		sf.append("\r\nTimestamp: " + timeStamp + "\r\n");
		return sf.toString();
	}
	/**
	 * @return
	 */
	public int getPercentUsage() {		
		if (totalMemory == 0)
			return 0;
		return (int) ((usedMemory  / totalMemory ) * 100);
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

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
