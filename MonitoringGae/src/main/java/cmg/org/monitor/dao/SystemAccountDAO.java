/**
 * Copyright (c) CMG Ltd All rights reserved.
 *
 * This software is the confidential and proprietary information of CMG
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with CMG.
 */

package cmg.org.monitor.dao;

import java.util.List;

import cmg.org.monitor.entity.shared.GoogleAccount;
import cmg.org.monitor.entity.shared.SystemUser;

/** 
 * DOCME
 * 
 * @Creator Hai Lu
 * @author $Author$
 * @version $Revision$
 * @Last changed: $LastChangedDate$
 */

public interface SystemAccountDAO {
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<GoogleAccount> listAllGoogleAccount() throws Exception;
	/**
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public GoogleAccount getGoogleAccountById(String id) throws Exception;
	
	public boolean deleteSystemUser(SystemUser user) throws Exception;
	
	public boolean updateSystemUser(SystemUser user) throws Exception;
	/**
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public boolean createSystemUser(SystemUser user) throws Exception;
	/**
	 * 
	 * @param users
	 * @return
	 * @throws Exception
	 */
	public boolean createSystemUsers(List<SystemUser> users) throws Exception;
	
	public SystemUser getSystemUserById(String id) throws Exception;
	
	public SystemUser getSystemUserByEmail(String email) throws Exception;
	/**
	 * 
	 * @param domain
	 * @return
	 * @throws Exception
	 */
	public boolean deleteAllDomainUser(String domain) throws Exception;
	/**
	 * 
	 * @param account
	 * @return
	 * @throws Exception
	 */
	public boolean updateGoogleAccount(GoogleAccount account) throws Exception;
	/**
	 * 
	 * @param account
	 * @return
	 * @throws Exception
	 */
	public boolean createGoogleAccount(GoogleAccount account) throws Exception;
	/**
	 * 
	 * @param googleAccId
	 * @return
	 * @throws Exception
	 */
	public boolean deleteGoogleAccount(String googleAccId) throws Exception;
	/**
	 * 
	 * @param account
	 * @return
	 * @throws Exception
	 */
	public boolean deleteGoogleAccount(GoogleAccount account) throws Exception;
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<SystemUser> listAllSystemUser() throws Exception;
	/**
	 * 
	 * @param domain
	 * @return
	 * @throws Exception
	 */
	public List<SystemUser> listAllSystemUserByDomain(String domain) throws Exception;
	
	public void initRole(SystemUser user) throws Exception;
	
	public void initGroup(SystemUser user) throws Exception;
}
