package cmg.org.monitor.dao.impl;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import cmg.org.monitor.dao.CpuMemoryDAO;
import cmg.org.monitor.dao.SystemMonitorDAO;
import cmg.org.monitor.entity.shared.CpuMemory;
import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.ext.model.dto.CpuDto;
import cmg.org.monitor.ext.model.dto.SystemDto;
import cmg.org.monitor.util.shared.PMF;

public class CpuMemoryDaoJDOImpl implements CpuMemoryDAO {

	private static final Logger logger = Logger.getLogger(AlertDaoJDOImpl.class
			.getName());

	private static SystemMonitorDAO systemDao = new SystemMonitorDaoJDOImpl();

	public CpuMemory[] getLastestCpuMemory(SystemMonitor system,
			int numberOfResult) throws Exception {

		PersistenceManager pm = PMF.get().getPersistenceManager();
		CpuMemory[] cpuMem = null;
		List<CpuMemory> list = null;
		Query query = pm.newQuery(CpuMemory.class);
		query.setFilter("systemMonitor == sys");
		query.declareParameters("SystemMonitor sys");
		query.setOrdering("timeStamp desc");
		query.setRange(0, numberOfResult);
		try {
			list = (List<CpuMemory>) query.execute(system);
			if (list.size() > 0) {
				cpuMem = new CpuMemory[list.size()];
				for (int i = 0; i < list.size(); i++) {
					cpuMem[i] = list.get(i);
				}
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			query.closeAll();
			pm.close();
		}
		return cpuMem;
	}

	@Override
	public void addCpuMemory(SystemMonitor system, CpuMemory cpuMemory) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		system.addCpuMemory(cpuMemory);
		try {
			pm.makePersistent(system);
		} finally {
			pm.close();
		}
	}

	/**
	 * @param cpuDTO
	 * @return
	 */
	public CpuDto updateCpu(CpuDto cpuDTO, SystemDto sysDto) {
		
		if (cpuDTO.getId() == null) {

			// Create new case
			CpuMemory newCpuEntity = addCpu(cpuDTO, sysDto);
			return newCpuEntity.toDTO();
		}

		PersistenceManager pm = PMF.get().getPersistenceManager();
		CpuMemory cpuEntity = null;
		try {
			cpuEntity = pm.getObjectById(CpuMemory.class, cpuDTO.getId());
			cpuEntity.updateFromDTO(cpuDTO);
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.fillInStackTrace().getMessage());
		} finally {
			pm.close();
		}
		return cpuDTO;
	}

	/**
	 * 
	 * Create new Alert object in Datastore.<br>
	 * 
	 * @param cpuDTO
	 * @return Alert Monitor object.
	 */
	private CpuMemory addCpu(CpuDto cpuDTO, SystemDto sysDto) {

		PersistenceManager pm = PMF.get().getPersistenceManager();
		CpuMemory cpuEntity = null;
		SystemDto existSystemDTO = null;
		try {

			// Begin a jdo transation
			pm.currentTransaction().begin();
			existSystemDTO = systemDao.getSystemEntity(sysDto.getId());
			cpuEntity = new CpuMemory(cpuDTO);

			// Check a system existence
			if (existSystemDTO != null)
				systemDao.updateSystemByCpu(sysDto, cpuEntity);

			// Do commit a transaction
			pm.currentTransaction().commit();
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.fillInStackTrace().getMessage());
			pm.currentTransaction().rollback();
			throw new RuntimeException(e);

		} finally {
			pm.close();
		}
		return cpuEntity;
	}
}
