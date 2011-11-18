package cmg.org.monitor.module.server;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import cmg.org.monitor.dao.CpuMemoryDAO;
import cmg.org.monitor.dao.FileSystemDAO;
import cmg.org.monitor.dao.ServiceMonitorDAO;
import cmg.org.monitor.dao.SystemMonitorDAO;
import cmg.org.monitor.dao.impl.CpuMemoryDaoJDOImpl;
import cmg.org.monitor.dao.impl.FileSystemDaoJDOImpl;
import cmg.org.monitor.dao.impl.ServiceMonitorDaoJDOImpl;
import cmg.org.monitor.dao.impl.SystemMonitorDaoJDOImpl;
import cmg.org.monitor.entity.shared.CpuMemory;
import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.ext.model.shared.MonitorEditDto;
import cmg.org.monitor.ext.model.shared.UserDto;
import cmg.org.monitor.ext.model.shared.UserLoginDto;
import cmg.org.monitor.module.client.MonitorGwtService;
import cmg.org.monitor.services.MonitorLoginService;
import cmg.org.monitor.util.shared.HTMLControl;
import cmg.org.monitor.util.shared.Ultility;

public class MonitorGwtServiceImpl extends RemoteServiceServlet implements
		MonitorGwtService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger logger = Logger
			.getLogger(MonitorGwtServiceImpl.class.getCanonicalName());

	SystemMonitorDAO sysDAO = new SystemMonitorDaoJDOImpl();
	CpuMemoryDAO cmDAO = new CpuMemoryDaoJDOImpl();
	FileSystemDAO fsDAO = new FileSystemDaoJDOImpl();
	ServiceMonitorDAO smDAO = new ServiceMonitorDaoJDOImpl();

	@Override
	public String addSystem(SystemMonitor system, String url) throws Exception {
		// TODO Auto-generated method stub
		String callback = null;
		boolean check= true;
		SystemMonitorDaoJDOImpl systemDAO = new SystemMonitorDaoJDOImpl();
		try {
			
			String remoteURL = system.getRemoteUrl();
			String[] remoteURLs = systemDAO.remoteURLs();
			for (int i = 0; i < remoteURLs.length; i++) {
				if (remoteURL.toLowerCase().equals(remoteURLs[i].toLowerCase())) {
					callback = "Remote-URL is existing";
					check = false;
					break;
					
				}
			}
			if(check){
				system.setCode(systemDAO.createCode());
				if (systemDAO.addnewSystem(system)) {
					callback = "done";
				} else {
					callback = "wrong to config jar or database";
				}
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			callback = e.toString();
		}

		return callback;
	}

	@Override
	public String[] groups() throws Exception {
		String[] list = null;
		try {
			list = sysDAO.groups();
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getCause().getMessage());
		}
		return list;
	}

	@Override
	public SystemMonitor[] listSystems() {
		SystemMonitor[] list = null;
		try {
			list = sysDAO.listSystems(false);
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getCause().getMessage());
		}
		return list;
	}

	@Override
	public MonitorEditDto getSystembyID(String id) throws Exception {
		MonitorEditDto monitorEdit = new MonitorEditDto();
		String[] groups;
		try {
			SystemMonitor system = sysDAO.getSystembyID(id);
			monitorEdit.setId(system.getId());
			monitorEdit.setActive(system.isActive());
			monitorEdit.setGroup(system.getGroupEmail());
			monitorEdit.setIp(system.getIp());
			monitorEdit.setProtocol(system.getProtocol());
			monitorEdit.setUrl(system.getUrl());
			monitorEdit.setName(system.getName());
			monitorEdit.setRemoteURl(system.getRemoteUrl());
			groups = sysDAO.groups();
			monitorEdit.setGroups(groups);
			for (int i = 0; i < groups.length; i++) {
				if (groups[i].toString().equals(system.getGroupEmail())) {
					monitorEdit.setSelect(i);
				}
			}
		} catch (Exception e) {
			throw e;
		}
		return monitorEdit;
	}

	@Override
	public String editSystembyID(MonitorEditDto system, String newName,
			String newAddress, String protocol, String group, String ip,
			String remoteURL, boolean isActive) throws Exception {
		String b = "";
		boolean check = false;
		try {
			SystemMonitorDaoJDOImpl systemDAO = new SystemMonitorDaoJDOImpl();
			if (system.getRemoteURl().equals(remoteURL)) {
				if (!systemDAO.editSystembyID(system.getId(), newName,
						newAddress, protocol, group, ip, remoteURL, isActive)) {
					b = "config database error";
				} else {
					b = "done";
				}
				check = true;
			}
			if (!check) {
				String[] remoteURLs = systemDAO.remoteURLs();
				for (int i = 0; i < remoteURLs.length; i++) {
					if (remoteURL.toLowerCase().equals(
							remoteURLs[i].toLowerCase())) {
						b = "Remote URL is exitsting";
						return b;
					}
				}
				if (!systemDAO.editSystembyID(system.getId(), newName,
						newAddress, protocol, group, ip, remoteURL, isActive)) {
					b = "config database error";
				} else {
					b = "done";
				}
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getCause().getMessage());
		}
		return b;

	}

	@Override
	public UserLoginDto getUserLogin() {
		return MonitorLoginService.getUserLogin();
	}

	public SystemMonitor getLastestDataMonitor(String sysID) {
		SystemMonitor sys = null;
		try {
			sys = sysDAO.getSystembyID(sysID);
			if (sys != null) {
				sys.setLastCpuMemory(cmDAO.getLastestCpuMemory(sys, 1) == null ? null
						: cmDAO.getLastestCpuMemory(sys, 1)[0]);
				sys.setLastestFileSystems(fsDAO.listLastestFileSystem(sys));
				sys.setLastestServiceMonitors(smDAO.listLastestService(sys));
				sys.setListHistoryCpuMemory(cmDAO.getLastestCpuMemory(sys, 20));
				sys.setHealthStatus(sysDAO.getCurrentHealthStatus(sys));
			}
		} catch (Exception ex) {
			// logger.log(Level.SEVERE, ex.getCause().getMessage());
		}
		return sys;
	}

	@Override
	public boolean validSystemId(String sysID) {
		boolean b = true;
		try {
			b = sysDAO.getSystembyID(sysID) != null;
		} catch (Exception ex) {
			b = false;
			logger.log(Level.SEVERE, ex.getCause().getMessage());
		}
		return b;
	}

	@Override
	public CpuMemory[] listCpuMemoryHistory(String sysID) {
		CpuMemory[] list = null;
		SystemMonitor sys = null;
		try {
			sys = sysDAO.getSystembyID(sysID);
			list = cmDAO.getLastestCpuMemory(sys, 64800);
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getCause().getMessage());
		}
		return list;
	}

	@Override
	public SystemMonitor[] listSystem(boolean isDeleted) throws Exception {
		SystemMonitor[] list = null;
		try {
			list = sysDAO.listSystems(false);
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getCause().getMessage());
		}
		return list;
	}

	@Override
	public String editSystem(String id) throws Exception {
		// TODO Auto-generated method stub
		String html = "EditSystem.html?id=" + id;
		return html;
	}

	@Override
	public boolean deleteSystem(String id) throws Exception {
		boolean check = sysDAO.deleteSystembyID(id);
		return check;
	}

	@Override
	public boolean deleteListSystem(String[] ids) throws Exception {
		return false;
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
		}
		return list;
	}

	@Override
	public String getAboutContent() {
		return HTMLControl.getAboutContent();
	}

	@Override
	public String getHelpContent() {
		return HTMLControl.getHelpContent();
	}
}
