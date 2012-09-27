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
public class SystemUser implements IsSerializable {
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	@Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
	private String id;

	@Persistent
	private String username;

	@Persistent
	private String email;

	@Persistent
	private String domain;

	@Persistent
	private String firstName;

	@Persistent
	private String lastName;

	@Persistent
	private List<SystemRole> roles;

	@Persistent
	private List<SystemGroup> groups;

	@Persistent
	private boolean isSuspended;

	@Persistent
	private boolean isDomainAdmin;

	public void swap(SystemUser in) {
		this.username = in.username;
		this.domain = in.domain;
		this.email = in.email;
		this.firstName = in.firstName;
		this.lastName = in.lastName;
		this.isSuspended = in.isSuspended;
		this.isDomainAdmin = in.isDomainAdmin;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getFullName() {
		if (firstName == null || lastName == null) {
			return "";
		}
		return firstName + " " + lastName;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *            the username to set
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
	 * @param email
	 *            the email to set
	 */

	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the roles
	 */
	public List<SystemRole> getRoles() {
		return roles;
	}

	/**
	 * @param roles
	 *            the roles to set
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
	 * @param groups
	 *            the groups to set
	 */

	public void setGroups(List<SystemGroup> groups) {
		this.groups = groups;
	}

	/**
	 * @return the domain
	 */
	public String getDomain() {
		return domain;
	}

	/**
	 * @param domain
	 *            the domain to set
	 */

	public void setDomain(String domain) {
		this.domain = domain;
	}

	/**
	 * @return the isSuspended
	 */
	public boolean isSuspended() {
		return isSuspended;
	}

	/**
	 * @param isSuspended
	 *            the isSuspended to set
	 */

	public void setSuspended(boolean isSuspended) {
		this.isSuspended = isSuspended;
	}

	/**
	 * @return the isDomainAdmin
	 */
	public boolean isDomainAdmin() {
		return isDomainAdmin;
	}

	/**
	 * @param isDomainAdmin
	 *            the isDomainAdmin to set
	 */

	public void setDomainAdmin(boolean isDomainAdmin) {
		this.isDomainAdmin = isDomainAdmin;
	}

	/** 
	 * @return the firstName 
	 */
	public String getFirstName() {
		return firstName;
	}

	/** 
	 * @param firstName the firstName to set 
	 */
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/** 
	 * @return the lastName 
	 */
	public String getLastName() {
		return lastName;
	}

	/** 
	 * @param lastName the lastName to set 
	 */
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
}
