package cmg.org.monitor.module.client;

import cmg.org.monitor.entity.shared.SystemMonitor;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AddnewSystemServiceAsync {

	void addSystem(SystemMonitor system, String url,
			AsyncCallback<String> callback);

	void groups(AsyncCallback<String[]> callback);




}
