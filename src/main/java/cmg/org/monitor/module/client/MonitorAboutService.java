package cmg.org.monitor.module.client;

import cmg.org.monitor.ext.model.shared.UserLoginDto;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("MonitorAbout")
public interface MonitorAboutService extends RemoteService {
	UserLoginDto getUserLogin();
}
