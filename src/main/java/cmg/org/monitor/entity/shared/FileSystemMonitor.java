/**
 * 
 */
package cmg.org.monitor.entity.shared;

import java.io.Serializable;
import java.util.Date;

import cmg.org.monitor.util.shared.Utility;


/**
 * @author admin
 *
 */
public class FileSystemMonitor implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8037940537272799331L;

	private String name;
	
	private String mount;

	private long size;

	private long used;

	private String type;

	private Date timeStamp;

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

	
}
