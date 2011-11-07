package cmg.org.monitor.module.server;




import cmg.org.monitor.dao.impl.SystemMonitorDaoJDOImpl;
import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.module.client.AddnewSystemService;
import cmg.org.monitor.services.SystemService;
import cmg.org.monitor.util.shared.Ultility;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class AddnewSystemServiceImpl  extends RemoteServiceServlet implements AddnewSystemService{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String addSystem(SystemMonitor system, String url) throws Exception {
		// TODO Auto-generated method stub
		String callback = null;
		String ip="";
		SystemMonitorDaoJDOImpl systemDAO = new SystemMonitorDaoJDOImpl();
		try {
			ip = systemDAO.getIPbyURL(url);
			if(ip==""){
				callback = "System is not alive";
			}else{
				system.setIp(ip);
				system.setCode(systemDAO.createCode());
				systemDAO.addnewSystem(system);
				callback="done"+ ip;
			}	
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return callback;
	}

	/**
	 * 
	 */


	
}
