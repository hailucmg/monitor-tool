package cmg.org.monitor.entity.shared;

import java.util.List;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

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
public class SystemUser implements IsSerializable  {
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	@Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
	private String id;
	
	@Persistent
	private
	String username;
	
	@Persistent
	private
	String email;
	
	@Persistent
	private
	String fullname;
	
	@Persistent
	private
	List<SystemRole> roles;
	
	@Persistent
	private
	List<SystemGroup> groups;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
	 * @return the email 
	 */
	public String getEmail() {
		return email;
	}

	/** 
	 * @param email the email to set 
	 */
	
	public void setEmail(String email) {
		this.email = email;
	}

	/** 
	 * @return the fullname 
	 */
	public String getFullname() {
		return fullname;
	}

	/** 
	 * @param fullname the fullname to set 
	 */
	
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	/** 
	 * @return the roles 
	 */
	public List<SystemRole> getRoles() {
		return roles;
	}

	/** 
	 * @param roles the roles to set 
	 */
	
	public void setRoles(List<SystemRole> roles) {
		this.roles = roles;
	}

	/** 
	 * @return the groups 
	 */
	public List<SystemGroup> getGroups() {
		return groups;
	}

	/** 
	 * @param groups the groups to set 
	 */
	
	public void setGroups(List<SystemGroup> groups) {
		this.groups = groups;
	}
}
