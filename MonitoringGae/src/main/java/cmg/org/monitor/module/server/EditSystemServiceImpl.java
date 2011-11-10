package cmg.org.monitor.module.server;





import cmg.org.monitor.dao.impl.SystemMonitorDaoJDOImpl;
import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.ext.model.shared.MonitorEditDto;
import cmg.org.monitor.ext.model.shared.UserLoginDto;
import cmg.org.monitor.module.client.EditSystemService;
import cmg.org.monitor.services.MonitorLoginService;


import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class EditSystemServiceImpl extends RemoteServiceServlet implements EditSystemService{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public UserLoginDto getUserLogin() {
		return MonitorLoginService.getUserLogin();
	}
	@Override
	public MonitorEditDto getSystembyID(String id) throws Exception{
		// TODO Auto-generated method stub
		MonitorEditDto monitorEdit = new MonitorEditDto();
		String[] groups;
		try {
			SystemMonitorDaoJDOImpl systemDAO = new SystemMonitorDaoJDOImpl();
			SystemMonitor system = systemDAO.getSystembyID(id);
			monitorEdit.setId(system.getId());
			monitorEdit.setActive(system.isActive());
			monitorEdit.setGroup(system.getGroupEmail());
			monitorEdit.setIp(system.getIp());
			monitorEdit.setProtocol(system.getProtocol());
			monitorEdit.setUrl(system.getUrl());
			monitorEdit.setName(system.getName());
			monitorEdit.setRemoteURl(system.getRemoteUrl());
			groups = systemDAO.groups();
			monitorEdit.setGroups(groups);
			for(int i = 0 ; i < groups.length;i++){
				if(groups[i]==system.getGroupEmail()){
					monitorEdit.setSelect(i);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw e;
		}
		return monitorEdit;
	}

	@Override
	public boolean editSystembyID(String id, String newName, String newAddress,String protocol,String group,String ip
			,String remoteURL,boolean isActive) throws Exception {
		// TODO Auto-generated method stub
		boolean b = false;
		try {
			SystemMonitorDaoJDOImpl systemDAO = new SystemMonitorDaoJDOImpl();
			 b = systemDAO.editSystembyID(id, newName, newAddress, protocol, group, ip,remoteURL,isActive);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return b;
	}

}
