package cmg.org.monitor.module.server;

import cmg.org.monitor.ext.model.shared.UserLoginDto;
import cmg.org.monitor.module.client.MonitorHelpService;
import cmg.org.monitor.services.MonitorLoginService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class MonitorHelpServiceImpl extends RemoteServiceServlet implements MonitorHelpService {

	/** Default serial UUID value*/
	private static final long serialVersionUID = 1L;

	@Override
	public UserLoginDto getUserLogin() {
		return MonitorLoginService.getUserLogin();
	}

}
