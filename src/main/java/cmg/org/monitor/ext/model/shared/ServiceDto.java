package cmg.org.monitor.ext.model.shared;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lamphan
 * 
 */
public class ServiceDto implements Serializable {

	/** Default UUID value */
	private static final long serialVersionUID = 1L;

	/** Service id value */
	private String id;

	/** Service name value */
	protected String name;

	/** Status of service */
	private boolean status = false;

	/** Ping constant */
	protected int ping;

	/** The time of service fetching */
	private Date sysDate;

	/** Description of service */
	private String description;

	/** Default time */
	private Date timeStamp;

	/**
	 * Default constructor.
	 */
	public ServiceDto() {
	}

	/**
	 * @param name
	 * @param status
	 * @param ping
	 * @param sysDate
	 * @param description
	 * @param timeStamp
	 */
	public ServiceDto(String name, boolean status, int ping, Date sysDate,
			String description, Date timeStamp) {
		super();

		this.name = name;
		this.status = status;
		this.ping = ping;
		this.sysDate = sysDate;
		this.description = description;
		this.timeStamp = timeStamp;
	}

	/**
	 * Retrieve an id value.
	 * 
	 * @return id presentation.
	 */
	public String getId() {
		return id;
	}

	/**
	 * Set an id value.
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Get name value.
	 * 
	 * @return name presentation.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set name value.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get status of service.
	 * 
	 * @return status presentation.
	 */
	public boolean isStatus() {
		return status;
	}

	/**
	 * Set status of service.
	 */
	public void setStatus(boolean status) {
		this.status = status;
	}

	/**
	 * Get ping connection value.
	 * 
	 * @return ping presentation.
	 */
	public int getPing() {
		return ping;
	}

	/**
	 * Set ping connection value.
	 */
	public void setPing(int ping) {
		this.ping = ping;
	}

	/**
	 * Get date value.
	 * 
	 * @return date presentation.
	 */
	public Date getSysDate() {
		return sysDate;
	}

	/**
	 * Set date value.
	 */
	public void setSysDate(Date sysDate) {
		this.sysDate = sysDate;
	}

	/**
	 * Get description value.
	 * 
	 * @return description presentation.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Get id value.
	 * 
	 * @return id presentation.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Get default time.
	 * 
	 * @return Date time.
	 */
	public Date getTimeStamp() {
		return timeStamp;
	}

	/**
	 * Set default time.
	 * 
	 * @param timeStamp
	 *            Date value.
	 */
	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

}
