package cmg.org.monitor.entity.shared;

import java.util.Date;

import javax.jdo.PersistenceManager;
import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.listener.StoreCallback;

import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.ext.model.dto.AlertDto;

/**
 * @author lamphan
 * @version 1.0
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class AlertMonitor implements StoreCallback {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	@Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
	private String id;

	@Persistent
	private String error;

	@Persistent
	private String description;

	@Persistent
	private Date timeStamp;

	@SuppressWarnings("unused")
	@Persistent
	private SystemMonitor systemMonitor;

	/**
	 * Default constructor.<br>
	 */
	public AlertMonitor() {

	}

	/**
	 * Constructor with parameters.
	 * 
	 * @param alertDto
	 */
	public AlertMonitor(AlertDto alertDto) {

		this();
		this.setBasicInfo(alertDto.getError(), alertDto.getDescription(),
				alertDto.getTimeStamp());
	}

	/**
	 * @param alertDto
	 * @param system
	 * @param pm
	 */
	public AlertMonitor(AlertDto alertDto, SystemMonitor system,
			PersistenceManager pm) {
		this();
		this.setBasicInfo(alertDto.getError(), alertDto.getDescription(),
				alertDto.getTimeStamp());
		system.getAlerts().add(this);
		pm.makePersistent(system); // this trunk will give you the newly-generated id
	}

	/**
	 * Method 'setBasicInfo' set basic class properties.<br>
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
	public AlertDto toDTO() {
		AlertDto alertDTO = new AlertDto(this.getError(),
				this.getDescription(), this.getTimeStamp());
		alertDTO.setId(this.getId());

		return alertDTO;
	}

	/**
	 * update existing alert object based on Alert DTO id
	 * 
	 * @param alertDTO
	 */
	public void updateFromDTO(AlertDto alertDTO) {
		this.error = alertDTO.getError();
		this.description = alertDTO.getDescription();
		this.timeStamp = alertDTO.getTimeStamp();

	}
	
	
	public String getId() {
		return id;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.jdo.listener.StoreCallback#jdoPreStore()
	 */
	public void jdoPreStore() {
		if (getError() != null) {
			error = getError().toLowerCase();
		} else {
			error = null;
		}
		if (getDescription() != null) {
			description = getDescription().toLowerCase();
		} else {
			description = null;
		}
	}
} // End class
