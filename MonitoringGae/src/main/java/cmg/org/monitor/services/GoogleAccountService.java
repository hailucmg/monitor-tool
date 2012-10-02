/**
 * Copyright (c) CMG Ltd All rights reserved.
 *
 * This software is the confidential and proprietary information of CMG
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with CMG.
 */

package cmg.org.monitor.services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import cmg.org.monitor.dao.SystemAccountDAO;
import cmg.org.monitor.dao.SystemRoleDAO;
import cmg.org.monitor.dao.impl.SystemAccountDaoImpl;
import cmg.org.monitor.dao.impl.SystemRoleDaoImpl;
import cmg.org.monitor.entity.shared.GoogleAccount;
import cmg.org.monitor.entity.shared.SystemRole;
import cmg.org.monitor.entity.shared.SystemUser;
import cmg.org.monitor.util.shared.MonitorConstant;
import cmg.org.monitor.util.shared.SecurityUtil;

import com.google.gdata.client.GoogleAuthTokenFactory.UserToken;
import com.google.gdata.client.appsforyourdomain.AppsGroupsService;
import com.google.gdata.client.appsforyourdomain.UserService;
import com.google.gdata.data.Link;
import com.google.gdata.data.appsforyourdomain.AppsForYourDomainException;
import com.google.gdata.data.appsforyourdomain.Login;
import com.google.gdata.data.appsforyourdomain.Name;
import com.google.gdata.data.appsforyourdomain.generic.GenericEntry;
import com.google.gdata.data.appsforyourdomain.generic.GenericFeed;
import com.google.gdata.data.appsforyourdomain.provisioning.UserEntry;
import com.google.gdata.data.appsforyourdomain.provisioning.UserFeed;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

/**
 * DOCME
 * 
 * @Creator Hai Lu
 * @author $Author$
 * @version $Revision$
 * @Last changed: $LastChangedDate$
 */

public class GoogleAccountService {
	private static final String APPS_FEEDS_URL_BASE = "https://apps-apis.google.com/a/feeds/";

	protected static final String SERVICE_VERSION = "2.0";

	private static final String APPLICATION_NAME = "cmg-monitor-tools";

	private static final Logger logger = Logger
			.getLogger(GoogleAccountService.class.getCanonicalName());

	private AppsGroupsService groupService;

	private UserService userService;

	private SimpleDateFormat sdfTimestamp = new SimpleDateFormat(
			"dd/MM/yyyy hh:mm:ss.SSS");

	private List<SystemUser> users;

	private static final int LOG_INFO_LEVEL = 0x001;
	private static final int LOG_WARNING_LEVEL = 0x002;
	private static final int LOG_ERROR_LEVEL = 0x003;

	StringBuffer bufferLog;
	GoogleAccount adminAcc;

	protected GoogleAccountService() {
		//
	}

	public GoogleAccountService(GoogleAccount adminAcc)
			throws AuthenticationException {
		try {
			if (adminAcc == null) {
				throw new AuthenticationException("Null account object");
			}
			this.adminAcc = adminAcc;
			if (adminAcc.getToken() != null && adminAcc.getToken().length() > 0) {
				groupService = new AppsGroupsService(adminAcc.getDomain(),
						APPLICATION_NAME);
				groupService.setUserToken(adminAcc.getToken());
				userService = new UserService(APPLICATION_NAME);
				userService.setUserToken(adminAcc.getToken());
			} else {
				groupService = new AppsGroupsService(adminAcc.getUsername()
						+ "@" + adminAcc.getDomain(),
						SecurityUtil.decrypt(adminAcc.getPassword()),
						adminAcc.getDomain(), APPLICATION_NAME);
				UserToken us = (UserToken) groupService.getAuthTokenFactory()
						.getAuthToken();
				adminAcc.setToken(us.getValue());

				SystemAccountDAO accountDao = new SystemAccountDaoImpl();
				userService = new UserService(APPLICATION_NAME);
				userService.setUserToken(adminAcc.getToken());
				try {
					// restore the token
					accountDao.updateGoogleAccount(adminAcc);
				} catch (Exception e) {
					logger.log(Level.SEVERE, e.getMessage());
				}
			}

		} catch (AuthenticationException e) {
			logger.log(Level.SEVERE, e.getMessage());
			throw e;
		}
	}

