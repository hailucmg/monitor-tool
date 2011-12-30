package cmg.org.monitor.module.client;

import java.util.ArrayList;

import cmg.org.monitor.entity.shared.CpuMonitor;
import cmg.org.monitor.entity.shared.FileSystemMonitor;
import cmg.org.monitor.entity.shared.JvmMonitor;
import cmg.org.monitor.entity.shared.MemoryMonitor;
import cmg.org.monitor.entity.shared.ServiceMonitor;
import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.ext.model.shared.MonitorContainer;
import cmg.org.monitor.ext.model.shared.UserLoginDto;
import cmg.org.monitor.ext.model.shared.UserMonitor;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface MonitorGwtServiceAsync {

	void deleteSystem(String id, AsyncCallback<Boolean> callback);

	void getAboutContent(AsyncCallback<String> callback);

	void getHelpContent(AsyncCallback<String> callback);

	void getUserLogin(AsyncCallback<UserLoginDto> callback);

	void listSystems(AsyncCallback<SystemMonitor[]> callback);

	void validSystemId(String sysID, AsyncCallback<SystemMonitor> callback);

	void getSystemMonitorContainer(String sysId,
			AsyncCallback<MonitorContainer> callback);

	void getSystemMonitorContainer(AsyncCallback<MonitorContainer> callback);

	void addSystem(SystemMonitor system, AsyncCallback<Boolean> callback);

	void listAllUsers(AsyncCallback<UserMonitor[]> callback);

	void editSystem(SystemMonitor sys, AsyncCallback<Boolean> callback);

	void listJvms(SystemMonitor sys, AsyncCallback<JvmMonitor[]> callback);

	void listCpus(SystemMonitor sys,
			AsyncCallback<CpuMonitor[]> callback);

	void listFileSystems(SystemMonitor sys,
			AsyncCallback<FileSystemMonitor[]> callback);

	void listServices(SystemMonitor sys,
			AsyncCallback<ServiceMonitor[]> callback);

	void listMems(SystemMonitor sys, AsyncCallback<MonitorContainer> callback);

}
