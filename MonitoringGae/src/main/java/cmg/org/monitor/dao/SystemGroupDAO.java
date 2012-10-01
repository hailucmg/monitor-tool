package cmg.org.monitor.dao;

import cmg.org.monitor.entity.shared.SystemGroup;
import cmg.org.monitor.entity.shared.SystemUser;

/** 
	* DOCME
	* 
	* @Creator Hai Lu
	* @author $Author$
	* @version $Revision$
	* @Last changed: $LastChangedDate$
*/
public interface SystemGroupDAO {
	
	public boolean addNewGroup(SystemGroup ss) throws Exception;
	public SystemGroup getByID(String id) throws Exception;
	public boolean updateGroup(String id, String groupName, String groupDescription) throws Exception;
	public boolean deleteGroup(String id) throws Exception;
	public SystemGroup[] getAllGroup() throws Exception;
	
	public boolean addUserToGroup(SystemUser user, SystemGroup group) throws Exception;
	
	public boolean removeUserFromGroup(SystemUser user, SystemGroup group) throws Exception;	
}
