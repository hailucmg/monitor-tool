package cmg.org.monitor.services;

import java.util.logging.Level;
import java.util.logging.Logger;

import cmg.org.monitor.ext.model.shared.UserLoginDto;
import cmg.org.monitor.module.server.DashBoardServiceImpl;
import cmg.org.monitor.util.shared.HTMLControl;
import cmg.org.monitor.util.shared.MonitorConstant;
import cmg.org.monitor.util.shared.Ultility;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class MonitorLoginService {
	private static final Logger logger = Logger.getLogger(DashBoardServiceImpl.class
			.getCanonicalName());
	private static  UserService userService = UserServiceFactory
			.getUserService();
	public static UserLoginDto getUserLogin() {
		UserLoginDto userLogin = new UserLoginDto(false);
		userLogin.setLogoutUrl(userService.createLogoutURL(HTMLControl.HTML_DASHBOARD_NAME, MonitorConstant.DOMAIN));
		userLogin.setLoginUrl(userService.createLoginURL(HTMLControl.HTML_DASHBOARD_NAME, MonitorConstant.DOMAIN));
		try {
			User user = userService.getCurrentUser();
			if (user != null) {
				userLogin.setAuthDomain(user.getAuthDomain());
				userLogin.setEmail(user.getEmail());
				userLogin.setNickName(user.getNickname());
				userLogin.setUserId(user.getUserId());
				userLogin.setRole(Ultility.getSystemRole(user.getEmail()));				
				userLogin.setLogin(true);
			}

		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getCause().getMessage());
		}
		return userLogin;
	}
}
