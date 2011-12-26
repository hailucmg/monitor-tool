package cmg.org.monitor.module.client;

import java.util.Map;

import cmg.org.monitor.entity.shared.CpuMemory;
import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.ext.model.shared.MonitorEditDto;
import cmg.org.monitor.ext.model.shared.UserDto;
import cmg.org.monitor.ext.model.shared.UserLoginDto;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface MonitorGwtServiceAsync {

	void deleteListSystem(String[] ids, AsyncCallback<Boolean> callback);

	void deleteSystem(String id, AsyncCallback<Boolean> callback);

	void editSystem(String id, AsyncCallback<String> callback);

	void editSystembyID(String id,SystemMonitor sysNew, AsyncCallback<Boolean> callback);

	void getLastestDataMonitor(String sysID,
			AsyncCallback<SystemMonitor> callback);

	void getSystembyID(String id, AsyncCallback<MonitorEditDto> callback);

	void getUserLogin(AsyncCallback<UserLoginDto> callback);

	void listCpuMemoryHistory(String sysID, AsyncCallback<CpuMemory[]> callback);

	void listSystem(boolean isDeleted, AsyncCallback<SystemMonitor[]> callback);

	void listSystems(AsyncCallback<SystemMonitor[]> callback);

	void listUser(AsyncCallback<Map<String, UserDto>> callback);

	void validSystemId(String sysID, AsyncCallback<SystemMonitor> callback);

	void addSystem(SystemMonitor system, AsyncCallback<Boolean> callback);

	void groups(AsyncCallback<MonitorEditDto> callback);

	void getAboutContent(AsyncCallback<String> callback);

	void getHelpContent(AsyncCallback<String> callback);

}
