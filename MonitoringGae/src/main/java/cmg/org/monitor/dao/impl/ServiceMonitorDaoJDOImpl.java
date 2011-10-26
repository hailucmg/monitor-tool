package cmg.org.monitor.dao.impl;

import javax.jdo.PersistenceManager;

import cmg.org.monitor.dao.ServiceMonitorDAO;
import cmg.org.monitor.entity.shared.ServiceMonitor;
import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.util.shared.PMF;

public class ServiceMonitorDaoJDOImpl implements ServiceMonitorDAO {
	@Override
	public void addServiceMonitor(SystemMonitor system, ServiceMonitor serviceMonitor) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		system.addService(serviceMonitor);
		try {			
			pm.makePersistent(system);
		} finally {
			pm.close();
		}
	}
}
