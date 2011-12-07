package cmg.org.monitor.module.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import cmg.org.monitor.dao.SystemMonitorDAO;
import cmg.org.monitor.dao.impl.SystemMonitorDaoJDOImpl;
import cmg.org.monitor.ext.model.shared.UserDto;
import cmg.org.monitor.ext.model.shared.UserLoginDto;
import cmg.org.monitor.memcache.MonitorMemcache;
import cmg.org.monitor.memcache.shared.SystemMonitorDto;
import cmg.org.monitor.module.client.MonitorGwtService;
import cmg.org.monitor.services.MonitorLoginService;
import cmg.org.monitor.services.SitesHelper;
import cmg.org.monitor.util.shared.MonitorConstant;
import cmg.org.monitor.util.shared.Ultility;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class MonitorGwtServiceImpl extends RemoteServiceServlet implements
		MonitorGwtService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger logger = Logger
			.getLogger(MonitorGwtServiceImpl.class.getCanonicalName());

	@Override
	public String addSystem(SystemMonitorDto system, String url) throws Exception {
		String callback = null;
		try {
			if (MonitorMemcache.checkRemoteUrl(system.getRemoteUrl())) {
				callback = "Remote-URL is existing";
			} else {
				if (MonitorMemcache.createSystem(system)) {
					callback = "done";
				} else {
					callback = "wrong to config jar or database";
				}				
			}

		} catch (Exception e) {
			callback = e.toString();
		}

		return callback;
	}

	@Override
	public String[] groups() throws Exception {
		SystemMonitorDAO sysDAO = new SystemMonitorDaoJDOImpl();
		String[] list = null;
		try {
			list = sysDAO.groups();
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getCause().getMessage());
		}
		return list;
	}
	

	@Override
	public SystemMonitorDto getSystembyID(String id) throws Exception {
		return MonitorMemcache.getSystemMonitorById(id);
	}

	@Override
	public String editSystembyID(SystemMonitorDto system) throws Exception {
		String callback = null;
		try {
			boolean check = MonitorMemcache.checkRemoteUrl(system.getRemoteUrl());
			SystemMonitorDto oldSys = MonitorMemcache.getSystemById(system.getId());
			if (check && !oldSys.getRemoteUrl().toLowerCase().equals(system.getRemoteUrl().toLowerCase())) {
				callback = "Remote URL is exitsting";
			} else {
				if (MonitorMemcache.updateSystem(system)) {
					callback = "done";
				} else {
					callback = "config database error";
				}				
			}

		} catch (Exception e) {
			callback = e.toString();
		}

		return callback;
	}

	@Override
	public UserLoginDto getUserLogin() {
		return MonitorLoginService.getUserLogin();
	}

	public SystemMonitorDto getLastestDataMonitor(String sid) {
		return MonitorMemcache.getLastestDataMonitor(sid);
	}

	@Override
	public boolean validSystemId(String sysID) {
		return MonitorMemcache.checkSystemId(sysID);
	}


	@Override
	public boolean deleteSystem(String id) throws Exception {
		SystemMonitorDAO sysDAO = new SystemMonitorDaoJDOImpl();
		boolean check = sysDAO.deleteSystembyID(id);
		return check;
	}


	@Override
	public Map<String, UserDto> listUser() throws Exception {
		String[] admins = null;
		String[] users = null;
		Map<String, UserDto> list = new HashMap<String, UserDto>();

		try {

			admins = Ultility.listAdmin();
			for (int i = 0; i < admins.length; i++) {
				String[] temp = admins[i].split(":");
				UserDto u = new UserDto();
				u.setUsername(temp[0].split("@")[0]);
				u.setEmail(temp[0]);
				u.setGroup(temp[1]);
				list.put(u.getUsername().toString().trim(), u);
			}

			users = Ultility.listUser();

			for (int j = 0; j < users.length; j++) {

				String[] temp = users[j].split(":");
				UserDto u = new UserDto();
				u.setUsername(temp[0].split("@")[0]);
				u.setEmail(temp[0]);
				u.setGroup(temp[1]);

				if (!list.containsKey(u.getUsername().toString().trim())) {
					list.put(u.getUsername(), u);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}

	@Override
	public String getAboutContent() {
		SitesHelper sh = new SitesHelper();
		return sh.getSiteEntryContent(MonitorConstant.SITES_ABOUT_CONTENT_ID);
	}

	@Override
	public String getHelpContent() {
		SitesHelper sh = new SitesHelper();
		return sh.getSiteEntryContent(MonitorConstant.SITES_HELP_CONTENT_ID);
	}

	@Override
	public ArrayList<SystemMonitorDto> listSystems() {
		return MonitorMemcache.listSystemMonitorToUi();
	}
}
