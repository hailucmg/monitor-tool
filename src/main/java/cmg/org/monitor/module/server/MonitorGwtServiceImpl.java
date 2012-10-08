package cmg.org.monitor.module.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringEscapeUtils;

import cmg.org.monitor.dao.AccountSyncLogDAO;
import cmg.org.monitor.dao.AlertDao;
import cmg.org.monitor.dao.CpuDAO;
import cmg.org.monitor.dao.FileSystemDAO;
import cmg.org.monitor.dao.JvmDAO;
import cmg.org.monitor.dao.MemoryDAO;
import cmg.org.monitor.dao.ServiceDAO;
import cmg.org.monitor.dao.SystemAccountDAO;
import cmg.org.monitor.dao.SystemDAO;
import cmg.org.monitor.dao.SystemGroupDAO;
import cmg.org.monitor.dao.UtilityDAO;
import cmg.org.monitor.dao.impl.AccountSyncLogDaoImpl;
import cmg.org.monitor.dao.impl.AlertDaoImpl;
import cmg.org.monitor.dao.impl.CpuDaoImpl;
import cmg.org.monitor.dao.impl.FileSystemDaoImpl;
import cmg.org.monitor.dao.impl.JvmDaoImpl;
import cmg.org.monitor.dao.impl.MemoryDaoImpl;
import cmg.org.monitor.dao.impl.ServiceDaoImpl;
import cmg.org.monitor.dao.impl.SystemAccountDaoImpl;
import cmg.org.monitor.dao.impl.SystemDaoImpl;
import cmg.org.monitor.dao.impl.SystemGroupDaoImpl;
import cmg.org.monitor.dao.impl.UtilityDaoImpl;
import cmg.org.monitor.entity.shared.AccountSyncLog;
import cmg.org.monitor.entity.shared.AlertStoreMonitor;
import cmg.org.monitor.entity.shared.ChangeLogMonitor;
import cmg.org.monitor.entity.shared.CpuMonitor;
import cmg.org.monitor.entity.shared.FileSystemMonitor;
import cmg.org.monitor.entity.shared.GoogleAccount;
import cmg.org.monitor.entity.shared.JvmMonitor;
import cmg.org.monitor.entity.shared.MemoryMonitor;
import cmg.org.monitor.entity.shared.NotifyMonitor;
import cmg.org.monitor.entity.shared.ServiceMonitor;
import cmg.org.monitor.entity.shared.SystemGroup;
import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.entity.shared.SystemUser;
import cmg.org.monitor.ext.model.shared.MonitorContainer;
import cmg.org.monitor.ext.model.shared.UserLoginDto;
import cmg.org.monitor.ext.model.shared.UserMonitor;
import cmg.org.monitor.module.client.MonitorGwtService;
import cmg.org.monitor.services.GoogleAccountService;
import cmg.org.monitor.services.MonitorLoginService;
import cmg.org.monitor.util.shared.HTMLControl;
import cmg.org.monitor.util.shared.MonitorConstant;
import cmg.org.monitor.util.shared.SecurityUtil;

