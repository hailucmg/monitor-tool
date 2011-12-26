package cmg.org.monitor.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import cmg.org.monitor.dao.SystemDAO;
import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.memcache.Key;
import cmg.org.monitor.memcache.MonitorMemcache;
import cmg.org.monitor.util.shared.PMF;

public class SystemDaoImpl implements SystemDAO {
	private static final Logger logger = Logger.getLogger(SystemDaoImpl.class
			.getCanonicalName());
	PersistenceManager pm;

	void initPersistence() {
		if (pm == null || pm.isClosed()) {
			pm = PMF.get().getPersistenceManager();
		}
	}

	@Override
	public String[] remoteURLs() throws Exception {
		initPersistence();
		String[] remoteURLs = null;
		List<String> list;
		Query q = pm.newQuery("select remoteUrl from "
				+ SystemMonitor.class.getName());
		try {
			pm.currentTransaction().begin();
			list = (List<String>) q.execute();
			if (list != null || list.size() > 0) {
				remoteURLs = new String[list.size()];
				for (int i = 0; i < list.size(); i++) {
					remoteURLs[i] = list.get(i);
				}
			}
			pm.currentTransaction().commit();
		} catch (Exception e) {
			throw e;
		} finally {
			q.closeAll();
			pm.close();
		}
		return remoteURLs;
	}

	@Override
	public ArrayList<SystemMonitor> listSystems(boolean isDeleted)
			throws Exception {
		initPersistence();
		ArrayList<SystemMonitor> systems = null;

		if (pm == null || pm.isClosed()) {
			pm = PMF.get().getPersistenceManager();
		}
		List<SystemMonitor> listData = null;
		Query query = pm.newQuery(SystemMonitor.class);
		query.setFilter("isDeleted == isDeletedPara");
		query.declareParameters("boolean isDeletedPara");
		try {
			pm.currentTransaction().begin();
			listData = (List<SystemMonitor>) query.execute(isDeleted);
			if (listData.size() > 0) {
				systems = new ArrayList<SystemMonitor>();
				for (SystemMonitor sys : listData) {
					systems.add(sys);
				}
			}
			pm.currentTransaction().commit();
		} catch (Exception ex) {
			throw ex;
		} finally {
			query.closeAll();
			pm.close();
		}
		return systems;
	}

	@Override
	public boolean removeSystem(SystemMonitor system) throws Exception {
		initPersistence();
		boolean check = false;
		try {
			pm.currentTransaction().begin();
			system = pm.getObjectById(SystemMonitor.class, system.getId());
			pm.deletePersistent(system);
			pm.currentTransaction().commit();
			check = true;
		} catch (Exception ex) {
			pm.currentTransaction().rollback();
			throw ex;
		} finally {
			pm.close();
		}
		return check;
	}

	@Override
	public boolean updateSystem(SystemMonitor sys) throws Exception {
		initPersistence();
		boolean check = false;
		try {
			pm.currentTransaction().begin();
			pm.makePersistent(sys);
			pm.currentTransaction().commit();
			check = true;
		} catch (Exception ex) {
			pm.currentTransaction().rollback();
			throw ex;
		} finally {
			pm.close();
		}
		return check;
	}

	@Override
	public SystemMonitor getSystemById(String id) throws Exception {
		initPersistence();
		SystemMonitor system;
		try {
			pm.currentTransaction().begin();
			system = pm.getObjectById(SystemMonitor.class, id);
			pm.currentTransaction().commit();
		} catch (Exception e) {
			pm.currentTransaction().rollback();
			throw e;
		} finally {
			pm.close();
		}
		return system;
	}

	@Override
	public boolean addSystem(SystemMonitor system) throws Exception {
		initPersistence();
		boolean check = false;
		try {
			pm.currentTransaction().begin();
			pm.makePersistent(system);
			pm.currentTransaction().commit();
			check = true;
		} catch (Exception e) {
			throw e;
		} finally {
			pm.close();
		}
		return check;
	}

	@Override
	public boolean deleteSystem(SystemMonitor sys) throws Exception {
		initPersistence();
		boolean check = false;
		try {
			pm.currentTransaction().begin();
			sys.setDeleted(true);
			sys.setActive(false);
			pm.makePersistent(sys);
			pm.currentTransaction().commit();
			check = true;
		} catch (Exception e) {
			throw e;
		} finally {
			pm.close();
		}
		return check;
	}

	public String createSID() throws Exception {
		initPersistence();
		String SID = null;
		String[] names = null;
		List<String> list;
		Query q = pm.newQuery("select name from "
				+ SystemMonitor.class.getName());
		try {
			pm.currentTransaction().begin();
			list = (List<String>) q.execute();
			if (list.size() == 0) {
				SID = "S001";
			} else {
				names = new String[list.size()];
				for (int i = 0; i < list.size(); i++) {
					names[i] = list.get(i);
				}
				if (names.length > 0 && names.length < 9) {
					int number = names.length + 1;
					SID = "S00" + number;
				} else if (names.length >= 9 && names.length < 98) {
					int number = names.length + 1;
					SID = "S0" + number;
				} else if (names.length >= 98 && names.length < 998) {
					int number = names.length + 1;
					SID = "S" + number;
				}
			}
			pm.currentTransaction().commit();
		} catch (Exception e) {
			throw e;
		} finally {
			q.closeAll();
			pm.close();
		}
		return SID;
	}

	@Override
	public ArrayList<SystemMonitor> listSystemsFromMemcache(boolean isDeleted) {
		ArrayList<SystemMonitor> list = null;
		Object obj = MonitorMemcache.get(Key.create(Key.SYSTEM_MONITOR_STORE));
		if (obj != null) {
			if (obj instanceof ArrayList<?>) {
				try {
					list = (ArrayList<SystemMonitor>) obj;
				} catch (Exception ex) {
					// do nothing
				}
			}
		}
		// try to load from JDO if list is null
		if (list == null) {
			try {
				list = listSystems(isDeleted);
			} catch (Exception ex) {
				logger.log(Level.WARNING, "ERROR: " + ex.fillInStackTrace().toString());
			}
		}
		return list;
	}

}
