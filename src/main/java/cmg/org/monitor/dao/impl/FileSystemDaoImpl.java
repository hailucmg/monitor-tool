package cmg.org.monitor.dao.impl;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
			ArrayList fileSystems) {
		if (fileSystems != null && fileSystems.size() > 0) {
			
			Gson gson = new Gson();
			try {
			MonitorMemcache.put(Key.create(Key.FILE_SYSTEM_STORE, sys.getId()),
					gson.toJson(fileSystems));
			} catch (Exception ex) {
				logger.log(Level.WARNING, "Error:" + ex.getMessage());
			}
		}
	}

	@Override
	public ArrayList<FileSystemMonitor> listFileSystems(SystemMonitor sys) {
		ArrayList<FileSystemMonitor> list = null;
		Object obj = MonitorMemcache.get(Key.create(Key.FILE_SYSTEM_STORE,
				sys.getId()));
		if (obj != null && obj instanceof String) {
			Type type = new TypeToken<Collection<FileSystemMonitor>>() {
			}.getType(); 
			Gson gson = new Gson();
			try {
				list = (ArrayList<FileSystemMonitor>) gson.fromJson(String.valueOf(obj), type);
			} catch (Exception ex) {
				logger.log(Level.WARNING, " -> ERROR: "
						+ ex.getMessage());
			}
		}
		
		return list;
	}

}
