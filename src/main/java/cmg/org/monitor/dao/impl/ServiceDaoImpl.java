package cmg.org.monitor.dao.impl;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import cmg.org.monitor.dao.ServiceDAO;
import cmg.org.monitor.entity.shared.ServiceMonitor;
import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.ext.util.MonitorUtil;
import cmg.org.monitor.memcache.Key;
import cmg.org.monitor.memcache.MonitorMemcache;

public class ServiceDaoImpl implements ServiceDAO {
	private static final Logger logger = Logger.getLogger(ServiceDaoImpl.class
			.getCanonicalName());

	@Override
	public void storeServices(SystemMonitor sys,
			ArrayList<ServiceMonitor> services) {
		if (services != null && services.size() > 0) {
			logger.log(Level.INFO,
					MonitorUtil.parseTime(System.currentTimeMillis(), true)
							+ " -> START Put Services information ... Size: " + services.size());
			for (int i = 0; i < services.size(); i++) {
				logger.log(Level.INFO, "Service #" + (i + 1) + services.get(i));
			}			
			MonitorMemcache.put(Key.create(Key.SERVICE_STORE, sys.getId()),
					services);
		}
	}

	@Override
	public ArrayList<ServiceMonitor> listService(SystemMonitor sys) {
		ArrayList<ServiceMonitor> list = null;
		Object obj = MonitorMemcache.get(Key.create(Key.SERVICE_STORE,
				sys.getId()));
		if (obj != null) {
			if (obj instanceof ArrayList<?>) {
				try {
					list = (ArrayList<ServiceMonitor>) obj;
				} catch (Exception ex) {
					logger.log(Level.WARNING, " -> ERROR: "
							+ ex.fillInStackTrace().toString());
				}
			}
		}
		return list;
	}

}
