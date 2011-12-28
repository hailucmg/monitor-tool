package cmg.org.monitor.services;

import java.util.logging.Level;
import java.util.logging.Logger;

import cmg.org.monitor.ext.model.shared.UserLoginDto;
import cmg.org.monitor.util.shared.HTMLControl;
import cmg.org.monitor.util.shared.MonitorConstant;
import cmg.org.monitor.util.shared.Utility;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class MonitorLoginService {
	private static final Logger logger = Logger.getLogger(MonitorLoginService.class
			.getCanonicalName());
	private static  UserService userService = UserServiceFactory
			.getUserService();
	public static UserLoginDto getUserLogin() {
		UserLoginDto userLogin = new UserLoginDto();
		userLogin.setLogoutUrl(userService.createLogoutURL(HTMLControl.LOGIN_SERVLET_NAME, MonitorConstant.DOMAIN));
		userLogin.setLoginUrl(userService.createLoginURL(HTMLControl.HTML_INDEX_NAME, MonitorConstant.DOMAIN));		
		try {
			
			User user = userService.getCurrentUser();
			if (user != null) {
				userLogin.setAuthDomain(user.getAuthDomain());
				userLogin.setEmail(user.getEmail());
				userLogin.setNickName(user.getNickname());
				userLogin.setUserId(user.getUserId());
				userLogin.setRole(Utility.getSystemRole(user.getEmail()));				
				userLogin.setLogin(true);
			}

		} catch (Exception ex) {
			logger.log(Level.SEVERE, "ERROR when login. Message: " + ex.getMessage());
		}
		return userLogin;
	}
}
