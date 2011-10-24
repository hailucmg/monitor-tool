package cmg.org.monitor.entry.dashboard.client;


import cmg.org.monitor.entity.shared.SystemMonitor;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
@RemoteServiceRelativePath("monitordashboard")
public interface DashBoardService extends RemoteService {
	SystemMonitor[] listSystems();
	void addSystem(SystemMonitor sys);
}
