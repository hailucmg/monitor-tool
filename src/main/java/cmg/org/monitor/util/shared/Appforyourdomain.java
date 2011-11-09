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
import com.google.protos.cloud.sql.Client;


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
	 * 
	 * @return
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
	 * @return
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
	 * @return
	 * @throws AppsForYourDomainException
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws ServiceException
	 */
	@SuppressWarnings("null")
	public String[] listUser(String filter, String[] listGroupID)
			throws AppsForYourDomainException, MalformedURLException,
			IOException, ServiceException {
		String idGroupAdmin = null;
		int countMember = 0;
		GenericFeed groupFeed = null;
		String[] listUser = null;
		Iterator<GenericEntry> groupsEntryIterator = null;
		if (filter.equals("admin")) {
			for (int i = 0; i < listGroupID.length; i++) {
				if (listGroupID[i].contains("admin")) {
					idGroupAdmin = listGroupID[i].toString();
				}
			}
			
			groupFeed = groupService.retrieveAllMembers(idGroupAdmin.toString());
			groupsEntryIterator = groupFeed.getEntries().iterator();
			StringBuffer members = new StringBuffer();
			while (groupsEntryIterator.hasNext()) {
				members.append(groupsEntryIterator.next().getProperty(
						AppsGroupsService.APPS_PROP_GROUP_ID));
				if (groupsEntryIterator.hasNext()) {
					members.append(",");
				}
			}
			listUser = members.toString().split(",");

		} else {
			for (int i = 0; i < listGroupID.length; i++) {
				if (listGroupID[i].contains("admin")) {
					continue;
				}
				groupFeed = groupService.retrieveAllMembers(listGroupID[i]);
				groupsEntryIterator = groupFeed.getEntries().iterator();
				StringBuffer members = new StringBuffer();
				while (groupsEntryIterator.hasNext()) {
					members.append(groupsEntryIterator.next().getProperty(
							AppsGroupsService.APPS_PROP_GROUP_ID));
					if (groupsEntryIterator.hasNext()) {
						members.append(",");
					}
				}
				String[] member = members.toString().trim().split(",");
				for (int j = 0; j < member.length; j++) {
					listUser[countMember] = member[j];
					countMember++;
				}
			}
		}
		return listUser;
	}

	public static void main(String[] arg) throws Exception {
		String[] list = null;
		
		Appforyourdomain client = new Appforyourdomain("monitor@c-mg.vn",
				"31102011", "c-mg.vn");
		try {
			String[] groupID = client.listGroupID();
			list = client.listGroup();
			
		
			for(int i= 0 ; i< list.length;i++)
			System.out.println(groupID[i].toString());
			String[] id=new String[2];
			id[0]= "admin_monitor%40c-mg.vn";
			id[1]="monitor.globe%40c-mg.vn";
			//String[] user = client.listUser("admin",id);
			//System.out.println(user[0].toString());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			
		}
				

	}
}
