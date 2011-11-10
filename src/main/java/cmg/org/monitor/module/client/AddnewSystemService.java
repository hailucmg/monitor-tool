package cmg.org.monitor.module.client;


import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.ext.model.shared.UserLoginDto;

import com.google.common.collect.Maps;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
@RemoteServiceRelativePath("AddSystem")
public interface AddnewSystemService extends RemoteService {
	String addSystem(SystemMonitor system,String url) throws Exception;
	String[] groups() throws Exception;
	UserLoginDto getUserLogin();
}
