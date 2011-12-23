package cmg.org.monitor.module.client;

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
	String addSystem(SystemMonitor system, String url) throws Exception;

	String[] groups() throws Exception;

	UserLoginDto getUserLogin();

	SystemMonitor[] listSystems();

	MonitorEditDto getSystembyID(String id) throws Exception;

	String editSystembyID(MonitorEditDto system,SystemMonitor sysNew) throws Exception;

	SystemMonitor getLastestDataMonitor(String sysID);

	SystemMonitor validSystemId(String sysID);

	CpuMemory[] listCpuMemoryHistory(String sysID);

	SystemMonitor[] listSystem(boolean isDeleted) throws Exception;

	String editSystem(String id) throws Exception;

	boolean deleteSystem(String id) throws Exception;

	boolean deleteListSystem(String[] ids) throws Exception;

	Map<String, UserDto> listUser() throws Exception;
	
	String getAboutContent();
	
	String getHelpContent();
}
