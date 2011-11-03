package cmg.org.monitor.dao.impl;

import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import cmg.org.monitor.dao.SystemMonitorDAO;
import cmg.org.monitor.entity.shared.AlertMonitor;
import cmg.org.monitor.entity.shared.CpuMemory;
import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.ext.model.dto.SystemDto;
import cmg.org.monitor.util.shared.MonitorConstant;
import cmg.org.monitor.util.shared.PMF;

public class SystemMonitorDaoJDOImpl implements SystemMonitorDAO {
	private static final Logger logger = Logger
			.getLogger(SystemMonitorDaoJDOImpl.class.getCanonicalName());

	public void addSystem(SystemMonitor system) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {

			pm.makePersistent(system);
			logger.info(MonitorConstant.DONE_MESSAGE);
		} finally {
			pm.close();
		}
	}

	@SuppressWarnings("unchecked")
	public List<SystemMonitor> listSystems() {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		String query = "select from " + SystemMonitor.class.getName();
		return (List<SystemMonitor>) pm.newQuery(query).execute();
	}

	public SystemDto getSystemEntity(String id) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		SystemDto sysDto = new SystemDto();
		SystemMonitor sysMonitor = new SystemMonitor();
		try {

			// We have to look it up first,
			sysMonitor = pm.getObjectById(SystemMonitor.class, id);

			// Check given entity object
			if (sysMonitor != null)
				return sysMonitor.toDTO();

		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			pm.close();
		}
		
		// Return DTO object
		return sysDto;
	}

	public void updateSystem(SystemDto aSystemDTO, AlertMonitor anAlertEntity) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		String name = aSystemDTO.getName();
		String address = aSystemDTO.getUrl();
		String ip = aSystemDTO.getIp();
		SystemMonitor sysMonitor = new SystemMonitor();
		try {
			pm.currentTransaction().begin();

			// We have to look it up first,
			sysMonitor = pm.getObjectById(SystemMonitor.class,
					aSystemDTO.getId());
			sysMonitor.setName(name);
			sysMonitor.setUrl(address);
			sysMonitor.setIp(ip);
			sysMonitor.getAlerts().add(anAlertEntity);
			pm.makePersistent(sysMonitor);
			pm.currentTransaction().commit();
		} catch (Exception ex) {
			pm.currentTransaction().rollback();
			throw new RuntimeException(ex);
		} finally {
			pm.close();
		}
	}

	public void updateSystemByCpu(SystemDto aSystemDTO, CpuMemory anCpuEntity) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		String name = aSystemDTO.getName();
		String address = aSystemDTO.getUrl();
		String ip = aSystemDTO.getIp();
		SystemMonitor sysMonitor = new SystemMonitor();
		try {
			pm.currentTransaction().begin();

			// We have to look it up first
			sysMonitor = pm.getObjectById(SystemMonitor.class,
					aSystemDTO.getId());

			// Set properties's object
			sysMonitor.setName(name);
			sysMonitor.setUrl(address);
			sysMonitor.setIp(ip);
			sysMonitor.addCpuMemory(anCpuEntity);

			// Store to JDO entity
			pm.makePersistent(sysMonitor);
			pm.currentTransaction().commit();
		} catch (Exception ex) {
			pm.currentTransaction().rollback();
			throw new RuntimeException(ex);
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
					cpuMem = (cmDao.getLastestCpuMemory(listReturn[i], 1) == null) ? null
							: cmDao.getLastestCpuMemory(listReturn[i], 1)[0];
					listReturn[i].setLastCpuMemory(cpuMem);
				}
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
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
		 * String name = contact.getName(); String url = contact.getUrl();
		 * String ip = contact.getIp();
		 */

		try {
			pm.currentTransaction().begin();
			// We have to look it up first,
			/*
			 * contact = pm.getObjectById(SystemMonitor.class, contact.getId());
			 * contact.setName(name); contact.setUrl(url); contact.setIp(ip);
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
