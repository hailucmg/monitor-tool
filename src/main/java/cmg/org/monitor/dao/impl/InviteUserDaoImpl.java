/**
 * Copyright (c) CMG Ltd All rights reserved.
 *
 * This software is the confidential and proprietary information of CMG
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with CMG.
 */

package cmg.org.monitor.dao.impl;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import cmg.org.monitor.dao.InviteUserDAO;
import cmg.org.monitor.dao.SystemAccountDAO;
import cmg.org.monitor.entity.shared.InvitedUser;
import cmg.org.monitor.entity.shared.SystemRole;
import cmg.org.monitor.entity.shared.SystemUser;
import cmg.org.monitor.memcache.Key;
import cmg.org.monitor.memcache.MonitorMemcache;
import cmg.org.monitor.services.PMF;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/** 
 * DOCME
 * 
 * @Creator Hai Lu
 * @author $Author$
 * @version $Revision$
 * @Last changed: $LastChangedDate$
 */

public class InviteUserDaoImpl implements InviteUserDAO {
	
	private static final Logger logger = Logger
			.getLogger(InviteUserDaoImpl.class.getCanonicalName());
	PersistenceManager pm;

	void initPersistence() {
		if (pm == null || pm.isClosed()) {
			pm = PMF.get().getPersistenceManager();
		}
	}
	/**
	 * (non-Javadoc)
	 * @see cmg.org.monitor.dao.InvitedUserDAO#list3rdUser() 
	 */
	public List list3rdUser() throws Exception {
		List<InvitedUser> list = list3rdUserFromMemcache();
		if (list != null && list.size() > 0) {
			return list;
		} else {
			list = new ArrayList<InvitedUser>();
			initPersistence();
			Query query = pm.newQuery(InvitedUser.class);
			List<InvitedUser> temp = null;
			try {
				temp = (List<InvitedUser>) query.execute();
				if (!temp.isEmpty()) {
					for (InvitedUser user : temp) {
						list.add(user);
					}
				}
				storeList3rdUserToMemcache(list);
			} catch (Exception e) {
				logger.log(
						Level.SEVERE,
						" ERROR when initList3rdUser. Message: "
								+ e.getMessage());
			} finally {
				pm.close();
			}	
			return list;
		}
	}

	/**
	 * (non-Javadoc)
	 * @see cmg.org.monitor.dao.InvitedUserDAO#create3rdUser(cmg.org.monitor.module.client.InvitedUser) 
	 */
	public boolean create3rdUser(InvitedUser user) throws Exception {
		initPersistence();
		boolean check = false;
		try {
			pm.currentTransaction().begin();
			pm.makePersistent(user);
			pm.currentTransaction().commit();
			check = true;
		} catch (Exception ex) {
			logger.log(Level.SEVERE,
					" ERROR when create3rdUser. Message: " + ex.getMessage());
			pm.currentTransaction().rollback();
			throw ex;
		} finally {
			pm.close();
		}
		return check;
	}

	/**
	 * (non-Javadoc)
	 * @see cmg.org.monitor.dao.InvitedUserDAO#update3rdUser(cmg.org.monitor.module.client.InvitedUser) 
	 */
	public boolean update3rdUser(InvitedUser user) throws Exception {
		initPersistence();
		boolean check = false;
		try {
			pm.currentTransaction().begin();
			pm.makePersistent(user);
			pm.currentTransaction().commit();
			check = true;
		} catch (Exception ex) {
			logger.log(Level.SEVERE,
					" ERROR when create3rdUser. Message: " + ex.getMessage());
			pm.currentTransaction().rollback();
			throw ex;
		} finally {
			pm.close();
		}
		return check;
	}

