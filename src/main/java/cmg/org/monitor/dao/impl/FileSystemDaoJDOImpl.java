package cmg.org.monitor.dao.impl;

import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import cmg.org.monitor.dao.FileSystemDAO;
import cmg.org.monitor.dao.SystemMonitorDAO;
import cmg.org.monitor.entity.shared.FileSystem;
import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.util.shared.PMF;

public class FileSystemDaoJDOImpl implements FileSystemDAO {
	@Override
	public void addFileSystem(SystemMonitor system, FileSystem fileSystem) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		system.addFileSystem(fileSystem);
		try {			
			pm.makePersistent(system);
		} finally {
			pm.close();
		}
	}

	@Override
	public FileSystem[] listLastestFileSystem(SystemMonitor system) throws Exception {
		FileSystem[] listFs = null;
		List<FileSystem> list = null;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(FileSystem.class, "systemMonitor == sys && timeStamp == time");	
		query.declareParameters("SystemMonitor sys, java.util.Date time");	
		SystemMonitorDAO sysDAO = new SystemMonitorDaoJDOImpl();
		Date time = sysDAO.getLastestTimeStamp(system, FileSystem.class.getName());
		
		try {
			list = (List<FileSystem>) query.execute(system, time);
			if (list.size() > 0) {
				listFs = new FileSystem[list.size()];
				for (int i = 0; i < list.size(); i++) {
					listFs[i] = list.get(i);
				}
			}
		} catch (Exception ex) {
			throw ex;
		}
		finally {
			query.closeAll();
			pm.close();
		}
		return listFs;
	}

}
