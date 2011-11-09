package cmg.org.monitor.module.client;

import cmg.org.monitor.entity.shared.CpuMemory;
import cmg.org.monitor.entity.shared.SystemMonitor;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface SystemDetailServiceAsync {

	void getLastestDataMonitor(String sysID,
			AsyncCallback<SystemMonitor> callback);

	void validSystemId(String sysID, AsyncCallback<Boolean> callback);

	void listCpuMemoryHistory(String sysID, AsyncCallback<CpuMemory[]> callback);

}
