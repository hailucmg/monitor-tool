package cmg.org.monitor.services;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import cmg.org.monitor.dao.SystemAccountDAO;
import cmg.org.monitor.dao.UtilityDAO;
import cmg.org.monitor.dao.impl.SystemAccountDaoImpl;
import cmg.org.monitor.dao.impl.UtilityDaoImpl;
import cmg.org.monitor.entity.shared.SystemRole;
import cmg.org.monitor.entity.shared.SystemUser;
import cmg.org.monitor.ext.model.shared.UserLoginDto;
import cmg.org.monitor.ext.model.shared.UserMonitor;
import cmg.org.monitor.util.shared.HTMLControl;
import cmg.org.monitor.util.shared.MonitorConstant;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class MonitorLoginService {
	private static final Logger logger = Logger
			.getLogger(MonitorLoginService.class.getCanonicalName());
	private UserService userService = UserServiceFactory.getUserService();

	public UserLoginDto getUserLogin() {
		UserLoginDto userLogin = new UserLoginDto();
		userLogin.setLogoutUrl(userService.createLogoutURL(
				HTMLControl.LOGIN_SERVLET_NAME, MonitorConstant.DOMAIN));
		userLogin.setLoginUrl(userService.createLoginURL(
				HTMLControl.HTML_INDEX_NAME, MonitorConstant.DOMAIN));
		try {
			User user = userService.getCurrentUser();
			if (user != null) {
				userLogin.setAuthDomain(user.getAuthDomain());
				userLogin.setEmail(user.getEmail());
				userLogin.setNickName(user.getNickname());
				userLogin.setUserId(user.getUserId());
				SystemAccountDAO accountDao = new SystemAccountDaoImpl();				
				List<SystemUser> list = null;
				try {
					list = accountDao.listAllSystemUser(true);
				} catch (Exception e) {
					//
				}
				
				if (userService.isUserAdmin()) {
					if (list == null || list.size() == 0) {
						userLogin.setNeedAddAccount(true);
					}
					userLogin.setAdmin(true);
					userLogin.setRole(MonitorConstant.ROLE_ADMIN);
				} else {
					SystemUser sysUser = accountDao.getSystemUserByEmail(user
							.getEmail());
					userLogin.setRole(MonitorConstant.ROLE_GUEST);
					if (sysUser != null) {
						userLogin.setGroupIds(sysUser.getGroupIDs());
						if (sysUser.checkRole(SystemRole.ROLE_ADMINISTRATOR)) {
							userLogin.setRole(MonitorConstant.ROLE_ADMIN);
						} else if (sysUser.checkRole(SystemRole.ROLE_USER)) {
							userLogin.setRole(MonitorConstant.ROLE_NORMAL_USER);
						}
					}
				}
				userLogin.setLogin(true);
			}

		} catch (Exception ex) {
			logger.log(Level.SEVERE,
					"ERROR when login. Message: " + ex.getMessage());
		}
		return userLogin;
	}

	public static int getSystemRole(String userId) throws Exception {
		int role = MonitorConstant.ROLE_GUEST;
		UtilityDAO utilDAO = new UtilityDaoImpl();
		ArrayList<UserMonitor> users = utilDAO.listAllUsers();
		if (users != null && users.size() > 0) {
			for (UserMonitor user : users) {
				if (user.getId().equalsIgnoreCase(userId)) {
					role = user.getRole();
					break;
				}
			}
		}
		return role;
	}
}
