package cmg.org.monitor.services;

import cmg.org.monitor.dao.SystemMonitorDAO;
import cmg.org.monitor.dao.impl.SystemMonitorDaoJDOImpl;
import cmg.org.monitor.entity.shared.SystemMonitor;


/**
 * @author admin
 * @deprecated
 */
public class SystemService {
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
