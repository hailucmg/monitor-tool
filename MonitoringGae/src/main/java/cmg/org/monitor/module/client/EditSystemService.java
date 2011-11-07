package cmg.org.monitor.module.client;

import cmg.org.monitor.entity.shared.SystemMonitor;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
@RemoteServiceRelativePath("EditSystem")
public interface EditSystemService extends RemoteService {
	SystemMonitor getSystembyID(String id) throws Exception;
	 boolean editSystembyID(String id, String newName, String newAddress,String group,String protocol,
			 boolean isActive) throws Exception; 
}
