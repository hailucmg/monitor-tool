/**
 * Copyright (c) CMG Ltd All rights reserved.
 *
 * This software is the confidential and proprietary information of CMG
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with CMG.
 */

package cmg.org.monitor.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import cmg.org.monitor.dao.SystemAccountDAO;
import cmg.org.monitor.dao.SystemGroupDAO;
import cmg.org.monitor.dao.SystemRoleDAO;
import cmg.org.monitor.entity.shared.GoogleAccount;
import cmg.org.monitor.entity.shared.SystemGroup;
import cmg.org.monitor.entity.shared.SystemRole;
import cmg.org.monitor.entity.shared.SystemUser;
import cmg.org.monitor.services.PMF;

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
	
	private List<SystemRole> tempRoles;
	private SystemRole tempRole;
	private List<SystemGroup> tempGroups;
	private SystemGroup tempGroup;

	
	
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
	public boolean createSystemUser(SystemUser user) throws Exception {
		initPersistence();
		SystemUser temp = new SystemUser();
		boolean check = false;
		try {
			pm.currentTransaction().begin();
			temp.swap(user);
			user.clear();
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
	 * @see cmg.org.monitor.dao.SystemAccountDAO#createSystemUsers(java.util.List)
	 */
	@SuppressWarnings("rawtypes")
	public boolean createSystemUsers(List users) throws Exception {
		initPersistence();
		boolean check = false;
		if (!users.isEmpty()) {
			try {
				SystemUser temp = null;
				for (Object user : users) {
					if (user instanceof SystemUser) {
						temp = new SystemUser();
						temp.swap((SystemUser) user);
						temp.clear();
						pm.currentTransaction().begin();
						pm.makePersistent(temp);
						pm.currentTransaction().commit();
					}
				}
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
		}
		return check;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see cmg.org.monitor.dao.SystemAccountDAO#deleteAllDomainUser(java.lang.String)
	 */
	public boolean deleteAllDomainUser(String domain) throws Exception {
		List<SystemUser> listUser = listAllSystemUserByDomain(domain);
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
			GoogleAccount temp = pm.getObjectById(GoogleAccount.class,
					account.getId());
			temp.swap(account);
			pm.makePersistent(temp);
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
		try {
			pm.currentTransaction().begin();
			GoogleAccount temp = pm.getObjectById(GoogleAccount.class,
					googleAccId);
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
	public List<SystemUser> listAllSystemUser() throws Exception {
		initPersistence();
		Query query = pm.newQuery(SystemUser.class);
		List<SystemUser> temp = null;
		List<SystemUser> tempOut = new ArrayList<SystemUser>();
		try {
			temp = (List<SystemUser>) query.execute();
			if (!temp.isEmpty()) {
				for (SystemUser user : temp) {
					tempOut.add(user);
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

	/**
	 * (non-Javadoc)
	 * 
	 * @see cmg.org.monitor.dao.SystemAccountDAO#listAllSystemUserByDomain(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<SystemUser> listAllSystemUserByDomain(String domain)
			throws Exception {
		initPersistence();
		Query query = pm.newQuery(SystemUser.class);
		query.setFilter("domain == domainPara");
		query.declareParameters("String domainPara");
		List<SystemUser> temp = null;
		List<SystemUser> tempOut = new ArrayList<SystemUser>();
		try {
			temp = (List<SystemUser>) query.execute(domain);
			if (!temp.isEmpty()) {
				for (SystemUser user : temp) {					
				
					tempOut.add(user);
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

	/**
	 * (non-Javadoc)
	 * 
	 * @see cmg.org.monitor.dao.SystemAccountDAO#getGoogleAccountById(java.lang.String)
	 */
	public GoogleAccount getGoogleAccountById(String id) throws Exception {
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

	/**
	 * (non-Javadoc)
	 * 
	 * @see cmg.org.monitor.dao.SystemAccountDAO#listAllGoogleAccount()
	 */
	@SuppressWarnings("unchecked")
	public List<GoogleAccount> listAllGoogleAccount() throws Exception {
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

	/**
	 * (non-Javadoc)
	 * 
	 * @see cmg.org.monitor.dao.SystemAccountDAO#deleteSystemUser(cmg.org.monitor.entity.shared.SystemUser)
	 */
	public boolean deleteSystemUser(SystemUser user) throws Exception {
		SystemUser temp = getSystemUserByEmail(user.getEmail());
		boolean check = false;
		if (temp != null) {
			initPersistence();
			try {
				pm.currentTransaction().begin();
				temp = pm.getObjectById(SystemUser.class, temp.getId());
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
		return check;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see cmg.org.monitor.dao.SystemAccountDAO#updateSystemUser(cmg.org.monitor.entity.shared.SystemUser)
	 */
	public boolean updateSystemUser(SystemUser user) throws Exception {
		SystemUser temp = getSystemUserByEmail(user.getEmail());
		boolean check = false;
		if (temp != null) {
			initPersistence();
			temp.swap(user);
			try {
				pm.currentTransaction().begin();
				temp.clear();
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
		return check;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see cmg.org.monitor.dao.SystemAccountDAO#getSystemUserByEmail(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public SystemUser getSystemUserByEmail(String email) throws Exception {
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

	/**
	 * (non-Javadoc)
	 * 
	 * @see cmg.org.monitor.dao.SystemAccountDAO#getSystemUserById(java.lang.String)
	 */
	public SystemUser getSystemUserById(String id) throws Exception {
		initPersistence();
		try {
			SystemUser user = pm.getObjectById(SystemUser.class, id);			
			return user;
		} catch (Exception e) {
			logger.log(Level.SEVERE, " ERROR when getSystemUserById. Message: "
					+ e.getMessage());
			throw e;
		} finally {
			pm.close();
		}
	}
	
	

	@Override
	public void initRole(SystemUser user) throws Exception {
		/*
		SystemRoleDAO roleDao = new SystemRoleDaoImpl();
		if (user.getRoleIDs().size() > 0) {
			tempRoles = new ArrayList<SystemRole>();
			for (Object roleId : user.getRoleIDs()) {			
				tempRole = roleDao.getById(roleId.toString());
				tempRoles.add(tempRole);
			}
			user.setRoles(tempRoles);
		}
		*/		
	}

	/**
	 * (non-Javadoc)
	 * @see cmg.org.monitor.dao.SystemAccountDAO#initGroup(java.lang.System) 
	 */
	public void initGroup(SystemUser user) throws Exception {
		SystemGroupDAO groupDao = new SystemGroupDaoImpl();
		if (user.getGroupIDs().size() > 0) {
			tempGroups = new ArrayList<SystemGroup>();
			for (Object groupId : user.getGroupIDs()) {			
				tempGroup = groupDao.getByID(groupId.toString());
				tempGroups.add(tempGroup);
			}
			user.setGroups(tempGroups);
		}		
	}
}