	private String getTimestampString() {
		return sdfTimestamp.format(new Date(System.currentTimeMillis()));
	}

	private void log(String input) {
		log(input, LOG_INFO_LEVEL);
	}

	private void log(String input, int level) {
		if (bufferLog == null) {
			bufferLog = new StringBuffer();
		}
		bufferLog.append("\n" + getTimestampString());
		switch (level) {
		case LOG_ERROR_LEVEL:
			bufferLog.append(" ERROR: ");
			break;
		case LOG_INFO_LEVEL:
			bufferLog.append(" INFO: ");
			break;
		case LOG_WARNING_LEVEL:
			bufferLog.append(" WARNING: ");
			break;
		default:
			bufferLog.append(" ALL: ");
			break;
		}
		bufferLog.append(input);
	}

	@SuppressWarnings("unchecked")
	public void sync() {
		SystemRoleDAO roleDao = new SystemRoleDaoImpl();
		roleDao.init();
		int problem = 0;
		long start = System.currentTimeMillis();
		log("User synchronization process started...");
		log("Querying Google for all users");
		URL metafeedUrl = null;
		try {
			metafeedUrl = new URL(APPS_FEEDS_URL_BASE + adminAcc.getDomain()
					+ "/user/" + SERVICE_VERSION + "/");
		} catch (MalformedURLException e) {
			problem++;
			log("Parse URL MalformedURLException. Message:" + e.getMessage(),
					LOG_ERROR_LEVEL);
		}
		if (metafeedUrl != null) {
			log("Getting user entries...");
			List<UserEntry> entries = new ArrayList<UserEntry>();
			while (metafeedUrl != null) {
				UserFeed resultFeed = null;
				try {
					resultFeed = userService.getFeed(metafeedUrl,
							UserFeed.class);
				} catch (AppsForYourDomainException e) {
					problem++;
					log("Get Feed AppsForYourDomainException. Message:"
							+ e.getMessage(), LOG_ERROR_LEVEL);
				} catch (IOException e) {
					problem++;
					log("Get Feed IOException. Message:" + e.getMessage(),
							LOG_ERROR_LEVEL);
				} catch (ServiceException e) {
					problem++;
					log("Get Feed ServiceException. Message:" + e.getMessage(),
							LOG_ERROR_LEVEL);
				}
				if (resultFeed != null) {
					entries.addAll(resultFeed.getEntries());
					// Check for next page
					Link nextLink = resultFeed.getNextLink();
					if (nextLink == null) {
						metafeedUrl = null;
					} else {
						try {
							metafeedUrl = new URL(nextLink.getHref());
						} catch (MalformedURLException e) {
							problem++;
							log("Parse URL MalformedURLException. Message:"
									+ e.getMessage(), LOG_ERROR_LEVEL);
						}
					}
				}
			}
			log(entries.size() + " user entries received from Google");
			if (!entries.isEmpty()) {
				Login login = null;
				Name name = null;
				SystemUser user = null;
				for (UserEntry entry : entries) {
					if (users == null) {
						users = new ArrayList<SystemUser>();
					}
					login = entry.getLogin();
					name = entry.getName();
					user = new SystemUser();
					user.setFirstName(name.getGivenName());
					user.setLastName(name.getFamilyName());
					user.setSuspended(login.getSuspended());
					user.setDomainAdmin(login.getAdmin());
					user.setUsername(login.getUserName());
					user.setEmail(login.getUserName() + "@"
							+ adminAcc.getDomain());
					user.setDomain(adminAcc.getDomain());
					if (user.isSuspended()) {
						log("User '"
								+ user.getUsername()
								+ " ("
								+ user.getFullName()
								+ ")"
								+ "' has been suspended in Google Apps and will also be disabled in "
								+ MonitorConstant.PROJECT_NAME + ".",
								LOG_WARNING_LEVEL);
					} 
					users.add(user);
				}
			}
		}
		if (!users.isEmpty()) {
			log("Initiating synchronization to " + MonitorConstant.PROJECT_NAME);
			SystemAccountDAO accountDao = new SystemAccountDaoImpl();
			List<SystemUser> activeUsers = null;
			try {
				log("Retrieving active user list from "
						+ MonitorConstant.PROJECT_NAME);
				activeUsers = accountDao.listAllSystemUserByDomain(adminAcc.getDomain());
			} catch (Exception e) {
				problem++;
				log("List all user by domain Exception. Message:"
						+ e.getMessage(), LOG_ERROR_LEVEL);
			}
			int updatedCount = 0, updatedFail = 0,
					createdCount = 0, createdFail = 0,
					removedCount = 0, removedFail = 0,
					conflictCount = 0, conflictFail = 0;
			List<SystemUser> removedList = new ArrayList<SystemUser>(),
					createdList = new ArrayList<SystemUser>(),
					updatedList = new ArrayList<SystemUser>();
			if (activeUsers == null || activeUsers.isEmpty()) {
				createdList.addAll(users);
			} else {			
				// Check user for update or delete
				for (SystemUser activeUser : activeUsers) {
					boolean check = false;
					for (SystemUser sysUser : users) {
						if (activeUser.getEmail().equalsIgnoreCase(sysUser.getEmail())) {
							updatedList.add(sysUser);
							check = true;
							break;
						}
					}
					if (!check) {
						removedList.add(activeUser);
					}					
				}
				// Check user for create
				for (SystemUser sysUser : users) {
					boolean check = false;
					for (SystemUser activeUser : activeUsers) {
						if (activeUser.getUsername().equalsIgnoreCase(sysUser.getUsername())) {							
							check = true;
							break;
						}
					}
					if (!check) {
						createdList.add(sysUser);
					}
				}		
				
			}
			// Start create new user
			if (!createdList.isEmpty()) {
				for (SystemUser sysUser : createdList) {
					createdCount++;
					try {						
						sysUser.addUserRole(SystemRole.ROLE_USER);
						boolean b = accountDao.createSystemUser(sysUser);
						if (!b) {
							createdFail++;
						}
						log("Creating user: " + sysUser.getUsername() + " ("
								+ sysUser.getFullName() + ")"
								+ (b ? " ... DONE" : " ... FAIL"));		
					} catch (Exception e) {
						e.printStackTrace();
						problem++;
						createdFail++;
						log("Creating user '" + sysUser.getUsername() + " ("
								+ sysUser.getFullName() + ")"
								+ "'. Exception message: " + e.getMessage(),
								LOG_ERROR_LEVEL);
					}
				}
			}
			// Start update user
			if (!updatedList.isEmpty()) {
				for (SystemUser sysUser : updatedList) {
					updatedCount++;
					try {
						boolean b = accountDao.updateSystemUser(sysUser, false);
						log("Updating user: " + sysUser.getUsername() + " ("
								+ sysUser.getFullName() + ")"
								+ (b ? " ... DONE" : " ... FAIL"));						
						if (!b) {
							updatedFail++;
						}
					} catch (Exception e) {
						problem++;
						updatedFail++;
						log("Updating user '" + sysUser.getUsername() + " ("
								+ sysUser.getFullName() + ")"
								+ "'. Exception message: " + e.getMessage(),
								LOG_ERROR_LEVEL);
					}
				}
			}
			// Start remove user
			if (!removedList.isEmpty()) {
				for (SystemUser user : removedList) {
					removedCount++;
					try {
						boolean b = accountDao.deleteSystemUser(user);
						log("Deleting user: " + user.getUsername() + " ("
								+ user.getFullName() + ")"
								+ (b ? " ... DONE" : " ... FAIL"));						
						if (!b) {
							removedFail++;
						}
					} catch (Exception e) {
						problem++;
						removedFail++;
						log("Deleting user '" + user.getUsername() + " ("
								+ user.getFullName() + ")"
								+ "'. Exception message: " + e.getMessage(),
								LOG_ERROR_LEVEL);
					}
				}
			}	
			log("Created user count: "+createdCount+" ("+createdFail+" failed)");
			log("Updated user count: "+updatedCount+" ("+updatedFail+" failed)");
			log("Removed user count: "+removedCount+" ("+removedFail+" failed)");
			log("Username conflicts count: "+conflictCount+" ("+conflictFail+" new)");

		}
		long end = System.currentTimeMillis();
		long total = end - start;
		log("User synchronization completed with " + problem
				+ " problem"+(problem > 1 ? "s" : "")+". Time executed: " + total + " ms");
	}

