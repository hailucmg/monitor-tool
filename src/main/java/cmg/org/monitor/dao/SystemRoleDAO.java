/**
 * Copyright (c) CMG Ltd All rights reserved.
 *
 * This software is the confidential and proprietary information of CMG
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with CMG.
 */

package cmg.org.monitor.dao;

import java.util.List;

import cmg.org.monitor.entity.shared.SystemRole;
import cmg.org.monitor.entity.shared.SystemUser;

/** 
 * DOCME
 * 
 * @Creator Hai Lu
 * @author $Author$
 * @version $Revision$
 * @Last changed: $LastChangedDate$
 */

public interface SystemRoleDAO {
	public boolean createRole(SystemRole role) throws Exception;
	
	public boolean updateRole(SystemRole role) throws Exception;
	
	public boolean deleteRole(SystemRole role) throws Exception;
	
	public SystemRole getById(String id) throws Exception;
	
	public List<SystemRole> listAll() throws Exception;
	
	public SystemRole getByName(String name) throws Exception;
	
	public boolean addRole(SystemUser user, String roleName) throws Exception;
	
	public boolean removeRole(SystemUser user, String roleName) throws Exception;
	
	public void init();
}
