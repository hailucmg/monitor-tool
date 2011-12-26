package cmg.org.monitor.dao.impl;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import cmg.org.monitor.dao.MemoryDAO;
import cmg.org.monitor.entity.shared.MemoryMonitor;
import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.ext.util.MonitorUtil;
import cmg.org.monitor.memcache.Key;
import cmg.org.monitor.memcache.MonitorMemcache;
import cmg.org.monitor.util.shared.MonitorConstant;

public class MemoryDaoImpl implements MemoryDAO {
	/** Default log for application */
	private static final Logger logger = Logger.getLogger(MemoryDaoImpl.class
			.getCanonicalName());

	@Override
	public void storeMemory(SystemMonitor sys, MemoryMonitor mem) {
		// BEGIN LOG
		long start = System.currentTimeMillis();
		logger.log(Level.INFO,
				MonitorUtil.parserTime(start, true) + sys.toString()
						+ " -> START: put Memory Information ... ");
		// BEGIN LOG
		ArrayList<MemoryMonitor> list = listMemory(sys, mem.getType());
		if (list == null) {
			list = new ArrayList<MemoryMonitor>();
		}
		logger.log(Level.INFO,
				"Start put to memcache. List size: " + list.size());
		list.add(mem);
		if (list.size() > MonitorConstant.CPU_MEMORY_HISTORY_LENGTH) {
			list.remove(0);
		}
		MonitorMemcache.put(
				Key.create(Key.MEMORY_STORE, sys.getId(), mem.getType()), list);

		logger.log(Level.INFO, "End put to memcache. List size: " + list.size());
		// END LOG
		long end = System.currentTimeMillis();
		logger.log(Level.INFO,
				MonitorUtil.parserTime(end, true) + sys.toString()
						+ " -> END: put Memory Information. Time executed: "
						+ (end - start) + " ms.");
		// END LOG
	}

	@Override
	public MemoryMonitor getLastestMemory(SystemMonitor sys, int type) {
		ArrayList<MemoryMonitor> list = listMemory(sys, type);
		MemoryMonitor mem = null;
		if (list != null && list.size() > 0) {
			mem = list.get(list.size() - 1);
		}
		return mem;
	}

	@Override
	public ArrayList<MemoryMonitor> listMemory(SystemMonitor sys, int type) {
		ArrayList<MemoryMonitor> list = null;
		Object obj = MonitorMemcache.get(Key.create(Key.MEMORY_STORE,
				sys.getId(), type));
		if (obj != null) {
			if (obj instanceof ArrayList<?>) {
				try {
					list = (ArrayList<MemoryMonitor>) obj;
				} catch (Exception ex) {
					logger.log(Level.WARNING, " -> ERROR: "
							+ ex.fillInStackTrace().toString());
				}
			}
		}
		return list;
	}

}