	/**
	 * (non-Javadoc)
	 * @see cmg.org.monitor.dao.InvitedUserDAO#delete3rdUser(java.lang.String) 
	 */
	public boolean delete3rdUser(String id) throws Exception {
		List<InvitedUser> list = list3rdUserFromMemcache();
		boolean check = false;
		initPersistence();
		InvitedUser temp;
		try {
			temp = pm.getObjectById(InvitedUser.class, id);
			if (temp != null) {
				pm.deletePersistent(temp);
				check = true;
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			pm.close();
		}
		
		if (check) {
			SystemAccountDAO accountDao = new SystemAccountDaoImpl();
			SystemUser user = accountDao.getSystemUserByEmail(temp.getEmail());
			if (user != null) {
				check = accountDao.deleteSystemUser(user);
			}
		}
		
		if (check && list != null && list.size() > 0) {
			int index = -1;
			for (int i = 0; i< list.size(); i++) {
				if (list.get(i).getId().equalsIgnoreCase(id)) {
					index = i;
				}
			}
			list.remove(index);
			storeList3rdUserToMemcache(list);
		}
		return check;
	}

	/**
	 * (non-Javadoc)
	 * @see cmg.org.monitor.dao.InvitedUserDAO#delete3rdUser(cmg.org.monitor.module.client.InvitedUser) 
	 */
	public boolean delete3rdUser(InvitedUser user) throws Exception {
		return delete3rdUser(user.getId());
	}

	/**
	 * (non-Javadoc)
	 * @see cmg.org.monitor.dao.InvitedUserDAO#active3rdUser(cmg.org.monitor.module.client.InvitedUser) 
	 */
	public boolean active3rdUser(InvitedUser in) throws Exception {
		List<InvitedUser> list = list3rdUserFromMemcache();
		boolean check = false;
		initPersistence();
		InvitedUser temp;
		try {
			temp = pm.getObjectById(InvitedUser.class, in.getId());
			if (temp != null) {
				temp.setStatus(InvitedUser.STATUS_ACTIVE);
				pm.makePersistent(temp);
				check = true;
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			pm.close();
		}
		if (check) {
			SystemUser user = new SystemUser();
			user.setFirstName(temp.getFirstName());
			user.setLastName(temp.getLastName());	
			user.setUsername(temp.getEmail());
			user.setEmail(temp.getEmail());
			user.setDomain(SystemUser.THIRD_PARTY_USER);
			user.addUserRole(SystemRole.ROLE_USER);
			user.setGroupIDs(in.getGroupIDs());
			SystemAccountDAO accountDao = new SystemAccountDaoImpl();
			check = accountDao.createSystemUser(user, true);
			accountDao.initSystemUserMemcache();
		}
		if (check && list != null && list.size() > 0) {			
			for (int i = 0; i< list.size(); i++) {
				if (list.get(i).getId().equalsIgnoreCase(in.getId())) {
					list.get(i).setStatus(InvitedUser.STATUS_ACTIVE);
					break;
				}
			}			
			storeList3rdUserToMemcache(list);
		}
		return check;
	}

	/**
	 * (non-Javadoc)
	 * @see cmg.org.monitor.dao.InvitedUserDAO#active3rdUser(java.lang.String) 
	 */
	@Deprecated
	public boolean active3rdUser(String id) throws Exception {
		return false;
		
	}
	/**
	 * (non-Javadoc)
	 * @see cmg.org.monitor.dao.InvitedUserDAO#initList3rdUser() 
	 */
	public void initList3rdUser() {
		List<InvitedUser> tempOut = new ArrayList<InvitedUser>();
		initPersistence();
		Query query = pm.newQuery(InvitedUser.class);
		List<InvitedUser> temp = null;
		try {
			temp = (List<InvitedUser>) query.execute();
			if (!temp.isEmpty()) {
				for (InvitedUser user : temp) {
					tempOut.add(user);
				}
			}
			storeList3rdUserToMemcache(tempOut);
		} catch (Exception e) {
			logger.log(
					Level.SEVERE,
					" ERROR when initList3rdUser. Message: "
							+ e.getMessage());
		} finally {
			pm.close();
		}		
	}
	/**
	 * (non-Javadoc)
	 * @see cmg.org.monitor.dao.InvitedUserDAO#list3rdUserFromMemcache() 
	 */
	public List list3rdUserFromMemcache() {
		List<InvitedUser> tempOut = new ArrayList<InvitedUser>();
		Gson gson = new Gson();
		Type type = new TypeToken<Collection<InvitedUser>>() {
		}.getType();
		Object obj = MonitorMemcache.get(Key.create(Key.INVITE_USER));
		if (obj != null && obj instanceof String) {
			try {				
				tempOut = gson.fromJson(String.valueOf(obj), type);
			} catch (Exception ex) {
				logger.log(Level.WARNING, "Error. Message: " + ex.getMessage());
			}
		}
		return tempOut;
	}
	/**
	 * (non-Javadoc)
	 * @see cmg.org.monitor.dao.InvitedUserDAO#storeList3rdUserToMemcache(java.util.List) 
	 */
	public void storeList3rdUserToMemcache(List list) {
		Gson gson = new Gson();
		try {			
			MonitorMemcache.put(Key.create(Key.INVITE_USER),
					gson.toJson(list));
		} catch (Exception ex) {
			logger.log(Level.WARNING, "Error. Message: " + ex.getMessage());
		}
		
	}

}
