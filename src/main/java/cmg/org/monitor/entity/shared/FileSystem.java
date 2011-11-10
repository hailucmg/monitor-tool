/**
 * 
 */
package cmg.org.monitor.entity.shared;

import java.util.Date;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import cmg.org.monitor.ext.model.shared.FileSystemDto;

import com.google.gwt.user.client.rpc.IsSerializable;

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
	private long size;

	@Persistent
	private long used;

	@Persistent
	private String type;
	
	@Persistent
	private Date timeStamp;

	@Persistent
	private SystemMonitor systemMonitor;
	
	/**
	 * Default constructor.<br>
	 */
	public FileSystem() {
		
	}

	/**
	 * Constructor with parameters.
	 * 
	 * @param fileDto File system data transfer object.
	 */
	public FileSystem(FileSystemDto fileDto) {

		this();
		this.setBasicInfo(fileDto.getName(), fileDto.getSize(),
				fileDto.getUsed(), fileDto.getType(), fileDto.getTimeStamp());
	}

	
	/**
	 * Constructor with parameters.<br>
	 * @param name
	 * @param size
	 * @param used
	 * @param type
	 * @param timeStamp
	 */
	public FileSystem(String name, long size, long used, String type,
			Date timeStamp) {
		super();
		this.name = name;
		this.size = size;
		this.used = used;
		this.type = type;
		this.timeStamp = timeStamp;
	}

	/**
	 * Method 'setBasicInfo' set basic class properties.<br>
	 * @param error
	 * @param description
	 * @param timeStamp
	 */
	public void setBasicInfo(String name, long size, long used, String type,
			Date timeStamp) {

		this.name = name;
		this.size = size;
		this.used = used;
		this.type = type;
		this.timeStamp = timeStamp;
	}
	
	/**
	 * Convert entity object to data transfer object.<br>
	 * @return data transfer object type.
	 */
	public FileSystemDto toDTO() {
		FileSystemDto alertDTO = new FileSystemDto(this.getName(),
				this.getSize(),this.getUsed(), this.getType(), this.getTimeStamp());
		alertDTO.setId(this.getId());

		return alertDTO;
	}
	
	/**
	 * update existing alert object based on File System DTO id
	 * 
	 * @param alertDTO
	 */
	public void updateFromDTO(FileSystemDto alertDTO) {
		this.name = alertDTO.getName();
		this.size = alertDTO.getSize();
		this.used = alertDTO.getUsed();
		this.type = alertDTO.getType();
		this.timeStamp = alertDTO.getTimeStamp();
	}
	
	public int getPercentUsage() {
		return (int) ((used / size) * 100);
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

	public SystemMonitor getSystemMonitor() {
		return systemMonitor;
	}

	public void setSystemMonitor(SystemMonitor systemMonitor) {
		this.systemMonitor = systemMonitor;
	}
	
}
