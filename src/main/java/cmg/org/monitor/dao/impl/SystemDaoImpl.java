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
import cmg.org.monitor.services.PMF;

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
	public String[] listRemoteURLs() throws Exception {
		String[] remoteURLs = null;
		ArrayList<SystemMonitor> list = listSystemsFromMemcache(false);
		if (list != null && list.size() > 0) {
			remoteURLs = new String[list.size()];
			for (int i = 0; i < list.size(); i++) {
				remoteURLs[i] = list.get(0).getRemoteUrl();
			}
		}
		return remoteURLs;
	}

	@Override
	public ArrayList<SystemMonitor> listSystems(boolean isDeleted)
			throws Exception {
		initPersistence();
		ArrayList<SystemMonitor> systems = null;
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
			logger.log(Level.SEVERE, " ERROR when list systems JDO. Message: "
					+ ex.getMessage());
			pm.currentTransaction().rollback();
			throw ex;
		} finally {
			query.closeAll();
			pm.close();
			storeSysList(systems);
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
			logger.log(Level.SEVERE, " ERROR when remove system JDO. Message: "
					+ ex.getMessage());
			pm.currentTransaction().rollback();
			throw ex;
		} finally {
			pm.close();
		}
		return check;
	}

	@Override
	public boolean updateSystem(SystemMonitor sys, boolean reloadMemcache)
			throws Exception {
		initPersistence();
		boolean check = false;
		try {
			pm.currentTransaction().begin();
			pm.makePersistent(sys);
			pm.currentTransaction().commit();
			check = true;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, " ERROR when update system JDO. Message: "
					+ ex.getMessage());
			pm.currentTransaction().rollback();
			throw ex;
		} finally {
			pm.close();
			if (reloadMemcache) {
				listSystems(false);
			}
		}
		return check;
	}

	@Override
	public SystemMonitor getSystemById(String id) throws Exception {
		initPersistence();
		SystemMonitor system = null;
		ArrayList<SystemMonitor> list = listSystemsFromMemcache(false);
		if (list != null) {
			if (list.size() > 0) {
				for (SystemMonitor sys : list) {
					if (sys.getId().equals(id)) {
						system = sys;
						break;
					}
				}
			}
		}
		return system;
	}

	@Override
	public boolean addSystem(SystemMonitor system, String code)
			throws Exception {
		initPersistence();
		boolean check = false;
		try {
			pm.currentTransaction().begin();
			SystemMonitor tempSys = new SystemMonitor();
			tempSys.swapValue(system);
			tempSys.setCode(code);
			pm.makePersistent(tempSys);
			pm.currentTransaction().commit();
			check = true;
		} catch (Exception e) {
			logger.log(Level.SEVERE, " ERROR when add system JDO. Message: "
					+ e.getMessage());
			pm.currentTransaction().rollback();
		} finally {
			pm.close();
			listSystems(false);
		}
		return check;
	}

	@Override
	public boolean deleteSystem(String sysId) throws Exception {
		SystemMonitor system = getSystemById(sysId);
		system.setDeleted(true);
		system.setActive(false);
		return updateSystem(system, true);
	}

	@Override
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
			pm.currentTransaction().rollback();
			logger.log(Level.SEVERE, "ERROR when create system Code. Message: "
					+ e.getMessage());
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
				logger.log(Level.WARNING, "ERROR: "
						+ ex.fillInStackTrace().toString());
			}
		}
		return list;
	}

	@Override
	public void storeSysList(ArrayList<SystemMonitor> list) {
		MonitorMemcache.put(Key.create(Key.SYSTEM_MONITOR_STORE), list);
	}

	@Override
	public String[] listEmails() {
		String[] emails = null;
		ArrayList<SystemMonitor> list = listSystemsFromMemcache(false);
		if (list != null && list.size() > 0) {
			emails = new String[list.size()];
			for (int i = 0; i < list.size(); i++) {
				emails[i] = list.get(0).getEmailRevice();
			}
		}
		return emails;
	}

	@Override
	public boolean editSystem(SystemMonitor sys) throws Exception {
		SystemMonitor temp = getSystemById(sys.getId());
		temp.setName(sys.getName());
		temp.setUrl(sys.getUrl());
		temp.setProtocol(sys.getProtocol());
		temp.setGroupEmail(sys.getGroupEmail());
		temp.setIp(sys.getIp());
		temp.setEmailRevice(sys.getEmailRevice());
		temp.setRemoteUrl(sys.getRemoteUrl());
		temp.setActive(sys.isActive());
		return updateSystem(temp, true);
	}

	@Override
	public boolean updateStatus(SystemMonitor sys, boolean status,
			String healthStatus) throws Exception {
		SystemMonitor temp = getSystemById(sys.getId());
		temp.setStatus(status);
		temp.setHealthStatus(healthStatus);
		updateSystem(temp, false);
		return false;
	}

}
