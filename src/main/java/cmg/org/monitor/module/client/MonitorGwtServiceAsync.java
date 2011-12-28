package cmg.org.monitor.module.client;

import java.util.ArrayList;

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

	void listSystems(AsyncCallback<ArrayList<SystemMonitor>> callback);

	void validSystemId(String sysID, AsyncCallback<SystemMonitor> callback);

	void getSystemMonitorContainer(AsyncCallback<MonitorContainer> callback);

	void addSystem(SystemMonitor system, AsyncCallback<Boolean> callback);

	void listAllUsers(AsyncCallback<ArrayList<UserMonitor>> callback);

}
