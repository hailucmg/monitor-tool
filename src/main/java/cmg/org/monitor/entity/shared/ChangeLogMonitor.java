package cmg.org.monitor.entity.shared;

import java.util.Date;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Text;
import com.google.gwt.user.client.rpc.IsSerializable;


/** 
	* DOCME
	* 
	* @Creator Hai Lu
	* @author $Author$
	* @version $Revision$
	* @Last changed: $LastChangedDate$
*/
@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class ChangeLogMonitor implements IsSerializable{

	public static final int LOG_ADD = 0x001;
	public static final int LOG_UPDATE = 0x002;
	public static final int LOG_DELETE = 0x003;
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	@Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
	private String id;
	
	@Persistent
	private Text description;
	
	@Persistent
	private Date datetime;
	
	@Persistent
	private String username;
	
	@Persistent
	private String sid;
	
	@Persistent
	private String systemName;
	
	@Persistent
	private int type;
	
	
	
	public ChangeLogMonitor(){
		
	}
	
	public ChangeLogMonitor(String sid,int type, Date datetime, Text description, String username){
		this.sid = sid;
		this.type = type;
		this.datetime = datetime;
		this.description = description;
		this.username = username;
	}
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getDescription() {
		return description.getValue();
	}

	public void setDescription(String description) {
		this.description = new Text(description);
	}

	public Date getDatetime() {
		return datetime;
	}

	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getId() {
		return id;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	
}