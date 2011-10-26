package cmg.org.monitor.dao;

import cmg.org.monitor.entity.shared.SystemMonitor;

public interface SystemMonitorDAO {
	void addSystem(SystemMonitor system);
	
	void removeSystem(SystemMonitor system);
	
	void updateSystem(SystemMonitor system);
	
	SystemMonitor[] listSystems(boolean isDeleted) throws Exception;
}
