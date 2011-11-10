package cmg.org.monitor.module.server;

import java.util.HashMap;
import java.util.Map;

import cmg.org.monitor.ext.model.shared.UserDto;
import cmg.org.monitor.ext.model.shared.UserLoginDto;
import cmg.org.monitor.module.client.UserManagementService;
import cmg.org.monitor.services.MonitorLoginService;
import cmg.org.monitor.util.shared.Ultility;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class UserManagementServiceImpl  extends RemoteServiceServlet implements UserManagementService{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public UserLoginDto getUserLogin() {
		return MonitorLoginService.getUserLogin();
	}
	@Override
	public Map<String, UserDto> listUser() throws Exception {
		// TODO Auto-generated method stub
		String[] admins =null;
		String[] users =null;
		Map<String, UserDto> list = new HashMap<String, UserDto>();
		
		try {
			
			admins = Ultility.listAdmin();
			for(int i = 0; i <admins.length;i++){
				String[] temp = admins[i].split(":");
				UserDto u = new UserDto();
				u.setUsername(temp[0].split("@")[0]);
				u.setEmail(temp[0]);
				u.setGroup(temp[1]);
				list.put(u.getUsername().toString().trim(),u);
			}
			
		    users =Ultility.listUser();
		    
			for(int j = 0 ; j< users.length;j++){
				
				String[] temp = users[j].split(":");
				UserDto u = new UserDto();
				u.setUsername(temp[0].split("@")[0]);
				u.setEmail(temp[0]);
				u.setGroup(temp[1]);
				
				if(!list.containsKey(u.getUsername().toString().trim())){
					list.put(u.getUsername(),u);
				}
				
			}
			
		} catch (Exception e) {
		}
		return list;
	}
	
	
}
