package cmg.org.monitor.dao.impl;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import cmg.org.monitor.dao.FileSystemDAO;
import cmg.org.monitor.entity.shared.FileSystemMonitor;
import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.memcache.Key;
import cmg.org.monitor.memcache.MonitorMemcache;

/**
 * @author HongHai
 * 
 */
public class FileSystemDaoImpl implements FileSystemDAO {
	private static final Logger logger = Logger.getLogger(AlertDaoImpl.class
			.getName());

	@Override
	public void storeFileSystems(SystemMonitor sys,
			ArrayList<FileSystemMonitor> fileSystems) {
		if (fileSystems != null && fileSystems.size() > 0) {
			logger.log(Level.INFO,
					"Put file system list to memcache ... Size: " + fileSystems.size());
			for (int i = 0; i < fileSystems.size(); i++) {
				logger.log(Level.INFO, "Filesystem #" + (i +1) + fileSystems.get(i));
			}
			MonitorMemcache.put(Key.create(Key.FILE_SYSTEM_STORE, sys.getId()),
					fileSystems);
		}
	}

	@Override
	public ArrayList<FileSystemMonitor> listFileSystems(SystemMonitor sys) {
		ArrayList<FileSystemMonitor> list = null;
		Object obj = MonitorMemcache.get(Key.create(Key.FILE_SYSTEM_STORE,
				sys.getId()));
		if (obj != null) {
			if (obj instanceof ArrayList<?>) {
				try {
					list = (ArrayList<FileSystemMonitor>) obj;
				} catch (Exception ex) {
					logger.log(Level.WARNING, " -> ERROR: "
							+ ex.fillInStackTrace().toString());
				}
			}
		}
		return list;
	}

}