	public String getLog() {
		return bufferLog.toString();
	}

	@Deprecated
	public List<SystemUser> listAllAccount() throws Exception {
		List<SystemUser> list = null;
		try {
			URL metafeedUrl = new URL(
					"https://www.google.com/a/feeds/c-mg.vn/user/2.0/");
			System.out.println("Getting user entries...\n");
			List<UserEntry> entries = new ArrayList<UserEntry>();
			while (metafeedUrl != null) {
				// Fetch page
				System.out.println("Fetching page...\n");
				UserFeed resultFeed = userService.getFeed(metafeedUrl,
						UserFeed.class);
				entries.addAll(resultFeed.getEntries());

				// Check for next page
				Link nextLink = resultFeed.getNextLink();
				if (nextLink == null) {
					metafeedUrl = null;
				} else {
					metafeedUrl = new URL(nextLink.getHref());
				}
			}

			// Handle results
			for (int i = 0; i < entries.size(); i++) {
				UserEntry entry = entries.get(i);
				Login login = entry.getLogin();
				Name name = entry.getName();
				System.out.print("|Familyname: " + name.getFamilyName());
				System.out.print("|Givenname: " + name.getGivenName());
				System.out.print("|Suspended: " + login.getSuspended());
				System.out.print("|Admin: " + login.getAdmin());
				System.out.println("|Username:" + login.getUserName());
			}
			System.out.println("\nTotal Entries: " + entries.size());
		} catch (Exception ex) {
			throw ex;
		}
		return list;
	}

