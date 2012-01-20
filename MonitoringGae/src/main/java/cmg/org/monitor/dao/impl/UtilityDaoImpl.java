package cmg.org.monitor.dao.impl;

import java.util.ArrayList;

import cmg.org.monitor.dao.UtilityDAO;
import cmg.org.monitor.ext.model.shared.GroupMonitor;
import cmg.org.monitor.ext.model.shared.UserMonitor;
import cmg.org.monitor.memcache.Key;
import cmg.org.monitor.memcache.MonitorMemcache;
import cmg.org.monitor.services.Appforyourdomain;
import cmg.org.monitor.services.SitesHelper;
import cmg.org.monitor.util.shared.MonitorConstant;

public class UtilityDaoImpl implements UtilityDAO {

	@Override
	public void putHelpContent(String content) {
		MonitorMemcache.put(Key.create(Key.HELP_CONTENT), content);
	}

	@Override
	public String getHelpContent() {
		String temp = "";
		Object obj = MonitorMemcache.get(Key.create(Key.HELP_CONTENT));
		if (obj != null && obj instanceof String) {
			temp = (String) obj;
		}
		if (temp == null || temp.equals("")) {
			temp = SitesHelper
					.getSiteEntryContent(MonitorConstant.SITES_HELP_CONTENT_ID);
			putHelpContent(temp);
		}
		return temp;
	}

	@Override
	public void putAboutContent(String content) {
		MonitorMemcache.put(Key.create(Key.ABOUT_CONTENT), content);
	}

	@Override
	public String getAboutContent() {
		String temp = "";
		Object obj = MonitorMemcache.get(Key.create(Key.ABOUT_CONTENT));
		if (obj != null && obj instanceof String) {
			temp = (String) obj;
		}
		if (temp == null || temp.equals("")) {
			temp = SitesHelper
					.getSiteEntryContent(MonitorConstant.SITES_ABOUT_CONTENT_ID);
			putAboutContent(temp);
		}
		return temp;
	}

	@Override
	public ArrayList<GroupMonitor> listGroups() {
		ArrayList<GroupMonitor> list = null;
		Object obj = MonitorMemcache.get(Key.create(Key.LIST_GROUP));
		if (obj != null && obj instanceof ArrayList<?>) {
			list = (ArrayList<GroupMonitor>) obj;
		}
		if (list == null || list.size() <= 0) {
			Appforyourdomain app = new Appforyourdomain(
					MonitorConstant.ADMIN_EMAIL,
					MonitorConstant.ADMIN_PASSWORD, MonitorConstant.DOMAIN);
			list = app.listGroups();
			putGroups(list);
		}
		return list;
	}

	@Override
	public ArrayList<UserMonitor> listAllUsers() {
		ArrayList<UserMonitor> list = null;
		Object obj = MonitorMemcache.get(Key.create(Key.LIST_ALL_USERS));
		if (obj != null && obj instanceof ArrayList<?>) {
			list = (ArrayList<UserMonitor>) obj;
		}
		if (list == null || list.size() <= 0) {
			Appforyourdomain app = new Appforyourdomain(
					MonitorConstant.ADMIN_EMAIL,
					MonitorConstant.ADMIN_PASSWORD, MonitorConstant.DOMAIN);
			
			list = app.listAllUsers();
			MonitorMemcache.put(Key.create(Key.LIST_ALL_USERS), list);
		}
		return list;
	}

	@Override
	public ArrayList<UserMonitor> listUsersInGroup(GroupMonitor group) {
		ArrayList<UserMonitor> list = null;
		Object obj = MonitorMemcache.get(Key.create(Key.LIST_USERS_IN_GROUP,
				group.getName()));
		if (obj != null && obj instanceof ArrayList<?>) {
			list = (ArrayList<UserMonitor>) obj;
		}
		if (list == null || list.size() <= 0) {
			Appforyourdomain app = new Appforyourdomain(
					MonitorConstant.ADMIN_EMAIL,
					MonitorConstant.ADMIN_PASSWORD, MonitorConstant.DOMAIN);
			list = app.listUser(group);
			putUsers(list, group);
		}
		return list;
	}

	@Override
	public void putGroups() {
		Appforyourdomain app = new Appforyourdomain(
				MonitorConstant.ADMIN_EMAIL, MonitorConstant.ADMIN_PASSWORD,
				MonitorConstant.DOMAIN);
		ArrayList<GroupMonitor> list = app.listGroups();
		MonitorMemcache.put(Key.create(Key.LIST_GROUP), list);

		if (list != null && list.size() > 0) {
			for (GroupMonitor g : list) {
				putUsers(g);
			}
		}

	}
	
	public void putGroups(ArrayList<GroupMonitor> groups) {
		MonitorMemcache.put(Key.create(Key.LIST_GROUP), groups);

		if (groups != null && groups.size() > 0) {
			for (GroupMonitor g : groups) {
				putUsers(g);
			}
		}

	}

	@Override
	public void putAllUsers() {
		Appforyourdomain app = new Appforyourdomain(
				MonitorConstant.ADMIN_EMAIL, MonitorConstant.ADMIN_PASSWORD,
				MonitorConstant.DOMAIN);
		ArrayList<UserMonitor> list = app.listAllUsers();
		MonitorMemcache.put(Key.create(Key.LIST_ALL_USERS), list);
	}

	public void putUsers(GroupMonitor group) {
		Appforyourdomain app = new Appforyourdomain(
				MonitorConstant.ADMIN_EMAIL, MonitorConstant.ADMIN_PASSWORD,
				MonitorConstant.DOMAIN);
		ArrayList<UserMonitor> list = app.listUser(group);
		MonitorMemcache.put(
				Key.create(Key.LIST_USERS_IN_GROUP, group.getName()), list);
	}
	
	public void putUsers(ArrayList<UserMonitor> users, GroupMonitor group) {
		MonitorMemcache.put(
				Key.create(Key.LIST_USERS_IN_GROUP, group.getName()), users);
	}
}
