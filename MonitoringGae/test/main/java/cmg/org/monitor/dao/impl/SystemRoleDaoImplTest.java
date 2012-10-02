/**
 * Copyright (c) CMG Ltd All rights reserved.
 *
 * This software is the confidential and proprietary information of CMG
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with CMG.
 */

package cmg.org.monitor.dao.impl;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cmg.org.monitor.dao.SystemAccountDAO;
import cmg.org.monitor.dao.SystemRoleDAO;
import cmg.org.monitor.entity.shared.SystemRole;
import cmg.org.monitor.entity.shared.SystemUser;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.gson.Gson;

/** 
 * DOCME
 * 
 * @Creator Hai Lu
 * @author $Author$
 * @version $Revision$
 * @Last changed: $LastChangedDate$
 */

public class SystemRoleDaoImplTest {
	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(
			new LocalDatastoreServiceTestConfig());
	SystemRoleDAO roleDao;
	SystemAccountDAO accountDao;
	Gson gson;

	@Before
	public void setUp() throws Exception {
		helper.setUp();
		roleDao = new SystemRoleDaoImpl();
		accountDao = new SystemAccountDaoImpl();
		gson = new Gson();		
		
		
		SystemUser user = new SystemUser();
		user.setDomain("c-mg.vn");
		user.setUsername("monitor");
		user.setFirstName("Hai");
		user.setEmail("monitor@c-mg.vn");

		accountDao.createSystemUser(user);

		user = new SystemUser();
		user.setDomain("c-mg.com");
		user.setUsername("hailu");
		user.setFirstName("Hai");
		user.setEmail("hailu@c-mg.com");
		accountDao.createSystemUser(user);
		
	}

	@After
	public void tearDown() throws Exception {
		helper.tearDown();
	}
	
	@Test
	public void test() {
		/*
		List<SystemRole> roles = null;
		try {
			roles = roleDao.listAll();
		
		} catch (Exception e) {
		
			e.printStackTrace();
		}
		assertNotNull(roles);
		assertEquals(2, roles.size());
		SystemRole adminRole = null;
		SystemRole userRole = null;
		try {
			userRole = roleDao.getByName(SystemRole.ROLE_USER);
			adminRole = roleDao.getByName(SystemRole.ROLE_ADMINISTRATOR);
		} catch (Exception e) {			
			e.printStackTrace();
		}		
		assertNotNull(adminRole);
		assertNotNull(userRole);
		System.out.println(adminRole.getId());
		System.out.println(userRole.getId());
		*/
		SystemUser adminUser = null;
		try {
			adminUser = accountDao.getSystemUserByEmail("hailu@c-mg.com");
			adminUser.addUserRole(SystemRole.ROLE_ADMINISTRATOR);
			adminUser.addUserRole(SystemRole.ROLE_USER);
			accountDao.updateSystemUser(adminUser);
			adminUser = accountDao.getSystemUserByEmail("hailu@c-mg.com");
		} catch (Exception e) {			
			e.printStackTrace();
		}
		/*
		List<String> roleUsers = adminUser.getRoleIDs();
		try {
			accountDao.initRole(adminUser);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		List<SystemRole> listRoles = adminUser.getRoles();
		for (SystemRole role: listRoles) {
			System.out.println(role.getName());
		}
		assertEquals(2, listRoles.size());
		
		
		try {
			adminUser = accountDao.getSystemUserByEmail("hailu@c-mg.com");
			adminUser.removeUserRole(SystemRole.ROLE_USER);
			adminUser.removeUserRole(SystemRole.ROLE_ADMINISTRATOR);
			
			accountDao.updateSystemUser(adminUser);
			adminUser = accountDao.getSystemUserByEmail("hailu@c-mg.com");
		} catch (Exception e) {			
			e.printStackTrace();
		}
		listRoles = adminUser.getRoles();
		for (SystemRole role: listRoles) {
			System.out.println(role.getName());
		}
		assertEquals(0, listRoles.size());
	}

}
