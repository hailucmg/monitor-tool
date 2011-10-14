package cmg.org.monitor.services;

import java.util.logging.Logger;

import cmg.org.monitor.dao.SystemMonitorDAO;
import cmg.org.monitor.dao.impl.SystemMonitorDaoJDOImpl;
import cmg.org.monitor.entity.SystemMonitor;


public class SystemService {
	private static final Logger logger = Logger.getLogger(SystemService.class
			.getCanonicalName());
	SystemMonitorDAO systemMonDao = null;

	public SystemService() {
		this.systemMonDao = new SystemMonitorDaoJDOImpl();
	}

	public void addSystemMonitor(SystemMonitor system) {

		if (systemMonDao != null) {
			system.setAddress("c-mg.com.vn");
			system.setIp("193.168.1.12");
			system.setName("cmg viet nam");
			systemMonDao.addSystem(system);
			logger.info("Add system ok");
		}


	}

}
