package cmg.org.monitor.module.server;



import cmg.org.monitor.dao.impl.SystemMonitorDaoJDOImpl;
import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.module.client.EditSystemService;
import cmg.org.monitor.services.SystemService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class EditSystemServiceImpl extends RemoteServiceServlet implements EditSystemService{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public SystemMonitor getSystembyID(String id) {
		// TODO Auto-generated method stub
		try {
			SystemMonitorDaoJDOImpl systemDAO = new SystemMonitorDaoJDOImpl();
			return systemDAO.getSystembyID(id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean editSystembyID(String id, String newName, String newAddress,String protocol,String group,
			boolean isActive) throws Exception {
		// TODO Auto-generated method stub
		boolean b = false;
		try {
			SystemMonitorDaoJDOImpl systemDAO = new SystemMonitorDaoJDOImpl();
			 b = systemDAO.editSystembyID(id, newName, newAddress, protocol, group, isActive);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return b;
	}

}
