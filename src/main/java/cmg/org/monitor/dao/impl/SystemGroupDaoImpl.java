package cmg.org.monitor.dao.impl;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import cmg.org.monitor.dao.SystemGroupDAO;
import cmg.org.monitor.entity.shared.SystemGroup;
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
			ex.printStackTrace();
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
		SystemGroup temp = pm.getObjectById(SystemGroup.class, id);
		boolean check = false;
		if (temp != null) {			
			try {
				pm.currentTransaction().begin();
				temp.setName(groupName);
				temp.setDescription(groupDescription);
				pm.makePersistent(temp);
				pm.currentTransaction().commit();
				check = true;
			} catch (Exception ex) {
				logger.log(Level.SEVERE, " ERROR when updateGroup. Message: "
						+ ex.getMessage());
				pm.currentTransaction().rollback();
				throw ex;
			} finally {
				pm.close();
			}

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
			e.printStackTrace();
			logger.log(
					Level.SEVERE,
					" ERROR when getAllGroup. Message: "
							+ e.getMessage());
			throw e;
		} finally {
			pm.close();
		}
		return groups;
	}

}
