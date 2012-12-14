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

import cmg.org.monitor.dao.AlertDao;
import cmg.org.monitor.entity.shared.AlertMonitor;
import cmg.org.monitor.entity.shared.AlertStoreMonitor;
import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.ext.util.MonitorUtil;
import cmg.org.monitor.memcache.Key;
import cmg.org.monitor.memcache.MonitorMemcache;
import cmg.org.monitor.services.PMF;
import cmg.org.monitor.util.shared.MonitorConstant;

public class AlertDaoImpl implements AlertDao {

	private static final Logger logger = Logger.getLogger(AlertDaoImpl.class.getCanonicalName());

	@Override
	public void storeAlert(SystemMonitor sys, AlertMonitor alert) {
		if (alert != null) {
			// BEGIN LOG
			long start = System.currentTimeMillis();
			// BEGIN LOG
			AlertStoreMonitor store = getTempStore(sys.getId());
			if (store == null) {
				// if is a first time create new object
				store = new AlertStoreMonitor();
				store.setName(MonitorConstant.ALERTSTORE_DEFAULT_NAME + ": " + MonitorUtil.parseTime(start, false));
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
			// END LOG
		}

	}

	@Override
	public ArrayList<AlertStoreMonitor> listAlertStore(String sysId) {
		// BEGIN LOG
		long start = System.currentTimeMillis();
		// BEGIN LOG
		ArrayList<AlertStoreMonitor> list = null;
		Object obj = MonitorMemcache.get(Key.create(Key.ALERT_STORE, sysId));
		if (obj != null && obj instanceof String) {
			Gson gson = new Gson();
			Type type = new TypeToken<Collection<AlertStoreMonitor>>() {
			}.getType();
			try {
				ArrayList<AlertStoreMonitor> listData = (ArrayList<AlertStoreMonitor>) gson.fromJson(String.valueOf(obj), type);
				if (listData != null && listData.size() > 0) {
					list = new ArrayList<AlertStoreMonitor>();
					for (AlertStoreMonitor alertStore : listData) {
						ArrayList<AlertMonitor> alerts = alertStore.getAlerts();
						ArrayList<AlertMonitor> tempAlerts = new ArrayList<AlertMonitor>();
						if (alerts != null && alerts.size() > 0) {
							for (AlertMonitor alert : alerts) {
								alert.setAlertStore(null);
								tempAlerts.add(alert);
							}
						}
						alertStore.setAlerts(tempAlerts);
						list.add(alertStore);
					}// for
				}// if
			} catch (Exception ex) {
				logger.log(Level.WARNING, " -> ERROR: " + ex.getMessage());
			}
		}
		boolean isJDO = false;
		// read store from JDO if memcahe is null
		if (list == null) {			
			PersistenceManager pm = PMF.get().getPersistenceManager();
			try {
				Query query = pm.newQuery(AlertStoreMonitor.class);
				query.setFilter("sysId == systemId");
				query.declareParameters("String systemId");
				query.setOrdering("timeStamp desc");
				query.setRange(0, MonitorConstant.STATISTIC_HISTORY_LENGTH);
				List<AlertStoreMonitor> listData = null;
				listData = (List<AlertStoreMonitor>) query.execute(sysId);
				if (listData != null && listData.size() > 0) {
					list = new ArrayList<AlertStoreMonitor>();
					for (AlertStoreMonitor alertStore : listData) {
						list.add(alertStore);
					}// for
				}// if
				isJDO = true;
			} catch (Exception ex) {
				logger.log(Level.SEVERE, " -> ERROR: " + ex.getMessage());
			} finally {
				pm.close();
			}
			
		}// if
		if (isJDO) {			
			if (list != null && list.size() > 0) {
				List<AlertMonitor> listAlerts = null;
				ArrayList<AlertMonitor> alerts = null;
				for (AlertStoreMonitor as : list) {
					if (as.getId() != null) {
						PersistenceManager pm = PMF.get().getPersistenceManager();
						try {
							AlertStoreMonitor s = pm.getObjectById(AlertStoreMonitor.class, as.getId());
							if (s != null) {
								Query q = pm.newQuery(AlertMonitor.class);
								q.setFilter("alertStore == store");
								q.declareParameters("AlertStoreMonitor store");
								
								listAlerts = (List<AlertMonitor>) q.execute(s);
								alerts = new ArrayList<AlertMonitor>();
								if (!listAlerts.isEmpty()) {
									for (AlertMonitor a : listAlerts) {
										alerts.add(a);
									}
								}
								as.setAlerts(alerts.size() > 0 ? alerts : null);
							}
						} catch (Exception ex) {
							logger.log(Level.SEVERE, " -> ERROR: " + ex.getMessage());
						} finally {
							pm.close();
						}
					}
				}

			}
			Gson gson = new Gson();
			MonitorMemcache.put(Key.create(Key.ALERT_STORE, sysId), gson.toJson(list));
		}
		// END LOG
		long end = System.currentTimeMillis();

		return list;
	}

	public void putAlertStore(AlertStoreMonitor store) {
		if (store != null) {
			// BEGIN LOG
			long start = System.currentTimeMillis();

			// BEGIN LOG
			ArrayList<AlertStoreMonitor> list = listAlertStore(store.getSysId());
			if (list == null) {
				list = new ArrayList<AlertStoreMonitor>();
			}

			list.add(store);
			if (list.size() > MonitorConstant.STATISTIC_HISTORY_LENGTH) {
				list.remove(0);
			}
			Gson gson = new Gson();
			// Store to memcahe
			try {
				MonitorMemcache.put(Key.create(Key.ALERT_STORE, store.getSysId()), gson.toJson(list));
			} catch (Exception ex) {
				logger.log(Level.WARNING, "Error:" + ex.getMessage());
			}

			// Store to JDO
			ArrayList<AlertMonitor> alerts = store.getAlerts();
			ArrayList<AlertMonitor> tempAlerts = new ArrayList<AlertMonitor>();
			if (alerts != null && alerts.size() > 0) {
				for (AlertMonitor alert : alerts) {
					alert.setAlertStore(store);
					tempAlerts.add(alert);
				}
			}
			store.setAlerts(tempAlerts);
			PersistenceManager pm = PMF.get().getPersistenceManager();
			try {
				pm.makePersistent(store);
			} catch (Exception ex) {
				logger.log(Level.WARNING, " -> ERROR: " + ex.fillInStackTrace().toString());
			} finally {
				pm.close();
			}

			// END LOG
			long end = System.currentTimeMillis();

			// END LOG
		}
	}

	@Override
	public void clearTempStore(SystemMonitor sys) {

		MonitorMemcache.delete(Key.create(Key.ALERT_TEMP_STORE, sys.getId()));
		try {
			MonitorMemcache.put(Key.create(Key.ALERT_TEMP_STORE, sys.getId()), null);
		} catch (Exception ex) {
			logger.log(Level.WARNING, "Error:" + ex.getMessage());
		}
	}

	public AlertStoreMonitor getTempStore(String sid) {
		Object obj = MonitorMemcache.get(Key.create(Key.ALERT_TEMP_STORE, sid));
		if (obj != null && obj instanceof String) {
			Gson gson = new Gson();
			try {
				AlertStoreMonitor store = gson.fromJson(String.valueOf(obj), AlertStoreMonitor.class);
				ArrayList<AlertMonitor> alerts = store.getAlerts();
				for (AlertMonitor alert : alerts) {
					alert.setAlertStore(null);
				}
				store.setAlerts(alerts);
				return store;
			} catch (Exception ex) {
				logger.log(Level.INFO, "Cast json string. Message:" + ex.getMessage());
			}
		}
		return null;
	}

	public void storeTemp(AlertStoreMonitor store) {
		if (store != null) {
			Gson gson = new Gson();
			try {
				MonitorMemcache.put(Key.create(Key.ALERT_TEMP_STORE, store.getSysId()), gson.toJson(store));
			} catch (Exception ex) {
				logger.log(Level.WARNING, "Error:" + ex.getMessage());
			}
		}
	}

	@Override
	public AlertStoreMonitor getLastestAlertStore(SystemMonitor sys) {
		return getTempStore(sys.getId());
	}

}
