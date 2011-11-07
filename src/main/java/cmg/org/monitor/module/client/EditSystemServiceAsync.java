package cmg.org.monitor.module.client;

import cmg.org.monitor.entity.shared.SystemMonitor;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface EditSystemServiceAsync {

	void getSystembyID(String id, AsyncCallback<SystemMonitor> callback);

	void editSystembyID(String id, String newName, String newAddress,String group,String protocol,
			boolean isActive, AsyncCallback<Boolean> callback);

}
