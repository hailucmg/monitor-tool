package cmg.org.monitor.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import cmg.org.monitor.dao.AlertDao;
import cmg.org.monitor.entity.shared.AlertMonitor;
import cmg.org.monitor.entity.shared.AlertStoreMonitor;
import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.ext.util.MonitorUtil;
import cmg.org.monitor.memcache.Key;
import cmg.org.monitor.memcache.MonitorMemcache;
import cmg.org.monitor.util.shared.MonitorConstant;
import cmg.org.monitor.util.shared.PMF;

public class AlertDaoImpl implements AlertDao {

	private static final Logger logger = Logger.getLogger(AlertDaoImpl.class
			.getCanonicalName());

	@Override
	public void storeAlert(SystemMonitor sys, AlertMonitor alert) {
		if (alert != null) {
			// BEGIN LOG
			long start = System.currentTimeMillis();
			logger.log(Level.INFO,
					MonitorUtil.parseTime(start, true) + sys.toString()
							+ " -> START: put AlertMonitor ... " + alert);
			// BEGIN LOG
			AlertStoreMonitor store = getTempStore(sys.getId());
			if (store == null) {
				// if is a first time create new object
				store = new AlertStoreMonitor();
				store.setCpuUsage(sys.getLastestCpuUsage());
				store.setMemUsage(sys.getLastestMemoryUsage());
				store.setSysId(sys.getId());
				store.setTimeStamp(sys.getTimeStamp());
			}

			store.addAlert(alert);
			// put store to temp memcache
			storeTemp(store);
			// END LOG
			long end = System.currentTimeMillis();
			logger.log(Level.INFO,
					MonitorUtil.parseTime(end, true) + sys.toString()
							+ " -> END: put AlertMonitor. Time executed: "
							+ (end - start) + " ms.");
			// END LOG
		}

	}

	@Override
	public ArrayList<AlertStoreMonitor> listAlertStore(String sysId) {
		// BEGIN LOG
		long start = System.currentTimeMillis();
		logger.log(Level.INFO, MonitorUtil.parseTime(start, true)
				+ "START: list Alert store ... ");
		// BEGIN LOG
		ArrayList<AlertStoreMonitor> list = null;
		Object obj = MonitorMemcache.get(Key.create(Key.ALERT_STORE, sysId));
		if (obj != null) {
			if (obj instanceof ArrayList<?>) {
				try {
					list = (ArrayList<AlertStoreMonitor>) obj;
					if (list != null) {
						logger.log(Level.INFO,
								"List Alert Store from Memcache. Size: "
										+ Integer.toString(list.size()));
					}// if
				} catch (Exception ex) {
					logger.log(Level.WARNING, " -> ERROR: "
							+ ex.fillInStackTrace().toString());
				}
			}// if
		}// if
			// read store from JDO if memcahe is null
		if (list == null) {
			PersistenceManager pm = PMF.get().getPersistenceManager();
			Query query = pm.newQuery(AlertStoreMonitor.class);
			query.setFilter("sysId == systemId");
			query.declareParameters("String systemId");
			query.setRange(0, MonitorConstant.STATISTIC_HISTORY_LENGTH);
			List<AlertStoreMonitor> listData = null;
			try {
				pm.currentTransaction().begin();
				listData = (List<AlertStoreMonitor>) query.execute(sysId);
				if (listData != null && listData.size() > 0) {
					list = new ArrayList<AlertStoreMonitor>();
					for (AlertStoreMonitor alertStore : listData) {
						list.add(alertStore);
					}// for
				}// if
				pm.currentTransaction().commit();
				logger.log(
						Level.INFO,
						"List Alert Store from JDO. Size: "
								+ ((list == null) ? "null" : Integer
										.toString(list.size())));
			} catch (Exception ex) {
				logger.log(Level.WARNING, " -> ERROR: "
						+ ex.fillInStackTrace().toString());
			} finally {
				query.closeAll();
				pm.close();
			}
		}// if
			// END LOG
		long end = System.currentTimeMillis();
		logger.log(Level.INFO, MonitorUtil.parseTime(end, true)
				+ "END: list Alert store. Time executed: " + (end - start)
				+ " ms.");
		// END LOG
		return list;
	}

	public void putAlertStore(AlertStoreMonitor store) {
		if (store != null) {
			// BEGIN LOG
			long start = System.currentTimeMillis();
			logger.log(Level.INFO, MonitorUtil.parseTime(start, true)
					+ "START: put Alert store ... " + store);
			// BEGIN LOG
			ArrayList<AlertStoreMonitor> list = listAlertStore(store.getSysId());
			if (list == null) {
				list = new ArrayList<AlertStoreMonitor>();
			}
			logger.log(Level.INFO,
					"Start put to memcache. List size: " + list.size());

			list.add(store);
			if (list.size() > MonitorConstant.STATISTIC_HISTORY_LENGTH) {
				list.remove(0);
			}
			// Store to memcahe
			MonitorMemcache.put(Key.create(Key.ALERT_STORE, store.getSysId()),
					list);

			logger.log(Level.INFO,
					"End put to memcache. List size: " + list.size());

			// Store to JDO
			PersistenceManager pm = PMF.get().getPersistenceManager();
			try {
				pm.currentTransaction().begin();
				pm.makePersistent(store);
				pm.currentTransaction().commit();
				logger.log(Level.INFO, "Put to JDO sucessfully!");
			} catch (Exception ex) {
				pm.currentTransaction().rollback();

				logger.log(Level.WARNING, " -> ERROR: "
						+ ex.fillInStackTrace().toString());
			} finally {
				pm.close();
			}

			// END LOG
			long end = System.currentTimeMillis();
			logger.log(Level.INFO, MonitorUtil.parseTime(end, true)
					+ "END: put Alert store. Time executed: " + (end - start)
					+ " ms.");
			// END LOG
		}
	}

	@Override
	public void clearTempStore(SystemMonitor sys) {
		logger.log(Level.INFO, sys.toString() + " -> Clear Alert store ...");
		MonitorMemcache.delete(Key.create(Key.ALERT_TEMP_STORE, sys.getId()));
	}

	public AlertStoreMonitor getTempStore(String sid) {
		AlertStoreMonitor store = null;
		Object obj = MonitorMemcache.get(Key.create(Key.ALERT_TEMP_STORE, sid));
		if (obj != null) {
			if (obj instanceof AlertStoreMonitor) {
				try {
					store = (AlertStoreMonitor) obj;
				} catch (Exception ex) {
					logger.log(Level.INFO, " -> ERROR: "
							+ ex.fillInStackTrace().toString());
				}
			}
		}
		return store;
	}

	public void storeTemp(AlertStoreMonitor store) {
		if (store != null) {
			logger.log(Level.INFO, "Put AlertStore to memcahe ... " + store);
			MonitorMemcache.put(
					Key.create(Key.ALERT_TEMP_STORE, store.getSysId()), store);
		}
	}

	@Override
	public AlertStoreMonitor getLastestAlertStore(SystemMonitor sys) {
		return getTempStore(sys.getId());
	}

}
