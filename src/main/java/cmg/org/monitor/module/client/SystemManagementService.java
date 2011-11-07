package cmg.org.monitor.module.client;

import cmg.org.monitor.entity.shared.SystemMonitor;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
@RemoteServiceRelativePath("SystemManagement")
public interface SystemManagementService extends RemoteService {
	SystemMonitor[] listSystem(boolean isDeleted) throws Exception;
	String editSystem(String id) throws Exception;
	boolean deleteSystem(String id) throws Exception;
	boolean deleteListSystem(String[] ids) throws Exception;
}
