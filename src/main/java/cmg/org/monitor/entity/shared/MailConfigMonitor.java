package cmg.org.monitor.entity.shared;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@SuppressWarnings("serial")
@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class MailConfigMonitor implements Model {
	public static final String DEFAULT_LABEL = "Monitor Alert";

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	@Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
	private String id;
	
	@Persistent
	private
	String mailId;
	
	@Persistent
	private
	String label;
	
	@Persistent
	private
	boolean isStarred;
	
	@Persistent
	private
	boolean isInbox;
	
	@Persistent
	private
	boolean isMarkAsUnread;
	
	public MailConfigMonitor() {
		this.label = DEFAULT_LABEL;
		this.isStarred = false;
		this.isInbox = true;
		this.isMarkAsUnread = false;
	}	
	
	@Override
	public String getId() {
		return id;
	}

	public String getMailId() {
		return mailId;
	}

	public void setMailId(String mailId) {
		this.mailId = mailId;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public boolean isStarred() {
		return isStarred;
	}

	public void setStarred(boolean isStarred) {
		this.isStarred = isStarred;
	}

	public boolean isInbox() {
		return isInbox;
	}

	public void setInbox(boolean isInbox) {
		this.isInbox = isInbox;
	}

	public boolean isMarkAsUnread() {
		return isMarkAsUnread;
	}

	public void setMarkAsUnread(boolean isMarkAsUnread) {
		this.isMarkAsUnread = isMarkAsUnread;
	}
	
}