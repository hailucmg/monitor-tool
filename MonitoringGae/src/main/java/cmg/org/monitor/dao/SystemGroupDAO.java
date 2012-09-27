package cmg.org.monitor.dao;

import cmg.org.monitor.entity.shared.SystemGroup;

public interface SystemGroupDAO {
	
	public boolean addNewGroup(SystemGroup ss) throws Exception;
	SystemGroup getByID(String id) throws Exception;
	public boolean updateGroup(String id, String groupName, String groupDescription) throws Exception;
	public boolean deleteGroup(String id) throws Exception;
	public SystemGroup[] getAllGroup() throws Exception;
	
}
