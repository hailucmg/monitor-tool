package cmg.org.monitor.entry.dashboard.client;

import cmg.org.monitor.entity.shared.SystemMonitor;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DashBoardServiceAsync {
	void listSystems(AsyncCallback<SystemMonitor[]> callback);

	void addSystem(SystemMonitor sys, AsyncCallback<Void> callback);
}
