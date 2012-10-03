package cmg.org.monitor.dao.impl;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import cmg.org.monitor.dao.SystemAccountDAO;
import cmg.org.monitor.dao.SystemGroupDAO;
import cmg.org.monitor.entity.shared.SystemGroup;
import cmg.org.monitor.entity.shared.SystemUser;
import cmg.org.monitor.memcache.Key;
import cmg.org.monitor.memcache.MonitorMemcache;
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
		List<SystemGroup> store = listSystemGroupFromMemcache();
		if (store != null && store.size() > 0) {
			for (SystemGroup g : store) {
				if (g.getId().equals(id)) {
					return g;
				}
			}
			return null;
		} else {
			initSystemGroupMemcache();
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
		if (check) {
			List<SystemGroup> groups = listSystemGroupFromMemcache();
			if (groups != null && groups.size() > 0) {
				for (SystemGroup g : groups) {
					if (g.getId().equals(id)) {
						g.setName(groupName);
						g.setDescription(groupDescription);
					}
				}
				storeListSystemGroupToMemcache(groups);
			}
		}
		return check;
	}

	@Override
	public boolean deleteGroup(String id) throws Exception {
		boolean check = false;
		initPersistence();
		List<String> userIds = null;
		try {
			pm.currentTransaction().begin();
			SystemGroup temp = pm.getObjectById(SystemGroup.class, id);
			if (temp != null) {
				userIds = temp.getUserIDs();
			}
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

		if (userIds != null && userIds.size() > 0) {
			for (String userId : userIds) {
				initPersistence();
				SystemUser user = pm.getObjectById(SystemUser.class, userId);
				user.removeGroup(id);
				pm.makePersistent(user);
				pm.close();
			}
		}
		List<SystemGroup> list = listSystemGroupFromMemcache();
		if (list != null && list.size() > 0) {
			int index = -1;
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getId().equals(id)) {
					index = i;
					break;
				}
			}
			if (index != -1) {
				list.remove(index);
				storeListSystemGroupToMemcache(list);
			}
		}

		return check;
	}

	@Override
	public SystemGroup[] getAllGroup() throws Exception {
		List<SystemGroup> store = listSystemGroupFromMemcache();
		if (store != null && store.size() > 0) {
			SystemGroup[] groups = new SystemGroup[store.size()];
			store.toArray(groups);
			return groups;
		} else {
			initSystemGroupMemcache();
			SystemGroup[] groups = null;
			initPersistence();
			Query query = pm.newQuery(SystemGroup.class);
			try {
				List<SystemGroup> temp = (List<SystemGroup>) query.execute();
				List<SystemGroup> listTemp = new ArrayList<SystemGroup>();
				if (!temp.isEmpty()) {
					for (SystemGroup g : temp) {
						listTemp.add(g);
					}
					groups = new SystemGroup[listTemp.size()];
					listTemp.toArray(groups);
				}
			} catch (Exception e) {
				logger.log(Level.SEVERE, " ERROR when getAllGroup. Message: "
						+ e.getMessage());
				throw e;
			} finally {
				pm.close();
			}
			return groups;
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see cmg.org.monitor.dao.SystemGroupDAO#addUserToGroup(cmg.org.monitor.entity.shared.SystemUser,
	 *      cmg.org.monitor.entity.shared.SystemGroup)
	 */
	public boolean addUserToGroup(String userEmail, String groupId)
			throws Exception {
		SystemAccountDAO accountDao = new SystemAccountDaoImpl();
		boolean b = false;
		String userId = "";
		try {
			SystemUser user = accountDao.getSystemUserByEmail(userEmail);
			userId = user.getId();
			user.addGroup(groupId);
			accountDao.updateSystemUser(user, true);
			initPersistence();
			SystemGroup group = pm.getObjectById(SystemGroup.class, groupId);
			group.addUser(userId);
			pm.makePersistent(group);
			b = true;
		} catch (Exception ex) {
			throw ex;
		} finally {
			pm.close();
		}
		if (b) {
			List<SystemUser> users = accountDao.listAllSystemUser(false);
			if (users != null && users.size() > 0) {
				for (SystemUser u : users) {
					if (u.getEmail().equalsIgnoreCase(userEmail)) {
						u.addGroup(groupId);
					}
				}
				accountDao.storeListSystemUserToMemcache(users);
			}
			List<SystemGroup> groups = listSystemGroupFromMemcache();
			if (groups != null && groups.size() > 0) {
				for (SystemGroup g : groups) {
					if (g.getId().equals(groupId)) {
						g.addUser(userId);
					}
				}
			}
			storeListSystemGroupToMemcache(groups);
		}

		return b;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see cmg.org.monitor.dao.SystemGroupDAO#removeUserFromGroup(cmg.org.monitor.entity.shared.SystemUser,
	 *      cmg.org.monitor.entity.shared.SystemGroup)
	 */
	public boolean removeUserFromGroup(String userEmail, String groupId)
			throws Exception {
		SystemAccountDAO accountDao = new SystemAccountDaoImpl();
		boolean b = false;
		String userId = "";
		try {
			SystemUser user = accountDao.getSystemUserByEmail(userEmail);
			userId = user.getId();
			user.removeGroup(groupId);
			accountDao.updateSystemUser(user, true);
			initPersistence();
			SystemGroup group = pm.getObjectById(SystemGroup.class, groupId);
			group.removeUser(userId);
			pm.makePersistent(group);
			b = true;
		} catch (Exception ex) {
			throw ex;
		} finally {
			pm.close();
		}

		if (b) {
			List<SystemUser> users = accountDao.listAllSystemUser(false);
			if (users != null && users.size() > 0) {
				for (SystemUser u : users) {
					if (u.getEmail().equalsIgnoreCase(userEmail)) {
						u.removeGroup(groupId);
					}
				}
				accountDao.storeListSystemUserToMemcache(users);
			}
			List<SystemGroup> groups = listSystemGroupFromMemcache();
			if (groups != null && groups.size() > 0) {
				for (SystemGroup g : groups) {
					if (g.getId().equals(groupId)) {
						g.removeUser(userId);
					}
				}
			}
			storeListSystemGroupToMemcache(groups);
		}

		return b;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see cmg.org.monitor.dao.SystemGroupDAO#listAllUserOfGroup(java.lang.String)
	 */
	public List listAllUserOfGroup(String groupName) throws Exception {
		initPersistence();
		Query query = pm.newQuery(SystemGroup.class);
		query.setFilter("name == paraName");
		query.declareParameters("String paraName");
		SystemGroup group = null;
		try {
			List<SystemGroup> temp = (List<SystemGroup>) query
					.execute(groupName);
			if (!temp.isEmpty()) {
				group = temp.get(0);
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE,
					" ERROR when getAllGroup. Message: " + e.getMessage());
			throw e;
		} finally {
			pm.close();
		}
		if (group != null) {
			SystemAccountDAO accountDao = new SystemAccountDaoImpl();
			List<String> userIds = group.getUserIDs();
			if (userIds != null && userIds.size() > 0) {
				List<SystemUser> users = new ArrayList<SystemUser>();
				for (String userId : userIds) {
					SystemUser user = accountDao.getSystemUserById(userId);
					users.add(user);
				}
				return users;
			}
		}
		return null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see cmg.org.monitor.dao.SystemGroupDAO#getByName(java.lang.String)
	 */
	public SystemGroup getByName(String name) throws Exception {
		List<SystemGroup> store = listSystemGroupFromMemcache();
		if (store != null && store.size() > 0) {
			for (SystemGroup g : store) {
				if (g.getName().equalsIgnoreCase(name)) {
					return g;
				}
			}
			return null;
		} else {
			initSystemGroupMemcache();
			initPersistence();
			Query query = pm.newQuery(SystemGroup.class);
			query.setFilter("name == paraName");
			query.declareParameters("String paraName");
			try {
				List<SystemGroup> temp = (List<SystemGroup>) query
						.execute(name);
				if (!temp.isEmpty()) {
					return temp.get(0);
				}
			} catch (Exception e) {
				logger.log(Level.SEVERE,
						" ERROR when getByName. Message: " + e.getMessage());
				throw e;
			} finally {
				pm.close();
			}
			return null;
		}
	}

	@Override
	public List<SystemGroup> listSystemGroupFromMemcache() {
		List<SystemGroup> tempOut = new ArrayList<SystemGroup>();
		Gson gson = new Gson();
		Type type = new TypeToken<Collection<SystemGroup>>() {
		}.getType();
		Object obj = MonitorMemcache.get(Key.create(Key.SYSTEM_GROUP));
		if (obj != null && obj instanceof String) {
			try {
				System.out.println("START listSystemGroupFromMemcache");
				tempOut = gson.fromJson(String.valueOf(obj), type);
			} catch (Exception ex) {
				logger.log(Level.WARNING, "Error. Message: " + ex.getMessage());
			}
		}
		return tempOut;
	}

	@Override
	public void storeListSystemGroupToMemcache(List list) {
		Gson gson = new Gson();
		try {
			System.out.println("START storeListSystemGroupToMemcache");
			MonitorMemcache
					.put(Key.create(Key.SYSTEM_GROUP), gson.toJson(list));
		} catch (Exception e) {
			logger.log(Level.WARNING, "Error: " + e.getMessage());
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see cmg.org.monitor.dao.SystemGroupDAO#initSystemGroupMemcache()
	 */
	@Override
	public void initSystemGroupMemcache() {
		initPersistence();
		Query query = pm.newQuery(SystemGroup.class);
		try {
			List<SystemGroup> temp = (List<SystemGroup>) query.execute();
			List<SystemGroup> listTemp = new ArrayList<SystemGroup>();
			if (!temp.isEmpty()) {
				for (SystemGroup g : temp) {
					listTemp.add(g);
				}
			}
			storeListSystemGroupToMemcache(listTemp);
		} catch (Exception e) {
			logger.log(
					Level.SEVERE,
					" ERROR when initSystemGroupMemcache. Message: "
							+ e.getMessage());
		} finally {
			pm.close();
		}
	}

}
