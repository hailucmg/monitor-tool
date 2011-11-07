package cmg.org.monitor.util.shared;

import static org.junit.Assert.*;

import javax.jdo.PersistenceManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cmg.org.monitor.dao.impl.SystemMonitorDaoJDOImpl;
import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.services.SystemService;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

public class UltilityTest {
	
	private final LocalServiceTestHelper helper = 
			new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
	
	@Before
	public void setUp() throws Exception {
		helper.setUp();
	}

	@After
	public void tearDown() throws Exception {
		helper.tearDown();
	}

	@Test
	public void testListGroup() {
		
	}

	@Test
	public void testListAdmin() {
		
	}

	@Test
	public void testListUser() {
		
	}

	@Test
	public void testGetIpbyUrl() {
		String url = "google.com";
		boolean check= false;
		String ip= "";
		try {
			 ip = Ultility.getIpbyUrl(url);
			if(ip!=""){
				check= true;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		assertTrue(check);
		System.out.println(ip);
	}

	@Test
	public void testCreateSID() {
		boolean check = false;
		try {
			
			SystemMonitor system = new SystemMonitor("google", "google.ocm", "1.1.1", true);
			SystemMonitorDaoJDOImpl systemDAO = new SystemMonitorDaoJDOImpl();
			system.setCode(systemDAO.createCode());
			systemDAO.addnewSystem(system);
			System.out.println(system.getCode());
			if(system.getCode().equals("S001")){
				check = true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertTrue(check);
	}

}