import com.google.gdata.util.AuthenticationException;
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
		SystemGroupDAO groupDao = new SystemGroupDaoImpl();
		MonitorLoginService loginSer = new MonitorLoginService();
		UserLoginDto user = loginSer.getUserLogin();
		try {
			list = sysDAO.listSystemsFromMemcache(false);			
			if (list != null && list.size() > 0) {
				ArrayList<SystemMonitor> tempList = new ArrayList<SystemMonitor>();
				for (SystemMonitor sys : list) {
					boolean check = false;
					SystemGroup gr = groupDao.getByName(sys.getGroupEmail());
					if (user.getRole() == MonitorConstant.ROLE_ADMIN) {
						check = true;
					} else if (user.checkGroup(gr == null ? "" : gr.getId())) {
						check = true;
					}
					if (check) {
						tempList.add(sys);
					}
				}
				if (tempList.size() > 0) {
					systems = new SystemMonitor[tempList.size()];
					tempList.toArray(systems);
				}
			}
		} catch (Exception ex) {
			logger.log(Level.SEVERE,
					" ERROR when load all systems from memcache. Message: "
							+ ex.getCause().getMessage());
		}
		if (systems == null) {
			return null;
		} else {
			String regex ="!@#$%^&*()+=-[]\\\';,./{}|\":<>?~_";
			for (SystemMonitor g: systems) {
				for(int i=0;i<g.getName().length();i++){
					if (regex.indexOf(g.getName().charAt(i)) != -1) {
							g.setName(StringEscapeUtils.escapeHtml3(g.getName()));
							break;
					}
				}
			}
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
		MonitorLoginService service = new MonitorLoginService();
		return service.getUserLogin();
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
			logger.log(Level.SEVERE,
					"ERROR when delete system. Message: " + ex.getMessage());
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
		SystemGroupDaoImpl groupDAO = new SystemGroupDaoImpl();
		try {
			container = new MonitorContainer();
			container.setRemoteUrls(sysDAO.listRemoteURLs());
			SystemGroup[] groups = groupDAO.getAllGroup();
			if (groups != null && groups.length > 0) {
				/*//special character will be replace in here
				String regex ="!@#$%^&*()+=-[]\\\';,./{}|\":<>?~_";
				for (SystemGroup g: groups) {
					for(int i=0;i<g.getName().length();i++){
						if (regex.indexOf(g.getName().charAt(i)) != -1) {
								g.setName(StringEscapeUtils.escapeHtml3(g.getName()));
								break;
						}
					}
					for(int i =0; i<g.getDescription().length(); i++){
						if (regex.indexOf(g.getDescription().charAt(i)) != -1) {
								g.setDescription(StringEscapeUtils.escapeHtml3(g.getDescription()));
								break;
						}
					}
				}*/
				container.setListSystemGroup(groups);
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
		SystemGroupDaoImpl groupDAO = new SystemGroupDaoImpl();
		try {
			if (container != null) {
				SystemMonitor sys = sysDAO.getSystemById(sysId);
				SystemGroup[] groups = groupDAO.getAllGroup();
				if(groups != null){
					//special character will be replace in here
				/*	String regex ="!@#$%^&*()+=-[]\\\';,./{}|\":<>?~_";
					for (SystemGroup g: groups) {
						for(int i=0;i<g.getName().length();i++){
							if (regex.indexOf(g.getName().charAt(i)) != -1) {
									g.setName(StringEscapeUtils.escapeHtml3(g.getName()));
									break;
							}
						}
						for(int i =0; i<g.getDescription().length(); i++){
							if (regex.indexOf(g.getDescription().charAt(i)) != -1) {
									g.setDescription(StringEscapeUtils.escapeHtml3(g.getDescription()));
									break;
							}
						}
					}*/
					container.setListSystemGroup(groups);
				}
				
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
			changelogs = sysDAO.listChangeLog(
					sys == null ? null : sys.getCode(), start, end);
			count = sysDAO.getCounterChangeLog(sys == null ? null : sys
					.getCode());
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

	/*
	 * (non-Javadoc) * @see
	 * cmg.org.monitor.module.client.MonitorGwtService#editLink
	 * (java.lang.String)
	 */
	@Override
	public void editLink(String link) {
		UtilityDAO dao = new UtilityDaoImpl();
		dao.putLinkDefault(link);
	}

	/*
	 * (non-Javadoc) * @see
	 * cmg.org.monitor.module.client.MonitorGwtService#getLink()
	 */
	@Override
	public String getLink() {
		UtilityDAO dao = new UtilityDaoImpl();
		String str = dao.getLinkDefault();
		return str;
	}

	@Override
	public SystemGroup getGroupById(String id) {
		SystemGroupDAO sysGroupDao = new SystemGroupDaoImpl();
		try {
			SystemGroup ss = sysGroupDao.getByID(id);
			return ss;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public boolean addnewGroup(String name, String description) {
		SystemGroupDAO sysGroupDao = new SystemGroupDaoImpl();
		SystemGroup ss = new SystemGroup();
		ss.setName(name);
		ss.setDescription(description);
		try {
			boolean b = sysGroupDao.addNewGroup(ss);
			if (b) {
				sysGroupDao.initSystemGroupMemcache();
			}
			return b;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public SystemGroup[] getAllGroup() {
		SystemGroupDAO sysGroupDao = new SystemGroupDaoImpl();
		try {
			SystemGroup[] sysGroup = sysGroupDao.getAllGroup();
			/*	List<SystemGroup> temp = new ArrayList<SystemGroup>();
			for(SystemGroup s : sysGroup){
				temp.add(s);
			}
			temp = sysGroupDao.sortBynameSystemGroup(temp);
			SystemGroup[] sortGroup = new SystemGroup[temp.size()];
			temp.toArray(sortGroup);*/
			if(sysGroup!=null){
				String regex ="!@#$%^&*()+=-[]\\\';,./{}|\":<>?~_";
				for (SystemGroup g: sysGroup) {
					for(int i=0;i<g.getName().length();i++){
						if (regex.indexOf(g.getName().charAt(i)) != -1) {
								g.setName(StringEscapeUtils.escapeHtml3(g.getName()));
								break;
						}
					}
					for(int i =0; i<g.getDescription().length(); i++){
						if (regex.indexOf(g.getDescription().charAt(i)) != -1) {
								g.setDescription(StringEscapeUtils.escapeHtml3(g.getDescription()));
								break;
						}
					}
				}
			}
			return sysGroup;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean deleteGroup(String name, String id) {
		SystemGroupDAO sysGroupDao = new SystemGroupDaoImpl();
		SystemAccountDAO accountDao = new SystemAccountDaoImpl();
		try {
			boolean check =sysGroupDao.deleteGroup(id);
			if (check) {
				accountDao.initSystemUserMemcache();
				sysGroupDao.initSystemGroupMemcache();
			}
			return check;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean updateUserMapping(String email, String idGroup, boolean mapp) {
		try {
			SystemAccountDAO sysAccDao = new SystemAccountDaoImpl();
			SystemGroupDAO groupDao = new SystemGroupDaoImpl();
			boolean b = false;
			if (mapp) {
				b = groupDao.addUserToGroup(email, idGroup);
			} else {
				b = groupDao.removeUserFromGroup(email, idGroup);
			}
			if (b) {
				sysAccDao.initSystemUserMemcache();
				groupDao.initSystemGroupMemcache();
			}			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	@Override
	public boolean updateGroup(String groupName, String groupDescription,
			String id) {
		SystemGroupDAO sysGroupDao = new SystemGroupDaoImpl();
		try {
			boolean check = sysGroupDao.updateGroup(id, groupName, groupDescription);
			if (check) {
				sysGroupDao.initSystemGroupMemcache();
			}
			return check;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/*
	 * (non-Javadoc) * @see
	 * cmg.org.monitor.module.client.MonitorGwtService#syncAccount
	 * (cmg.org.monitor.entity.shared.GoogleAccount)
	 */
	@Override
	public String syncAccount(GoogleAccount googleacc) {
		GoogleAccountService service;
		SystemAccountDAO accountDao = new SystemAccountDaoImpl();
		try {
			GoogleAccount ac = accountDao.getGoogleAccountByDomain(googleacc.getDomain());
			if (ac.getUsername().equalsIgnoreCase(googleacc.getUsername())) {
				service = new GoogleAccountService(ac);
				service.sync();
				return service.getLog();
			} else {
				return "FAIL: Can not sync google account. Wrong username!";
			}
			
		} catch (AuthenticationException ae) {
			return "FAIL: Authenticantion fail. Exception: " + ae.getMessage();
		}  catch (Exception e) {
			return "FAIL: Can not sync google account. Exception: " + e.getMessage();
		}
	}

	/*
	 * (non-Javadoc) * @see
	 * cmg.org.monitor.module.client.MonitorGwtService#listAllGoogleAcc()
	 */
	@Override
	public GoogleAccount[] listAllGoogleAcc() throws Exception {		
		SystemAccountDAO dao = new SystemAccountDaoImpl();
		List<GoogleAccount> list = new ArrayList<GoogleAccount>();
		try {
			list = dao.listAllGoogleAccount();
			if (list != null && !list.isEmpty()) {
				GoogleAccount[] tempList = new GoogleAccount[list.size()];
				list.toArray(tempList);
				return tempList;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * (non-Javadoc) * @see
	 * cmg.org.monitor.module.client.MonitorGwtService#addGoogleAccount
	 * (cmg.org.monitor.entity.shared.GoogleAccount)
	 */
	@Override
	public boolean addGoogleAccount(GoogleAccount acc) {
		SystemAccountDAO dao = new SystemAccountDaoImpl();
		try {
			GoogleAccount temp = dao.getGoogleAccountByDomain(acc.getDomain());
			boolean b = false;
			if (temp != null) {
				if (!temp.getPassword().equalsIgnoreCase(acc.getPassword())) {
					temp.setPassword(SecurityUtil.encrypt(acc.getPassword()));
				}
				temp.setDomain(acc.getDomain());
				temp.setUsername(acc.getUsername());
				b = dao.updateGoogleAccount(temp);
			} else {
				acc.setPassword(SecurityUtil.encrypt(acc.getPassword()));
				b = dao.createGoogleAccount(acc);
			}		
			if (b) {
				dao.initGoogleAccountMemcache();
			}
			return b;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/*
	 * (non-Javadoc) * @see
	 * cmg.org.monitor.module.client.MonitorGwtService#addGroupByObj
	 * (cmg.org.monitor.entity.shared.SystemGroup)
	 */
	@Override
	public boolean addGroupByObj(SystemGroup s) {
		SystemGroupDAO sysGroupDao = new SystemGroupDaoImpl();
		try {
			boolean check = sysGroupDao.addNewGroup(s);
			if (check) {
				sysGroupDao.initSystemGroupMemcache();
			}
			return check;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public MonitorContainer getAllUserAndGroup() {
		SystemGroupDAO sysGroupDao = new SystemGroupDaoImpl();
		SystemAccountDAO sysAccountDAO = new SystemAccountDaoImpl();
		try {
			MonitorContainer container = new MonitorContainer();

			SystemGroup[] sysGroup = sysGroupDao.getAllGroup();
			/*	List<SystemGroup> temp = new ArrayList<SystemGroup>();
			for(SystemGroup s : sysGroup){
				temp.add(s);
			}
			temp = sysGroupDao.sortBynameSystemGroup(temp);
			SystemGroup[] sortGroup = new SystemGroup[temp.size()];
			temp.toArray(sortGroup);*/
			if(sysGroup!=null){
				String regex ="!@#$%^&*()+=-[]\\\';,./{}|\":<>?~_";
				for (SystemGroup g: sysGroup) {
					for(int i=0;i<g.getName().length();i++){
						if (regex.indexOf(g.getName().charAt(i)) != -1) {
								g.setName(StringEscapeUtils.escapeHtml3(g.getName()));
								break;
						}
					}
					for(int i =0; i<g.getDescription().length(); i++){
						if (regex.indexOf(g.getDescription().charAt(i)) != -1) {
								g.setDescription(StringEscapeUtils.escapeHtml3(g.getDescription()));
								break;
						}
					}
				}
			}
			container.setListSystemGroup(sysGroup);
			List<SystemUser> sysUSers = sysAccountDAO.listAllSystemUser(true);
			SystemUser[] listUser = new SystemUser[sysUSers.size()];
			sysUSers.toArray(listUser);
			container.setListSystemUsers(listUser);
			return container;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * (non-Javadoc) * @see
	 * cmg.org.monitor.module.client.MonitorGwtService#deleteGoogleAccount
	 * (java.lang.String)
	 */
	@Override
	public boolean deleteGoogleAccount(String id) {
		SystemAccountDAO accDao = new SystemAccountDaoImpl();
		try {
			boolean check = accDao.deleteGoogleAccount(id);
			if (check) {
				accDao.initGoogleAccountMemcache();
			}
			return check;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/*
	 * (non-Javadoc) * @see
	 * cmg.org.monitor.module.client.MonitorGwtService#updateGoogleAccount
	 * (cmg.org.monitor.entity.shared.GoogleAccount)
	 */
	@Override
	public boolean updateGoogleAccount(GoogleAccount acc) {
		SystemAccountDAO accDao = new SystemAccountDaoImpl();
		try {
			boolean check = accDao.updateGoogleAccount(acc);
			if (check) {
				accDao.initGoogleAccountMemcache();
			}
			return check;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/*
	 * (non-Javadoc) * @see
	 * cmg.org.monitor.module.client.MonitorGwtService#listAllSystemUsers()
	 */
	@Override
	public List<SystemUser> listAllSystemUsers() {
		// TODO Auto-generated method stub return null;
		SystemAccountDAO accDao = new SystemAccountDaoImpl();
		try {
			List<SystemUser> list = accDao.listAllSystemUser(true);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * (non-Javadoc) * @see
	 * cmg.org.monitor.module.client.MonitorGwtService#updateUserRole
	 * (java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public boolean updateUserRole(String email, String role, boolean b) {
		SystemAccountDAO systemDao = new SystemAccountDaoImpl();
		try {
			boolean done = systemDao.updateRole(email, role, b);
			if (done) {
				systemDao.initSystemUserMemcache();
			}
			return done;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * (non-Javadoc)
	 * @see cmg.org.monitor.module.client.MonitorGwtService#viewLastestLog(java.lang.String) 
	 */
	public String viewLastestLog(String adminAccount) {
		AccountSyncLogDAO logDao = new AccountSyncLogDaoImpl();
		try {
			AccountSyncLog log  = logDao.getLastestLog(adminAccount);
			return log == null ? "" : log.getLog();
		} catch (Exception e) {
			return "Error. Message: " + e.getMessage();
		}		
	}
}
