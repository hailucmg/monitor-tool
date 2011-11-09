package cmg.org.monitor.module.client;
import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.ext.model.shared.MonitorEditDto;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface EditSystemServiceAsync {

	void getSystembyID(String id, AsyncCallback<MonitorEditDto> asyncCallback);

	void editSystembyID(String id, String newName, String newAddress,
			String group, String protocol, String ip, String remoteURL,
			boolean isActive, AsyncCallback<Boolean> callback);

}
