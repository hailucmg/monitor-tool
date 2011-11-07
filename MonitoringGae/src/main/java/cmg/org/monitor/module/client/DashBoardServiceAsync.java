package cmg.org.monitor.module.client;

import cmg.org.monitor.entity.shared.SystemMonitor;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DashBoardServiceAsync {
	void listSystems(AsyncCallback<SystemMonitor[]> callback);
}
