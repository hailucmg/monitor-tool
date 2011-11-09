package cmg.org.monitor.dao.impl;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import cmg.org.monitor.dao.ServiceMonitorDAO;
import cmg.org.monitor.dao.SystemMonitorDAO;
import cmg.org.monitor.entity.shared.FileSystem;
import cmg.org.monitor.entity.shared.ServiceMonitor;
import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.util.shared.PMF;

public class ServiceMonitorDaoJDOImpl implements ServiceMonitorDAO {
	private static final Logger logger = Logger
			.getLogger(ServiceMonitorDaoJDOImpl.class.getCanonicalName());
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
	
	@Override
	public boolean checkStatusAllService(SystemMonitor system) throws Exception {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(ServiceMonitor.class, 
									"systemMonitor == sys && timeStamp == time");	
		query.declareParameters("SystemMonitor sys, java.util.Date time");	
		List<ServiceMonitor> list = null;
		boolean b = true;
		SystemMonitorDAO sysDAO = new SystemMonitorDaoJDOImpl();
		Date time = sysDAO.getLastestTimeStamp(system, FileSystem.class.getName());
		try {
			list = (List<ServiceMonitor>) query.execute(system, time);
			for (ServiceMonitor sm : list) {
				if (!sm.getStatus() || sm.getPing() >= 500) {
					b = false;
					break;
				}
			}
		} catch(Exception ex) {
			throw ex;
		} finally {
			query.closeAll();
			pm.close();
		}
		return b;
	}

	@Override
	public ServiceMonitor[] listLastestService(SystemMonitor system) throws Exception {
		ServiceMonitor[] listSm = null;
		List<ServiceMonitor> list = null;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(ServiceMonitor.class, "systemMonitor == sys && timeStamp == time");	
		query.declareParameters("SystemMonitor sys, java.util.Date time");	
		SystemMonitorDAO sysDAO = new SystemMonitorDaoJDOImpl();
		Date time = sysDAO.getLastestTimeStamp(system, ServiceMonitor.class.getName());
		
		try {
			list = (List<ServiceMonitor>) query.execute(system, time);
			if (list.size() > 0) {
				listSm = new ServiceMonitor[list.size()];
				for (int i = 0; i < list.size(); i++) {
					listSm[i] = list.get(i);
				}
			}
		} catch (Exception ex) {
			throw ex;
		}
		finally {
			query.closeAll();
			pm.close();
		}
		return listSm;
	}
}
