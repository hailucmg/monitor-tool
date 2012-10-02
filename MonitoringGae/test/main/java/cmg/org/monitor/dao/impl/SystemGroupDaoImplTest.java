/**
 * Copyright (c) CMG Ltd All rights reserved.
 *
 * This software is the confidential and proprietary information of CMG
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with CMG.
 */

package cmg.org.monitor.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cmg.org.monitor.dao.SystemAccountDAO;
import cmg.org.monitor.dao.SystemGroupDAO;
import cmg.org.monitor.entity.shared.SystemGroup;
import cmg.org.monitor.entity.shared.SystemUser;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * DOCME
 * 
 * @Creator Hai Lu
 * @author $Author$
 * @version $Revision$
 * @Last changed: $LastChangedDate$
 */

public class SystemGroupDaoImplTest {
	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(
			new LocalDatastoreServiceTestConfig());
	SystemGroupDAO groupDao;
	Gson gson;
	public static String idtest;

	@Before
	public void setUp() throws Exception {
		helper.setUp();
		groupDao = new SystemGroupDaoImpl();
		gson = new Gson();
		String jsonInit = "[{\"id\":\"agR0ZXN0chELEgtTeXN0ZW1Hcm91cBgBDA\",\"name\":\"Group1\",\"description\":\"Group1 Description\"},{\"id\":\"agR0ZXN0chELEgtTeXN0ZW1Hcm91cBgCDA\",\"name\":\"Group2\",\"description\":\"Group2 Description\"},{\"id\":\"agR0ZXN0chELEgtTeXN0ZW1Hcm91cBgDDA\",\"name\":\"Group3\",\"description\":\"Group3 Description\"},{\"id\":\"agR0ZXN0chELEgtTeXN0ZW1Hcm91cBgEDA\",\"name\":\"Group4\",\"description\":\"Group4 Description\"},{\"id\":\"agR0ZXN0chELEgtTeXN0ZW1Hcm91cBgFDA\",\"name\":\"Group5\",\"description\":\"Group5 Description\"}]";
		Type type = new TypeToken<Collection<SystemGroup>>() {
		}.getType();
		List<SystemGroup> list = gson.fromJson(jsonInit, type);
		try {
			if (!list.isEmpty()) {
				for (SystemGroup group : list) {
					SystemGroup temp = new SystemGroup();
					temp.setName(group.getName());
					temp.setDescription(group.getDescription());
					groupDao.addNewGroup(temp);
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		idtest = "agR0ZXN0chELEgtTeXN0ZW1Hcm91cBgBDA";

	}

	@After
	public void tearDown() throws Exception {
		helper.tearDown();
	}

	/**
	 * Test method for
	 * {@link cmg.org.monitor.dao.impl.SystemGroupDaoImpl#getByID(java.lang.String)}
	 * .
	 */
	@Test
	public void testGetByID() {
		SystemGroup group = null;
		try {
			group = groupDao.getByID(idtest);
			assertNotNull(group);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Test method for
	 * {@link cmg.org.monitor.dao.impl.SystemGroupDaoImpl#updateGroup(java.lang.String, java.lang.String, java.lang.String)}
	 * .
	 */
	@Test
	public void testUpdateGroup() {
		SystemGroup group = null;
		try {
			group = groupDao.getByID(idtest);
			group.setName("Funny");
			groupDao.updateGroup(group.getId(), group.getName(),
					group.getDescription());
			group = null;
			group = groupDao.getByID(idtest);
			assertEquals("Funny", group.getName());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Test method for
	 * {@link cmg.org.monitor.dao.impl.SystemGroupDaoImpl#deleteGroup(java.lang.String)}
	 * .
	 */
	@Test
	public void testDeleteGroup() {
		boolean b = false;
		SystemGroup group = null;
		try {
			b = groupDao.deleteGroup(idtest);
			try {
				group = groupDao.getByID(idtest);
			} catch (Exception ex) {
				//
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(true, b);
		assertNull(group);

	}

	/**
	 * Test method for
	 * {@link cmg.org.monitor.dao.impl.SystemGroupDaoImpl#getAllGroup()}.
	 */
	@Test
	public void testGetAllGroup() {
		try {
			SystemGroup[] list = groupDao.getAllGroup();
			assertNotNull(list);
			assertEquals(5, list.length);
			System.out.println(gson.toJson(list));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testAddGroupUser() {
		try {
			SystemAccountDAO accountDao = new SystemAccountDaoImpl();
			SystemGroup[] list = groupDao.getAllGroup();
			SystemGroup group = list[0];
			System.out.println(group.getName());
			SystemUser user = new SystemUser();
			user.setDomain("c-mg.vn");
			user.setUsername("monitor");
			user.setFirstName("Hai");
			user.setEmail("monitor@c-mg.vn");

			accountDao.createSystemUser(user, false);

			user = new SystemUser();
			user.setDomain("c-mg.com");
			user.setUsername("hailu");
			user.setFirstName("Hai");
			user.setEmail("hailu@c-mg.com");
			accountDao.createSystemUser(user, false);
			
			user = accountDao.getSystemUserByEmail("monitor@c-mg.vn");
			assertEquals("monitor@c-mg.vn", user.getEmail());
			groupDao.addUserToGroup(user.getEmail(), list[0].getId());
			groupDao.addUserToGroup(user.getEmail(), list[1].getId());
			groupDao.addUserToGroup(user.getEmail(), list[3].getId());
			groupDao.addUserToGroup("hailu@c-mg.com", list[3].getId());
			user = accountDao.getSystemUserByEmail("monitor@c-mg.vn");
			List<String> groups = user.getGroupIDs();
			assertEquals(3, groups.size());
			if (!groups.isEmpty()) {
				for (String gp : groups) {
					System.out.println("#ID: " + gp);
					SystemGroup groupTemp = groupDao.getByID(gp);
					System.out.println("-> Name:" + groupTemp.getName());
					System.out
							.println("#UID: " + groupTemp.getUserIDs().get(0));
					assertEquals(user.getId(), groupTemp.getUserIDs().get(0));
				}
			}
			SystemGroup groupTemp = groupDao.getByID(list[3].getId());
			List<String> users = groupTemp.getUserIDs();
			assertEquals(2, users.size());
			for (String ur: users) {
				System.out.println(accountDao.getSystemUserById(ur).getEmail());
			}
			
			groupDao.removeUserFromGroup(user.getEmail(), list[3].getId());
			
			
			groupTemp = groupDao.getByID(list[3].getId());
			users = groupTemp.getUserIDs();
			assertEquals(1, users.size());
			for (String ur: users) {
				System.out.println(accountDao.getSystemUserById(ur).getEmail());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
