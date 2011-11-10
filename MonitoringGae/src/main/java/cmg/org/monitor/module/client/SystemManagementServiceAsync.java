package cmg.org.monitor.module.client;

import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.ext.model.shared.UserLoginDto;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface SystemManagementServiceAsync {

	void deleteListSystem(String[] ids, AsyncCallback<Boolean> callback);

	void deleteSystem(String id, AsyncCallback<Boolean> callback);

	void editSystem(String id, AsyncCallback<String> callback);

	void listSystem(boolean isDeleted, AsyncCallback<SystemMonitor[]> callback);

	void getUserLogin(AsyncCallback<UserLoginDto> callback);

}
