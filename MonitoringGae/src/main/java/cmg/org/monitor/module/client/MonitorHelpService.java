package cmg.org.monitor.module.client;

import cmg.org.monitor.ext.model.shared.UserLoginDto;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;


@RemoteServiceRelativePath("MonitorHelp")
public interface MonitorHelpService extends RemoteService {
	UserLoginDto getUserLogin();
}
