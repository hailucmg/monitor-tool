package cmg.org.monitor.dao;

import java.util.ArrayList;

import cmg.org.monitor.ext.model.shared.GroupMonitor;
import cmg.org.monitor.ext.model.shared.UserMonitor;

public interface UtilityDAO {
	public void putHelpContent(String content);
	
	public String getHelpContent();
	
	public void putAboutContent(String content);
	
	public String getAboutContent();
	
	public ArrayList<GroupMonitor> listGroups();
	
	public ArrayList<UserMonitor> listAllUsers();
	
	public ArrayList<UserMonitor> listUsersInGroup(GroupMonitor group);
	
	public void putGroups();
	
	public void putAllUsers();
	
	public void putTokenSite();
	
	public void putTokenMail();
	
	
}
