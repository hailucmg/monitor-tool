package cmg.org.monitor.module.client;

import cmg.org.monitor.ext.model.shared.UserLoginDto;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface MonitorAboutServiceAsync {

	void getUserLogin(AsyncCallback<UserLoginDto> callback);

}
