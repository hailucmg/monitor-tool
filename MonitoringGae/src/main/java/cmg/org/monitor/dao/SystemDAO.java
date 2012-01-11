package cmg.org.monitor.dao;

import java.util.ArrayList;

import cmg.org.monitor.entity.shared.ChangeLogMonitor;
import cmg.org.monitor.entity.shared.NotifyMonitor;
import cmg.org.monitor.entity.shared.SystemMonitor;

public interface SystemDAO {
	
	public String[] listRemoteURLs() throws Exception;

	public ArrayList<SystemMonitor> listSystems(boolean isDeleted)
			throws Exception;
	
	public ArrayList<SystemMonitor> listSystemsFromMemcache(boolean isDeleted);

	public boolean addSystem(SystemMonitor system, String code) throws Exception;

	public boolean removeSystem(SystemMonitor system) throws Exception;

	public boolean updateSystem(SystemMonitor system, boolean reloadMemcache) throws Exception;

	public SystemMonitor getSystemById(String id) throws Exception;

	public boolean deleteSystem(String sysId) throws Exception;
	
	public void storeSysList(ArrayList<SystemMonitor> list);
	
	public String[] listEmails();
	
	public String createSID() throws Exception;
	
	public boolean editSystem(SystemMonitor sys) throws Exception ;
	
	public boolean updateStatus(SystemMonitor sys, boolean status, String healthStatus) throws Exception ;
	
	public NotifyMonitor getNotifyOption(String sid) throws Exception;
	
	public boolean setNotifyOption(String sid, NotifyMonitor notify) throws Exception;
	
	public boolean addChangeLog(ChangeLogMonitor log) throws Exception;
	
	public ArrayList<ChangeLogMonitor> listChangeLog(String sid, int start, int end) throws Exception;
	
	public int getCounterChangeLog(String sid) throws Exception;
	
	public int getCountAllChangeLog() throws Exception;
	
	public boolean setCountChangeLog(String sid) throws Exception;
	
	public ArrayList<ChangeLogMonitor> listChangeLog() throws Exception;
	
	public boolean setCounterChangeLogByID(String c_id) throws Exception;
}
