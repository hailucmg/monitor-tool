package cmg.org.monitor.dao;

import java.util.ArrayList;

import cmg.org.monitor.entity.shared.CpuMonitor;
import cmg.org.monitor.entity.shared.SystemMonitor;

/**
 * @author HongHai
 * @version 1.0
 */
public interface CpuDAO {
	
	public void storeCpu(SystemMonitor sys, CpuMonitor cpu);
	
	public CpuMonitor getLastestCpu(SystemMonitor sys);
	
	public ArrayList<CpuMonitor> listCpu(SystemMonitor sys);
	
}
