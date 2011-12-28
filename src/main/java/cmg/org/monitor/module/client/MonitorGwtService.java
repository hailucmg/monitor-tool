package cmg.org.monitor.module.client;

import java.util.ArrayList;

import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.ext.model.shared.MonitorContainer;
import cmg.org.monitor.ext.model.shared.UserLoginDto;
import cmg.org.monitor.ext.model.shared.UserMonitor;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("MonitorGwtService")
public interface MonitorGwtService extends RemoteService {
	boolean addSystem(SystemMonitor system);
	
	UserLoginDto getUserLogin();
	
	ArrayList<SystemMonitor> listSystems();

	SystemMonitor validSystemId(String sysID);

	boolean deleteSystem(String id) throws Exception;

	String getAboutContent();
	
	String getHelpContent();
	
	MonitorContainer getSystemMonitorContainer();
	
	ArrayList<UserMonitor> listAllUsers();
}
