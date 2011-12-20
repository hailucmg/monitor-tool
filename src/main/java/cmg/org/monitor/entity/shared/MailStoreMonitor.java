package cmg.org.monitor.entity.shared;


import java.util.Date;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import cmg.org.monitor.ext.model.shared.MailStoreDto;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class MailStoreMonitor implements Model {
	
	private static final long serialVersionUID = 1L;

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	@Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
	private String encodedKey;

	@Persistent
	private String content;
	
	@Persistent
	private Date timeStamp;

	
	@Persistent
	private SystemMonitor systemMonitor;
	/**
	 * Default constructor.<br>
	 */
	public MailStoreMonitor() {

	}
	
	/**
	 * Constructor with parameters.
	 * 
	 * @param mailDto
	 */
	public MailStoreMonitor(MailStoreDto mailDto) {

		this();
		this.setBasicInfo(mailDto.getContent(),
				mailDto.getTimeStamp());
	}
	
	@Override
	public String getId() {
		return encodedKey;
	}
	
	/**
	 * Method 'setBasicInfo' set basic class properties.<br>
	 * @param error
	 * @param description
	 * @param timeStamp
	 */
	public void setBasicInfo(String content,  Date timeStamp) {

		this.content = content;
		this.timeStamp = timeStamp;
	}
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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
