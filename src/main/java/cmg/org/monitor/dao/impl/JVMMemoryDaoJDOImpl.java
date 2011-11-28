package cmg.org.monitor.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import cmg.org.monitor.dao.JVMMemoryDAO;
import cmg.org.monitor.entity.shared.CpuMemory;
import cmg.org.monitor.entity.shared.JVMMemory;
import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.util.shared.PMF;

public class JVMMemoryDaoJDOImpl implements JVMMemoryDAO {

	@Override
	public JVMMemory getLastestJvm(SystemMonitor system) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		JVMMemory jvm = null;
		List<JVMMemory> list = new ArrayList<JVMMemory>();
		Query query = pm.newQuery(JVMMemory.class);
		query.setFilter("systemMonitor == sys");
		query.declareParameters("SystemMonitor sys");
		query.setOrdering("encodedKey desc");
		query.setRange(0, 1);
		try {
			list = (List<JVMMemory>) query.execute(system);
			if (list.size() > 0) {
				jvm = list.get(0);
			}
		} finally {
			query.closeAll();
			pm.close();
		}
		return jvm;
	}

}
