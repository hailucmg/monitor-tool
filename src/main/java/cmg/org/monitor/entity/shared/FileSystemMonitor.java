/**
 * 
 */
package cmg.org.monitor.entity.shared;

import java.util.Date;

import cmg.org.monitor.util.shared.Utility;

import com.google.gwt.user.client.rpc.IsSerializable;


/** 
	* DOCME
	* 
	* @Creator Hai Lu
	* @author $Author$
	* @version $Revision$
	* @Last changed: $LastChangedDate$
*/
public class FileSystemMonitor implements IsSerializable {
	/**
	 * 
	 */

	private String name;
	
	private String mount;

	private long size;

	private long used;
	
	private long free;
	private String strFree;

	private String type;

	private Date timeStamp;
	
	private String strSize;
	private String strUsed;

	/**
	 * Default constructor.<br>
	 */
	public FileSystemMonitor() {
		
	}
	
	public FileSystemMonitor(String name, long size, long used, String type,
			Date timeStamp) {
		super();
		this.name = name;
		this.size = size;
		this.used = used;
		this.type = type;
		this.timeStamp = timeStamp;
	}
	@Override
	public String toString() {
		StringBuffer sf = new StringBuffer();
		sf.append("\r\nName: " + name);
		sf.append("\r\nMount: " + mount);
		sf.append("\r\nSize: " + size);
		sf.append("\r\nUsed: " + used);
		sf.append("\r\nType: " + type);
		sf.append("\r\nTimestamp: " + timeStamp + "\r\n");
		return sf.toString();
	}
	
	public int getPercentUsage() {
		if (size == 0)
			return 0;
		return (int) ((used  / size ) * 100);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = Utility.parseFileSystemValue(size);
	}
	

	public long getUsed() {
		return used;
	}

	public void setUsed(long used) {
		this.used = Utility.parseFileSystemValue(used);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getMount() {
		return mount;
	}

	public void setMount(String mount) {
		this.mount = mount;
	}

	/** 
	 * @return the strSize 
	 */
	public String getStrSize() {
		return strSize;
	}

	/** 
	 * @param strSize the strSize to set 
	 */
	
	public void setStrSize(String strSize) {
		this.strSize = strSize;
	}

	/** 
	 * @return the strUsed 
	 */
	public String getStrUsed() {
		return strUsed;
	}

	/** 
	 * @param strUsed the strUsed to set 
	 */
	
	public void setStrUsed(String strUsed) {
		this.strUsed = strUsed;
	}

	/** 
	 * @return the strFree 
	 */
	public String getStrFree() {
		return strFree;
	}

	/** 
	 * @param strFree the strFree to set 
	 */
	
	public void setStrFree(String strFree) {
		this.strFree = strFree;
	}

	/** 
	 * @return the free 
	 */
	public long getFree() {
		return free;
	}

	/** 
	 * @param free the free to set 
	 */
	
	public void setFree(long free) {
		this.free = free;
	}

	
}
