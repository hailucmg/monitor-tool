package cmg.org.monitor.entry.dashboard.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.entry.dashboard.client.DashBoardService;
import cmg.org.monitor.services.SystemService;

public class DashBoardServiceImpl extends RemoteServiceServlet implements DashBoardService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public SystemMonitor[] listSystems() {		
		return SystemService.listSystems(false);
	}

	@Override
	public void addSystem(SystemMonitor sys) {
		SystemService.addSystemMonitor(sys);
	}
	
}
