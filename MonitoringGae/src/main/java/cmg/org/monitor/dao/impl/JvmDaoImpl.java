package cmg.org.monitor.dao.impl;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import cmg.org.monitor.dao.JvmDAO;
import cmg.org.monitor.entity.shared.JvmMonitor;
import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.ext.util.MonitorUtil;
import cmg.org.monitor.memcache.Key;
import cmg.org.monitor.memcache.MonitorMemcache;
import cmg.org.monitor.util.shared.MonitorConstant;

public class JvmDaoImpl implements JvmDAO {

	/** Default log for application */
	private static final Logger logger = Logger.getLogger(JvmDaoImpl.class
			.getCanonicalName());

	@Override
	public void storeJvm(SystemMonitor sys, JvmMonitor jvm) {
		if (jvm != null) {
			// BEGIN LOG
			long start = System.currentTimeMillis();
		
			// BEGIN LOG

			ArrayList<JvmMonitor> list = listJvm(sys);
			if (list == null) {
				list = new ArrayList<JvmMonitor>();
			}
			list.add(jvm);
			if (list.size() > MonitorConstant.CPU_MEMORY_HISTORY_LENGTH) {
				list.remove(0);
			}
			Gson gson = new Gson();
			try {
			MonitorMemcache.put(Key.create(Key.JVM_STORE, sys.getId()), gson.toJson(list));
			} catch (Exception ex) {
				logger.log(Level.WARNING, "Error:" + ex.getMessage());
			}

		
			// END LOG
			long end = System.currentTimeMillis();
		
			// END LOG
		}
	}

	@Override
	public JvmMonitor getJvm(SystemMonitor sys) {
		ArrayList<JvmMonitor> list = listJvm(sys);
		JvmMonitor jvm = null;
		if (list != null && list.size() > 0) {
			jvm = list.get(list.size() - 1);
		}
		return jvm;
	}

	@Override
	public ArrayList<JvmMonitor> listJvm(SystemMonitor sys) {
		ArrayList<JvmMonitor> list = null;
		Object obj = MonitorMemcache
				.get(Key.create(Key.JVM_STORE, sys.getId()));
		if (obj != null && obj instanceof String) {
			Gson gson = new Gson();
			Type type = new TypeToken<Collection<JvmMonitor>>() {
			}.getType();
			try {
				list = (ArrayList<JvmMonitor>) gson.fromJson(String.valueOf(obj), type);
			} catch (Exception ex) {
				logger.log(Level.WARNING, " -> ERROR: "
						+ ex.getMessage());
			}
		}
		
		return list;
	}

}
