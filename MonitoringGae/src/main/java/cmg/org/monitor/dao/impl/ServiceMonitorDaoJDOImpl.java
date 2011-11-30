package cmg.org.monitor.dao.impl;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import cmg.org.monitor.dao.ServiceMonitorDAO;
import cmg.org.monitor.dao.SystemMonitorDAO;
import cmg.org.monitor.entity.shared.FileSystem;
import cmg.org.monitor.entity.shared.JVMMemory;
import cmg.org.monitor.entity.shared.ServiceMonitor;
import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.exception.MonitorException;
import cmg.org.monitor.ext.model.shared.JVMMemoryDto;
import cmg.org.monitor.ext.model.shared.ServiceDto;
import cmg.org.monitor.ext.model.shared.SystemDto;
import cmg.org.monitor.util.shared.PMF;

public class ServiceMonitorDaoJDOImpl implements ServiceMonitorDAO {
	private static final Logger logger = Logger
			.getLogger(ServiceMonitorDaoJDOImpl.class.getCanonicalName());
	private static SystemMonitorDAO systemDao = new SystemMonitorDaoJDOImpl();
	
	public ServiceDto updateServiceEntity(ServiceDto serviceDTO, SystemDto sysDto, JVMMemoryDto jvmDto) throws MonitorException {
		if (serviceDTO.getId() == null) {

			// Create new case
			ServiceMonitor newServiceEntity = addService(serviceDTO, sysDto, jvmDto);
			return newServiceEntity.toDTO();
		}

		PersistenceManager pm = PMF.get().getPersistenceManager();
		ServiceMonitor systemEntity = null;
		try {
			systemEntity = pm.getObjectById(ServiceMonitor.class, serviceDTO.getId());
			systemEntity.updateFromDTO(serviceDTO);
			pm.makePersistent(systemEntity);
		} finally {
			pm.close();
		}
		return serviceDTO;
	}

	

	public ServiceDto getService(String id) {

		PersistenceManager pm = PMF.get().getPersistenceManager();
		ServiceMonitor dsAlert = null, detached = null;

		try {
			dsAlert = pm.getObjectById(ServiceMonitor.class, id);
			detached = pm.detachCopy(dsAlert);
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.fillInStackTrace().getMessage());
		} finally {
			pm.close();
		}

		return detached.toDTO();
	}
	
	
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
	
	/**
	 * Create new Alert object in JDO Datastore.<br>
	 * 
	 * @param serviceDTO service data transfer object.
	 * @param sysDto System data transfer object.
	 * @return Service Monitor object.
	 */
	private ServiceMonitor addService(ServiceDto serviceDTO, SystemDto sysDto, JVMMemoryDto jvmDto) {

		PersistenceManager pm = PMF.get().getPersistenceManager();
		ServiceMonitor serviceEntity = null;
		SystemMonitor existSysEntity = null;
		JVMMemory jvmEntity = null;
		try {

			// Begin a jdo transation
			pm.currentTransaction().begin();
			existSysEntity = systemDao.getSystembyID(sysDto.getId());
			
			// Check a system existence
			if (existSysEntity != null) {
				serviceEntity = new ServiceMonitor(serviceDTO);
				jvmEntity = new JVMMemory(jvmDto);
				
				existSysEntity.addService(serviceEntity);
				existSysEntity.addJVMMemory(jvmEntity);
				existSysEntity.setStatus(sysDto.getSystemStatus());
				pm.makePersistent(existSysEntity);
			}
			// Do commit a transaction
			pm.currentTransaction().commit();
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.fillInStackTrace().getMessage());
			pm.currentTransaction().rollback();
			throw new RuntimeException(e);

		} finally {
			pm.close();
		}
		return serviceEntity;
	}
}
