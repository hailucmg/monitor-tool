/**
 * 
 */
package cmg.org.monitor.entity;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * @author admin
 *
 */

@SuppressWarnings("serial")
@PersistenceCapable
public class FileSystem implements Model {
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	@Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
	private String encodedKey;
	
	@Persistent
	private String name;

	@Persistent
	private Long size;

	@Persistent
	private String used;

	@Persistent
	private String type;
	
	@Persistent
	private Boolean timeStamp;

	/**
	 * Default constructor.<br>
	 */
	private FileSystem() {
		
	}

	/**
	 * @param name
	 * @param size
	 * @param used
	 * @param type
	 * @param timeStamp
	 */
	private FileSystem(String name, Long size, String used, String type,
			Boolean timeStamp) {
		super();
		this.name = name;
		this.size = size;
		this.used = used;
		this.type = type;
		this.timeStamp = timeStamp;
	}

	@Override
    public String getId() {
        return encodedKey;
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

	public String getUsed() {
		return used;
	}

	public void setUsed(String used) {
		this.used = used;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Boolean getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Boolean timeStamp) {
		this.timeStamp = timeStamp;
	}
	
}
