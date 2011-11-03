package cmg.org.monitor.dao;

import java.util.List;

import cmg.org.monitor.entity.shared.AlertMonitor;
import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.ext.model.dto.SystemDto;

public interface SystemMonitorDAO {
	
	
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
}
