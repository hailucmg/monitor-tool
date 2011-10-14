package cmg.org.monitor.dao;

import java.util.List;

import cmg.org.monitor.entity.SystemMonitor;

public interface SystemMonitorDAO {
	void addSystem(SystemMonitor system);
	void removeSystem(SystemMonitor system);
	void updateSystem(SystemMonitor system);
	List<SystemMonitor> listSystems();
}
