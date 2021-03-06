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

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface MonitorGwtServiceAsync {

	void deleteSystem(String id, AsyncCallback<Boolean> callback);

	void getAboutContent(AsyncCallback<String> callback);

	void getHelpContent(AsyncCallback<String> callback);

	void getUserLogin(AsyncCallback<UserLoginDto> callback);

	void listSystems(AsyncCallback<SystemMonitor[]> callback);

	void validSystemId(String sysID, AsyncCallback<SystemMonitor> callback);

	void getSystemMonitorContainer(String sysId,
			AsyncCallback<MonitorContainer> callback);

	void getSystemMonitorContainer(AsyncCallback<MonitorContainer> callback);

	void addSystem(SystemMonitor system, AsyncCallback<Boolean> callback);

	void listAllUsers(AsyncCallback<UserMonitor[]> callback);

	void editSystem(SystemMonitor sys, AsyncCallback<Boolean> callback);

	void listJvms(SystemMonitor sys, AsyncCallback<JvmMonitor[]> callback);

	void listCpus(SystemMonitor sys,
			AsyncCallback<CpuMonitor[]> callback);

	void listFileSystems(SystemMonitor sys,
			AsyncCallback<FileSystemMonitor[]> callback);

	void listServices(SystemMonitor sys,
			AsyncCallback<ServiceMonitor[]> callback);

	void listMems(SystemMonitor sys, AsyncCallback<MonitorContainer> callback);

	void listAlertStore(SystemMonitor sys,
			AsyncCallback<AlertStoreMonitor[]> callback);

	void listChangeLog(SystemMonitor sys, int start, int end,
			AsyncCallback<MonitorContainer> callback);

	void editLink(String link, AsyncCallback<Void> callback);
	
	void getLink(AsyncCallback<String> callback);
	
	void getDefaultContent(AsyncCallback<String> callback);

	void getGroupById(String id, AsyncCallback<SystemGroup> callback);

	void addnewGroup(String name, String description,
			AsyncCallback<Boolean> callback);

	void getAllGroup(AsyncCallback<SystemGroup[]> callback);

	void deleteGroup(String name, String id, AsyncCallback<Boolean> callback);

	void updateUserMapping(String email, String idGroup, boolean mapp,
			AsyncCallback<Boolean> callback);

	void updateGroup(String groupName, String groupDescription, String id,
			AsyncCallback<Boolean> callback);

	void syncAccount(GoogleAccount googleacc, AsyncCallback<String> callback);

	void listAllGoogleAcc(AsyncCallback<GoogleAccount[]> callback);

	void addGoogleAccount(GoogleAccount acc, AsyncCallback<Boolean> callback);

	void addGroupByObj(SystemGroup s, AsyncCallback<Boolean> callback);

	void getAllUserAndGroup(AsyncCallback<MonitorContainer> callback);
	
	void deleteGoogleAccount(String id, AsyncCallback<Boolean> callback);

	void updateGoogleAccount(GoogleAccount acc, AsyncCallback<Boolean> callback);

	void listAllSystemUsers(AsyncCallback<List<SystemUser>> callback);

	void updateUserRole(String email, String role, boolean b, AsyncCallback<Boolean> callback);

	void viewLastestLog(String adminAccount, AsyncCallback<String> callback);

	void listSystemsForChangelog(AsyncCallback<SystemMonitor[]> callback);

	void getRevisionContent(AsyncCallback<String> callback);

	void getAllForInvite(AsyncCallback<MonitorContainer> callback);

	void inviteUser3rd(String[] user, String groupID,
			AsyncCallback<Boolean> callback);

	void action3rd(String actionType, InvitedUser u,
			AsyncCallback<Boolean> callback);

	void sendRequestPermission(String firstname, String lastname,
			String description, AsyncCallback<Boolean> callback);

	void updateFullName(String firstname, String lastname,
			AsyncCallback<Boolean> callback);

	void listTimeZone(AsyncCallback<MonitorContainer> callback);

	void updateTimeZone(String timeZone, AsyncCallback<Boolean> callback);

	void listPools(SystemMonitor sys, AsyncCallback<ConnectionPool[]> callback);
}
