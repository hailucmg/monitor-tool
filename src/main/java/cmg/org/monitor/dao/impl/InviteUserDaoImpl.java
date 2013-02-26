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
	 * 
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
	 * 
	 * @see cmg.org.monitor.dao.InvitedUserDAO#create3rdUser(cmg.org.monitor.module.client.InvitedUser)
	 */
	public boolean create3rdUser(InvitedUser user) throws Exception {
		List<InvitedUser> list = list3rdUserFromMemcache();
		initPersistence();
		boolean check = false;
		try {
			pm.currentTransaction().begin();
			pm.makePersistent(user);

			pm.currentTransaction().commit();
			check = true;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, " ERROR when create3rdUser. Message: "
					+ ex.getMessage());
			pm.currentTransaction().rollback();
			throw ex;
		} finally {
			pm.close();
		}
		if (check) {
			list.add(user);
			storeList3rdUserToMemcache(list);
		}
		return check;
	}

	/**
	 * (non-Javadoc)
	 * 
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
			logger.log(Level.SEVERE, " ERROR when create3rdUser. Message: "
					+ ex.getMessage());
			pm.currentTransaction().rollback();
			throw ex;
		} finally {
			pm.close();
		}
		return check;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see cmg.org.monitor.dao.InvitedUserDAO#delete3rdUser(java.lang.String)
	 */
	public boolean delete3rdUser(String id, boolean allowDeleteSystemUser) throws Exception {
		List<InvitedUser> list = list3rdUserFromMemcache();
		boolean check = false;
		initPersistence();
		InvitedUser temp = null;

		Query query = pm.newQuery(InvitedUser.class);
		query.setFilter("email == para");
		query.declareParameters("String para");
		try {
			List<InvitedUser> tempList = (List<InvitedUser>) query.execute(id);
			if (tempList != null && tempList.size() > 0) {
				temp = tempList.get(0);
				pm.deletePersistent(temp);
				check = true;
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			pm.close();
		}

		if (check && allowDeleteSystemUser) {
			SystemAccountDAO accountDao = new SystemAccountDaoImpl();
			try {
				SystemUser user = accountDao.getSystemUserByEmail(temp
						.getEmail());
				if (user != null) {
					check = accountDao.deleteSystemUser(user);
				}
			} catch (Exception ex) {
				//
			}
		}

		if (check && list != null && list.size() > 0) {			
			int index = -1;
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getEmail().equalsIgnoreCase(id)) {
					index = i;
				}
			}
			if (index != -1) {
				list.remove(index);
				storeList3rdUserToMemcache(list);
			}
		}
		return check;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see cmg.org.monitor.dao.InvitedUserDAO#delete3rdUser(cmg.org.monitor.module.client.InvitedUser)
	 */
	public boolean delete3rdUser(InvitedUser user) throws Exception {
		return delete3rdUser(user.getEmail(), true);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see cmg.org.monitor.dao.InvitedUserDAO#active3rdUser(cmg.org.monitor.module.client.InvitedUser)
	 */
	public boolean active3rdUser(InvitedUser in) throws Exception {
		SystemAccountDAO accountDao = new SystemAccountDaoImpl();
		List<SystemUser> listUsers = accountDao.listAllSystemUser(false);
		if (listUsers != null && listUsers.size() > 0) {
			for (SystemUser u : listUsers) {
				if (u.getEmail().equalsIgnoreCase(in.getEmail())) {
					return false;
				}
			}
		}
		List<InvitedUser> list = list3rdUserFromMemcache();
		boolean check = false;
		initPersistence();
		InvitedUser temp = null;
		Query query = pm.newQuery(InvitedUser.class);
		query.setFilter("email == para");
		query.declareParameters("String para");
		try {
			List<InvitedUser> tempList = (List<InvitedUser>) query.execute(in
					.getEmail());
			if (!tempList.isEmpty()) {
				temp = tempList.get(0);
				temp.setStatus(InvitedUser.STATUS_ACTIVE);
				pm.makePersistent(temp);
				check = true;
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, " ERROR when initList3rdUser. Message: "
					+ e.getMessage());
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
			check = accountDao.createSystemUser(user, true);

		}
		if (check && list != null && list.size() > 0) {
			for (InvitedUser u : list) {
				if (u.getEmail().equalsIgnoreCase(in.getEmail())) {
					u.setStatus(InvitedUser.STATUS_ACTIVE);
					break;
				}
			}
			storeList3rdUserToMemcache(list);
			accountDao.initSystemUserMemcache();
		}
		return check;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see cmg.org.monitor.dao.InvitedUserDAO#active3rdUser(java.lang.String)
	 */

	public boolean active3rdUser(String email) throws Exception {
		initPersistence();
		Query query = pm.newQuery(InvitedUser.class);
		query.setFilter("email == para");
		query.declareParameters("String para");
		List<InvitedUser> temp = null;
		try {
			temp = (List<InvitedUser>) query.execute(email);
			if (!temp.isEmpty()) {
				InvitedUser u = temp.get(0);
				u.setStatus(InvitedUser.STATUS_ACTIVE);
				pm.makePersistent(u);
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, " ERROR when initList3rdUser. Message: "
					+ e.getMessage());
		} finally {
			pm.close();
		}
		return false;

	}

	/**
	 * (non-Javadoc)
	 * 
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
			logger.log(Level.SEVERE, " ERROR when initList3rdUser. Message: "
					+ e.getMessage());
		} finally {
			pm.close();
		}
	}

	/**
	 * (non-Javadoc)
	 * 
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
	 * 
	 * @see cmg.org.monitor.dao.InvitedUserDAO#storeList3rdUserToMemcache(java.util.List)
	 */
	public void storeList3rdUserToMemcache(List list) {
		Gson gson = new Gson();
		try {
			MonitorMemcache.put(Key.create(Key.INVITE_USER), gson.toJson(list));
		} catch (Exception ex) {
			logger.log(Level.WARNING, "Error. Message: " + ex.getMessage());
		}

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see cmg.org.monitor.dao.InviteUserDAO#updateFullname(cmg.org.monitor.entity.shared.InvitedUser)
	 */
	public boolean updateFullname(InvitedUser user) throws Exception {
		boolean check = false;
		initPersistence();
		Query query = pm.newQuery(InvitedUser.class);
		query.setFilter("email == para");
		query.declareParameters("String para");
		List<InvitedUser> temp = null;
		try {
			temp = (List<InvitedUser>) query.execute(user.getEmail());
			if (!temp.isEmpty()) {
				InvitedUser u = temp.get(0);
				u.setFirstName(user.getFirstName());
				u.setLastName(user.getLastName());
				pm.makePersistent(u);
				check = true;
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, " ERROR when initList3rdUser. Message: "
					+ e.getMessage());
		} finally {
			pm.close();
		}
		if (check) {
			List<InvitedUser> list = list3rdUserFromMemcache();
			if (list != null && list.size() > 0) {
				for (InvitedUser ur : list) {
					if (ur.getEmail().equalsIgnoreCase(user.getEmail())) {
						ur.setFirstName(user.getFirstName());
						ur.setLastName(user.getLastName());
						break;
					}
				}
			}
			storeList3rdUserToMemcache(list);
		}

		if (check) {
			SystemAccountDAO accountDao = new SystemAccountDaoImpl();
			try {
				accountDao.updateFullname(user.getEmail(), user.getFirstName(),
						user.getLastName());
			} catch (Exception ex) {
				//
			}
		}

		return check;
	}

}
