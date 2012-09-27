package cmg.org.monitor.dao.impl;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;

import cmg.org.monitor.dao.SystemGroupDAO;
import cmg.org.monitor.entity.shared.SystemGroup;
import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.services.PMF;

public class SystemGroupDaoImpl implements SystemGroupDAO{
	private static final Logger logger = Logger.getLogger(SystemGroupDaoImpl.class
			.getCanonicalName());
	PersistenceManager pm;

	void initPersistence() {
		if (pm == null || pm.isClosed()) {
			pm = PMF.get().getPersistenceManager();
		}
	}

	@Override
	public boolean addNewGroup(SystemGroup sg)
			throws Exception {
		return false;
	}

	@Override
	public SystemGroup getByID(String id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean updateGroup(String id, String groupName,
			String groupDescription) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteGroup(String id) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public SystemGroup[] getAllGroup() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
