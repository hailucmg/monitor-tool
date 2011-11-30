package cmg.org.monitor.memcache.shared;

import java.io.Serializable;
import java.util.Date;

public class FileSystemDto implements Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name;

	private long size;

	private long used;

	private String type;
	
	private Date timeStamp;

	public FileSystemDto() {
		
	}
	
	public FileSystemDto(String name, long size, long used, String type,
			Date timeStamp) {
		super();
		this.setName(name);
		this.setSize(size);
		this.setUsed(used);
		this.setType(type);
		this.setTimeStamp(timeStamp);
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
	
	
}
