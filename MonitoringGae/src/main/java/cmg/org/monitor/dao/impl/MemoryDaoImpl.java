package cmg.org.monitor.dao.impl;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
		if (mem != null && sys != null) {
			// BEGIN LOG
			long start = System.currentTimeMillis();
		
			// BEGIN LOG
			ArrayList<MemoryMonitor> list = listMemory(sys, mem.getType());
			if (list == null) {
				list = new ArrayList<MemoryMonitor>();
			}
			
			list.add(mem);
			if (list.size() > MonitorConstant.CPU_MEMORY_HISTORY_LENGTH) {
				list.remove(0);
			}
			Gson gson = new Gson();
			try {
			MonitorMemcache.put(
					Key.create(Key.MEMORY_STORE, sys.getId(), mem.getType()),
					gson.toJson(list));
			} catch (Exception ex) {
				logger.log(Level.WARNING, "Error:" + ex.getMessage());
			}

		
			long end = System.currentTimeMillis();
		
		}
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
		if (obj != null && obj instanceof String) {
			Gson gson = new Gson();
			Type typeToken = new TypeToken<Collection<MemoryMonitor>>() {
			}.getType();
			try {
				list = (ArrayList<MemoryMonitor>) gson.fromJson(String.valueOf(obj), typeToken);
			} catch (Exception ex) {
				logger.log(Level.WARNING, " -> ERROR: "
						+ ex.getMessage());
			}
		}
		
		return list;
	}

}
