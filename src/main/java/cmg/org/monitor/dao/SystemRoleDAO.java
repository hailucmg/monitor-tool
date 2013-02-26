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
	@Deprecated
	public boolean createRole(SystemRole role) throws Exception;
	@Deprecated
	public boolean updateRole(SystemRole role) throws Exception;
	@Deprecated
	public boolean deleteRole(SystemRole role) throws Exception;
	@Deprecated
	public SystemRole getById(String id) throws Exception;
	@Deprecated
	public List<SystemRole> listAll() throws Exception;
	@Deprecated
	public SystemRole getByName(String name) throws Exception;
	@Deprecated
	public boolean addRole(String userEmail, String roleName) throws Exception;
	@Deprecated
	public boolean removeRole(String userEmail, String roleName) throws Exception;
	@Deprecated
	public void init();
}
