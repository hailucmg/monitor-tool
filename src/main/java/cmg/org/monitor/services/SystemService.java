package cmg.org.monitor.services;

import java.util.logging.Logger;

import cmg.org.monitor.dao.SystemMonitorDAO;
import cmg.org.monitor.dao.impl.SystemMonitorDaoJDOImpl;
import cmg.org.monitor.entity.shared.SystemMonitor;


public class SystemService {
	private static final Logger logger = Logger.getLogger(SystemService.class
			.getCanonicalName());
	private static SystemMonitorDAO systemMonDao =  new SystemMonitorDaoJDOImpl();

	public SystemService() {
	}

	public static void addSystemMonitor(SystemMonitor system) {
		systemMonDao.addSystem(system);
	}
	
	
	public static SystemMonitor[] listSystems(boolean b) {
		SystemMonitor[] list = null;
		try {
			list = systemMonDao.listSystems(b);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return list;
	}

}
