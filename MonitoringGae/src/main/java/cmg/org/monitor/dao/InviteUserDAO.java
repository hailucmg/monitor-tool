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

import cmg.org.monitor.entity.shared.InvitedUser;
import cmg.org.monitor.module.client.InviteUser;

/** 
 * DOCME
 * 
 * @Creator Hai Lu
 * @author $Author$
 * @version $Revision$
 * @Last changed: $LastChangedDate$
 */

public interface InviteUserDAO {
	public List<InvitedUser> list3rdUser() throws Exception;
	
	public boolean create3rdUser(InvitedUser user) throws Exception;
	
	public boolean update3rdUser(InvitedUser user) throws Exception;
	
	public boolean delete3rdUser(String id) throws Exception;
	
	public boolean delete3rdUser(InvitedUser user) throws Exception;
	
	public boolean active3rdUser(InvitedUser user) throws Exception;
	
	public boolean active3rdUser(String email) throws Exception;
	
	public void initList3rdUser();
	
	public List<InvitedUser> list3rdUserFromMemcache();
	
	public void storeList3rdUserToMemcache(List<InvitedUser> list);
	
	public boolean updateFullname(InvitedUser user) throws Exception;
}
