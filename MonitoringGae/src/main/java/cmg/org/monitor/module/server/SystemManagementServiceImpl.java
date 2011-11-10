package cmg.org.monitor.module.server;

import cmg.org.monitor.dao.impl.SystemMonitorDaoJDOImpl;
import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.ext.model.shared.UserLoginDto;
import cmg.org.monitor.module.client.SystemManagementService;
import cmg.org.monitor.services.MonitorLoginService;
import cmg.org.monitor.services.SystemService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class SystemManagementServiceImpl extends RemoteServiceServlet implements SystemManagementService{

	@Override
	public UserLoginDto getUserLogin() {
		return MonitorLoginService.getUserLogin();
	}
	@Override
	public SystemMonitor[] listSystem(boolean isDeleted) throws Exception {
		// TODO Auto-generated method stub
		
		SystemMonitor[] list =null;
		try {
			SystemMonitorDaoJDOImpl systemDAO = new SystemMonitorDaoJDOImpl();
			list = systemDAO.listSystems(false);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public String editSystem(String id) throws Exception {
		// TODO Auto-generated method stub
		String html = "EditSystem.html?id="+id;
		return html;
	}

	@Override
	public boolean deleteSystem(String id) throws Exception {
		// TODO Auto-generated method stub
		SystemMonitorDaoJDOImpl systemDAO = new SystemMonitorDaoJDOImpl();
		boolean check = systemDAO.deleteSystembyID(id);
		return check;
	}

	@Override
	public boolean deleteListSystem(String[] ids) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

}
