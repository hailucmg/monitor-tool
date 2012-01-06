package cmg.org.monitor.dao;

import java.util.ArrayList;

import cmg.org.monitor.entity.shared.MemoryMonitor;
import cmg.org.monitor.entity.shared.SystemMonitor;

public interface MemoryDAO {
	
	public void storeMemory(SystemMonitor sys, MemoryMonitor mem);
	
	public MemoryMonitor getLastestMemory(SystemMonitor sys, int type);
	
	public ArrayList<MemoryMonitor> listMemory(SystemMonitor sys, int type);
	
}
