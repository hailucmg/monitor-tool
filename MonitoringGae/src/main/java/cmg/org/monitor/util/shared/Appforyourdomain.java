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
		for(int i = 0;i<listUser.length;i++){
			listUser[i]= listUser[i]+":"+group;
		}
		return listUser;
	}

	
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
				for(int i = 0;i<listUser.length;i++){
					listUser[i]= listUser[i]+":"+group;
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


	/*public static void main(String[] arg) throws Exception {
		String[] list = null;

		Appforyourdomain client = new Appforyourdomain("monitor@c-mg.vn",
				"31102011", "c-mg.vn");
		String temp = "https://apps-apis.google.com/a/feeds/group/2.0/c-mg.vn/admin_monitor%40c-mg.vn";
		String value = temp.substring(temp.lastIndexOf("/") + 1, temp.length());
		System.out.print(temp.lastIndexOf("/") + " " + temp.length() + " "
				+ value);
		String test = value.substring(value.lastIndexOf("%") +1, value.length());
		System.out.println(test);
		String[] test1 = value.split("%");
		for(int i = 0;i<test1.length;i++){
			System.out.println(test1[i]);
		}
		try {

			list = client.listGroup();
			for (int i = 0; i < list.length; i++) {
				System.out.println(list[i]);
			}
			String[] id = client.listGroupID();
			for (int j = 0; j < id.length; j++) {
				System.out.println(id[j]);
			}
			
			List<String> member = new ArrayList<String>();
			
			String[] admin = client.listAdmin(id);
			
			for(int k = 0 ;k<id.length;k++){
				if(client.listUser(id[k])!=null){
					String[] user = client.listUser(id[k]);
					for(int b = 0;b < user.length;b++){
						member.add(user[b]);
					}
				}
		}
			String[] normal= new String[member.size()];
			for(int b = 0; b<member.size();b++){
				normal[b] = member.get(b);
			}
			List<String> allMember = new ArrayList<String>();
			
			for(int s = 0;s < admin.length;s++){
				allMember.add(admin[s]);
			}
			for(int n = 0 ;n < admin.length;n++){
				for(int m = 0; m<normal.length;m++){
					if(admin[n].split(":")[0]!=normal[m].split(":")[0]){
						allMember.add(normal[m]);
					}
				}
			}
			for(int y=0;y<allMember.size();y++){
				System.out.println(allMember.get(y));
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		}

	}*/
}
