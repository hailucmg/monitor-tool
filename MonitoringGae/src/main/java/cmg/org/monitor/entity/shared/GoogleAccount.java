package cmg.org.monitor.entity.shared;

import java.util.Date;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import cmg.org.monitor.ext.model.shared.UserMonitor;

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
public class GoogleAccount implements IsSerializable {
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	@Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
	private String id;
	
	@Persistent
	private
	String domain;
	
	@Persistent
	private
	String username;
	
	@Persistent
	private
	String password;
	
	@Persistent
	private
	String token;
	
	@Persistent
	private
	Date lastSync;

	@NotPersistent
	private
	String strLastSync;
	
	public void swap(GoogleAccount in) {
		this.domain = in.domain;
		this.username = in.username;
		this.password = in.password;
		this.token = in.token;
		this.lastSync = in.lastSync;
	}
	/** 
	 * @return the id 
	 */
	public String getId() {
		return id;
	}

	/** 
	 * @param id the id to set 
	 */
	
	public void setId(String id) {
		this.id = id;
	}

	/** 
	 * @return the domain 
	 */
	public String getDomain() {
		return domain;
	}

	/** 
	 * @param domain the domain to set 
	 */
	
	public void setDomain(String domain) {
		this.domain = domain;
	}

	/** 
	 * @return the username 
	 */
	public String getUsername() {
		return username;
	}

	/** 
	 * @param username the username to set 
	 */
	
	public void setUsername(String username) {
		this.username = username;
	}

	/** 
	 * @return the password 
	 */
	public String getPassword() {
		return password;
	}

	/** 
	 * @param password the password to set 
	 */
	
	public void setPassword(String password) {
		this.password = password;
	}

	/** 
	 * @return the token 
	 */
	public String getToken() {
		return token;
	}

	/** 
	 * @param token the token to set 
	 */
	
	public void setToken(String token) {
		this.token = token;
	}

	/** 
	 * @return the lastSync 
	 */
	public Date getLastSync() {
		return lastSync;
	}

	/** 
	 * @param lastSync the lastSync to set 
	 */
	
	public void setLastSync(Date lastSync) {
		this.lastSync = lastSync;
	}
	
	public int compareByName(GoogleAccount c) {
		return username.trim().compareTo(c.getUsername().trim());
	}
	/** 
	 * @return the strLastSync 
	 */
	public String getStrLastSync() {
		return strLastSync;
	}
	/** 
	 * @param strLastSync the strLastSync to set 
	 */
	
	public void setStrLastSync(String strLastSync) {
		this.strLastSync = strLastSync;
	}
}
