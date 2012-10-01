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

import cmg.org.monitor.dao.SystemRoleDAO;
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

public class SystemRoleDaoImpl implements SystemRoleDAO {
	private static final Logger logger = Logger
			.getLogger(SystemRoleDaoImpl.class.getCanonicalName());
	PersistenceManager pm;

	void initPersistence() {
		if (pm == null || pm.isClosed()) {
			pm = PMF.get().getPersistenceManager();
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see cmg.org.monitor.dao.SystemRoleDAO#createRole(cmg.org.monitor.entity.shared.SystemRole)
	 */
	public boolean createRole(SystemRole role) throws Exception {
		initPersistence();
		boolean check = false;
		try {
			pm.currentTransaction().begin();
			pm.makePersistent(role);
			pm.currentTransaction().commit();
			check = true;
		} catch (Exception ex) {
			logger.log(Level.SEVERE,
					" ERROR when createRole. Message: " + ex.getMessage());
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
	 * @see cmg.org.monitor.dao.SystemRoleDAO#updateRole(cmg.org.monitor.entity.shared.SystemRole)
	 */
	public boolean updateRole(SystemRole role) throws Exception {
		initPersistence();
		SystemRole temp = pm.getObjectById(SystemRole.class, role.getId());
		boolean check = false;
		if (temp != null) {
			try {
				pm.currentTransaction().begin();
				temp.setName(role.getName());
				pm.makePersistent(temp);
				pm.currentTransaction().commit();
				check = true;
			} catch (Exception ex) {
				logger.log(Level.SEVERE, " ERROR when updateRole. Message: "
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
	 * @see cmg.org.monitor.dao.SystemRoleDAO#deleteRole(cmg.org.monitor.entity.shared.SystemRole)
	 */
	public boolean deleteRole(SystemRole role) throws Exception {
		boolean check = false;
		initPersistence();
		try {
			pm.currentTransaction().begin();
			SystemRole temp = pm.getObjectById(SystemRole.class, role.getId());
			pm.deletePersistent(temp);
			pm.currentTransaction().commit();
			check = true;
		} catch (Exception ex) {
			logger.log(Level.SEVERE,
					" ERROR when deleteRole. Message: " + ex.getMessage());
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
	 * @see cmg.org.monitor.dao.SystemRoleDAO#getById(java.lang.String)
	 */
	public SystemRole getById(String id) throws Exception {
		initPersistence();
		try {
			SystemRole role = new SystemRole();
			SystemRole tmp = pm.getObjectById(SystemRole.class, id);
			role.setId(tmp.getId());
			role.setName(tmp.getName());
			return role;
		} catch (Exception ex) {
			logger.log(Level.SEVERE,
					" ERROR when getByID. Message: " + ex.getMessage());
			throw ex;
		} finally {
			pm.close();
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see cmg.org.monitor.dao.SystemRoleDAO#listAll()
	 */
	public List<SystemRole> listAll() throws Exception {
		List<SystemRole> roles = null;
		initPersistence();
		Query query = pm.newQuery(SystemRole.class);
		try {
			List<SystemRole> temp = (List<SystemRole>) query.execute();
			if (!temp.isEmpty()) {
				roles = new ArrayList<SystemRole>();
				roles.addAll(temp);
			}
		} catch (Exception e) {
			logger.log(
					Level.SEVERE,
					" ERROR when listAll. Message: "
							+ e.getMessage());
			throw e;
		} finally {
			pm.close();
		}
		return roles;
	}

	@Override
	public void init() {
		try {
			SystemRole role = getByName(SystemRole.ROLE_ADMINISTRATOR);
			if (role == null) {
				role = new SystemRole();
				role.setName(SystemRole.ROLE_ADMINISTRATOR);
				createRole(role);
			}
			role = getByName(SystemRole.ROLE_USER);
			if (role == null) {
				role = new SystemRole();
				role.setName(SystemRole.ROLE_USER);
				createRole(role);
			}
		} catch (Exception e) {		
		}
	}
	/**
	 * (non-Javadoc)
	 * 
	 * @see cmg.org.monitor.dao.SystemRoleDAO#getByName(java.lang.String)
	 */
	public SystemRole getByName(String name) throws Exception {
		initPersistence();
		Query query = pm.newQuery(SystemRole.class);
		query.setFilter("name == namePara");
		query.declareParameters("String namePara");
		try {
			List<SystemRole> temp = (List<SystemRole>) query.execute(name);
			if (!temp.isEmpty()) {
				SystemRole role = new SystemRole();
				role.setId(temp.get(0).getId());
				role.setName(temp.get(0).getName());
				return role;
			}
		} catch (Exception e) {
			logger.log(
					Level.SEVERE,
					" ERROR when getByName. Message: "
							+ e.getMessage());
			throw e;
		} finally {
			pm.close();
		}
		return null;
	}

	/**
	 * (non-Javadoc)
	 * @see cmg.org.monitor.dao.SystemRoleDAO#addRole(cmg.org.monitor.entity.shared.SystemUser, cmg.org.monitor.entity.shared.SystemRole) 
	 */
	public boolean addRole(SystemUser user, String roleName) throws Exception {
		SystemRole role = getByName(roleName);
		if (role == null) {
			role = new SystemRole();
			role.setName(roleName);
			createRole(role);
		}
		initPersistence();
		user = pm.getObjectById(SystemUser.class, user.getId());
		boolean b= false;
		try {			
			user.addRole(role);		
			user.clear();
			pm.makePersistent(role);
			pm.makePersistent(user);
			b = true;
		} catch (Exception ex) {
			logger.log(
					Level.SEVERE,
					" ERROR when addRole. Message: "
							+ ex.getMessage());
			throw ex;
		} finally {
			pm.close();
		}		
		return b;
	}

	/**
	 * (non-Javadoc)
	 * @see cmg.org.monitor.dao.SystemRoleDAO#removeRole(cmg.org.monitor.entity.shared.SystemUser, cmg.org.monitor.entity.shared.SystemRole) 
	 */
	public boolean removeRole(SystemUser user, String roleName)
			throws Exception {
		SystemRole role = getByName(roleName);	
		initPersistence();
		user = pm.getObjectById(SystemUser.class, user.getId());
		boolean b= false;
		try {			
			user.removeRole(role);			
			user.clear();
			pm.makePersistent(role);
			pm.makePersistent(user);
			b = true;
		} catch (Exception ex) {		
			logger.log(
					Level.SEVERE,
					" ERROR when removeRole. Message: "
							+ ex.getMessage());
			throw ex;
		} finally {
			pm.close();
		}		
		return b;
	}

}
