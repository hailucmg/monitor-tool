package cmg.org.monitor.ext.model.shared;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author lamphan
 * @version 1.0
 * The 'detailed' Data Transfer Object for the AlertMonitor class
 * 
 */
@SuppressWarnings("serial")
public class AlertDto implements Serializable {
	private String id;
	private String error;
	private String description;
	private Date timeStamp;

	/**
	 * Default constructor.
	 */
	public AlertDto() {
	}

	/**
	 * @param error
	 * @param description
	 * @param timeStamp
	 */
	public AlertDto(String error, String description, Date timeStamp) {
		this();
		setBasicInfo(error, description, timeStamp);
	}

	/**
	 * @param error
	 * @param description
	 * @param timeStamp
	 */
	public void setBasicInfo(String error, String description, Date timeStamp) {
		this.error = error;
		this.description = description;
		this.timeStamp = timeStamp;
	}

	/**
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return
	 */
	public String getError() {
		return error;
	}

	/**
	 * @param error
	 */
	public void setError(String error) {
		this.error = error;
	}

	/**
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return
	 */
	public Date getTimeStamp() {
		return timeStamp;
	}

	/**
	 * @param timeStamp
	 */
	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

}
