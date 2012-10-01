package cmg.org.monitor.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import cmg.org.monitor.dao.SystemAccountDAO;
import cmg.org.monitor.dao.SystemGroupDAO;
import cmg.org.monitor.entity.shared.SystemGroup;
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
public class SystemGroupDaoImpl implements SystemGroupDAO {
	private static final Logger logger = Logger
			.getLogger(SystemGroupDaoImpl.class.getCanonicalName());
	PersistenceManager pm;

	void initPersistence() {
		if (pm == null || pm.isClosed()) {
			pm = PMF.get().getPersistenceManager();
		}
	}

	@Override
	public boolean addNewGroup(SystemGroup sg) throws Exception {
		boolean check = false;
		initPersistence();
		try {
			pm.currentTransaction().begin();
			SystemGroup temp = new SystemGroup();
			temp.setName(sg.getName());
			temp.setDescription(sg.getDescription());
			pm.makePersistent(temp);
			pm.currentTransaction().commit();
			check = true;
		} catch (Exception ex) {
			logger.log(Level.SEVERE,
					" ERROR when addNewGroup. Message: " + ex.getMessage());
			pm.currentTransaction().rollback();
			throw ex;
		} finally {
			pm.close();
		}

		return check;
	}

	@Override
	public SystemGroup getByID(String id) throws Exception {
		initPersistence();
		try {
			return pm.getObjectById(SystemGroup.class, id);
		} catch (Exception ex) {
			logger.log(Level.SEVERE,
					" ERROR when getByID. Message: " + ex.getMessage());
			throw ex;
		} finally {
			pm.close();
		}
	}

	@Override
	public boolean updateGroup(String id, String groupName,
			String groupDescription) throws Exception {
		initPersistence();
		boolean check = false;
		try {
			SystemGroup temp = pm.getObjectById(SystemGroup.class, id);
			pm.currentTransaction().begin();
			temp.setName(groupName);
			temp.setDescription(groupDescription);
			pm.makePersistent(temp);
			pm.currentTransaction().commit();
			check = true;
		} catch (Exception ex) {
			logger.log(Level.SEVERE,
					" ERROR when updateGroup. Message: " + ex.getMessage());
			pm.currentTransaction().rollback();
			throw ex;
		} finally {
			pm.close();
		}

		return check;
	}

	@Override
	public boolean deleteGroup(String id) throws Exception {
		boolean check = false;
		initPersistence();
		try {
			pm.currentTransaction().begin();
			SystemGroup temp = pm.getObjectById(SystemGroup.class, id);
			pm.deletePersistent(temp);
			pm.currentTransaction().commit();
			check = true;
		} catch (Exception ex) {
			logger.log(Level.SEVERE,
					" ERROR when deleteGroup. Message: " + ex.getMessage());
			pm.currentTransaction().rollback();
			throw ex;
		} finally {
			pm.close();
		}
		return check;
	}

	@Override
	public SystemGroup[] getAllGroup() throws Exception {
		SystemGroup[] groups = null;
		initPersistence();
		Query query = pm.newQuery(SystemGroup.class);
		try {
			List<SystemGroup> temp = (List<SystemGroup>) query.execute();
			if (!temp.isEmpty()) {
				groups = new SystemGroup[temp.size()];
				temp.toArray(groups);
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE,
					" ERROR when getAllGroup. Message: " + e.getMessage());
			throw e;
		} finally {
			pm.close();
		}
		return groups;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see cmg.org.monitor.dao.SystemGroupDAO#addUserToGroup(cmg.org.monitor.entity.shared.SystemUser,
	 *      cmg.org.monitor.entity.shared.SystemGroup)
	 */
	public boolean addUserToGroup(SystemUser user, SystemGroup group)
			throws Exception {
		initPersistence();
		user = pm.getObjectById(SystemUser.class, user.getId());
		group = pm.getObjectById(SystemGroup.class, group.getId());
		boolean b = false;
		try {
			user.addToGroup(group);
			user.clear();
			pm.makePersistent(group);
			pm.makePersistent(user);
			b = true;
		} catch (Exception ex) {
			throw ex;
		} finally {
			pm.close();
		}
		return b;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see cmg.org.monitor.dao.SystemGroupDAO#removeUserFromGroup(cmg.org.monitor.entity.shared.SystemUser,
	 *      cmg.org.monitor.entity.shared.SystemGroup)
	 */
	public boolean removeUserFromGroup(SystemUser user, SystemGroup group)
			throws Exception {
		initPersistence();
		user = pm.getObjectById(SystemUser.class, user.getId());
		group = pm.getObjectById(SystemGroup.class, group.getId());
		boolean b = false;
		try {
			user.removeFromGroup(group);
			user.clear();
			pm.makePersistent(group);
			pm.makePersistent(user);
			b = true;
		} catch (Exception ex) {
			throw ex;
		} finally {
			pm.close();
		}
		return b;
	}

}
