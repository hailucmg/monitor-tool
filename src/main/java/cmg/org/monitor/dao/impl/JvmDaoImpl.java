package cmg.org.monitor.dao.impl;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

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
		// BEGIN LOG
		long start = System.currentTimeMillis();
		logger.log(Level.INFO,
				MonitorUtil.parserTime(start, true) + sys.toString()
						+ " -> START: put Jvm Information ... ");
		// BEGIN LOG

		ArrayList<JvmMonitor> list = listJvm(sys);
		if (list == null) {
			list = new ArrayList<JvmMonitor>();
		}
		logger.log(Level.INFO,
				"Start put to memcache. List size: " + list.size());
		list.add(jvm);
		if (list.size() > MonitorConstant.CPU_MEMORY_HISTORY_LENGTH) {
			list.remove(0);
		}
		MonitorMemcache.put(Key.create(Key.JVM_STORE, sys.getId()), list);

		logger.log(Level.INFO,
				"End put to memcache. List size: " + list.size());
		// END LOG
		long end = System.currentTimeMillis();
		logger.log(Level.INFO,
				MonitorUtil.parserTime(end, true) + sys.toString()
						+ " -> END: put Jvm Information. Time executed: "
						+ (end - start) + " ms.");
		// END LOG
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
		if (obj != null) {
			if (obj instanceof ArrayList<?>) {
				try {
					list = (ArrayList<JvmMonitor>) obj;
				} catch (Exception ex) {
					logger.log(Level.WARNING, " -> ERROR: "
							+ ex.fillInStackTrace().toString());
				}
			}
		}
		return list;
	}

}
