package cmg.org.monitor.services;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import cmg.org.monitor.dao.InviteUserDAO;
import cmg.org.monitor.dao.SystemAccountDAO;
import cmg.org.monitor.dao.UtilityDAO;
import cmg.org.monitor.dao.impl.InviteUserDaoImpl;
import cmg.org.monitor.dao.impl.SystemAccountDaoImpl;
import cmg.org.monitor.dao.impl.UtilityDaoImpl;
import cmg.org.monitor.entity.shared.InvitedUser;
import cmg.org.monitor.entity.shared.SystemRole;
import cmg.org.monitor.entity.shared.SystemUser;
import cmg.org.monitor.ext.model.shared.UserLoginDto;
import cmg.org.monitor.ext.model.shared.UserMonitor;
import cmg.org.monitor.memcache.Key;
import cmg.org.monitor.memcache.MonitorMemcache;

import cmg.org.monitor.util.shared.MonitorConstant;


import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;

public class MonitorLoginService {
	private static final Logger logger = Logger.getLogger(MonitorLoginService.class.getCanonicalName());
	private UserService userService = UserServiceFactory.getUserService();

	public static List<InvitedUser> temp3rdUsers;
	private String sessionID;

	public UserLoginDto getUserLogin() {
		UserLoginDto userLogin = new UserLoginDto();
		Object obj = MonitorMemcache.get(Key.create(Key.PROJECT_HOST_NAME));
		String projectHostName = "";
		if (obj != null) {
			projectHostName = String.valueOf(obj);
		} else {
			projectHostName = "http://" + MonitorConstant.PROJECT_HOST_NAME;
		}
		userLogin.setLogoutUrl(userService.createLogoutURL(projectHostName, MonitorConstant.DOMAIN));
		userLogin.setLoginUrl(userService.createLoginURL(projectHostName, MonitorConstant.DOMAIN));
		try {
			User user = userService.getCurrentUser();
			if (user != null) {			

				userLogin.setAuthURL("https://accounts.google.com/o/oauth2/auth?response_type=token&redirect_uri=http%3A%2F%2F" + MonitorConstant.PROJECT_HOST_NAME + "&client_id="
						+ MonitorConstant.OAUTH_CLIENT_ID + "&approval_prompt=force&scope=https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fplus.me");
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
					SystemUser sysUser = null;
					if (list != null && list.size() > 0) {
						for (SystemUser u : list) {
							if (u.getEmail().equalsIgnoreCase(user.getEmail())) {
								sysUser = u;
								break;
							}
						}
					}

					userLogin.setRole(MonitorConstant.ROLE_GUEST);
					if (sysUser != null) {
						userLogin.setFirstName(sysUser.getFirstName());
						userLogin.setLastName(sysUser.getLastName());
						userLogin.setGroupIds(sysUser.getGroupIDs());
						if (sysUser.checkRole(SystemRole.ROLE_ADMINISTRATOR)) {
							userLogin.setRole(MonitorConstant.ROLE_ADMIN);
						} else if (sysUser.checkRole(SystemRole.ROLE_USER)) {
							userLogin.setRole(MonitorConstant.ROLE_NORMAL_USER);
						}
					} else {
						InviteUserDAO invUserDAO = new InviteUserDaoImpl();
						List<InvitedUser> list3rdUser = invUserDAO.list3rdUser();
						boolean check = false;
						if (getTemp3rdUsers() == null) {
							setTemp3rdUsers(new ArrayList<InvitedUser>());
						}
						for (InvitedUser tempU : getTemp3rdUsers()) {
							if (tempU.getEmail().equalsIgnoreCase(user.getEmail())) {
								userLogin.setGroupIds(tempU.getGroupIDs());
								check = true;
								break;
							}
						}
						if (check) {
							userLogin.setRole(MonitorConstant.ROLE_NORMAL_USER);
						} else {
							if (list3rdUser != null && list3rdUser.size() > 0) {
								for (InvitedUser u : list3rdUser) {
									if (u.getEmail().equalsIgnoreCase(user.getEmail()) && u.getStatus().equalsIgnoreCase(InvitedUser.STATUS_PENDING)) {
										userLogin.setRole(MonitorConstant.ROLE_NORMAL_USER);
										userLogin.setGroupIds(u.getGroupIDs());
										userLogin.setFirstName(u.getFirstName());
										userLogin.setLastName(u.getLastName());
										getTemp3rdUsers().add(u);
										Gson gson = new Gson();
										MonitorMemcache.put(Key.create(Key.TEMP_INVITED_USERS), gson.toJson(getTemp3rdUsers()));
										break;
									}
								}
							}

						}
					}
				}
				userLogin.setLogin(true);
			}

		} catch (Exception ex) {
			logger.log(Level.SEVERE, "ERROR when login. Message: " + ex.getMessage());
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

	/**
	 * @return the temp3rdUsers
	 */
	public static List<InvitedUser> getTemp3rdUsers() {
		return temp3rdUsers;
	}

	/**
	 * @param temp3rdUsers
	 *            the temp3rdUsers to set
	 */

	public static void setTemp3rdUsers(List<InvitedUser> temp3rdUsers) {
		MonitorLoginService.temp3rdUsers = temp3rdUsers;
	}

	/**
	 * @return the sessionID
	 */
	public String getSessionID() {
		return sessionID;
	}

	/**
	 * @param sessionID
	 *            the sessionID to set
	 */

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}
}
