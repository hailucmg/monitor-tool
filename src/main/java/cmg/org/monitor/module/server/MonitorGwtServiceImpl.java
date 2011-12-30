package cmg.org.monitor.module.server;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import cmg.org.monitor.dao.CpuDAO;
import cmg.org.monitor.dao.FileSystemDAO;
import cmg.org.monitor.dao.JvmDAO;
import cmg.org.monitor.dao.MemoryDAO;
import cmg.org.monitor.dao.ServiceDAO;
import cmg.org.monitor.dao.SystemDAO;
import cmg.org.monitor.dao.UtilityDAO;
import cmg.org.monitor.dao.impl.CpuDaoImpl;
import cmg.org.monitor.dao.impl.FileSystemDaoImpl;
import cmg.org.monitor.dao.impl.JvmDaoImpl;
import cmg.org.monitor.dao.impl.MemoryDaoImpl;
import cmg.org.monitor.dao.impl.ServiceDaoImpl;
import cmg.org.monitor.dao.impl.SystemDaoImpl;
import cmg.org.monitor.dao.impl.UtilityDaoImpl;
import cmg.org.monitor.entity.shared.CpuMonitor;
import cmg.org.monitor.entity.shared.FileSystemMonitor;
import cmg.org.monitor.entity.shared.JvmMonitor;
import cmg.org.monitor.entity.shared.MemoryMonitor;
import cmg.org.monitor.entity.shared.ServiceMonitor;
import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.ext.model.shared.GroupMonitor;
import cmg.org.monitor.ext.model.shared.MonitorContainer;
import cmg.org.monitor.ext.model.shared.UserLoginDto;
import cmg.org.monitor.ext.model.shared.UserMonitor;
import cmg.org.monitor.module.client.MonitorGwtService;
import cmg.org.monitor.services.MonitorLoginService;
import cmg.org.monitor.util.shared.MonitorConstant;

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
			String code = sysDAO.createSID();
			check = sysDAO.addSystem(system, code);
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
		return systems;
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
		SystemDAO sysDAO = new SystemDaoImpl();
		return sysDAO.deleteSystem(id);
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
				container.setSys(sysDAO.getSystemById(sysId));
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
		boolean b = false;
		try {
			b = sysDAO.editSystem(sys);
		} catch (Exception ex) {
			logger.log(Level.SEVERE,
					"ERROR when edit system. Message: " + ex.getMessage());
		}
		return b;
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
		ArrayList<MemoryMonitor> list = memDAO.listMemory(sys, MemoryMonitor.MEM);
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

}
