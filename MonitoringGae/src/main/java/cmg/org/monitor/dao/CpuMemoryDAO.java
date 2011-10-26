package cmg.org.monitor.dao;

import cmg.org.monitor.entity.shared.CpuMemory;
import cmg.org.monitor.entity.shared.SystemMonitor;

public interface CpuMemoryDAO {
	CpuMemory[] getLastestCpuMemory(SystemMonitor system, int numberOfResult) throws Exception ;
	
	void addCpuMemory(SystemMonitor system, CpuMemory cpuMemory);
}
