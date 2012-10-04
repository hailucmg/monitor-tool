/**
 * Copyright (c) CMG Ltd All rights reserved.
 *
 * This software is the confidential and proprietary information of CMG
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with CMG.
 */

package cmg.org.monitor.entity.shared;

import java.util.Date;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Text;

/** 
 * DOCME
 * 
 * @Creator Hai Lu
 * @author $Author$
 * @version $Revision$
 * @Last changed: $LastChangedDate$
 */

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class AccountSyncLog {
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	@Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
	private String id;
	
	@Persistent
	private String adminAccount;
	
	@Persistent
	private Text log;
	
	@Persistent
	private Date timestamp;

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
	 * @return the adminAccount 
	 */
	public String getAdminAccount() {
		return adminAccount;
	}

	/** 
	 * @param adminAccount the adminAccount to set 
	 */
	
	public void setAdminAccount(String adminAccount) {
		this.adminAccount = adminAccount;
	}

	/** 
	 * @return the log 
	 */
	public String getLog() {
		return log == null ? "" : log.getValue();
	}

	/** 
	 * @param log the log to set 
	 */
	
	public void setLog(String log) {
		this.log = new Text(log);
	}

	/** 
	 * @return the timestamp 
	 */
	public Date getTimestamp() {
		return timestamp;
	}

	/** 
	 * @param timestamp the timestamp to set 
	 */
	
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
}
