/**
 * 
 */
package cmg.org.monitor.entity.shared;

import java.io.Serializable;
import java.util.Date;


/**
 * @author admin
 *
 */

@SuppressWarnings("serial")
public class FileSystemMonitor implements Serializable {
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
	
	public int getPercentUsage() {
		return (int) ((used / size) * 100);
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
		this.size = size;
	}

	public long getUsed() {
		return used;
	}

	public void setUsed(long used) {
		this.used = used;
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
