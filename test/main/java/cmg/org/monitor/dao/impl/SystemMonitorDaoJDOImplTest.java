package cmg.org.monitor.dao.impl;

import static org.junit.Assert.*;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import cmg.org.monitor.dao.impl.SystemMonitorDaoJDOImpl;
import cmg.org.monitor.entity.SystemMonitor;



import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;  
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;  

public class SystemMonitorDaoJDOImplTest {
	private final LocalServiceTestHelper helper =  
	        new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
	SystemMonitorDaoJDOImpl systemDao = new SystemMonitorDaoJDOImpl();
	void init() throws Exception{
		SystemMonitor system = new SystemMonitor("google", "google.com", "10.1.1", true, true, false);
		SystemMonitor system1 = new SystemMonitor("dantri", "dantri.com", "2.2.2", false, false, false);
		systemDao.addnewSystem(system1);
		systemDao.addnewSystem(system);	
	}

	@Before
	public void setUp() throws Exception {
		helper.setUp();
		init();
	}

	@After
	public void tearDown() throws Exception {
		helper.tearDown();
	}

	@Test
	public void testListSystem() {
		SystemMonitor[] list = systemDao.listSystem(false);
		assertNotNull(list);
		assertEquals(2, list.length);

	}

	@Test
	public void testGetSystembyID() {
		SystemMonitor system = new SystemMonitor("acb", "acb.com", "10.1.1", true, true, false);
		systemDao.addnewSystem(system);
		assertEquals(system.getName(), systemDao.getSystembyID(system.getId()).getName());
		assertEquals(system.getIp(), systemDao.getSystembyID(system.getId()).getIp());
		assertEquals(system.getAddress(), systemDao.getSystembyID(system.getId()).getAddress());
		assertEquals(system.getProtocol(), systemDao.getSystembyID(system.getId()).getProtocol());

	}

	@Test
	public void testAddnewSystem() {
		SystemMonitor system = new SystemMonitor("xyz", "xyz.com", "10.1.1", true, true, false);
		systemDao.addnewSystem(system);
		SystemMonitor systemTest = systemDao.getSystembyID(system.getId());
		assertEquals(systemTest.getName(), system.getName());
		assertEquals(systemTest.getAddress(), system.getAddress());
		assertEquals(systemTest.getIp(), system.getIp());
		assertEquals(systemTest.getIsActive(), system.getIsActive());
		assertEquals(systemTest.getSystemStatus(), system.getSystemStatus());
		assertEquals(systemTest.getIsDeleted(), system.getIsDeleted());

	}

	@Test
	public void testEditSystembyID() throws Exception {
		SystemMonitor system = new SystemMonitor("abc", "abc.com", "10.1.1", true, true, false);
		systemDao.addnewSystem(system);
		systemDao.editSystembyID(system.getId(), "new", "a.com", "10.10.1", false);
		SystemMonitor editSystem = systemDao.getSystembyID(system.getId());
		assertEquals("new", editSystem.getName());
		assertEquals("a.com", editSystem.getAddress());
		assertEquals("10.10.1", editSystem.getIp());
		assertEquals(false,editSystem.getIsActive());

	}

	@Test
	public void testDeleteSystembyID() {
		SystemMonitor system = new SystemMonitor("klm", "klm.com", "10.1.1", true, true, false);
		systemDao.addnewSystem(system);
		systemDao.deleteSystembyID(system.getId());
		SystemMonitor systemTest = systemDao.getSystembyID(system.getId());
		assertEquals(true, systemTest.getIsDeleted());
		assertEquals(false, systemTest.getIsActive());
	}

	@Test
	public void testDeleteListSystembyID() {
		try {
			String[] ids = new String[2];
			SystemMonitor systemTest;
			SystemMonitor system = new SystemMonitor("afk", "afk.com", "10.1.1", true, true, false);
			SystemMonitor system1 = new SystemMonitor("kln", "kln.com", "10.1.1", true, true, false);
			
			systemDao.addnewSystem(system1);
			systemDao.addnewSystem(system);
			ids[0] = system.getId();
			ids[1] = system1.getId();

			systemDao.deleteListSystembyID(ids);
		
			for(int i = 0;i<ids.length;i++){
				systemTest = systemDao.getSystembyID(ids[i]);
				assertEquals(true, systemTest.getIsDeleted());
				assertEquals(false, systemTest.getIsActive());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	}


