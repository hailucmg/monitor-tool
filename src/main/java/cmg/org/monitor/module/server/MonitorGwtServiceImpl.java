package cmg.org.monitor.module.server;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import cmg.org.monitor.dao.SystemDAO;
import cmg.org.monitor.dao.UtilityDAO;
import cmg.org.monitor.dao.impl.SystemDaoImpl;
import cmg.org.monitor.dao.impl.UtilityDaoImpl;
import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.ext.model.shared.MonitorContainer;
import cmg.org.monitor.ext.model.shared.UserLoginDto;
import cmg.org.monitor.ext.model.shared.UserMonitor;
import cmg.org.monitor.module.client.MonitorGwtService;
import cmg.org.monitor.services.MonitorLoginService;

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
		SystemDAO sysDAO = new SystemDaoImpl();
		try {
			check = sysDAO.addSystem(system);
		} catch (Exception e) {
			logger.log(Level.SEVERE, " ERROR when add new system. Message: "
					+ e.getCause().getMessage());
		}

		return check;
	}

	@Override
	public ArrayList<SystemMonitor> listSystems() {
		SystemDAO sysDAO = new SystemDaoImpl();
		ArrayList<SystemMonitor> list = null;
		try {
			list = sysDAO.listSystemsFromMemcache(false);
		} catch (Exception ex) {
			logger.log(Level.SEVERE,
					" ERROR when load all systems from memcache. Message: "
							+ ex.getCause().getMessage());
		}
		return list;
	}

	@Override
	public UserLoginDto getUserLogin() {
		return MonitorLoginService.getUserLogin();
	}

	@Override
	public SystemMonitor validSystemId(String sysID) {
		SystemDAO sysDAO = new SystemDaoImpl();
		SystemMonitor sys = null;
		try {
			sys = sysDAO.getSystemById(sysID);
		} catch (Exception ex) {
			logger.log(Level.SEVERE, "ERROR when valid system ID. Message: "
					+ ex.getCause().getMessage());
		}
		return sys;
	}

	@Override
	public boolean deleteSystem(String id) throws Exception {
		return false;
	}

	@Override
	public String getAboutContent() {
		UtilityDAO utilDAO = new UtilityDaoImpl();
		return utilDAO.getAboutContent();
	}

	@Override
	public String getHelpContent() {
		UtilityDAO utilDAO = new UtilityDaoImpl();
		return utilDAO.getHelpContent();
	}

	@Override
	public MonitorContainer getSystemMonitorContainer() {
		MonitorContainer container = null;
		SystemDAO sysDAO = new SystemDaoImpl();
		try {
			container = new MonitorContainer();
			container.setRemoteURLs(sysDAO.remoteURLs());
		} catch (Exception ex) {
			logger.log(Level.SEVERE,
					"ERROR when load system container information. Message: "
							+ ex.getMessage());
		}
		return container;
	}

	@Override
	public ArrayList<UserMonitor> listAllUsers() {
		UtilityDAO utilDAO = new UtilityDaoImpl();
		return utilDAO.listAllUsers();
	}
}
