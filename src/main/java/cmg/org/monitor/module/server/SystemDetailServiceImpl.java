package cmg.org.monitor.module.server;

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
import cmg.org.monitor.module.client.SystemDetailService;

public class SystemDetailServiceImpl extends RemoteServiceServlet implements SystemDetailService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	SystemMonitorDAO sysDAO = new SystemMonitorDaoJDOImpl();
	CpuMemoryDAO cmDAO = new CpuMemoryDaoJDOImpl();
	FileSystemDAO fsDAO = new FileSystemDaoJDOImpl();
	ServiceMonitorDAO smDAO = new ServiceMonitorDaoJDOImpl();
	
	private static final Logger logger = Logger.getLogger(SystemDetailServiceImpl.class
			.getCanonicalName());
	
	public SystemMonitor getLastestDataMonitor(String sysID) {		
		SystemMonitor sys = null;
		try {
			sys = sysDAO.getSystembyID(sysID);
			if (sys != null) {
				sys.setLastCpuMemory(cmDAO.getLastestCpuMemory(sys, 1) == null
						  				? null
						  						: cmDAO.getLastestCpuMemory(sys, 1)[0]);
				sys.setLastestFileSystems(fsDAO.listLastestFileSystem(sys));
				sys.setLastestServiceMonitors(smDAO.listLastestService(sys));
				sys.setListHistoryCpuMemory(cmDAO.getLastestCpuMemory(sys, 20));
				sys.setHealthStatus(sysDAO.getCurrentHealthStatus(sys));
			}
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getCause().getMessage());
		}		
		return sys;
	}

	@Override
	public boolean validSystemId(String sysID) {
		boolean b = true;
		try {
			b = sysDAO.getSystembyID(sysID) != null;
		} catch (Exception ex) {
			b= false;
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

}
