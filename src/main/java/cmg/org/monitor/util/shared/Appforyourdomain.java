/**
 * 
 */
package cmg.org.monitor.util.shared;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Iterator;

import com.google.gdata.client.appsforyourdomain.AppsGroupsService;
import com.google.gdata.client.appsforyourdomain.UserService;
import com.google.gdata.data.appsforyourdomain.AppsForYourDomainException;
import com.google.gdata.data.appsforyourdomain.generic.GenericEntry;
import com.google.gdata.data.appsforyourdomain.generic.GenericFeed;
import com.google.gdata.util.ServiceException;

public class Appforyourdomain {

	private AppsGroupsService groupService;

	private UserService userService;

	private static final String APPS_FEEDS_URL_BASE = "https://apps-apis.google.com/a/feeds/";

	private static final String SERVICE_VERSION = "2.0";

	private String domainUrlBase;

	private String domain;

	protected Appforyourdomain(String domain) {
		this.domain = domain;
		this.domainUrlBase = APPS_FEEDS_URL_BASE + domain + "/";
	}

	/**
	 * 
	 * @param adminEmail
	 * @param adminPassword
	 * @param domain
	 * @throws Exception
	 */
	public Appforyourdomain(String adminEmail, String adminPassword,
			String domain) throws Exception {
		this(domain);
		// Configure all of the different Provisioning services
		userService = new UserService(
				"gdata-sample-AppsForYourDomain-UserService");
		userService.setUserCredentials(adminEmail, adminPassword);
		groupService = new AppsGroupsService(adminEmail, adminPassword, domain,
				"gdata-sample-AppsForYourDomain-AppsGroupService");
	}

	/**
	 * Get all user from group email ID
	 * @param groupID
	 * @return all user
	 * @throws Exception
	 */
	public String[] listAllUser(String groupID) throws Exception {
		String[] listUser = null;
		String id;

		id = groupID.substring(groupID.lastIndexOf("/") + 1,
				groupID.length());
		String group = id.split("%")[0];
		
		GenericFeed groupsFeed;
		try {
			groupsFeed = groupService.retrieveAllMembers(id);
			Iterator<GenericEntry> groupsEntryIterator = groupsFeed
					.getEntries().iterator();
			StringBuffer members = new StringBuffer();
			while (groupsEntryIterator.hasNext()) {
				members.append(groupsEntryIterator.next().getProperty(
						AppsGroupsService.APPS_PROP_GROUP_MEMBER_ID));
				if (groupsEntryIterator.hasNext()) {
					members.append(",");
				}
			}
			listUser = members.toString().trim().split(",");
			for (int i = 0; i < listUser.length; i++) {
				listUser[i] = listUser[i] + ":" + group;
			}
		} catch (AppsForYourDomainException e) {
			// TODO Auto-generated catch block
			throw e;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			throw e;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw e;
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			throw e;
		}
		return listUser;
	}
	
	
	/**
	 * 
	 * @return all group
	 */
	public String[] listGroup() {
		String[] listGroup = null;
		GenericFeed groupFeed = null;
		Iterator<GenericEntry> groupsEntryIterator = null;
		try {
			groupFeed = groupService.retrieveAllGroups();
			groupsEntryIterator = groupFeed.getEntries().iterator();
			StringBuffer groups = new StringBuffer();
			while (groupsEntryIterator.hasNext()) {
				groups.append(groupsEntryIterator.next().getProperty(
						AppsGroupsService.APPS_PROP_GROUP_ID));
				if (groupsEntryIterator.hasNext()) {
					groups.append(",");
				}
			}
			listGroup = groups.toString().trim().split(",");
		} catch (AppsForYourDomainException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return listGroup;
	}

	/**
	 * 
	 * @return all id of group
	 */
	public String[] listGroupID() {
		String[] listGroupID = null;
		GenericFeed groupFeed = null;
		Iterator<GenericEntry> groupsEntryIterator = null;
		try {
			groupFeed = groupService.retrieveAllGroups();
			groupsEntryIterator = groupFeed.getEntries().iterator();
			StringBuffer groups = new StringBuffer();
			while (groupsEntryIterator.hasNext()) {
				groups.append(groupsEntryIterator.next().getId());
				if (groupsEntryIterator.hasNext()) {
					groups.append(",");
				}
			}
			listGroupID = groups.toString().trim().split(",");
		} catch (AppsForYourDomainException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return listGroupID;
	}

	/**
	 * 
	 * @param filter
	 * @param listGroupID
	 * @return all admin
	 * @throws AppsForYourDomainException
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws ServiceException
	 */

	public String[] listAdmin(String[] listGroupID)
			throws AppsForYourDomainException, MalformedURLException,
			IOException, ServiceException {
		String idGroupAdmin = null;
		String[] listUser = null;
		GenericFeed groupsFeed = null;
		GenericEntry groupsEntry = null;
		Iterator<GenericEntry> groupsEntryIterator = null;

		for (int i = 0; i < listGroupID.length; i++) {
			if (listGroupID[i].contains("admin")) {
				idGroupAdmin = listGroupID[i].substring(
						listGroupID[i].lastIndexOf("/") + 1,
						listGroupID[i].length()).toString();
			}
		}
		String group = idGroupAdmin.split("%")[0];
		groupsFeed = groupService.retrieveAllMembers(idGroupAdmin);
		groupsEntryIterator = groupsFeed.getEntries().iterator();
		StringBuffer members = new StringBuffer();
		while (groupsEntryIterator.hasNext()) {
			members.append(groupsEntryIterator.next().getProperty(
					AppsGroupsService.APPS_PROP_GROUP_MEMBER_ID));
			if (groupsEntryIterator.hasNext()) {
				members.append(", ");
			}
		}

		listUser = members.toString().trim().split(",");
		for (int i = 0; i < listUser.length; i++) {
			listUser[i] = listUser[i] + ":" + group;
		}
		return listUser;
	}

	/**
	 * @param listGroupID
	 * @return all user
	 * @throws Exception
	 */
	public String[] listUser(String listGroupID) throws Exception {
		String[] listUser = null;
		String id;

		id = listGroupID.substring(listGroupID.lastIndexOf("/") + 1,
				listGroupID.length());
		String group = id.split("%")[0];
		if (id.startsWith("monitor")) {
			GenericFeed groupsFeed;
			try {
				groupsFeed = groupService.retrieveAllMembers(id);
				GenericEntry groupsEntry = null;
				Iterator<GenericEntry> groupsEntryIterator = groupsFeed
						.getEntries().iterator();
				StringBuffer members = new StringBuffer();
				while (groupsEntryIterator.hasNext()) {
					members.append(groupsEntryIterator.next().getProperty(
							AppsGroupsService.APPS_PROP_GROUP_MEMBER_ID));
					if (groupsEntryIterator.hasNext()) {
						members.append(",");
					}
				}
				listUser = members.toString().trim().split(",");
				for (int i = 0; i < listUser.length; i++) {
					listUser[i] = listUser[i] + ":" + group;
				}
			} catch (AppsForYourDomainException e) {
				// TODO Auto-generated catch block
				throw e;
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				throw e;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				throw e;
			} catch (ServiceException e) {
				// TODO Auto-generated catch block
				throw e;
			}

		}

		return listUser;
	}

	
	  public static void main(String[] arg) throws Exception { String[] list =
	  null;
	  
	  Appforyourdomain client = new Appforyourdomain("monitor@c-mg.vn",
	  "31102011", "c-mg.vn"); 
	  String[] ids = client.listGroupID();
	  System.out.println(ids.toString());
	  
	  }
	 
}
