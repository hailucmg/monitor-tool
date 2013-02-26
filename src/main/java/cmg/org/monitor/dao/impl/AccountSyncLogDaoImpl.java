/**
 * Copyright (c) CMG Ltd All rights reserved.
 *
 * This software is the confidential and proprietary information of CMG
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with CMG.
 */

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

import cmg.org.monitor.dao.AccountSyncLogDAO;
import cmg.org.monitor.dao.SystemAccountDAO;
import cmg.org.monitor.entity.shared.AccountSyncLog;
import cmg.org.monitor.entity.shared.GoogleAccount;
import cmg.org.monitor.entity.shared.SystemGroup;
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

public class AccountSyncLogDaoImpl implements AccountSyncLogDAO {
	private static final Logger logger = Logger
			.getLogger(AccountSyncLogDaoImpl.class.getCanonicalName());
	PersistenceManager pm;

	void initPersistence() {
		if (pm == null || pm.isClosed()) {
			pm = PMF.get().getPersistenceManager();
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see cmg.org.monitor.dao.AccountSyncLogDAO#createLog(cmg.org.monitor.entity.shared.AccountSyncLog)
	 */
	public boolean createLog(AccountSyncLog log) throws Exception {
		boolean check = false;
		initPersistence();
		try {
			pm.currentTransaction().begin();
			pm.makePersistent(log);
			pm.currentTransaction().commit();
			check = true;
		} catch (Exception ex) {
			logger.log(Level.SEVERE,
					" ERROR when createLog. Message: " + ex.getMessage());
			pm.currentTransaction().rollback();
			throw ex;
		} finally {
			pm.close();
		}
		if (check) {
			List<AccountSyncLog> list = listAccountSyncLogFromMemcahce(log.getAdminAccount());
			list.add(log);
			if (list.size() > 200) {
				list.remove(0);
			}
			storeListAccountSyncLogToMemcache(list, log.getAdminAccount());
		}
		return check;
	}

	private void storeListAccountSyncLogToMemcache(List<AccountSyncLog> list,
			String adminAccount) {
		Gson gson = new Gson();
		try {
			MonitorMemcache.put(Key.create(Key.ACCOUNT_SYNC_LOG, adminAccount),
					gson.toJson(list));
		} catch (Exception ex) {
			logger.log(Level.SEVERE, "Error: " + ex.getMessage());
		}
	}

	private List<AccountSyncLog> listAccountSyncLogFromMemcahce(
			String adminAccount) {
		List<AccountSyncLog> tempOut = new ArrayList<AccountSyncLog>();
		Gson gson = new Gson();
		Type type = new TypeToken<Collection<AccountSyncLog>>() {
		}.getType();
		Object obj = MonitorMemcache.get(Key.create(Key.ACCOUNT_SYNC_LOG,
				adminAccount));
		if (obj != null && obj instanceof String) {
			try {
				System.out.println("START listAccountSyncLogFromMemcahce");
				tempOut = gson.fromJson(String.valueOf(obj), type);
			} catch (Exception ex) {
				logger.log(Level.WARNING, "Error. Message: " + ex.getMessage());
			}
		}
		return tempOut;
	}

	public void initAccountSyncLogStore() {
		SystemAccountDAO accountDao = new SystemAccountDaoImpl();
		List<GoogleAccount> list = null;
		try {
			list = accountDao.listAllGoogleAccount();
		} catch (Exception e1) {
			//
		}
		if (list != null && list.size() > 0) {
			initPersistence();
			Query query = pm.newQuery(AccountSyncLog.class);
			query.setRange(0, 500);
			query.setOrdering("timestamp desc");
			try {
				List<AccountSyncLog> temp = (List<AccountSyncLog>) query
						.execute();				
				for (GoogleAccount acc : list) {
					List<AccountSyncLog> listTemp = new ArrayList<AccountSyncLog>();
					if (!temp.isEmpty()) {
						for (int i = temp.size() -1; i >= 0; i--) {
							if (temp.get(i).getAdminAccount().equalsIgnoreCase(acc.getUsername() + "@" + acc.getDomain())) {
								listTemp.add(temp.get(i));
							}
						}
					}
					storeListAccountSyncLogToMemcache(listTemp, acc.getUsername() + "@" + acc.getDomain());
				}
			} catch (Exception e) {
				logger.log(
						Level.SEVERE,
						" ERROR when initAccountSyncLogStore. Message: "
								+ e.getMessage());
			} finally {
				pm.close();
			}
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see cmg.org.monitor.dao.AccountSyncLogDAO#getLastestLog(java.lang.String)
	 */
	public AccountSyncLog getLastestLog(String adminAccount) throws Exception {
		List<AccountSyncLog> store = listAccountSyncLogFromMemcahce(adminAccount);
		if (store != null && store.size() > 0) {
			return store.get(store.size() - 1);
		} else {
			initAccountSyncLogStore();
			store = listAccountSyncLogFromMemcahce(adminAccount);
			if (store != null && store.size() > 0) {
				return store.get(store.size() - 1);
			}
		}
		return null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see cmg.org.monitor.dao.AccountSyncLogDAO#listLog(java.lang.String, int)
	 */
	public List<AccountSyncLog> listLog(String adminAccount, int len)
			throws Exception {
		List<AccountSyncLog> store = listAccountSyncLogFromMemcahce(adminAccount);
		if (store == null || store.isEmpty()) {
			initAccountSyncLogStore();
			store = listAccountSyncLogFromMemcahce(adminAccount);
		}
		if (store != null && store.size() > 0) {			
			if (store.size() <= len) {
				return store;
			} else {
				List<AccountSyncLog> temp = new ArrayList<AccountSyncLog>();
				for (int i = store.size() -len; i > (store.size() - 1); i++) {
					temp.add(store.get(i));
				}
				return temp;
			}
		}
		return null;
	}

}
