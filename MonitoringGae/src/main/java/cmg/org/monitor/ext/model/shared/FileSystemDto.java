package cmg.org.monitor.ext.model.shared;

import java.io.Serializable;
import java.util.Date;

/**
 * @author admin
 * 
 */
public class FileSystemDto implements Serializable {

	/** Default UUID value */
	private static final long serialVersionUID = 1L;

	private String id;
	private String name;
	private Long size;
	private Long used;
	private String type;
	private String mount;
	private Long available;
	private Integer percentUsed;
	private Date timeStamp;

	/**
	 * Default constructor.<br>
	 */
	public FileSystemDto() {

	}

	/**
	 * @param name
	 * @param size
	 * @param used
	 * @param type
	 * @param timeStamp
	 */
	public FileSystemDto(String name, Long size, Long used, String type,
			Date timeStamp) {
		super();
		this.name = name;
		this.size = size;
		this.used = used;
		this.type = type;
		this.timeStamp = timeStamp;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public Long getUsed() {
		return used;
	}

	public void setUsed(Long used) {
		this.used = used;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

}
