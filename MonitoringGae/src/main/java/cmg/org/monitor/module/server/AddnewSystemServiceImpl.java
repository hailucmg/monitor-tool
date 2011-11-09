package cmg.org.monitor.module.server;

import cmg.org.monitor.dao.impl.SystemMonitorDaoJDOImpl;
import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.module.client.AddnewSystemService;


import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class AddnewSystemServiceImpl extends RemoteServiceServlet implements
		AddnewSystemService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String addSystem(SystemMonitor system, String url) throws Exception {
		// TODO Auto-generated method stub
		String callback = null;

		SystemMonitorDaoJDOImpl systemDAO = new SystemMonitorDaoJDOImpl();
		try {
			system.setCode(systemDAO.createCode());
			if (systemDAO.addnewSystem(system)) {
				callback = "done";
			} else {
				callback = "wrong to config jar or database";
			}
		} catch (Exception e) {
			// TODO: handle exception
			callback = e.toString();
		}

		return callback;
	}

	@Override
	public String[] groups() throws Exception {
		// TODO Auto-generated method stub
		String[] list = null;
		try {
			SystemMonitorDaoJDOImpl systemDAO = new SystemMonitorDaoJDOImpl();
			list = systemDAO.groups();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 
	 */

}
