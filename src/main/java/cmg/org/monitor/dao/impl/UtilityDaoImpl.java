package cmg.org.monitor.dao.impl;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.mortbay.log.Log;

import com.google.gdata.client.GoogleAuthTokenFactory.UserToken;
import com.google.gdata.client.appsforyourdomain.AppsGroupsService;
import com.google.gdata.client.appsforyourdomain.migration.MailItemService;
import com.google.gdata.client.sites.SitesService;
import com.google.gdata.util.AuthenticationException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import cmg.org.monitor.app.schedule.MailServiceScheduler;
import cmg.org.monitor.dao.UtilityDAO;
import cmg.org.monitor.entity.shared.LinkDefaultMonitor;
import cmg.org.monitor.ext.model.shared.GroupMonitor;
import cmg.org.monitor.ext.model.shared.UserMonitor;
import cmg.org.monitor.memcache.Key;
import cmg.org.monitor.memcache.MonitorMemcache;
import cmg.org.monitor.services.Appforyourdomain;
import cmg.org.monitor.services.LinkService;
import cmg.org.monitor.services.SitesHelper;
import cmg.org.monitor.util.shared.Constant;
import cmg.org.monitor.util.shared.MonitorConstant;

public class UtilityDaoImpl implements UtilityDAO {
    private static final Logger logger = Logger.getLogger(UtilityDaoImpl.class.getCanonicalName());

    @Override
    public void putHelpContent(String content) {
	try {
	    MonitorMemcache.put(Key.create(Key.HELP_CONTENT), content);
	} catch (Exception ex) {
	    logger.log(Level.WARNING, "Error:" + ex.getMessage());
	}
    }

    @Override
    public String getHelpContent() {
	String temp = "";
	Object obj = MonitorMemcache.get(Key.create(Key.HELP_CONTENT));
	if (obj != null && obj instanceof String) {
	    temp = (String) obj;
	}
	if (temp == null || temp.equals("")) {
	    temp = SitesHelper.getSiteEntryContent(MonitorConstant.SITES_HELP_CONTENT_ID);
	    putHelpContent(temp);
	}
	return temp;
    }

    @Override
    public void putAboutContent(String content) {
	try {
	    MonitorMemcache.put(Key.create(Key.ABOUT_CONTENT), content);
	} catch (Exception ex) {
	    logger.log(Level.WARNING, "Error:" + ex.getMessage());
	}
    }

    @Override
    public String getAboutContent() {
	String temp = "";
	Object obj = MonitorMemcache.get(Key.create(Key.ABOUT_CONTENT));
	if (obj != null && obj instanceof String) {
	    temp = (String) obj;
	}
	if (temp == null || temp.equals("")) {
	    temp = SitesHelper.getSiteEntryContent(MonitorConstant.SITES_ABOUT_CONTENT_ID);
	    putAboutContent(temp);
	}
	return temp;
    }

    @Override
    public ArrayList<GroupMonitor> listGroups() {
	ArrayList<GroupMonitor> list = null;
	Gson gson = new Gson();
	Type type = new TypeToken<Collection<GroupMonitor>>() {
	}.getType();
	Object obj = MonitorMemcache.get(Key.create(Key.LIST_GROUP));
	if (obj != null && obj instanceof String) {
	    try {
		list = (ArrayList<GroupMonitor>) gson.fromJson(String.valueOf(obj), type);
	    } catch (Exception e) {
		logger.log(Level.WARNING, "Error:" + e.getMessage());
	    }
	}

	if (list == null || list.size() <= 0) {
	    Appforyourdomain app = new Appforyourdomain(MonitorConstant.ADMIN_EMAIL, MonitorConstant.ADMIN_PASSWORD, MonitorConstant.DOMAIN);
	    list = app.listGroups();
	    putGroups(list);
	}
	return list;
    }

