package cmg.org.monitor.dao.impl;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import cmg.org.monitor.dao.CpuDAO;
import cmg.org.monitor.entity.shared.CpuMonitor;
import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.ext.util.MonitorUtil;
import cmg.org.monitor.memcache.Key;
import cmg.org.monitor.memcache.MonitorMemcache;
import cmg.org.monitor.util.shared.MonitorConstant;

public class CpuDaoImpl implements CpuDAO {

	/** Default log for application */
	private static final Logger logger = Logger.getLogger(CpuDaoImpl.class
			.getCanonicalName());

	@Override
	public void storeCpu(SystemMonitor sys, CpuMonitor cpu) {
		if (cpu != null) {
			// BEGIN LOG
			long start = System.currentTimeMillis();
			logger.log(Level.INFO,
					MonitorUtil.parseTime(start, true) + sys.toString()
							+ " -> START: put Cpu Information ... " + cpu);
			// BEGIN LOG
			ArrayList<CpuMonitor> list = listCpu(sys);
			if (list == null) {
				list = new ArrayList<CpuMonitor>();
			}

			logger.log(Level.INFO,
					"Start put to memcache. List size: " + list.size());

			list.add(cpu);
			if (list.size() > MonitorConstant.CPU_MEMORY_HISTORY_LENGTH) {
				list.remove(0);
			}
			MonitorMemcache.put(Key.create(Key.CPU__STORE, sys.getId()), list);

			logger.log(Level.INFO,
					"End put to memcache. List size: " + list.size());

			// END LOG
			long end = System.currentTimeMillis();
			logger.log(Level.INFO,
					MonitorUtil.parseTime(end, true) + sys.toString()
							+ " -> END: put Cpu Information. Time executed: "
							+ (end - start) + " ms.");
			// END LOG
		}
	}

	@Override
	public CpuMonitor getLastestCpu(SystemMonitor sys) {
		ArrayList<CpuMonitor> list = listCpu(sys);
		CpuMonitor cpu = null;
		if (list != null && list.size() > 0) {
			cpu = list.get(list.size() - 1);
		}
		return cpu;
	}

	@Override
	public ArrayList<CpuMonitor> listCpu(SystemMonitor sys) {
		ArrayList<CpuMonitor> list = null;
		Object obj = MonitorMemcache
				.get(Key.create(Key.CPU__STORE, sys.getId()));
		if (obj != null) {
			if (obj instanceof ArrayList<?>) {
				try {
					list = (ArrayList<CpuMonitor>) obj;
				} catch (Exception ex) {
					logger.log(Level.WARNING, " -> ERROR: "
							+ ex.fillInStackTrace().toString());
				}
			}
		}
		return list;
	}
}
