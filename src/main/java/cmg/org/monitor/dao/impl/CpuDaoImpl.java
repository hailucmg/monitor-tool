package cmg.org.monitor.dao.impl;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
			
			// BEGIN LOG
			ArrayList<CpuMonitor> list = listCpu(sys);
			if (list == null) {
				list = new ArrayList<CpuMonitor>();
			}

		

			list.add(cpu);
			if (list.size() > MonitorConstant.CPU_MEMORY_HISTORY_LENGTH) {
				list.remove(0);
			}
			Gson gson = new Gson();
			try {
			MonitorMemcache.put(Key.create(Key.CPU__STORE, sys.getId()), gson.toJson(list));
			} catch (Exception ex) {
				logger.log(Level.WARNING, "Error:" + ex.getMessage());
			}

		
			// END LOG
			long end = System.currentTimeMillis();
		
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
		if (obj != null && obj instanceof String) {
			Type type = new TypeToken<Collection<CpuMonitor>>() {
			}.getType();
			Gson gson = new Gson();
			try {
				list = (ArrayList<CpuMonitor>) gson.fromJson(String.valueOf(obj), type);
			} catch (Exception ex) {
				logger.log(Level.WARNING, " -> ERROR: "
						+ ex.getMessage());
			}
		}		
		return list;
	}
}
