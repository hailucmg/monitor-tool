package cmg.org.monitor.dao.impl;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import cmg.org.monitor.dao.FileSystemDAO;
import cmg.org.monitor.dao.SystemMonitorDAO;
import cmg.org.monitor.entity.shared.FileSystem;
import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.ext.model.shared.FileSystemDto;
import cmg.org.monitor.ext.model.shared.SystemDto;
import cmg.org.monitor.util.shared.PMF;

/**
 * @author admin
 *
 */
public class FileSystemDaoJDOImpl implements FileSystemDAO {
	private static final Logger logger = Logger.getLogger(AlertDaoJDOImpl.class
			.getName());
	private static SystemMonitorDAO systemDao = new SystemMonitorDaoJDOImpl();

	public FileSystemDto updateFileSystem(FileSystemDto fileSysDTO,
			SystemDto sysDto) {
		if (fileSysDTO.getId() == null) {

			// Create new case
			FileSystem newFileSystem = addFileSystem(fileSysDTO, sysDto);
			return newFileSystem.toDTO();
		}

		PersistenceManager pm = PMF.get().getPersistenceManager();
		FileSystem alert = null;
		try {
			alert = pm.getObjectById(FileSystem.class, fileSysDTO.getId());
			alert.updateFromDTO(fileSysDTO);
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.fillInStackTrace().getMessage());
		} finally {
			pm.close();
		}
		return fileSysDTO;
	}

	/**
	 * 
	 * Create new FileSystem object in Datastore.<br>
	 * 
	 * @param fileSysDTO
	 * @return FileSystem object.
	 */
	private FileSystem addFileSystem(FileSystemDto fileSysDTO, SystemDto sysDto) {

		PersistenceManager pm = PMF.get().getPersistenceManager();
		FileSystem fileEntity = null;
		SystemMonitor existSystemEntity = null;

		try {

			// Begin a jdo transation
			pm.currentTransaction().begin();
			existSystemEntity = systemDao.getSystembyID(sysDto.getId());
			

			// Check a system existence
			if (existSystemEntity != null) {
				fileEntity = new FileSystem(fileSysDTO);
				
				existSystemEntity.addFileSystem(fileEntity);
				pm.makePersistent(existSystemEntity);
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
		return fileEntity;
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

	@Override
	public void addFileSystem(SystemMonitor system, FileSystem fileSystem) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		system.addFileSystem(fileSystem);
		try {
			pm.makePersistent(system);
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getCause().getMessage());
		}
		finally {
			pm.close();
		}
	}

	@Override
	public FileSystem[] listLastestFileSystem(SystemMonitor system)
			throws Exception {
		FileSystem[] listFs = null;
		List<FileSystem> list = null;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(FileSystem.class,
				"systemMonitor == sys && timeStamp == time");
		query.declareParameters("SystemMonitor sys, java.util.Date time");
		SystemMonitorDAO sysDAO = new SystemMonitorDaoJDOImpl();
		Date time = sysDAO.getLastestTimeStamp(system,
				FileSystem.class.getName());

		try {
			list = (List<FileSystem>) query.execute(system, time);
			if (list.size() > 0) {
				listFs = new FileSystem[list.size()];
				for (int i = 0; i < list.size(); i++) {
					listFs[i] = list.get(i);
				}
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			query.closeAll();
			pm.close();
		}
		return listFs;
	}

}
