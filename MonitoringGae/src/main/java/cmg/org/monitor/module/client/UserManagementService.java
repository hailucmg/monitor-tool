package cmg.org.monitor.module.client;

import java.util.Map;



import cmg.org.monitor.ext.model.shared.UserDto;
import cmg.org.monitor.ext.model.shared.UserLoginDto;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
@RemoteServiceRelativePath("usermanagement")
public interface UserManagementService extends RemoteService{
	Map<String, UserDto> listUser() throws Exception;
	
	UserLoginDto getUserLogin();
}
