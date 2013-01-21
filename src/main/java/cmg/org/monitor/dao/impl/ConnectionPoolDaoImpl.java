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
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import cmg.org.monitor.dao.ConnectionPoolDAO;
import cmg.org.monitor.entity.shared.ConnectionPool;
import cmg.org.monitor.entity.shared.ServiceMonitor;
import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.memcache.Key;
import cmg.org.monitor.memcache.MonitorMemcache;

/** 
 * DOCME
 * 
 * @Creator Hai Lu
 * @author $Author$
 * @version $Revision$
 * @Last changed: $LastChangedDate$
 */

public class ConnectionPoolDaoImpl implements ConnectionPoolDAO {
	private static final Logger logger = Logger.getLogger(ConnectionPoolDaoImpl.class
			.getCanonicalName());
	/**
	 * (non-Javadoc)
	 * @see cmg.org.monitor.dao.ConnectionPoolDAO#storePools(cmg.org.monitor.entity.shared.SystemMonitor, java.util.ArrayList) 
	 */
	@Override
	public void storePools(SystemMonitor sys, ArrayList connectionPools) throws Exception {
			if (connectionPools != null && connectionPools.size() > 0) {			
			Gson gson = new Gson();
			try {
			MonitorMemcache.put(Key.create(Key.CONNECTION_POOL_STORE, sys.getId()),
					gson.toJson(connectionPools));
			} catch (Exception ex) {
				logger.log(Level.WARNING, "Error:" + ex.getMessage());
			}
		}
	}

	/**
	 * (non-Javadoc)
	 * @see cmg.org.monitor.dao.ConnectionPoolDAO#getPools(cmg.org.monitor.entity.shared.SystemMonitor) 
	 */
	@Override
	public ArrayList<ConnectionPool> getPools(SystemMonitor sys) throws Exception {
		ArrayList<ConnectionPool> list = null;
		Object obj = MonitorMemcache.get(Key.create(Key.CONNECTION_POOL_STORE,
				sys.getId()));
		if (obj != null && obj instanceof String) {
			Gson gson = new Gson();
			Type type = new TypeToken<Collection<ConnectionPool>>() {
			}.getType();
			try {
				list = (ArrayList<ConnectionPool>) gson.fromJson(String.valueOf(obj), type);
			} catch (Exception ex) {
				logger.log(Level.WARNING, " -> ERROR: "
						+ ex.getMessage());
			}
		}
		
		return list;
	}

}
