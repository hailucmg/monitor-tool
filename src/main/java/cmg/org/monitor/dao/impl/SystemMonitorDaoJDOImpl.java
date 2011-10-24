package cmg.org.monitor.dao.impl;

import java.util.List;



import javax.jdo.PersistenceManager;


import cmg.org.monitor.dao.SystemMonitorDAO;
import cmg.org.monitor.entity.SystemMonitor;

import cmg.org.monitor.util.PMF;
import javax.jdo.Query;
public class SystemMonitorDaoJDOImpl implements SystemMonitorDAO {

	@SuppressWarnings("unchecked")
	@Override
	public SystemMonitor[] listSystem(boolean isDeleted) {
		// TODO Auto-generated method stub
		PersistenceManager pm = PMF.get().getPersistenceManager();
		List<SystemMonitor> list = null;
		SystemMonitor[] listReturn = null;
		Query query = pm.newQuery(SystemMonitor.class);
		query.setFilter("isDeleted == isDeletedPara");
		query.declareParameters("boolean isDeletedPara");
		try
		{
			list = (List<SystemMonitor>) query.execute(isDeleted);
			if(list.size() > 0){
				listReturn = new SystemMonitor[list.size()];
				for(int i = 0; i< list.size();i++){
					listReturn[i] = list.get(i);
				}
			}
		}finally {
			query.closeAll();
			pm.close();
			
		}
		return listReturn;
	}
	
	@Override
	public SystemMonitor getSystembyID(String id){
		PersistenceManager pm = PMF.get().getPersistenceManager();
		SystemMonitor system;
		try{
			system = pm.getObjectById(SystemMonitor.class,id);
			
		}finally{
			pm.close();
		}
		return system;
		
	}
	@Override
	public boolean addnewSystem(SystemMonitor system) {
		// TODO Auto-generated method stub
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try{
			pm.makePersistent(system);
		}finally {
			// TODO: handle exception
			pm.close();
		}
		return true;
	}

	@Override
	public boolean editSystembyID(String id, String newName, String newAddress,
			String newIp, boolean isActive) throws Exception {
		// TODO Auto-generated method stub
		PersistenceManager pm = PMF.get().getPersistenceManager();
		SystemMonitor system;
		try{
			
			system = pm.getObjectById(SystemMonitor.class,id);;
			pm.currentTransaction().begin();
			system.setName(newName);
			system.setAddress(newAddress);
			system.setIp(newIp);
			system.setIsActive(isActive);
			pm.makePersistent(system);
			pm.currentTransaction().commit();
		}catch (Exception e) {
			// TODO: handle exception
			pm.currentTransaction().rollback();
			throw e;
		}finally{
			pm.close();	
			
		}
		return true;
	}

	@Override
	public boolean deleteSystembyID(String id) {
		// TODO Auto-generated method stub
		PersistenceManager pm = PMF.get().getPersistenceManager();
		SystemMonitor system = pm.getObjectById(SystemMonitor.class,id);
		try
		{
			boolean delele = true;
			pm.currentTransaction().begin();
			system.setIsDeleted(delele);
			system.setIsActive(false);
			pm.makePersistent(system);
			pm.currentTransaction().commit();
		}catch (Exception e) {
			// TODO: handle exception
		}finally{
			pm.close();
		}
		return true;
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
				system.setIsDeleted(delete);
				system.setIsActive(false);
				pm.makePersistent(system);
				pm.currentTransaction().commit();
			}catch (Exception e) {
				pm.currentTransaction().rollback();
				throw e;
			}
			pm.close();
		}
		
		return true;
	}
	
  
}
