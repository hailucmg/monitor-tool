package cmg.org.monitor.entity.shared;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lamphan
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
	/**
	 * @return
	 */
	public int getPercentMemoryUsage() {
		
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
