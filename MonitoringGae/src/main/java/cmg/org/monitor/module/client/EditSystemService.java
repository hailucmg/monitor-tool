package cmg.org.monitor.module.client;

import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.ext.model.shared.MonitorEditDto;
import cmg.org.monitor.ext.model.shared.UserLoginDto;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
@RemoteServiceRelativePath("EditSystem")
public interface EditSystemService extends RemoteService {
	MonitorEditDto getSystembyID(String id) throws Exception;
	 boolean editSystembyID(String id, String newName, String newAddress,String group,String protocol, String ip
			,String remoteURL ,boolean isActive) throws Exception; 
	 UserLoginDto getUserLogin();
}
