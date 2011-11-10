package cmg.org.monitor.module.server;

import java.util.logging.Level;
import java.util.logging.Logger;

import cmg.org.monitor.dao.SystemMonitorDAO;
import cmg.org.monitor.dao.impl.SystemMonitorDaoJDOImpl;
import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.ext.model.shared.UserLoginDto;
import cmg.org.monitor.module.client.DashBoardService;
import cmg.org.monitor.services.MonitorLoginService;
import cmg.org.monitor.services.MonitorService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DashBoardServiceImpl extends RemoteServiceServlet implements DashBoardService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = Logger.getLogger(DashBoardServiceImpl.class
			.getCanonicalName());

	@Override
	public SystemMonitor[] listSystems() {	
		SystemMonitorDAO sysDAO =  new SystemMonitorDaoJDOImpl();
		SystemMonitor[] list = null;
		try {
			list = sysDAO.listSystems(false);
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getCause().getMessage());
		}
		return list;
	}

	@Override
	public UserLoginDto getUserLogin() {
		return MonitorLoginService.getUserLogin();
	}
	
}
