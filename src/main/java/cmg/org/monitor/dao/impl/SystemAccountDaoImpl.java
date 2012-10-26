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

import cmg.org.monitor.dao.SystemAccountDAO;
import cmg.org.monitor.dao.SystemGroupDAO;
import cmg.org.monitor.entity.shared.GoogleAccount;
import cmg.org.monitor.entity.shared.SystemGroup;
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

public class SystemAccountDaoImpl implements SystemAccountDAO {
	private static final Logger logger = Logger
			.getLogger(SystemAccountDaoImpl.class.getCanonicalName());
	PersistenceManager pm;

	void initPersistence() {
		if (pm == null || pm.isClosed()) {
			pm = PMF.get().getPersistenceManager();
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see cmg.org.monitor.dao.SystemAccountDAO#createSystemUser(cmg.org.monitor.entity.shared.SystemUser)
	 */
	public boolean createSystemUser(SystemUser user, boolean isAddRole)
			throws Exception {
		initPersistence();
		SystemUser temp = new SystemUser();
		boolean check = false;
		try {
			pm.currentTransaction().begin();
			temp.swap(user);
			if (isAddRole) {
				temp.setRoleIDs(user.getRoleIDs());
				temp.setGroupIDs(user.getGroupIDs());
			}
			pm.makePersistent(temp);
			pm.currentTransaction().commit();
			check = true;
		} catch (Exception ex) {
			logger.log(
					Level.SEVERE,
					" ERROR when create System User. Message: "
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
	 * @see cmg.org.monitor.dao.SystemAccountDAO#deleteAllDomainUser(java.lang.String)
	 */
	public boolean deleteAllDomainUser(String domain) throws Exception {
		List<SystemUser> listUser = listAllSystemUserByDomain(domain, false);
		boolean check = false;
		if (!listUser.isEmpty()) {
			initPersistence();
			try {
				pm.currentTransaction().begin();
				for (SystemUser user : listUser) {
					user = pm.getObjectById(SystemUser.class, user.getId());
					pm.deletePersistent(user);
				}
				pm.currentTransaction().commit();
				check = true;
			} catch (Exception ex) {
				logger.log(
						Level.SEVERE,
						" ERROR when deleteAllDomainUser. Message: "
								+ ex.getMessage());
				pm.currentTransaction().rollback();
				throw ex;
			} finally {
				pm.close();
			}

		}
		if (check) {
			List<SystemUser> listAll = listAllSystemUser(false);
			if (listAll != null && listAll.size() > 0 && listUser != null
					&& listUser.size() > 0) {
				listAll.removeAll(listUser);
				storeListSystemUserToMemcache(listAll);
			}
		}
		return check;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see cmg.org.monitor.dao.SystemAccountDAO#updateGoogleAccount(cmg.org.monitor.entity.shared.GoogleAccount)
	 */
	public boolean updateGoogleAccount(GoogleAccount account) throws Exception {
		boolean check = false;
		initPersistence();
		try {
			pm.currentTransaction().begin();
			pm.makePersistent(account);
			pm.currentTransaction().commit();
			check = true;
		} catch (Exception ex) {
			logger.log(
					Level.SEVERE,
					" ERROR when deleteAllDomainUser. Message: "
							+ ex.getMessage());
			pm.currentTransaction().rollback();
			throw ex;
		} finally {
			pm.close();
		}

		if (check) {
			List<GoogleAccount> list = listAllGoogleAccount();
			if (list != null && list.size() > 0) {
				for (GoogleAccount acc : list) {
					if (acc.getId().equals(account.getId())) {
						acc.swap(account);
					}
				}
			}
			storeListGoogleAccountToMemcache(list);
		}
		return check;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see cmg.org.monitor.dao.SystemAccountDAO#createGoogleAccount(cmg.org.monitor.entity.shared.GoogleAccount)
	 */
	public boolean createGoogleAccount(GoogleAccount account) throws Exception {
		boolean check = false;
		initPersistence();
		try {
			pm.currentTransaction().begin();
			GoogleAccount acc = new GoogleAccount();
			acc.swap(account);
			pm.makePersistent(acc);
			pm.currentTransaction().commit();
			check = true;
		} catch (Exception ex) {
			logger.log(
					Level.SEVERE,
					" ERROR when createGoogleAccount. Message: "
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
	 * @see cmg.org.monitor.dao.SystemAccountDAO#deleteGoogleAccount(java.lang.String)
	 */
	public boolean deleteGoogleAccount(String googleAccId) throws Exception {
		boolean check = false;
		initPersistence();
		String domain = "";
		try {
			pm.currentTransaction().begin();
			GoogleAccount temp = pm.getObjectById(GoogleAccount.class,
					googleAccId);
			domain = temp.getDomain();
			pm.deletePersistent(temp);
			pm.currentTransaction().commit();
			check = true;
		} catch (Exception ex) {
			logger.log(
					Level.SEVERE,
					" ERROR when deleteGoogleAccount. Message: "
							+ ex.getMessage());
			pm.currentTransaction().rollback();
			throw ex;
		} finally {
			pm.close();
		}
		if (check) {
			if (domain != null && domain.length() > 0) {
				List<SystemUser> users = listAllSystemUserByDomain(domain,
						false);
				List<SystemUser> listAll = listAllSystemUser(false);
				if (listAll != null && listAll.size() > 0 && users != null
						&& users.size() > 0) {
					for (SystemUser u : users) {
						int index = -1;
						for (int i = 0; i < listAll.size(); i++) {
							if (u.getId().equals(listAll.get(i).getId())) {
								index = i;
								break;
							}
						}
						listAll.remove(index);
					}
					storeListSystemUserToMemcache(listAll);
				}

				if (users != null && users.size() > 0) {
					for (SystemUser user : users) {
						deleteSystemUser(user);
					}
				}
			}
		}

		if (check) {
			List<GoogleAccount> list = listAllGoogleAccount();
			if (list != null && list.size() > 0) {
				int index = -1;
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i).getId().equals(googleAccId)) {
						index = i;
						break;
					}
				}
				if (index != -1) {
					list.remove(index);
				}
				storeListGoogleAccountToMemcache(list);
			}
		}
		return check;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see cmg.org.monitor.dao.SystemAccountDAO#deleteGoogleAccount(cmg.org.monitor.entity.shared.GoogleAccount)
	 */
	public boolean deleteGoogleAccount(GoogleAccount account) throws Exception {
		return deleteGoogleAccount(account.getId());
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see cmg.org.monitor.dao.SystemAccountDAO#listAllSystemUser()
	 */
	@SuppressWarnings("unchecked")
	public List<SystemUser> listAllSystemUser(boolean b) throws Exception {
		List<SystemUser> tempOut = listSystemUserFromMemcache();
		if (tempOut == null || tempOut.size() == 0) {
			initPersistence();
			Query query = pm.newQuery(SystemUser.class);
			List<SystemUser> temp = null;
			try {
				temp = (List<SystemUser>) query.execute();
				if (!temp.isEmpty()) {
					for (SystemUser user : temp) {
						tempOut.add(user);
					}
				}
				storeListSystemUserToMemcache(tempOut);
			} catch (Exception e) {
				logger.log(
						Level.SEVERE,
						" ERROR when getSystemUserByEmail. Message: "
								+ e.getMessage());
				throw e;
			} finally {
				pm.close();
			}

		}
		if (tempOut != null && tempOut.size() > 0) {
			List<SystemUser> temp = new ArrayList<SystemUser>();
			for (SystemUser user : tempOut) {
				if (b) {
					if (!user.isSuspended()) {
						temp.add(user);
					}
				} else {
					temp.add(user);
				}
			}
			return temp;
		}
		return null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see cmg.org.monitor.dao.SystemAccountDAO#listAllSystemUserByDomain(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<SystemUser> listAllSystemUserByDomain(String domain, boolean b)
			throws Exception {
		List<SystemUser> store = listSystemUserFromMemcache();
		if (store != null && store.size() > 0) {
			List<SystemUser> temp = new ArrayList<SystemUser>();
			for (SystemUser user : store) {
				if (b) {
					if (user.getDomain().equalsIgnoreCase(domain)
							&& !user.isSuspended()) {
						temp.add(user);
					}
				} else {
					if (user.getDomain().equalsIgnoreCase(domain)) {
						temp.add(user);
					}
				}
			}
			return temp;
		} else {
			initSystemUserMemcache();
			initPersistence();
			Query query = pm.newQuery(SystemUser.class);
			if (b) {
				query.setFilter("domain == domainPara && isSuspended == value");
				query.declareParameters("String domainPara, boolean domainPara");
			} else {
				query.setFilter("domain == domainPara");
				query.declareParameters("String domainPara");
			}
			List<SystemUser> temp = null;
			List<SystemUser> tempOut = new ArrayList<SystemUser>();
			try {
				if (b) {
					temp = (List<SystemUser>) query.execute(domain, false);
				} else {
					temp = (List<SystemUser>) query.execute(domain);
				}
				if (!temp.isEmpty()) {
					for (SystemUser user : temp) {
						if (b) {
							if (!user.isSuspended()) {
								tempOut.add(user);
							}
						} else {
							tempOut.add(user);
						}
					}
				}
			} catch (Exception e) {
				logger.log(
						Level.SEVERE,
						" ERROR when getSystemUserByEmail. Message: "
								+ e.getMessage());
				throw e;
			} finally {
				pm.close();
			}

			return tempOut;
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see cmg.org.monitor.dao.SystemAccountDAO#getGoogleAccountById(java.lang.String)
	 */
	public GoogleAccount getGoogleAccountById(String id) throws Exception {
		List<GoogleAccount> store = listGoogleAccountFromMemcache();
		if (store != null && store.size() > 0) {
			for (GoogleAccount acc : store) {
				if (acc.getId().equals(id)) {
					return acc;
				}
			}
			return null;
		} else {
			initPersistence();
			try {
				return pm.getObjectById(GoogleAccount.class, id);
			} catch (Exception ex) {
				logger.log(
						Level.SEVERE,
						" ERROR when getGoogleAccountById. Message: "
								+ ex.getMessage());
				pm.currentTransaction().rollback();
				throw ex;
			} finally {
				pm.close();
			}
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see cmg.org.monitor.dao.SystemAccountDAO#listAllGoogleAccount()
	 */
	@SuppressWarnings("unchecked")
	public List<GoogleAccount> listAllGoogleAccount() throws Exception {
		List<GoogleAccount> store = listGoogleAccountFromMemcache();
		if (store != null && store.size() > 0) {
			return store;
		} else {
			initGoogleAccountMemcache();
			initPersistence();
			Query query = pm.newQuery(GoogleAccount.class);
			List<GoogleAccount> temp = null;
			List<GoogleAccount> tempOut = new ArrayList<GoogleAccount>();
			try {
				temp = (List<GoogleAccount>) query.execute();
				if (!temp.isEmpty()) {
					for (GoogleAccount user : temp) {
						tempOut.add(user);
					}
				}
			} catch (Exception e) {
				logger.log(
						Level.SEVERE,
						" ERROR when listAllGoogleAccount. Message: "
								+ e.getMessage());
				throw e;
			} finally {
				pm.close();
			}

			return tempOut;
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see cmg.org.monitor.dao.SystemAccountDAO#deleteSystemUser(cmg.org.monitor.entity.shared.SystemUser)
	 */
	public boolean deleteSystemUser(SystemUser user) throws Exception {
		SystemUser temp = getSystemUserByEmail(user.getEmail());
		boolean check = false;
		List<String> groupIds = null;
		String userId = "";
		if (temp != null) {
			initPersistence();
			try {
				pm.currentTransaction().begin();
				temp = pm.getObjectById(SystemUser.class, temp.getId());
				groupIds = temp.getGroupIDs();
				userId = temp.getId();
				pm.deletePersistent(temp);
				pm.currentTransaction().commit();
				check = true;
			} catch (Exception ex) {
				logger.log(
						Level.SEVERE,
						" ERROR when deleteSystemUser. Message: "
								+ ex.getMessage());
				pm.currentTransaction().rollback();
				ex.printStackTrace();
				throw ex;
			} finally {
				pm.close();
			}

		}
		if (groupIds != null && groupIds.size() > 0 && userId != null
				&& userId.length() > 0) {
			initPersistence();
			for (String groupId : groupIds) {
				SystemGroup group = pm
						.getObjectById(SystemGroup.class, groupId);
				group.removeUser(userId);
				pm.makePersistent(group);

			}
			pm.close();
		}

		if (check) {
			List<SystemUser> users = listAllSystemUser(false);
			if (users != null && users.size() > 0) {
				int index = -1;
				for (int i = 0; i < users.size(); i++) {
					if (users.get(i).getId().equals(user.getId())) {
						index = i;
						break;
					}
				}
				if (index != -1) {
					users.remove(index);
					storeListSystemUserToMemcache(users);
				}
			}
		}
		return check;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see cmg.org.monitor.dao.SystemAccountDAO#updateSystemUser(cmg.org.monitor.entity.shared.SystemUser)
	 */
	public boolean updateSystemUser(SystemUser user, boolean b)
			throws Exception {
		SystemUser temp = getSystemUserByEmail(user.getEmail());
		boolean check = false;
		if (temp != null) {
			initPersistence();
			temp.swap(user);
			if (b) {
				temp.setGroupIDs(user.getGroupIDs());
				temp.setRoleIDs(user.getRoleIDs());
			}
			try {
				pm.currentTransaction().begin();
				pm.makePersistent(temp);
				pm.currentTransaction().commit();
				check = true;
			} catch (Exception ex) {
				logger.log(
						Level.SEVERE,
						" ERROR when updateSystemUser. Message: "
								+ ex.getMessage());
				pm.currentTransaction().rollback();
				throw ex;
			} finally {
				pm.close();
			}

		}
		
		if (check) {
			List<SystemUser> list = listAllSystemUser(false);
			if (list != null && list.size() > 0) {
				for (SystemUser u : list) {
					if (u.getId().equals(user.getId())) {
						u.swap(user);
						if (b) {
							u.setGroupIDs(user.getGroupIDs());
							u.setRoleIDs(user.getRoleIDs());
						}
					}
				}
				storeListSystemUserToMemcache(list);
			}
		}
		return check;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see cmg.org.monitor.dao.SystemAccountDAO#getSystemUserByEmail(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public SystemUser getSystemUserByEmail(String email) throws Exception {
		List<SystemUser> store = listSystemUserFromMemcache();
		if (store != null && store.size() > 0) {
			for (SystemUser user : store) {
				if (user.getEmail().equals(email)) {
					return user;
				}
			}
			return null;
		} else {
			initSystemUserMemcache();
			initPersistence();
			Query query = pm.newQuery(SystemUser.class);
			query.setFilter("email == emailPara");
			query.declareParameters("String emailPara");
			List<SystemUser> temp = null;
			try {
				temp = (List<SystemUser>) query.execute(email);
				if (!temp.isEmpty()) {
					SystemUser user = temp.get(0);

					return user;
				}
			} catch (Exception e) {
				logger.log(
						Level.SEVERE,
						" ERROR when getSystemUserByEmail. Message: "
								+ e.getMessage());
				throw e;
			} finally {
				pm.close();
			}
			return null;
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see cmg.org.monitor.dao.SystemAccountDAO#getSystemUserById(java.lang.String)
	 */
	public SystemUser getSystemUserById(String id) throws Exception {
		List<SystemUser> store = listSystemUserFromMemcache();
		if (store != null && store.size() > 0) {
			for (SystemUser user : store) {
				if (user.getId().equals(id)) {
					return user;
				}
			}
			return null;
		} else {
			initSystemUserMemcache();
			initPersistence();
			try {
				SystemUser user = pm.getObjectById(SystemUser.class, id);
				return user;
			} catch (Exception e) {
				logger.log(
						Level.SEVERE,
						" ERROR when getSystemUserById. Message: "
								+ e.getMessage());
				throw e;
			} finally {
				pm.close();
			}
		}
	}

	@Override
	public void initRole(SystemUser user) throws Exception {
		/*
		 * SystemRoleDAO roleDao = new SystemRoleDaoImpl(); if
		 * (user.getRoleIDs().size() > 0) { tempRoles = new
		 * ArrayList<SystemRole>(); for (Object roleId : user.getRoleIDs()) {
		 * tempRole = roleDao.getById(roleId.toString());
		 * tempRoles.add(tempRole); } user.setRoles(tempRoles); }
		 */
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see cmg.org.monitor.dao.SystemAccountDAO#initGroup(java.lang.System)
	 */
	public List<SystemGroup> initGroup(SystemUser user) throws Exception {
		SystemGroupDAO groupDao = new SystemGroupDaoImpl();
		if (user.getGroupIDs() != null && user.getGroupIDs().size() > 0) {
			List<SystemGroup> tempGroups = new ArrayList<SystemGroup>();
			for (Object groupId : user.getGroupIDs()) {
				SystemGroup tempGroup = groupDao.getByID(groupId.toString());
				tempGroups.add(tempGroup);
			}
			return tempGroups;
		} else {
			return null;
		}
	}

	/*
	 * (non-Javadoc) * @see
	 * cmg.org.monitor.dao.SystemAccountDAO#createSystemUsers(java.util.List)
	 */
	@Override
	public boolean createSystemUsers(List users, boolean isAddRole)
			throws Exception {
		initPersistence();
		boolean check = false;
		if (!users.isEmpty()) {
			try {
				SystemUser temp = null;
				for (Object user : users) {
					if (user instanceof SystemUser) {
						Query query = pm.newQuery(SystemUser.class);
						query.setFilter("email == emailPara");
						query.declareParameters("String emailPara");
						List<SystemUser> tempList = (List<SystemUser>) query.execute(((SystemUser) user).getEmail());						
						if (tempList != null && !tempList.isEmpty()) {
							temp = tempList.get(0);
						} else {
							temp = new SystemUser();
						}						
						temp.swap((SystemUser) user);
						if (isAddRole) {
							temp.setRoleIDs(((SystemUser) user).getRoleIDs());
						}
						pm.makePersistent(temp);
					}
				}
				check = true;
			} catch (Exception ex) {
				logger.log(
						Level.SEVERE,
						" ERROR when create System User. Message: "
								+ ex.getMessage());
				throw ex;
			} finally {
				pm.close();
			}
		}
		return check;

	}

	/*
	 * (non-Javadoc) * @see
	 * cmg.org.monitor.dao.SystemAccountDAO#addRole(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public boolean updateRole(String email, String role, boolean b)
			throws Exception {
		boolean check = false;
		try {
			SystemUser user = getSystemUserByEmail(email);
			if (b) {
				user.addUserRole(role);
			} else {
				user.removeUserRole(role);
			}
			initPersistence();
			pm.currentTransaction().begin();
			pm.makePersistent(user);
			pm.currentTransaction().commit();
			check = true;
		} catch (Exception e) {
			logger.log(
					Level.SEVERE,
					" ERROR when getSystemUserByEmail. Message: "
							+ e.getMessage());
			pm.currentTransaction().rollback();
			throw e;
		} finally {
			pm.close();
		}
		if (check) {
			List<SystemUser> list = listAllSystemUser(false);
			if (list != null && list.size() > 0) {
				for (SystemUser user : list) {
					if (user.getEmail().equalsIgnoreCase(email)) {
						if (b) {
							user.addUserRole(role);
						} else {
							user.removeUserRole(role);
						}
						break;
					}
				}
				storeListSystemUserToMemcache(list);
			}
		}
		return check;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see cmg.org.monitor.dao.SystemAccountDAO#getGoogleAccountByDomain(java.lang.String)
	 */
	public GoogleAccount getGoogleAccountByDomain(String domain)
			throws Exception {
		List<GoogleAccount> store = listGoogleAccountFromMemcache();
		if (store != null && store.size() > 0) {
			for (GoogleAccount acc : store) {
				if (acc.getDomain().equalsIgnoreCase(domain)) {
					return acc;
				}
			}
			return null;
		} else {
			initGoogleAccountMemcache();
			initPersistence();
			Query query = pm.newQuery(GoogleAccount.class);
			query.setFilter("domain == domainPara");
			query.declareParameters("String domainPara");
			List<GoogleAccount> temp = null;
			try {
				temp = (List<GoogleAccount>) query.execute(domain);
				if (!temp.isEmpty()) {
					GoogleAccount user = temp.get(0);
					return user;
				}
			} catch (Exception e) {
				logger.log(
						Level.SEVERE,
						" ERROR when getGoogleAccountByDomain. Message: "
								+ e.getMessage());
				throw e;
			} finally {
				pm.close();
			}
			return null;
		}
	}

	@Override
	public List<GoogleAccount> listGoogleAccountFromMemcache() {
		List<GoogleAccount> tempOut = new ArrayList<GoogleAccount>();
		Gson gson = new Gson();
		Type type = new TypeToken<Collection<GoogleAccount>>() {
		}.getType();
		Object obj = MonitorMemcache.get(Key.create(Key.GOOGLE_ACCOUNT));
		if (obj != null && obj instanceof String) {
			try {
				System.out.println("START listGoogleAccountFromMemcache");
				tempOut = gson.fromJson(String.valueOf(obj), type);
			} catch (Exception ex) {
				logger.log(Level.WARNING, "Error. Message: " + ex.getMessage());
			}
		}
		return tempOut;
	}

	@Override
	public void storeListGoogleAccountToMemcache(List list) {
		Gson gson = new Gson();
		try {
			System.out.println("START storeListGoogleAccountToMemcache");
			MonitorMemcache.put(Key.create(Key.GOOGLE_ACCOUNT),
					gson.toJson(list));
		} catch (Exception ex) {
			logger.log(Level.WARNING, "Error. Message: " + ex.getMessage());
		}
	}

	@Override
	public void initGoogleAccountMemcache() {
		List<GoogleAccount> tempOut = new ArrayList<GoogleAccount>();
		initPersistence();
		Query query = pm.newQuery(GoogleAccount.class);
		List<GoogleAccount> temp = null;
		try {
			temp = (List<GoogleAccount>) query.execute();
			if (!temp.isEmpty()) {
				for (GoogleAccount user : temp) {
					tempOut.add(user);
				}
			}
			storeListGoogleAccountToMemcache(tempOut);
		} catch (Exception e) {
			logger.log(
					Level.SEVERE,
					" ERROR when initGoogleAccountMemcache. Message: "
							+ e.getMessage());
		} finally {
			pm.close();
		}
	}

	@Override
	public List<SystemUser> listSystemUserFromMemcache() {
		List<SystemUser> tempOut = new ArrayList<SystemUser>();
		Gson gson = new Gson();
		Type type = new TypeToken<Collection<SystemUser>>() {
		}.getType();
		Object obj = MonitorMemcache.get(Key.create(Key.SYSTEM_USER));
		if (obj != null && obj instanceof String) {
			try {
				System.out.println("START listSystemUserFromMemcache");
				tempOut = gson.fromJson(String.valueOf(obj), type);
			} catch (Exception ex) {
				logger.log(Level.WARNING, "Error. Message: " + ex.getMessage());
			}
		}
		return tempOut;
	}

	@Override
	public void storeListSystemUserToMemcache(List list) {
		Gson gson = new Gson();
		try {
			System.out.println("START storeListSystemUserToMemcache");
			MonitorMemcache.put(Key.create(Key.SYSTEM_USER), gson.toJson(list));
		} catch (Exception ex) {
			logger.log(Level.WARNING, "Error. Message: " + ex.getMessage());
		}
	}

	@Override
	public void initSystemUserMemcache() {
		List<SystemUser> tempOut = new ArrayList<SystemUser>();
		initPersistence();
		Query query = pm.newQuery(SystemUser.class);
		List<SystemUser> temp = null;
		try {
			temp = (List<SystemUser>) query.execute();
			if (!temp.isEmpty()) {
				for (SystemUser user : temp) {
					tempOut.add(user);
				}
			}
			storeListSystemUserToMemcache(tempOut);
		} catch (Exception e) {
			logger.log(
					Level.SEVERE,
					" ERROR when initSystemUserMemcache. Message: "
							+ e.getMessage());
		} finally {
			pm.close();
		}
	}

	/**
	 * (non-Javadoc)
	 * @see cmg.org.monitor.dao.SystemAccountDAO#updateFullname(java.lang.String, java.lang.String, java.lang.String) 
	 */
	public boolean updateFullname(String email, String firstName,
			String lastName) throws Exception {
		boolean check = false;
		initPersistence();
		Query query = pm.newQuery(SystemUser.class);
		query.setFilter("email == emailPara");
		query.declareParameters("String emailPara");
		List<SystemUser> temp = null;
		try {
			temp = (List<SystemUser>) query.execute(email);
			if (!temp.isEmpty()) {
				SystemUser user = temp.get(0);
				user.setFirstName(firstName);
				user.setLastName(lastName);
				pm.makePersistent(user);			
				check = true;
			}
		} catch (Exception e) {
			logger.log(
					Level.SEVERE,
					" ERROR when getSystemUserByEmail. Message: "
							+ e.getMessage());
			throw e;
		} finally {
			pm.close();
		}
		if (check) {
			List<SystemUser> list = listSystemUserFromMemcache();
			if (list != null && list.size() > 0) {
				for (SystemUser u : list ) {
					if (u.getEmail().equalsIgnoreCase(email)) {
						u.setFirstName(firstName);
						u.setLastName(lastName);
						break;
					}
				}
			}
			storeListSystemUserToMemcache(list);
		}
		return check;
	}
}
