package cmg.org.monitor.module.client;

import java.util.ArrayList;
import java.util.Map;

import cmg.org.monitor.entity.shared.CpuMemory;
import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.ext.model.shared.MonitorEditDto;
import cmg.org.monitor.ext.model.shared.UserDto;
import cmg.org.monitor.ext.model.shared.UserLoginDto;
import cmg.org.monitor.memcache.shared.SystemMonitorDto;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface MonitorGwtServiceAsync {

	void deleteSystem(String id, AsyncCallback<Boolean> callback);

	void editSystembyID(SystemMonitorDto system, AsyncCallback<String> callback);

	void getSystembyID(String id, AsyncCallback<SystemMonitorDto> callback);

	void getUserLogin(AsyncCallback<UserLoginDto> callback);
	
	void listUser(AsyncCallback<Map<String, UserDto>> callback);

	void validSystemId(String sysID, AsyncCallback<Boolean> callback);

	void addSystem(SystemMonitorDto system, String url,
			AsyncCallback<String> callback);

	void getAboutContent(AsyncCallback<String> callback);

	void getHelpContent(AsyncCallback<String> callback);

	void listSystems(AsyncCallback<ArrayList<SystemMonitorDto>> callback);

	void groups(AsyncCallback<String[]> callback);

	void getLastestDataMonitor(String sid,
			AsyncCallback<SystemMonitorDto> callback);

}
