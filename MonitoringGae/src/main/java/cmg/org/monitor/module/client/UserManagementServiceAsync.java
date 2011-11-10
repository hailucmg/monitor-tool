package cmg.org.monitor.module.client;

import java.util.Map;

import cmg.org.monitor.ext.model.shared.UserDto;
import cmg.org.monitor.ext.model.shared.UserLoginDto;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface UserManagementServiceAsync {

	void listUser(AsyncCallback<Map<String, UserDto>> callback);

	void getUserLogin(AsyncCallback<UserLoginDto> callback);

}
