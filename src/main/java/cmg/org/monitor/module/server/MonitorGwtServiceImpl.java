package cmg.org.monitor.module.server;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import cmg.org.monitor.dao.CpuMemoryDAO;
import cmg.org.monitor.dao.FileSystemDAO;
import cmg.org.monitor.dao.JVMMemoryDAO;
import cmg.org.monitor.dao.ServiceMonitorDAO;
import cmg.org.monitor.dao.SystemMonitorDAO;
import cmg.org.monitor.dao.impl.CpuMemoryDaoJDOImpl;
import cmg.org.monitor.dao.impl.FileSystemDaoJDOImpl;
import cmg.org.monitor.dao.impl.JVMMemoryDaoJDOImpl;
import cmg.org.monitor.dao.impl.ServiceMonitorDaoJDOImpl;
import cmg.org.monitor.dao.impl.SystemMonitorDaoJDOImpl;
import cmg.org.monitor.entity.shared.CpuMemory;
import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.ext.model.shared.MonitorEditDto;
import cmg.org.monitor.ext.model.shared.UserDto;
import cmg.org.monitor.ext.model.shared.UserLoginDto;
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
	public boolean addSystem(SystemMonitor system) {

		boolean check = false;
		SystemMonitorDaoJDOImpl systemDAO = new SystemMonitorDaoJDOImpl();
		try {
			system.setCode(systemDAO.createCode());
			check = systemDAO.addnewSystem(system);
		} catch (Exception e) {
			// TODO: handle exception
			logger.log(Level.SEVERE, e.getCause().getMessage());
		}

		return check;
	}

	@Override
	public MonitorEditDto groups() throws Exception {
		SystemMonitorDAO sysDAO = new SystemMonitorDaoJDOImpl();
		String[] list = null;
		String[] emails = null;
		String[] remoteURLS = null;
		MonitorEditDto sys = new MonitorEditDto();
		try {
			list = sysDAO.groups();
			emails = sysDAO.getEmails();
			remoteURLS = sysDAO.remoteURLs();
			sys.setGroups(list);
			sys.setEmails(emails);
			sys.setRemoteURLs(remoteURLS);
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getCause().getMessage());
		}
		return sys;
	}

	@Override
	public SystemMonitor[] listSystems() {
		SystemMonitorDAO sysDAO = new SystemMonitorDaoJDOImpl();
		SystemMonitor[] list = null;
		try {
			list = sysDAO.listSystems(false);
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getCause().getMessage());
		}
		return list;
	}

	@Override
	public MonitorEditDto getSystembyID(String id) {
		SystemMonitorDAO sysDAO = new SystemMonitorDaoJDOImpl();
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
			monitorEdit.setEmail(system.getEmail());
			monitorEdit.setPasswordEmail(system.getEmailPassword());
			groups = sysDAO.groups();
			monitorEdit.setGroups(groups);
			monitorEdit.setEmails(sysDAO.getEmails());
			monitorEdit.setRemoteURLs(sysDAO.remoteURLs());
			for (int i = 0; i < groups.length; i++) {
				if (groups[i].toString().equals(system.getGroupEmail())) {
					monitorEdit.setSelect(i);
				}
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getCause().getMessage());
		}
		return monitorEdit;
	}

	@Override
	public boolean editSystembyID(String id,SystemMonitor sysNew) throws Exception {
		String b = "";
		boolean check = false;
		SystemMonitorDaoJDOImpl systemDAO = new SystemMonitorDaoJDOImpl();
		try {
			check = systemDAO
					.editSystembyID(id, sysNew.getName(),
							sysNew.getUrl(), sysNew.getProtocol(),
							sysNew.getGroupEmail(), sysNew.getIp(),
							sysNew.getRemoteUrl(), sysNew.getEmail(),
							sysNew.isActive());
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getCause().getMessage());
		}
		return check;

	}

	@Override
	public UserLoginDto getUserLogin() {
		return MonitorLoginService.getUserLogin();
	}

	public SystemMonitor getLastestDataMonitor(String sysID) {
		SystemMonitorDAO sysDAO = new SystemMonitorDaoJDOImpl();
		FileSystemDAO fsDAO = new FileSystemDaoJDOImpl();
		ServiceMonitorDAO smDAO = new ServiceMonitorDaoJDOImpl();
		CpuMemoryDAO cmDAO = new CpuMemoryDaoJDOImpl();
		JVMMemoryDAO jvmDAO = new JVMMemoryDaoJDOImpl();
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
				sys.setLastestJvm(jvmDAO.getLastestJvm(sys));
			}
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getCause().getMessage());
		}
		return sys;
	}

	@Override
	public SystemMonitor validSystemId(String sysID) {
		SystemMonitorDAO sysDAO = new SystemMonitorDaoJDOImpl();
		SystemMonitor sys = null;
		try {
			sys = sysDAO.getSystembyID(sysID);
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getCause().getMessage());
		}
		return sys;
	}

	@Override
	public CpuMemory[] listCpuMemoryHistory(String sysID) {
		SystemMonitorDAO sysDAO = new SystemMonitorDaoJDOImpl();
		CpuMemoryDAO cmDAO = new CpuMemoryDaoJDOImpl();
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
		SystemMonitorDAO sysDAO = new SystemMonitorDaoJDOImpl();
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
		SystemMonitorDAO sysDAO = new SystemMonitorDaoJDOImpl();
		boolean check = sysDAO.deleteSystembyID(id);
		return check;
	}

	@Override
	public boolean deleteListSystem(String[] ids) throws Exception {
		return false;
	}

	@Override
	public Map<String, UserDto> listUser() throws Exception {
		Map<String, UserDto> listAllUser = Ultility.listAllUser();
		return listAllUser;

	}

	@Override
	public String getAboutContent() {
		SitesHelper sh = new SitesHelper();
		return sh.getSiteEntryContent(MonitorConstant.SITES_ABOUT_CONTENT_ID);
		// MonitorUtil.parseHref(sh.getSiteEntryContent(MonitorConstant.SITES_ABOUT_CONTENT_ID));
	}

	@Override
	public String getHelpContent() {
		SitesHelper sh = new SitesHelper();
		return sh.getSiteEntryContent(MonitorConstant.SITES_HELP_CONTENT_ID);
		// MonitorUtil.parseHref(sh.getSiteEntryContent(MonitorConstant.SITES_HELP_CONTENT_ID));
	}
}
