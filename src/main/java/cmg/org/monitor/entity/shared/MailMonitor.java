package cmg.org.monitor.entity.shared;

import java.io.Serializable;
import java.util.Date;

public class MailMonitor implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2413988831812933493L;
	private String sender;
	private String subject;
	private String contentType;
	private String content;
	private Date timeStamp;
	
	@Override
	public String toString() {
		StringBuffer sf = new StringBuffer();
		sf.append("\r\nSender: " + sender);
		sf.append("\r\nSubject: " + subject);
		sf.append("\r\nContent Type: " + contentType);
		sf.append("\r\nContent: " + content);
		sf.append("\r\nTimestamp: " + timeStamp + "\r\n");
		return sf.toString();
	}
	
	public String getSender() {
		return sender.toLowerCase();
	}
	public void setSender(String sender) {
		this.sender = sender.toLowerCase();
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
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
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}

}
