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

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;

import cmg.org.monitor.dao.AccountSyncLogDAO;
import cmg.org.monitor.dao.InviteUserDAO;
import cmg.org.monitor.dao.SystemAccountDAO;
import cmg.org.monitor.dao.SystemGroupDAO;
import cmg.org.monitor.dao.UtilityDAO;
import cmg.org.monitor.dao.impl.AccountSyncLogDaoImpl;
import cmg.org.monitor.dao.impl.InviteUserDaoImpl;
import cmg.org.monitor.dao.impl.SystemAccountDaoImpl;
import cmg.org.monitor.dao.impl.SystemGroupDaoImpl;
import cmg.org.monitor.dao.impl.UtilityDaoImpl;
import cmg.org.monitor.entity.shared.AccountSyncLog;
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
 * DOCME.
 *
 * @Creator Hai Lu
 * @author $Author$
 * @version $Revision$
 * @Last changed: $LastChangedDate$
 */

public class GoogleAccountService {
	
	/** The Constant APPS_FEEDS_URL_BASE. */
	private static final String APPS_FEEDS_URL_BASE = "https://apps-apis.google.com/a/feeds/";

	/** The Constant SERVICE_VERSION. */
	protected static final String SERVICE_VERSION = "2.0";

	/** The Constant APPLICATION_NAME. */
	public static final String APPLICATION_NAME = "cmg-monitor-tools";

	/** The Constant logger. */
	private static final Logger logger = Logger
			.getLogger(GoogleAccountService.class.getCanonicalName());

	/** The group service. */
	private AppsGroupsService groupService;

	/** The user service. */
	private UserService userService;

	/** The sdf timestamp. */
	private SimpleDateFormat sdfTimestamp = new SimpleDateFormat(
			"dd/MM/yyyy hh:mm:ss.SSS");

	/** The users. */
	private List<SystemUser> users;

	/** The Constant LOG_INFO_LEVEL. */
	private static final int LOG_INFO_LEVEL = 0x001;
	
	/** The Constant LOG_WARNING_LEVEL. */
	private static final int LOG_WARNING_LEVEL = 0x002;
	
	/** The Constant LOG_ERROR_LEVEL. */
	private static final int LOG_ERROR_LEVEL = 0x003;
	
	/** The account dao. */
	SystemAccountDAO accountDao = new SystemAccountDaoImpl();
	
	/** The sync log dao. */
	AccountSyncLogDAO syncLogDao = new AccountSyncLogDaoImpl();

	/** The buffer log. */
	StringBuffer bufferLog;
	
	/** The admin acc. */
	GoogleAccount adminAcc;
	
	/** The current zone. */
	String currentZone;

	/**
	 * Instantiates a new google account service.
	 */
	protected GoogleAccountService() {
		//
	}

	/**
	 * Instantiates a new google account service.
	 *
	 * @param admin the admin
	 * @throws AuthenticationException the authentication exception
	 */
	public GoogleAccountService(GoogleAccount admin)
			throws AuthenticationException {
		try {
			UtilityDAO utilDao = new UtilityDaoImpl();
			currentZone = utilDao.getCurrentTimeZone();
			if (admin == null) {
				throw new AuthenticationException("Null account object");
			}
			this.adminAcc = admin;
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
				
				userService = new UserService(APPLICATION_NAME);
				userService.setUserToken(adminAcc.getToken());				
			}

		} catch (AuthenticationException e) {
			AccountSyncLog log = new AccountSyncLog();
			log.setAdminAccount(adminAcc.getUsername()
					+ "@" + adminAcc.getDomain());
			log.setTimestamp(new Date(System.currentTimeMillis()));
			log.setLog("Authenticantion fail. Message: " + e.getMessage());
			try {
				syncLogDao.createLog(log);
			} catch (Exception e1) {
				//
			}
			logger.log(Level.SEVERE, e.getMessage());
			throw e;
		}
	}
	
	/**
	 * Gets the timestamp string.
	 *
	 * @return the timestamp string
	 */
	private String getTimestampString() {
		DateTime t = new DateTime(new Date(System.currentTimeMillis()));
		t = t.withZone(DateTimeZone.forID(currentZone));	
		return t.toString(DateTimeFormat.forPattern("dd/MM/yyyy hh:mm:ss.SSS"));
	}

	/**
	 * Log.
	 *
	 * @param input the input
	 */
	private void log(String input) {
		log(input, LOG_INFO_LEVEL);
	}

	/**
	 * Log.
	 *
	 * @param input the input
	 * @param level the level
	 */
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

	/**
	 * Sync.
	 */
	@SuppressWarnings("unchecked")
	public void sync() {		
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
			List<SystemUser> activeUsers = null;
			try {
				log("Retrieving active user list from "
						+ MonitorConstant.PROJECT_NAME);
				activeUsers = accountDao.listAllSystemUserByDomain(adminAcc.getDomain(), false);
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
						if (activeUser.getEmail().equalsIgnoreCase(sysUser.getEmail())) {							
							check = true;
							break;
						}
					}
					if (!check) {
						createdList.add(sysUser);
					}
				}		
				
			}
			InviteUserDAO userDao = new InviteUserDaoImpl();
			// Start create new user
			if (!createdList.isEmpty()) {
				for (SystemUser sysUser : createdList) {
					try {
						userDao.delete3rdUser(sysUser.getEmail(), false);
					} catch (Exception ex) {
						
					}
					createdCount++;
					try {						
						sysUser.addUserRole(SystemRole.ROLE_USER);
						boolean b = accountDao.createSystemUser(sysUser, true);
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
					try {
						userDao.delete3rdUser(sysUser.getEmail(), false);
					} catch (Exception ex) {
						
					}
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
					try {
						userDao.delete3rdUser(user.getEmail(), false);
					} catch (Exception ex) {
						
					}
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
		try {
			adminAcc.setLastSync(new Date(end));
			accountDao.updateGoogleAccount(adminAcc);
			
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
		SystemGroupDAO groupDao = new SystemGroupDaoImpl();
		groupDao.initSystemGroupMemcache();
		accountDao.initGoogleAccountMemcache();
		accountDao.initSystemUserMemcache();
		AccountSyncLog log = new AccountSyncLog();
		log.setAdminAccount(adminAcc.getUsername()
				+ "@" + adminAcc.getDomain());
		log.setTimestamp(new Date(end));
		log.setLog(getLog());
		try {
			syncLogDao.createLog(log);
		} catch (Exception e1) {
			//
		}
	}

	/**
	 * Gets the log.
	 *
	 * @return the log
	 */
	public String getLog() {
		return bufferLog.toString();
	}

	/**
	 * List all account.
	 *
	 * @return the list
	 * @throws Exception the exception
	 */
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

	/**
	 * List groups.
	 *
	 * @return the list
	 * @throws Exception the exception
	 */
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

	/**
	 * List user in group.
	 *
	 * @param group the group
	 * @return the list
	 * @throws Exception the exception
	 */
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
	 * Gets the users.
	 *
	 * @return the users
	 */
	public List<SystemUser> getUsers() {
		return users;
	}
}
