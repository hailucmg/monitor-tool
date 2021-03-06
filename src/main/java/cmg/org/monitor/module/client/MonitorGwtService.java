package cmg.org.monitor.module.client;

import java.util.List;

import cmg.org.monitor.entity.shared.AlertStoreMonitor;
import cmg.org.monitor.entity.shared.ConnectionPool;
import cmg.org.monitor.entity.shared.CpuMonitor;
import cmg.org.monitor.entity.shared.FileSystemMonitor;
import cmg.org.monitor.entity.shared.GoogleAccount;
import cmg.org.monitor.entity.shared.InvitedUser;
import cmg.org.monitor.entity.shared.JvmMonitor;
import cmg.org.monitor.entity.shared.ServiceMonitor;
import cmg.org.monitor.entity.shared.SystemGroup;
import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.entity.shared.SystemUser;
import cmg.org.monitor.ext.model.shared.MonitorContainer;
import cmg.org.monitor.ext.model.shared.UserLoginDto;
import cmg.org.monitor.ext.model.shared.UserMonitor;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("MonitorGwtService")
public interface MonitorGwtService extends RemoteService {
	boolean addSystem(SystemMonitor system);
	
	UserLoginDto getUserLogin();
	
	SystemMonitor[] listSystems();
	SystemMonitor[] listSystemsForChangelog();
	
	SystemMonitor validSystemId(String sysID);

	boolean deleteSystem(String id) throws Exception;

	String getAboutContent();
	
	String getHelpContent();
	
	String getRevisionContent();
	
	String getDefaultContent();
	
	MonitorContainer getSystemMonitorContainer();
	
	UserMonitor[] listAllUsers();
	
	MonitorContainer getSystemMonitorContainer(String sysId);
	
	boolean editSystem(SystemMonitor sys);
	
	JvmMonitor[] listJvms(SystemMonitor sys);
	
	ServiceMonitor[] listServices(SystemMonitor sys);
	
	ConnectionPool[] listPools(SystemMonitor sys);
	
	FileSystemMonitor[] listFileSystems(SystemMonitor sys);
	
	CpuMonitor[] listCpus(SystemMonitor sys);
	
	MonitorContainer listMems(SystemMonitor sys);
	
	AlertStoreMonitor[] listAlertStore(SystemMonitor sys);
	
	MonitorContainer listChangeLog(SystemMonitor sys, int start, int end);
	
	void editLink(String link);
	
	String getLink();
	
	SystemGroup getGroupById(String id);
	
	boolean addnewGroup(String name, String description);
	
	SystemGroup[] getAllGroup();
	
	MonitorContainer getAllUserAndGroup();
	
	boolean deleteGroup(String name, String id);
	
	boolean updateGroup(String groupName, String groupDescription, String id);
	
	boolean updateUserMapping(String email, String idGroup, boolean mapp);
	
	String syncAccount(GoogleAccount googleacc);
	
	GoogleAccount[] listAllGoogleAcc() throws Exception;
	
	public boolean addGoogleAccount(GoogleAccount acc);
	
	boolean addGroupByObj(SystemGroup s);
	
	boolean deleteGoogleAccount(String id);
	
	boolean updateGoogleAccount(GoogleAccount acc);
	
	List<SystemUser> listAllSystemUsers();
	
	boolean updateUserRole(String email, String role, boolean b);
	
	String viewLastestLog(String adminAccount);
	
	MonitorContainer getAllForInvite();
	
	boolean inviteUser3rd(String[] user, String groupID);
	boolean action3rd(String actionType,InvitedUser u);
	
	boolean sendRequestPermission(String firstname, String lastname, String description);
	
	boolean updateFullName(String firstname, String lastname);
	
	MonitorContainer listTimeZone();
	
	boolean updateTimeZone(String timeZone);
	
}
