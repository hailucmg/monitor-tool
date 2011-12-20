package cmg.org.monitor.ext.model.shared;

import java.io.Serializable;
import java.util.Date;


@SuppressWarnings("serial")
public class MailStoreDto implements Serializable {
	private String id;
	private String content;
	private Date timeStamp;
	
	/**
	 * Default constructor.
	 */
	public MailStoreDto() {
	}

	/**
	 * @param content
	 * @param description
	 * @param timeStamp
	 */
	public MailStoreDto(String content, Date timeStamp) {
		this();
		setBasicInfo(content, timeStamp);
	}

	/**
	 * @param error
	 * @param description
	 * @param timeStamp
	 */
	public void setBasicInfo(String content, Date timeStamp) {
		this.content = content;
		this.timeStamp = timeStamp;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
	
}