package cmg.org.monitor.memcache.shared;

import java.io.Serializable;
import java.util.Date;

public class FileSystemCacheDto implements Serializable  {
	
	/** Default serial UUID */
	private static final long serialVersionUID = 1L;

	private String name;

	private long size;

	private long used;

	private String type;
	
	private String mount;
	
	private Long available;
	
	private Integer percentUsed;
	
	private Date timeStamp;

	public FileSystemCacheDto() {
		
	}
	
	public FileSystemCacheDto(String name, long size, long used, String type,
			Date timeStamp) {
		super();
		this.setName(name);
		this.setSize(size);
		this.setUsed(used);
		this.setType(type);
		this.setTimeStamp(timeStamp);
	}

	public String getMount() {
		return mount;
	}

	public void setMount(String mount) {
		this.mount = mount;
	}

	public Long getAvailable() {
		return available;
	}

	public void setAvailable(Long available) {
		this.available = available;
	}

	public Integer getPercentUsed() {
		return percentUsed;
	}

	public void setPercentUsed(Integer percentUsed) {
		this.percentUsed = percentUsed;
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