    @Override
    public ArrayList<UserMonitor> listAllUsers() {
	ArrayList<UserMonitor> list = null;
	Gson gson = new Gson();
	Type type = new TypeToken<Collection<UserMonitor>>() {
	}.getType();
	Object obj = MonitorMemcache.get(Key.create(Key.LIST_ALL_USERS));
	if (obj != null && obj instanceof String) {
	    try {
		list = (ArrayList<UserMonitor>) gson.fromJson(String.valueOf(obj), type);
	    } catch (Exception e) {
		logger.log(Level.WARNING, "Error:" + e.getMessage());
	    }
	}

	if (list == null || list.size() <= 0) {
	    Appforyourdomain app = new Appforyourdomain(MonitorConstant.ADMIN_EMAIL, MonitorConstant.ADMIN_PASSWORD, MonitorConstant.DOMAIN);

	    list = app.listAllUsers();
	    try {
		MonitorMemcache.put(Key.create(Key.LIST_ALL_USERS), gson.toJson(list));
	    } catch (Exception ex) {
		logger.log(Level.WARNING, "Error:" + ex.getMessage());
	    }
	}
	return list;
    }

    @Override
    public ArrayList<UserMonitor> listUsersInGroup(GroupMonitor group) {
	ArrayList<UserMonitor> list = null;
	Gson gson = new Gson();
	Type type = new TypeToken<Collection<UserMonitor>>() {
	}.getType();
	Object obj = MonitorMemcache.get(Key.create(Key.LIST_USERS_IN_GROUP, group.getName()));
	if (obj != null && obj instanceof String) {
	    try {
		list = (ArrayList<UserMonitor>) gson.fromJson(String.valueOf(obj), type);
	    } catch (Exception e) {
		logger.log(Level.WARNING, "Error:" + e.getMessage());
	    }
	}
	if (list == null || list.size() <= 0) {
	    Appforyourdomain app = new Appforyourdomain(MonitorConstant.ADMIN_EMAIL, MonitorConstant.ADMIN_PASSWORD, MonitorConstant.DOMAIN);
	    list = app.listUser(group);
	    putUsers(list, group);
	}
	return list;
    }

    @Override
    public void putGroups() {
	Appforyourdomain app = new Appforyourdomain(MonitorConstant.ADMIN_EMAIL, MonitorConstant.ADMIN_PASSWORD, MonitorConstant.DOMAIN);
	ArrayList<GroupMonitor> list = app.listGroups();
	Gson gson = new Gson();
	try {
	    MonitorMemcache.put(Key.create(Key.LIST_GROUP), gson.toJson(list));
	} catch (Exception ex) {
	    logger.log(Level.WARNING, "Error:" + ex.getMessage());
	}

	if (list != null && list.size() > 0) {
	    for (GroupMonitor g : list) {
		putUsers(g);
	    }
	}

    }

    public void putGroups(ArrayList<GroupMonitor> groups) {
	Gson gson = new Gson();
	try {
	    MonitorMemcache.put(Key.create(Key.LIST_GROUP), gson.toJson(groups));
	} catch (Exception ex) {
	    logger.log(Level.WARNING, "Error:" + ex.getMessage());
	}

	if (groups != null && groups.size() > 0) {
	    for (GroupMonitor g : groups) {
		putUsers(g);
	    }
	}

    }

    @Override
    public void putAllUsers() {
	Appforyourdomain app = new Appforyourdomain(MonitorConstant.ADMIN_EMAIL, MonitorConstant.ADMIN_PASSWORD, MonitorConstant.DOMAIN);
	ArrayList<UserMonitor> list = app.listAllUsers();
	Gson gson = new Gson();
	try {
	    MonitorMemcache.put(Key.create(Key.LIST_ALL_USERS), gson.toJson(list));
	} catch (Exception ex) {
	    logger.log(Level.WARNING, "Error:" + ex.getMessage());
	}
    }

    public void putUsers(GroupMonitor group) {
	Appforyourdomain app = new Appforyourdomain(MonitorConstant.ADMIN_EMAIL, MonitorConstant.ADMIN_PASSWORD, MonitorConstant.DOMAIN);
	ArrayList<UserMonitor> list = app.listUser(group);
	Gson gson = new Gson();
	try {
	    MonitorMemcache.put(Key.create(Key.LIST_USERS_IN_GROUP, group.getName()), gson.toJson(list));
	} catch (Exception ex) {
	    logger.log(Level.WARNING, "Error:" + ex.getMessage());
	}
    }

    public void putUsers(ArrayList<UserMonitor> users, GroupMonitor group) {
	Gson gson = new Gson();
	try {
	    MonitorMemcache.put(Key.create(Key.LIST_USERS_IN_GROUP, group.getName()), gson.toJson(users));
	} catch (Exception ex) {
	    logger.log(Level.WARNING, "Error:" + ex.getMessage());
	}
    }

