package cmg.org.monitor.dao;

import java.util.List;

import cmg.org.monitor.entity.shared.CpuMemory;
import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.ext.model.shared.CpuDto;
import cmg.org.monitor.ext.model.shared.SystemDto;

/**
 * @author HongHai and Lamphan
 * @version 1.0
 */
public interface CpuMemoryDAO {
	
	/**
	 * Get the lastest Cpu object by given number.<br>
	 * 
	 * @param system System object
	 * @param numberOfResult number of record.
	 * @return collection of cpu objects.
	 */
	public List<CpuDto> getLastestCpuMemory(SystemDto system, int numberOfResult)throws Exception;

	CpuMemory[] getLastestCpuMemory(SystemMonitor system, int numberOfResult)
			throws Exception;

	void addCpuMemory(SystemMonitor system, CpuMemory cpuMemory);

	/**
	 * Update or add cpu object to JDO.<br>
	 * 
	 * @param cpuDto
	 * @param systemDto
	 * @return
	 */
	public CpuDto updateCpu(CpuDto cpuDto, SystemDto systemDto);

}
