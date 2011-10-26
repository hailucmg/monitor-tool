package cmg.org.monitor.dao.impl;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import cmg.org.monitor.dao.CpuMemoryDAO;
import cmg.org.monitor.entity.shared.CpuMemory;
import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.util.shared.PMF;

public class CpuMemoryDaoJDOImpl implements CpuMemoryDAO {
	@Override
	public CpuMemory[] getLastestCpuMemory(SystemMonitor system, int numberOfResult) throws Exception {
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		CpuMemory[] cpuMem = null;
		List<CpuMemory> list = null;
		Query query = pm.newQuery(CpuMemory.class);
		query.setFilter("systemMonitor == sys");
		query.declareParameters("SystemMonitor sys");
		query.setOrdering("timeStamp desc");
		query.setRange(0, numberOfResult);
		try {
			list = (List<CpuMemory>) query.execute(system);
			if (list.size() > 0) {
				cpuMem = new CpuMemory[list.size()];
				for (int i = 0; i < list.size(); i++) {
					cpuMem[i] = list.get(i);
				}
			}
		}
		catch (Exception ex) {
			throw ex;
		}
		finally {
			query.closeAll();
			pm.close();
		}
		return cpuMem;
	}
	
	@Override
	public void addCpuMemory(SystemMonitor system, CpuMemory cpuMemory) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		system.addCpuMemory(cpuMemory);
		try {			
			pm.makePersistent(system);
		} finally {
			pm.close();
		}
	}

}
