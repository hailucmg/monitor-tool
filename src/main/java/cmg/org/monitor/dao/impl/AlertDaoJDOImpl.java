package cmg.org.monitor.dao.impl;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import cmg.org.monitor.dao.AlertDao;
import cmg.org.monitor.dao.SystemMonitorDAO;
import cmg.org.monitor.entity.shared.AlertMonitor;
import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.exception.MonitorException;
import cmg.org.monitor.ext.model.shared.AlertDto;
import cmg.org.monitor.ext.model.shared.SystemDto;
import cmg.org.monitor.util.shared.PMF;

public class AlertDaoJDOImpl implements AlertDao {

	private static final Logger logger = Logger.getLogger(AlertDaoJDOImpl.class
			.getName());
	private static SystemMonitorDAO systemDao = new SystemMonitorDaoJDOImpl();

	/**
	 * @param alertDTO
	 * @return
	 */
	public AlertDto updateAlert(AlertDto alertDTO, SystemDto sysDto) {
		if (alertDTO.getId() == null) {

			// Create new case
			AlertMonitor newAlert = addAlert(alertDTO, sysDto);
			return newAlert.toDTO();
		}

		PersistenceManager pm = PMF.get().getPersistenceManager();
		AlertMonitor alert = null;
		try {
			alert = pm.getObjectById(AlertMonitor.class, alertDTO.getId());
			alert.updateFromDTO(alertDTO);
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.fillInStackTrace().getMessage());
		} finally {
			pm.close();
		}
		return alertDTO;
	}

	/**
	 * 
	 * Create new Alert object in Datastore.<br>
	 * 
	 * @param alertDTO
	 * @return Alert Monitor object.
	 */
	private AlertMonitor addAlert(AlertDto alertDTO, SystemDto sysDto) {

		PersistenceManager pm = PMF.get().getPersistenceManager();
		AlertMonitor alertEntity = null;
		SystemDto existSystemDTO = null;

		try {

			// Begin a jdo transation
			pm.currentTransaction().begin();
			existSystemDTO = getSystem(sysDto.getId());
			alertEntity = new AlertMonitor(alertDTO);

			// Check a system existence
			if (existSystemDTO != null)
				systemDao.updateSystem(sysDto, alertEntity);

			// Do commit a transaction
			pm.currentTransaction().commit();
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.fillInStackTrace().getMessage());
			pm.currentTransaction().rollback();
			throw new RuntimeException(e);

		} finally {
			pm.close();
		}
		return alertEntity;
	}

	public AlertDto getAlert(String id) {

		PersistenceManager pm = PMF.get().getPersistenceManager();
		AlertMonitor dsAlert = null, detached = null;

		try {
			dsAlert = pm.getObjectById(AlertMonitor.class, id);
			detached = pm.detachCopy(dsAlert);
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.fillInStackTrace().getMessage());
		} finally {
			pm.close();
		}

		return detached.toDTO();
	}

	public SystemDto getSystem(String id) {

		PersistenceManager pm = PMF.get().getPersistenceManager();
		SystemMonitor systemEntity = null, detached = null;

		try {
			systemEntity = pm.getObjectById(SystemMonitor.class, id);
			detached = pm.detachCopy(systemEntity);
		} catch (Exception e) {

			logger.log(Level.SEVERE, e.getCause().getMessage());

		} finally {
			pm.close();
		}

		return detached.toDTO();
	}

	@SuppressWarnings("unchecked")
	public SystemDto getSystemByAddress(String address) throws MonitorException {

		PersistenceManager pm = PMF.get().getPersistenceManager();
		SystemMonitor oneResult = null, detached = null;

		Query q = pm.newQuery(SystemMonitor.class);
		q.setFilter("url == name");
		q.declareParameters("String name");

		try {
			List<SystemMonitor> lists = null;

			lists = (List<SystemMonitor>) q.execute(address);

			if (lists != null && lists.size() == 1) {
				oneResult = (SystemMonitor) lists.get(0);
				oneResult.getAlerts();

				// Fetch alerts list before detaching
				detached = pm.detachCopy(oneResult);
			}

		} catch (Exception e) {

			logger.log(Level.SEVERE, e.getCause().getMessage());
			throw new MonitorException(e);
		} finally {
			pm.close();
			q.closeAll();
		}
		return detached.toDTO();
	}

}
