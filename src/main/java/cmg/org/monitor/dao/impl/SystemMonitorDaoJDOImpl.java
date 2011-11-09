package cmg.org.monitor.dao.impl;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import cmg.org.monitor.dao.CpuMemoryDAO;
import cmg.org.monitor.dao.FileSystemDAO;
import cmg.org.monitor.dao.ServiceMonitorDAO;
import cmg.org.monitor.dao.SystemMonitorDAO;
import cmg.org.monitor.entity.shared.AlertMonitor;
import cmg.org.monitor.entity.shared.CpuMemory;
import cmg.org.monitor.entity.shared.FileSystem;
import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.ext.model.shared.SystemDto;
import cmg.org.monitor.util.shared.MonitorConstant;
import cmg.org.monitor.util.shared.PMF;
import cmg.org.monitor.util.shared.Ultility;

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
	
	public void updateSystemByFileSystem(SystemDto aSystemDTO, FileSystem anFileSystemEntity) {
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
			sysMonitor.addFileSystem(anFileSystemEntity);
			
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
		SystemMonitorDAO sysDao = new SystemMonitorDaoJDOImpl();
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
					listReturn[i].setHealthStatus(sysDao.getCurrentHealthStatus(listReturn[i]));
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
	
	@Override
	public SystemMonitor getSystembyID(String id) throws Exception{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		SystemMonitor system;
		try{
			system = pm.getObjectById(SystemMonitor.class,id);
		}catch (Exception e) {
			throw e;
			
		}finally{
			pm.close();
		}
		return system;
		
	}
	@Override
	public boolean addnewSystem(SystemMonitor system) throws Exception {
		// TODO Auto-generated method stub
		PersistenceManager pm = PMF.get().getPersistenceManager();
		boolean check = false;
		try{
			//system.setCode(Ultility.createSID(pm));
			pm.makePersistent(system);
			logger.info(MonitorConstant.DONE_MESSAGE);
			check = true;
		}catch (Exception e) {
			throw e;
		}finally {
			// TODO: handle exception
			pm.close();
		}
		return check;
	}

	@Override
	public boolean editSystembyID(String id, String newName, String newAddress, String protocol, String group,
			 String ip,String remoteURL,boolean isActive) throws Exception {
		// TODO Auto-generated method stub
		PersistenceManager pm = PMF.get().getPersistenceManager();
		SystemMonitor system;
		boolean check =false;
		try{
			system = pm.getObjectById(SystemMonitor.class,id);;
			pm.currentTransaction().begin();
			system.setName(newName);
			system.setUrl(newAddress);
			system.setProtocol(protocol);
			system.setGroupEmail(group);
			system.setActive(isActive);
			system.setIp(ip);
			system.setRemoteUrl(remoteURL);
			pm.makePersistent(system);
			pm.currentTransaction().commit();
			check = true;
		}catch (Exception e) {
			// TODO: handle exception
			pm.currentTransaction().rollback();
			
		}finally{
			pm.close();		
		}
		return check;
	}

	@Override
	public boolean deleteSystembyID(String id) throws Exception {
		// TODO Auto-generated method stub
		PersistenceManager pm = PMF.get().getPersistenceManager();
		SystemMonitor system = pm.getObjectById(SystemMonitor.class,id);
		boolean check = false;
		try
		{
			boolean delele = true;
			pm.currentTransaction().begin();
			system.setDeleted(delele);
			system.setActive(false);
			pm.makePersistent(system);
			pm.currentTransaction().commit();
			check = true;
		}catch (Exception e) {
			// TODO: handle exception
			throw e;	
		}finally{
			pm.close();
		}
		return check;
	}

	@Override
	public boolean deleteListSystembyID(String[] ids) throws Exception {
		// TODO Auto-generated method stub
		boolean delete = true;
		for(int i=0;i<ids.length;i++){
			 PersistenceManager pm = PMF.get().getPersistenceManager();
			 SystemMonitor system = pm.getObjectById(SystemMonitor.class,ids[i]);
			try
			{
				pm.currentTransaction().begin();
				system.setDeleted(delete);
				system.setActive(false);
				pm.makePersistent(system);
				pm.currentTransaction().commit();
			}catch (Exception e) {
				pm.currentTransaction().rollback();
				return false;	
			}
			pm.close();
		}
		
		return true;
	}
	
	@Override
	public String getIPbyURL(String url) throws Exception{
		String ip = null;
		try {
			ip = Ultility.getIpbyUrl(url);
		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
		return ip;
		
	}
	
	@Override
	public String createCode() throws Exception{
		String code=null;
		try {
			code = Ultility.createSID();
		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
		
		return code;
		
	}
	
	@Override
	public Date getLastestTimeStamp(SystemMonitor system, String className) {
		Date time = null;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery("select timeStamp from " + className);
		query.setFilter("systemMonitor == sys");
		query.declareParameters("SystemMonitor sys");
		query.setOrdering("timeStamp desc");
		try {
			List<Date> list = (List<Date>) query.execute(system);
			if (list.size() > 0) {
				time = list.get(0);
			}
		} finally {
			query.closeAll();
			pm.close();
		}
		return time;
	}

	@Override
	public String getCurrentHealthStatus(SystemMonitor system) {
		ServiceMonitorDAO smDAO = new ServiceMonitorDaoJDOImpl();
		CpuMemoryDAO cmDAO = new CpuMemoryDaoJDOImpl();
		FileSystemDAO fsDAO = new FileSystemDaoJDOImpl();
		
		boolean checkService = false;
		boolean checkCpuMemory = false;
		boolean checkFileSystem = true;
		
		try {
			CpuMemory cm = (cmDAO.getLastestCpuMemory(system, 1) == null)
					         ? null
					        		 :cmDAO.getLastestCpuMemory(system, 1)[0];
			if (cm != null) {
				checkCpuMemory = (cm.getPercentMemoryUsage() < 90) && (cm.getCpuUsage() < 90);
			}
			checkService = smDAO.checkStatusAllService(system);
			
			FileSystem[] listFs = fsDAO.listLastestFileSystem(system);
			if (listFs != null) {
				for (FileSystem fs : listFs) {
					if (fs.getPercentUsage() >= 90) {
						checkFileSystem = false;
						break;
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
			
		return system.getStatus() && system.isActive()
				 ?  (checkCpuMemory && checkFileSystem && checkService 
						 ? "smile"
								 : "bored")
				  : "dead";
	}

	@Override
	public String[] groups() throws Exception {
		// TODO Auto-generated method stub
		String[] groups = null;
		try {
			groups=Ultility.listGroup();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return groups;
	}

}