    @Override
    public void putTokenSite() {
	String token = null;
	SitesService service = new SitesService(MonitorConstant.SITES_APP_NAME);
	try {
	    service.setUserCredentials(MonitorConstant.SITES_USERNAME, MonitorConstant.SITES_PASSWORD);
	    UserToken us = (UserToken) service.getAuthTokenFactory().getAuthToken();
	    token = us.getValue();
	} catch (AuthenticationException e) {
	    logger.log(Level.INFO, "getting exception from memcache token site:" + e.getMessage());
	}
	if (token != null) {
	    try {
		MonitorMemcache.put(Key.create(Key.TOKEN_SITES), token);
	    } catch (Exception ex) {
		logger.log(Level.WARNING, "Error:" + ex.getMessage());
	    }
	}
    }

    @Override
    public void putTokenMail() {
	String token = null;
	MailItemService mailItemService = new MailItemService(MonitorConstant.SITES_APP_NAME);
	try {
	    mailItemService.setUserCredentials(MonitorConstant.ADMIN_EMAIL, MonitorConstant.ADMIN_PASSWORD);
	    UserToken us = (UserToken) mailItemService.getAuthTokenFactory().getAuthToken();
	    token = us.getValue();
	} catch (AuthenticationException e) {
	    logger.log(Level.INFO, "getting exception from memcache token mail:" + e.getMessage());
	}
	if (token != null) {
	    try {
		MonitorMemcache.put(Key.create(Key.TOKEN_MAIL), token);
	    } catch (Exception ex) {
		logger.log(Level.WARNING, "Error:" + ex.getMessage());
	    }
	}
    }

    @Override
    public void putTokenGroup() {
	String token = null;
	try {
	    AppsGroupsService groupService = new AppsGroupsService(MonitorConstant.ADMIN_EMAIL, MonitorConstant.ADMIN_PASSWORD,
		    MonitorConstant.DOMAIN, "monitor-tool-app-group-service");
	    UserToken us = (UserToken) groupService.getAuthTokenFactory().getAuthToken();
	    token = us.getValue();
	} catch (AuthenticationException e) {
	    logger.log(Level.INFO, "getting exception from memcache token group:" + e.getMessage());
	}
	if (token != null) {
	    try {
		MonitorMemcache.put(Key.create(Key.TOKEN_GROUP), token);
	    } catch (Exception ex) {
		logger.log(Level.WARNING, "Error:" + ex.getMessage());
	    }
	}
    }

    /*
     * (non-Javadoc) * @see
     * cmg.org.monitor.dao.UtilityDAO#putLinkDefault(java.lang.String)
     */
    @Override
    public void putLinkDefault(String link) {
	try {
	    MonitorMemcache.put(Key.create(Key.LINK_DEFAULT), link);
	    LinkService linkService = new LinkService();
	    LinkDefaultMonitor link1 = linkService.getLink();
	    if (link1 == null) {
		link1 = new LinkDefaultMonitor();
	    }
	    link1.setLinkContent(link);
	    linkService.updateLink(link1);
	} catch (Exception ex) {
	    logger.log(Level.WARNING, "Error:" + ex.getMessage());
	}
    }

    /* (non-Javadoc) * @see cmg.org.monitor.dao.UtilityDAO#getLinkDefault() */
    @Override
    public String getLinkDefault() {
	// TODO Auto-generated method stub
	String temp = "";
	Object obj = MonitorMemcache.get(Key.create(Key.LINK_DEFAULT));
	if (obj != null && obj instanceof String) {
	    temp = (String) obj;
	}
	if (temp == null || temp.equals("")) {
	    LinkService linkService = new LinkService();
	    try {
		LinkDefaultMonitor link = linkService.getLink();
		if (link == null) {
		    link = new LinkDefaultMonitor();
		    link.setLinkContent(Constant.REDIRECTOR_WORKER_URL);
		    linkService.updateLink(link);
		}
		temp = link.getLinkContent();

		MonitorMemcache.put(Key.create(Key.LINK_DEFAULT), temp);
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}
	return temp;
    }
}
