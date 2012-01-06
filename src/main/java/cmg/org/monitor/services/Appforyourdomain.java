/**
 * 
 */
package cmg.org.monitor.services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import cmg.org.monitor.ext.model.shared.GroupMonitor;
import cmg.org.monitor.ext.model.shared.UserMonitor;
import cmg.org.monitor.util.shared.MonitorConstant;

import com.google.gdata.client.appsforyourdomain.AppsGroupsService;
import com.google.gdata.client.appsforyourdomain.UserService;
import com.google.gdata.data.appsforyourdomain.AppsForYourDomainException;
import com.google.gdata.data.appsforyourdomain.generic.GenericEntry;
import com.google.gdata.data.appsforyourdomain.generic.GenericFeed;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

public class Appforyourdomain {

	private static final Logger logger = Logger
			.getLogger(Appforyourdomain.class.getCanonicalName());

	private AppsGroupsService groupService;

	private UserService userService;

	public Appforyourdomain(String adminEmail, String adminPassword,
			String domain) {

		try {
			userService = new UserService("monitor-tool-user-service");
			userService.setUserCredentials(adminEmail, adminPassword);
			groupService = new AppsGroupsService(adminEmail, adminPassword,
					domain, "monitor-tool-app-group-service");
		} catch (AuthenticationException e) {
			logger.log(
					Level.SEVERE,
					"ERROR when list group. AuthenticationException: "
							+ e.getMessage());
		}

	}

	public ArrayList<GroupMonitor> listGroups() {
		ArrayList<GroupMonitor> list = null;
		GroupMonitor group = null;
		try {
			GenericFeed groupFeed = groupService.retrieveAllGroups();
			Iterator<GenericEntry> groupsEntryIterator = groupFeed.getEntries()
					.iterator();
			while (groupsEntryIterator.hasNext()) {
				if (list == null) {
					list = new ArrayList<GroupMonitor>();
				}
				group = new GroupMonitor();
				group.setId(groupsEntryIterator.next().getId());
				group.parseName();
				list.add(group);
			}
		} catch (AppsForYourDomainException e) {
			logger.log(
					Level.SEVERE,
					"ERROR when list group. AppsForYourDomainException: "
							+ e.getMessage());
		} catch (MalformedURLException e) {
			logger.log(
					Level.SEVERE,
					"ERROR when list group. MalformedURLException: "
							+ e.getMessage());
		} catch (IOException e) {
			logger.log(Level.SEVERE,
					"ERROR when list group. IOException: " + e.getMessage());
		} catch (ServiceException e) {
			logger.log(
					Level.SEVERE,
					"ERROR when list group. ServiceException: "
							+ e.getMessage());
		}
		return list;
	}

	public ArrayList<UserMonitor> listAllUsers() {
		ArrayList<UserMonitor> list = null;
		ArrayList<UserMonitor> users = null;
		ArrayList<GroupMonitor> groups = listGroups();
		if (groups != null && groups.size() > 0) {
			list = new ArrayList<UserMonitor>();
			for (GroupMonitor g : groups) {
				users = listUser(g);
				if (users != null && users.size() > 0) {
					for (UserMonitor user : users) {
						boolean check = false;
						if (list.size() == 0) {
							check = true;
						} else {
							for (UserMonitor u : list) {
								if (user.getId().equals(u.getId())) {
									u.addGroup(g);
									check = false;
									break;
								} else {
									check = true;
								}
							}// for
						}// if-else
						if (check) {
							list.add(user);
						}
					}// for
				}// if
			}// for
		}//
		return list;
	}

	public ArrayList<UserMonitor> listUser(GroupMonitor group) {
		ArrayList<UserMonitor> list = null;
		UserMonitor user = null;
		try {
			GenericFeed groupsFeed = groupService.retrieveAllMembers(group
					.getName());
			Iterator<GenericEntry> groupsEntryIterator = groupsFeed
					.getEntries().iterator();
			while (groupsEntryIterator.hasNext()) {
				if (list == null) {
					list = new ArrayList<UserMonitor>();
				}
				user = new UserMonitor();
				user.setId(groupsEntryIterator.next().getProperty(
						AppsGroupsService.APPS_PROP_GROUP_MEMBER_ID));
				user.setRole(group.getName().contains("admin") ? MonitorConstant.ROLE_ADMIN
						: MonitorConstant.ROLE_NORMAL_USER);
				user.addGroup(group);
				list.add(user);
			}
		} catch (AppsForYourDomainException e) {
			logger.log(
					Level.SEVERE,
					"ERROR when list group. AppsForYourDomainException: "
							+ e.getMessage());
		} catch (MalformedURLException e) {
			logger.log(
					Level.SEVERE,
					"ERROR when list group. MalformedURLException: "
							+ e.getMessage());
		} catch (IOException e) {
			logger.log(Level.SEVERE,
					"ERROR when list group. IOException: " + e.getMessage());
		} catch (ServiceException e) {
			logger.log(
					Level.SEVERE,
					"ERROR when list group. ServiceException: "
							+ e.getMessage());
		}
		return list;
	}

}
