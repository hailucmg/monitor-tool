package cmg.org.monitor.module.server;

import cmg.org.monitor.dao.SystemMonitorDAO;
import cmg.org.monitor.dao.impl.SystemMonitorDaoJDOImpl;
import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.module.client.DashBoardService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DashBoardServiceImpl extends RemoteServiceServlet implements DashBoardService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public SystemMonitor[] listSystems() {	
		SystemMonitorDAO sysDAO =  new SystemMonitorDaoJDOImpl();
		SystemMonitor[] list = null;
		try {
			list = sysDAO.listSystems(false);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return list;
	}
	
}
