package cmg.org.monitor.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import net.sourceforge.htmlunit.corejs.javascript.ast.ThrowStatement;

import cmg.org.monitor.dao.SystemDAO;
import cmg.org.monitor.entity.shared.ChangeLogMonitor;
import cmg.org.monitor.entity.shared.CounterChangeLog;
import cmg.org.monitor.entity.shared.NotifyMonitor;
import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.ext.model.shared.UserLoginDto;
import cmg.org.monitor.memcache.Key;
import cmg.org.monitor.memcache.MonitorMemcache;
import cmg.org.monitor.services.MonitorLoginService;
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

		ChangeLogMonitor clm = new ChangeLogMonitor();
		UserLoginDto user = MonitorLoginService.getUserLogin();
		clm.setUsername(user.getEmail());
		clm.setSystemName(sys.toString());
		clm.setDescription((sys.isDeleted() ? "Delete system "
				: ("Update information of ")) + sys.getName());
		clm.setType(sys.isDeleted() ? ChangeLogMonitor.LOG_DELETE
				: ChangeLogMonitor.LOG_UPDATE);
		clm.setDatetime(new Date());
		clm.setSid(sys.getCode());
		addChangeLog(clm);
		setNotifyOption(sys.getCode(), sys.getNotify());
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
		ChangeLogMonitor clm = new ChangeLogMonitor();
		UserLoginDto user = MonitorLoginService.getUserLogin();
		clm.setUsername(user.getEmail());
		clm.setSystemName(code + " - " + system.getName());
		clm.setSid(code);
		clm.setDescription("Add new System Monitor : " + system.getName());
		Date date = new Date();
		clm.setDatetime(date);
		clm.setType(ChangeLogMonitor.LOG_ADD);
		addChangeLog(clm);
		setNotifyOption(code, system.getNotify());
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
		temp.setNotify(sys.getNotify());
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

	@Override
	public NotifyMonitor getNotifyOption(String sid) throws Exception {

		initPersistence();
		Query query = pm.newQuery(NotifyMonitor.class);
		query.setFilter("sid == sidPara");
		query.declareParameters("String sidPara");
		NotifyMonitor nm = null;
		List<NotifyMonitor> temp = null;
		try {
			pm.currentTransaction().begin();
			temp = (List<NotifyMonitor>) query.execute(sid);
			if (temp != null && temp.size() > 0) {
				nm = new NotifyMonitor();
				nm.swapValue(temp.get(0));
			}
			pm.currentTransaction().commit();
		} catch (Exception e) {
			logger.log(Level.SEVERE, " ERROR when get NOTIFY JDO. Message: "
					+ e.getMessage());
			pm.currentTransaction().rollback();
			throw e;
		} finally {
			query.closeAll();
			pm.close();
		}

		return nm;
	}

	@Override
	public boolean setNotifyOption(String sid, NotifyMonitor notify)
			throws Exception {
		initPersistence();
		boolean check = false;
		SystemDAO sysDAO = new SystemDaoImpl();
		NotifyMonitor temp = sysDAO.getNotifyOption(sid);
		try {
			pm.currentTransaction().begin();
			if (temp != null) {
				NotifyMonitor nm = pm.getObjectById(NotifyMonitor.class,
						temp.getId());
				nm.setJVM(notify.isJVM());
				nm.setNotifyCpu(notify.isNotifyCpu());
				nm.setNotifyMemory(notify.isNotifyMemory());
				nm.setNotifyServices(notify.isNotifyServices());
				nm.setNotifyServicesConnection(notify
						.isNotifyServicesConnection());
				pm.makePersistent(nm);
				pm.currentTransaction().commit();
				check = true;
			} else {
				notify.setSid(sid);
				NotifyMonitor nm = new NotifyMonitor();
				nm.swapValue(notify);
				pm.makePersistent(nm);
				pm.currentTransaction().commit();
				check = true;
			}

		} catch (Exception e) {
			logger.log(Level.SEVERE, " ERROR when update NOTIFY JDO. Message: "
					+ e.getMessage());
			pm.currentTransaction().rollback();
			throw e;
		} finally {
			pm.close();
		}
		return check;
	}

	@Override
	public boolean addChangeLog(ChangeLogMonitor log) throws Exception {
		initPersistence();
		boolean check = false;
		try {
			pm.currentTransaction().begin();
			pm.makePersistent(log);
			pm.currentTransaction().commit();
			check = true;
		} catch (Exception e) {
			logger.log(Level.SEVERE, " ERROR when add ChangeLog JDO. Message: "
					+ e.getMessage());
			pm.currentTransaction().rollback();
			throw e;
		} finally {
			pm.close();
		}
		// add to memcache
		ArrayList<ChangeLogMonitor> changelogs = null;
		Object obj = MonitorMemcache.get(Key.create(Key.CHANGE_LOG,
				log.getSid()));
		if (obj != null && obj instanceof ArrayList<?>) {
			changelogs = (ArrayList<ChangeLogMonitor>) obj;
			changelogs.add(0, log);
			MonitorMemcache.put(Key.create(Key.CHANGE_LOG, log.getSid()),
					changelogs);
		}

		obj = MonitorMemcache.get(Key.create(Key.CHANGE_LOG));
		if (obj != null && obj instanceof ArrayList<?>) {
			changelogs = (ArrayList<ChangeLogMonitor>) obj;
			changelogs.add(0, log);
			MonitorMemcache.put(Key.create(Key.CHANGE_LOG), changelogs);
		}

		int countAll = getCountAllChangeLog();
		MonitorMemcache.put(Key.create(Key.CHANGE_LOG_COUNT), new Integer(
				countAll + 1));
		setCountChangeLog(log.getSid());

		return check;
	}

	@Override
	public ArrayList<ChangeLogMonitor> listChangeLog(String sid, int start,
			int end) throws Exception {
		ArrayList<ChangeLogMonitor> list = null;
		// get list from memcache
		Object obj = MonitorMemcache.get((sid == null || sid.equals("")) ? Key
				.create(Key.CHANGE_LOG) : Key.create(Key.CHANGE_LOG, sid));
		if (obj != null && obj instanceof ArrayList<?>) {
			ArrayList<ChangeLogMonitor> temp = (ArrayList<ChangeLogMonitor>) obj;
			if (temp.size() > start) {
				if (end > temp.size()) {
					end = temp.size();
				}
				list = new ArrayList<ChangeLogMonitor>();
				for (int i = 0; i < end - start; i++) {
					list.add(temp.get(i + start));
				}
			}
		}
		// try to load from JDO if no list found
		if (list == null) {
			initPersistence();
			Query query = pm.newQuery(ChangeLogMonitor.class);
			if (sid != null && sid.trim().length() > 0) {
				query.setFilter("sid == sidPara");
				query.declareParameters("String sidPara");
			}
			query.setOrdering("datetime desc");
			// query.setRange(start, end);
			List<ChangeLogMonitor> temp = null;
			try {
				pm.currentTransaction().begin();
				if (sid != null && sid.trim().length() > 0) {
					temp = (List<ChangeLogMonitor>) query.execute(sid);
				} else {
					temp = (List<ChangeLogMonitor>) query.execute();
				}
				if (temp != null && temp.size() > 0) {
					list = new ArrayList<ChangeLogMonitor>();
					for (int i = 0; i < temp.size(); i++) {
						list.add(temp.get(i));
					}
				}
				pm.currentTransaction().commit();
			} catch (Exception e) {
				logger.log(
						Level.SEVERE,
						" ERROR when get ChangeLog JDO. Message: "
								+ e.getMessage());
				pm.currentTransaction().rollback();
				throw e;
			} finally {
				query.closeAll();
				pm.close();
			}			
			temp = list;
			if (temp != null) {
				MonitorMemcache.put(
						(sid == null || sid.equals("")) ? Key
								.create(Key.CHANGE_LOG) : Key.create(
								Key.CHANGE_LOG, sid), list);
				if (temp.size() > start) {
					if (end > temp.size()) {
						end = temp.size();
					}
					list = new ArrayList<ChangeLogMonitor>();
					for (int i = 0; i < end - start; i++) {
						list.add(temp.get(i + start));
					}
				}
			}
		}
		return list;
	}

	@Override
	public int getCounterChangeLog(String sid) throws Exception {
		initPersistence();
		int count = -1;
		Object obj = null;
		if (sid == null || sid.trim().length() == 0) {
			count = getCountAllChangeLog();
		} else {
			obj = MonitorMemcache.get(Key.create(Key.CHANGE_LOG_COUNT, sid));
			if (obj != null && obj instanceof Integer) {
				count = (Integer) obj;
			} else {
				Query query = pm.newQuery(CounterChangeLog.class);
				query.setFilter("sid == sidPara");
				query.declareParameters("String sidPara");
				List<CounterChangeLog> temp = null;
				try {
					pm.currentTransaction().begin();
					temp = (List<CounterChangeLog>) query.execute(sid);
					if (temp != null && temp.size() > 0) {
						count = temp.get(0).getCount();
					}
					pm.currentTransaction().commit();
				} catch (Exception e) {
					logger.log(
							Level.SEVERE,
							" ERROR when get CountChangeLog JDO. Message: "
									+ e.getMessage());
					pm.currentTransaction().rollback();
					throw e;
				}
				MonitorMemcache.put(Key.create(Key.CHANGE_LOG_COUNT, sid),
						new Integer(count));
			}
		}
		return count;
	}

	public int getCountAllChangeLog() throws Exception {
		int count = 0;
		Object obj = MonitorMemcache.get(Key.create(Key.CHANGE_LOG_COUNT));
		if (obj != null && obj instanceof Integer) {
			count = (Integer) obj;
		} else {
			initPersistence();
			List<Integer> counterAll = null;
			Query q = pm.newQuery("select count from "
					+ CounterChangeLog.class.getName());
			try {
				pm.currentTransaction().begin();
				counterAll = (List<Integer>) q.execute();
				if (counterAll != null && counterAll.size() > 0) {
					for (int i = 0; i < counterAll.size(); i++) {
						count = count + counterAll.get(i);
					}
				}
				pm.currentTransaction().commit();
			} catch (Exception e) {
				logger.log(
						Level.SEVERE,
						" ERROR when get allCounter JDO. Message: "
								+ e.getMessage());
				pm.currentTransaction().rollback();
				throw e;
			} finally {
				q.closeAll();
				pm.close();
			}
			MonitorMemcache.put(Key.create(Key.CHANGE_LOG_COUNT), new Integer(
					count));
		}

		return count;
	}

	public boolean setCountChangeLog(String sid) throws Exception {
		initPersistence();
		boolean check = false;
		Query query = pm.newQuery(CounterChangeLog.class);
		query.setFilter("sid == sidPara");
		query.declareParameters("String sidPara");
		List<CounterChangeLog> temp = null;
		try {
			temp = (List<CounterChangeLog>) query.execute(sid);
			if (temp != null && temp.size() > 0) {
				check = setCounterChangeLogByID(sid, temp.get(0).getId());
			} else {
				pm.currentTransaction().begin();
				CounterChangeLog ccl = new CounterChangeLog();
				ccl.setSid(sid);
				ccl.setCount(1);
				pm.makePersistent(ccl);
				pm.currentTransaction().commit();
				check = true;
				MonitorMemcache.put(Key.create(Key.CHANGE_LOG_COUNT, sid), 1);
				pm.close();
			}
		} catch (Exception e) {
			logger.log(
					Level.SEVERE,
					" ERROR when update CounterChangeLog JDO. Message: "
							+ e.getMessage());
			throw e;
		}

		return check;
	}

	public boolean setCounterChangeLogByID(String sid, String c_id)
			throws Exception {
		initPersistence();
		CounterChangeLog ccl = null;
		boolean check = false;
		try {
			pm.currentTransaction().begin();
			ccl = pm.getObjectById(CounterChangeLog.class, c_id);
			ccl.setCount(ccl.getCount() + 1);
			pm.currentTransaction().commit();
			check = true;
			MonitorMemcache.put(Key.create(Key.CHANGE_LOG_COUNT, sid),
					ccl.getCount());
		} catch (Exception e) {
			pm.currentTransaction().rollback();
			throw e;
		} finally {
			pm.close();
		}

		return check;
	}

	@Override
	public boolean updateSystem(SystemMonitor system) throws Exception {
		initPersistence();
		boolean check = false;
		try {
			pm.currentTransaction().begin();
			pm.makePersistent(system);
			pm.currentTransaction().commit();
			check = true;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, " ERROR when update system JDO. Message: "
					+ ex.getMessage());
			pm.currentTransaction().rollback();
			throw ex;
		} finally {
			pm.close();			
		}
		return check;
	}

}
