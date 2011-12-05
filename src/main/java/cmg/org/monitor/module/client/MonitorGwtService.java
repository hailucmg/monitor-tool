package cmg.org.monitor.module.client;

import java.util.ArrayList;
import java.util.Map;

import cmg.org.monitor.entity.shared.CpuMemory;
import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.ext.model.shared.MonitorEditDto;
import cmg.org.monitor.ext.model.shared.UserDto;
import cmg.org.monitor.ext.model.shared.UserLoginDto;
import cmg.org.monitor.memcache.shared.SystemMonitorDto;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("MonitorGwtService")
public interface MonitorGwtService extends RemoteService {
	String addSystem(SystemMonitorDto system, String url) throws Exception;

	String[] groups() throws Exception;

	UserLoginDto getUserLogin();

	ArrayList<SystemMonitorDto> listSystems();

	SystemMonitorDto getSystembyID(String id) throws Exception;

	String editSystembyID(SystemMonitorDto system) throws Exception;

	boolean validSystemId(String sysID);

	boolean deleteSystem(String id) throws Exception;

	Map<String, UserDto> listUser() throws Exception;
	
	String getAboutContent();
	
	String getHelpContent();
	
	SystemMonitorDto getLastestDataMonitor(String sid);
}
