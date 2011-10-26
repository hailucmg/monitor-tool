package cmg.org.monitor.dao.impl;

import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import cmg.org.monitor.dao.SystemMonitorDAO;
import cmg.org.monitor.entity.shared.CpuMemory;
import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.util.shared.MonitorConstant;
import cmg.org.monitor.util.shared.PMF;

public class SystemMonitorDaoJDOImpl implements SystemMonitorDAO {
	private static final Logger logger = Logger.getLogger(SystemMonitorDaoJDOImpl.class.getCanonicalName());
	
	private static SystemMonitorDaoJDOImpl sysDAO;
	
	public SystemMonitorDaoJDOImpl() {
		sysDAO = this;
	}
	
	public static SystemMonitorDaoJDOImpl getInstance() {
		return sysDAO;
	}
	
	public void addSystem(SystemMonitor system) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {			
			pm.makePersistent(system);
			logger.info(MonitorConstant.DONE_MESSAGE);
		} finally {
			pm.close();
		}
	}
	
	public SystemMonitor[] listSystems(boolean isDeleted) throws Exception {
		// TODO Auto-generated method stub
		PersistenceManager pm = PMF.get().getPersistenceManager();
		CpuMemoryDaoJDOImpl cmDao = new CpuMemoryDaoJDOImpl();
		List<SystemMonitor> listData = null;
		SystemMonitor[] listReturn = null;
		CpuMemory cpuMem = null;
		
		Query query = pm.newQuery(SystemMonitor.class);
		query.setFilter("isDeleted == isDeletedPara");
		query.declareParameters("boolean isDeletedPara");
		try {
			listData = (List<SystemMonitor>) query.execute(isDeleted);
			if (listData.size() > 0) {
				listReturn = new SystemMonitor[listData.size()];
				for (int i = 0; i < listData.size(); i++) {
					listReturn[i] = listData.get(i);
					cpuMem = (cmDao.getLastestCpuMemory(listReturn[i], 1) == null) 
								? null
								: cmDao.getLastestCpuMemory(listReturn[i], 1)[0];
					listReturn[i].setLastCpuMemory(cpuMem);
				}
			}
		} catch (Exception ex) {
			throw ex;
		}
		finally {
			query.closeAll();
			pm.close();
		}
		return listReturn;
	}
	
	public void removeSystem(SystemMonitor system) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			pm.currentTransaction().begin();

			// We have to look it up first,
			system = pm.getObjectById(SystemMonitor.class, system.getId());
			pm.deletePersistent(system);

			pm.currentTransaction().commit();
		} catch (Exception ex) {
			pm.currentTransaction().rollback();
			throw new RuntimeException(ex);
		} finally {
			pm.close();
		}
	}

	public void updateSystem(SystemMonitor contact) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		/*
		String name = contact.getName();
		String url = contact.getUrl();
		String ip = contact.getIp();
		*/

		try {
			pm.currentTransaction().begin();			
			// We have to look it up first,
			/*
			contact = pm.getObjectById(SystemMonitor.class, contact.getId());
			contact.setName(name);
			contact.setUrl(url);
			contact.setIp(ip);
			*/
			pm.makePersistent(contact);
			pm.currentTransaction().commit();
		} catch (Exception ex) {
			pm.currentTransaction().rollback();
			throw new RuntimeException(ex);
		} finally {
			pm.close();
		}
	}
	
  
}
