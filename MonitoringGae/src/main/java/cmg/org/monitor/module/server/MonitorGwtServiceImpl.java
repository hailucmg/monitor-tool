package cmg.org.monitor.module.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import cmg.org.monitor.dao.AlertDao;
import cmg.org.monitor.dao.CpuDAO;
import cmg.org.monitor.dao.FileSystemDAO;
import cmg.org.monitor.dao.JvmDAO;
import cmg.org.monitor.dao.MemoryDAO;
import cmg.org.monitor.dao.ServiceDAO;
import cmg.org.monitor.dao.SystemDAO;
import cmg.org.monitor.dao.UtilityDAO;
import cmg.org.monitor.dao.impl.AlertDaoImpl;
import cmg.org.monitor.dao.impl.CpuDaoImpl;
import cmg.org.monitor.dao.impl.FileSystemDaoImpl;
import cmg.org.monitor.dao.impl.JvmDaoImpl;
import cmg.org.monitor.dao.impl.MemoryDaoImpl;
import cmg.org.monitor.dao.impl.ServiceDaoImpl;
import cmg.org.monitor.dao.impl.SystemDaoImpl;
import cmg.org.monitor.dao.impl.UtilityDaoImpl;
import cmg.org.monitor.entity.shared.AlertStoreMonitor;
import cmg.org.monitor.entity.shared.ChangeLogMonitor;
import cmg.org.monitor.entity.shared.CpuMonitor;
import cmg.org.monitor.entity.shared.FileSystemMonitor;
import cmg.org.monitor.entity.shared.JvmMonitor;
import cmg.org.monitor.entity.shared.MemoryMonitor;
import cmg.org.monitor.entity.shared.NotifyMonitor;
import cmg.org.monitor.entity.shared.ServiceMonitor;
import cmg.org.monitor.entity.shared.SystemGroup;
import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.ext.model.shared.GroupMonitor;
import cmg.org.monitor.ext.model.shared.MonitorContainer;
import cmg.org.monitor.ext.model.shared.UserLoginDto;
import cmg.org.monitor.ext.model.shared.UserMonitor;
import cmg.org.monitor.module.client.MonitorGwtService;
import cmg.org.monitor.services.MonitorLoginService;
import cmg.org.monitor.util.shared.HTMLControl;

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
			check = sysDAO.addSystem(system, sysDAO.createSID());
		} catch (Exception e) {
			logger.log(Level.SEVERE, " ERROR when add new system. Message: "
					+ e.getCause().getMessage());
		}
		return check;
	}

	@Override
	public SystemMonitor[] listSystems() {
		SystemDAO sysDAO = new SystemDaoImpl();
		ArrayList<SystemMonitor> list = null;
		SystemMonitor[] systems = null;
		try {
			list = sysDAO.listSystemsFromMemcache(false);
			if (list != null && list.size() > 0) {
				systems = new SystemMonitor[list.size()];
				list.toArray(systems);
			}
		} catch (Exception ex) {
			logger.log(Level.SEVERE,
					" ERROR when load all systems from memcache. Message: "
							+ ex.getCause().getMessage());
		}
		if (systems == null) {
			return systems;
		} else {
			return sortByname(systems);
		}

	}

	/**
	 * @param sys
	 * @return
	 */
	public SystemMonitor[] sortByname(SystemMonitor[] sys) {
		SystemMonitor temp = null;
		for (int i = 1; i < sys.length; i++) {
			int j;
			SystemMonitor val = sys[i];
			for (j = i - 1; j > -1; j--) {
				temp = sys[j];
				if (temp.compareByCode(val) <= 0) {
					break;
				}
				sys[j + 1] = temp;
			}
			sys[j + 1] = val;
		}
		return sys;
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
	public boolean deleteSystem(String id) {
		SystemDAO sysDAO = new SystemDaoImpl();
		boolean checkdelete = false;
		try {
			checkdelete = sysDAO.deleteSystem(id);
		} catch (Exception ex) {
			logger.log(Level.SEVERE, "ERROR when delete system. Message: " + ex.getMessage());
		}
		return checkdelete;
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
		UtilityDAO utilDAO = new UtilityDaoImpl();
		try {
			container = new MonitorContainer();
			container.setRemoteUrls(sysDAO.listRemoteURLs());
			ArrayList<GroupMonitor> groups = utilDAO.listGroups();
			if (groups != null && groups.size() > 0) {
				GroupMonitor[] listGroups = new GroupMonitor[groups.size()];
				groups.toArray(listGroups);
				container.setGroups(listGroups);
			}
			container.setEmails(sysDAO.listEmails());

		} catch (Exception ex) {
			logger.log(Level.SEVERE,
					"ERROR when load system container information. Message: "
							+ ex.getMessage());
		}
		return container;
	}

	@Override
	public MonitorContainer getSystemMonitorContainer(String sysId) {
		MonitorContainer container = getSystemMonitorContainer();
		SystemDAO sysDAO = new SystemDaoImpl();
		try {
			if (container != null) {
				SystemMonitor sys = sysDAO.getSystemById(sysId);
				container.setSys(sys);
				NotifyMonitor nm = sysDAO.getNotifyOption(sys.getCode());
				if (nm == null) {
					nm = new NotifyMonitor();
				}
				container.setNotify(nm);
			}

		} catch (Exception ex) {
			logger.log(Level.SEVERE,
					"ERROR when load system container information. Message: "
							+ ex.getMessage());
		}
		return container;
	}

	@Override
	public UserMonitor[] listAllUsers() {
		UtilityDAO utilDAO = new UtilityDaoImpl();
		ArrayList<UserMonitor> list = utilDAO.listAllUsers();
		UserMonitor[] users = null;
		if (list != null && list.size() > 0) {
			users = new UserMonitor[list.size()];
			list.toArray(users);
		}
		return users;
	}

	@Override
	public boolean editSystem(SystemMonitor sys) {
		SystemDAO sysDAO = new SystemDaoImpl();
		boolean check = false;
		try {
			check = sysDAO.editSystem(sys);
		} catch (Exception ex) {
			logger.log(Level.SEVERE,
					"ERROR when edit system. Message: " + ex.getMessage());
		}
		return check;
	}

	@Override
	public JvmMonitor[] listJvms(SystemMonitor sys) {
		JvmDAO jvmDAO = new JvmDaoImpl();
		ArrayList<JvmMonitor> list = jvmDAO.listJvm(sys);
		JvmMonitor[] jvms = null;
		if (list != null && list.size() > 0) {
			jvms = new JvmMonitor[list.size()];
			list.toArray(jvms);
		}
		return jvms;
	}

	@Override
	public ServiceMonitor[] listServices(SystemMonitor sys) {
		ServiceDAO serviceDAO = new ServiceDaoImpl();
		ServiceMonitor[] services = null;
		ArrayList<ServiceMonitor> list = serviceDAO.listService(sys);
		if (list != null && list.size() > 0) {
			services = new ServiceMonitor[list.size()];
			list.toArray(services);
		}
		return services;
	}

	@Override
	public FileSystemMonitor[] listFileSystems(SystemMonitor sys) {
		FileSystemDAO fileSystemDAO = new FileSystemDaoImpl();
		ArrayList<FileSystemMonitor> list = fileSystemDAO.listFileSystems(sys);
		FileSystemMonitor[] fileSystems = null;
		if (list != null && list.size() > 0) {
			fileSystems = new FileSystemMonitor[list.size()];
			list.toArray(fileSystems);
		}
		return fileSystems;
	}

	@Override
	public CpuMonitor[] listCpus(SystemMonitor sys) {
		CpuDAO cpuDAO = new CpuDaoImpl();
		ArrayList<CpuMonitor> list = cpuDAO.listCpu(sys);
		CpuMonitor[] cpus = null;
		if (list != null && list.size() > 0) {
			cpus = new CpuMonitor[list.size()];
			list.toArray(cpus);
		}
		return cpus;
	}

	@Override
	public MonitorContainer listMems(SystemMonitor sys) {
		MonitorContainer container = new MonitorContainer();
		MemoryDAO memDAO = new MemoryDaoImpl();
		MemoryMonitor[] memories = null;
		ArrayList<MemoryMonitor> list = memDAO.listMemory(sys,
				MemoryMonitor.MEM);
		if (list != null && list.size() > 0) {
			memories = new MemoryMonitor[list.size()];
			list.toArray(memories);
			container.setRams(memories);
		}

		list = memDAO.listMemory(sys, MemoryMonitor.SWAP);
		if (list != null && list.size() > 0) {
			memories = new MemoryMonitor[list.size()];
			list.toArray(memories);
			container.setSwaps(memories);
		}
		return container;
	}

	@Override
	public AlertStoreMonitor[] listAlertStore(SystemMonitor sys) {
		try {
			AlertDao alertDao = new AlertDaoImpl();
			SystemDAO sysDAO = new SystemDaoImpl();
			AlertStoreMonitor[] stores = null;
			ArrayList<AlertStoreMonitor> list = alertDao.listAlertStore(sys
					.getId());
			AlertStoreMonitor store = alertDao.getLastestAlertStore(sys);

			SystemMonitor system = sysDAO.getSystemById(sys.getId());
			if (store == null) {
				store = new AlertStoreMonitor();
				store.setSysId(system.getId());
				store.setCpuUsage(system.getLastestCpuUsage());
				store.setMemUsage(system.getLastestMemoryUsage());
				store.setTimeStamp(new Date(System.currentTimeMillis()));
			}

			if (list != null && list.size() > 0) {
				stores = new AlertStoreMonitor[list.size() + 1];
				for (int i = 0; i < list.size(); i++) {
					stores[i] = list.get(i);
				}
				stores[list.size()] = store;
			} else {
				stores = new AlertStoreMonitor[1];
				stores[0] = store;
			}
			return stores;
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error when list alert store");
		}
		return null;
	}

	@Override
	public MonitorContainer listChangeLog(SystemMonitor sys, int start, int end) {
		MonitorContainer container = new MonitorContainer();
		SystemDAO sysDAO = new SystemDaoImpl();

		ArrayList<ChangeLogMonitor> changelogs = null;
		int count = -1;
		try {
			changelogs = sysDAO.listChangeLog(sys == null ? null : sys.getCode(),
					start, end);
			count = sysDAO
					.getCounterChangeLog(sys == null ? null : sys.getCode());
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(Level.SEVERE,
					"ERROR when list changelog. Message: " + e.getMessage());
		}
		ChangeLogMonitor[] list = null;
		if (changelogs != null && changelogs.size() > 0) {
			list = new ChangeLogMonitor[changelogs.size()];
			changelogs.toArray(list);
		}
		container.setChangelogCount(count);
		container.setChangelogs(list);
		return container;
	}

	public String getDefaultContent() {
		return HTMLControl.getDefaultContent();
	}
	
	/* (non-Javadoc) * @see cmg.org.monitor.module.client.MonitorGwtService#editLink(java.lang.String) */
	@Override
	public void editLink(String link) {
	    // TODO Auto-generated method stub return false;
	    UtilityDAO dao = new UtilityDaoImpl();
	    dao.putLinkDefault(link);
	}

	/* (non-Javadoc) * @see cmg.org.monitor.module.client.MonitorGwtService#getLink() */
	@Override
	public String getLink() {
	    // TODO Auto-generated method stub return null;
	    UtilityDAO dao = new UtilityDaoImpl();
	    String str = dao.getLinkDefault();
	    return str;
	}

	@Override
	public SystemGroup getGroupById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean addnewGroup(String name, String description) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public SystemGroup[] getAllGroup() {
		// TODO Auto-generated method stub
		return null;
	}
}
