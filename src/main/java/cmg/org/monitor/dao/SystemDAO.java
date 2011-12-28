package cmg.org.monitor.dao;

import java.util.ArrayList;

import cmg.org.monitor.entity.shared.SystemMonitor;

public interface SystemDAO {
	
	public String[] remoteURLs() throws Exception;

	public ArrayList<SystemMonitor> listSystems(boolean isDeleted)
			throws Exception;
	
	public ArrayList<SystemMonitor> listSystemsFromMemcache(boolean isDeleted);

	public boolean addSystem(SystemMonitor system) throws Exception;

	public boolean removeSystem(SystemMonitor system) throws Exception;

	public boolean updateSystem(SystemMonitor system) throws Exception;

	public SystemMonitor getSystemById(String id) throws Exception;

	public boolean deleteSystem(SystemMonitor sys) throws Exception;
	
	public void storeSysList(ArrayList<SystemMonitor> list);

}
