package cmg.org.monitor.dao.impl;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import cmg.org.monitor.dao.ServiceDAO;
import cmg.org.monitor.entity.shared.MemoryMonitor;
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
			ArrayList services) {
		if (services != null && services.size() > 0) {
			
			Gson gson = new Gson();
			try {
			MonitorMemcache.put(Key.create(Key.SERVICE_STORE, sys.getId()),
					gson.toJson(services));
			} catch (Exception ex) {
				logger.log(Level.WARNING, "Error:" + ex.getMessage());
			}
		}
	}

	@Override
	public ArrayList<ServiceMonitor> listService(SystemMonitor sys) {
		ArrayList<ServiceMonitor> list = null;
		Object obj = MonitorMemcache.get(Key.create(Key.SERVICE_STORE,
				sys.getId()));
		if (obj != null && obj instanceof String) {
			Gson gson = new Gson();
			Type type = new TypeToken<Collection<ServiceMonitor>>() {
			}.getType();
			try {
				list = (ArrayList<ServiceMonitor>) gson.fromJson(String.valueOf(obj), type);
			} catch (Exception ex) {
				logger.log(Level.WARNING, " -> ERROR: "
						+ ex.getMessage());
			}
		}
		
		return list;
	}

}