	@Deprecated
	protected List<String> listGroups() throws Exception {
		List<String> list = null;
		try {
			GenericFeed groupFeed = groupService.retrieveAllGroups();
			Iterator<GenericEntry> groupsEntryIterator = groupFeed.getEntries()
					.iterator();
			String group = "";
			while (groupsEntryIterator.hasNext()) {
				if (list == null) {
					list = new ArrayList<String>();
				}
				group = groupsEntryIterator.next().getId();
				group = group.substring(group.lastIndexOf("/") + 1,
						group.lastIndexOf("%40"));
				list.add(group);
			}
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage());
			throw ex;
		}
		return list;
	}

	public List<String> listUserInGroup(String group) throws Exception {
		ArrayList<String> list = new ArrayList<String>();
		try {
			GenericFeed groupsFeed = groupService.retrieveAllMembers(group);
			Iterator<GenericEntry> groupsEntryIterator = groupsFeed
					.getEntries().iterator();
			while (groupsEntryIterator.hasNext()) {
				list.add(groupsEntryIterator.next().getProperty(
						AppsGroupsService.APPS_PROP_GROUP_MEMBER_ID));
			}
		} catch (Exception ex) {
			throw ex;
		}
		return list;
	}

	/**
	 * @return the users
	 */
	public List<SystemUser> getUsers() {
		return users;
	}
}
