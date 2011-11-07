package cmg.org.monitor.dao;

import java.util.Date;
import java.util.List;

import cmg.org.monitor.entity.shared.AlertMonitor;
import cmg.org.monitor.entity.shared.CpuMemory;
import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.ext.model.shared.SystemDto;

public interface SystemMonitorDAO {
	
	
	/**
	 * @param id
	 * @return
	 */
	public SystemDto getSystemEntity(String id);
	
	/**
	 * @param aSystemDTO
	 * @param anCpuEntity
	 */
	public void updateSystemByCpu(SystemDto aSystemDTO, CpuMemory anCpuEntity) ;
	
	/**
	 * @param isDeleted
	 * @return
	 * @throws Exception
	 */
	SystemMonitor[] listSystems(boolean isDeleted) throws Exception;
	
	/**
	 * @param system
	 */
	void addSystem(SystemMonitor system);

	/**
	 * @param system
	 */
	void removeSystem(SystemMonitor system);

	/**
	 * @param system
	 * @param alert
	 */
	public void updateSystem(SystemMonitor system);
	
	/**
	 * @param system
	 * @param alert
	 */
	public void updateSystem(SystemDto system, AlertMonitor alert);
	
	/**
	 * @return
	 */
	List<SystemMonitor> listSystems();
	
	public SystemMonitor getSystembyID(String id) throws Exception;
	
	public boolean addnewSystem(SystemMonitor system) throws Exception;
	
	public boolean editSystembyID(String id, String newName, String newAddress, String protocol, String group,
			boolean isActive) throws Exception;
	
	public boolean deleteSystembyID(String id) throws Exception;
	
	
	public boolean deleteListSystembyID(String[] ids) throws Exception;
	
	public String getIPbyURL(String url) throws Exception;
	
	public String createCode() throws Exception;
	
	Date getLastestTimeStamp(SystemMonitor system, String className);
	
	String getCurrentHealthStatus(SystemMonitor system);	
}
