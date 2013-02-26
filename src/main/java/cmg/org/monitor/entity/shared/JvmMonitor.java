package cmg.org.monitor.entity.shared;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

/** 
	* DOCME
	* 
	* @Creator Hai Lu
	* @author $Author$
	* @version $Revision$
	* @Last changed: $LastChangedDate$
*/
public class JvmMonitor implements IsSerializable {
	
	
	private double freeMemory;

	private double totalMemory;

	private double maxMemory;

	private double usedMemory;

	private Date timeStamp;
	
	private String strFreeMemory;
	private String strUsedMemory;
	private String strTotalMemory;
	private String strMaxMemory;

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

	/** 
	 * @return the strTotalMemory 
	 */
	public String getStrTotalMemory() {
		return strTotalMemory;
	}

	/** 
	 * @param strTotalMemory the strTotalMemory to set 
	 */
	
	public void setStrTotalMemory(String strTotalMemory) {
		this.strTotalMemory = strTotalMemory;
	}

	/** 
	 * @return the strMaxMemory 
	 */
	public String getStrMaxMemory() {
		return strMaxMemory;
	}

	/** 
	 * @param strMaxMemory the strMaxMemory to set 
	 */
	
	public void setStrMaxMemory(String strMaxMemory) {
		this.strMaxMemory = strMaxMemory;
	}

	/** 
	 * @return the strFreeMemory 
	 */
	public String getStrFreeMemory() {
		return strFreeMemory;
	}

	/** 
	 * @param strFreeMemory the strFreeMemory to set 
	 */
	
	public void setStrFreeMemory(String strFreeMemory) {
		this.strFreeMemory = strFreeMemory;
	}

	/** 
	 * @return the strUsedMemory 
	 */
	public String getStrUsedMemory() {
		return strUsedMemory;
	}

	/** 
	 * @param strUsedMemory the strUsedMemory to set 
	 */
	
	public void setStrUsedMemory(String strUsedMemory) {
		this.strUsedMemory = strUsedMemory;
	}

}
