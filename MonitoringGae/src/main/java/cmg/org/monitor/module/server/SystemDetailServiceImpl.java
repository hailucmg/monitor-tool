package cmg.org.monitor.module.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import cmg.org.monitor.dao.CpuMemoryDAO;
import cmg.org.monitor.dao.FileSystemDAO;
import cmg.org.monitor.dao.ServiceMonitorDAO;
import cmg.org.monitor.dao.SystemMonitorDAO;
import cmg.org.monitor.dao.impl.CpuMemoryDaoJDOImpl;
import cmg.org.monitor.dao.impl.FileSystemDaoJDOImpl;
import cmg.org.monitor.dao.impl.ServiceMonitorDaoJDOImpl;
import cmg.org.monitor.dao.impl.SystemMonitorDaoJDOImpl;
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
			ex.printStackTrace();
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
		}
		return b;
	}

}
