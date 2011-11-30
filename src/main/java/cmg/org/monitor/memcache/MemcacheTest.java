package cmg.org.monitor.memcache;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cmg.org.monitor.dao.SystemMonitorDAO;
import cmg.org.monitor.dao.impl.SystemMonitorDaoJDOImpl;
import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.memcache.MonitorMemcache;
import cmg.org.monitor.memcache.SystemMonitorStore;
import cmg.org.monitor.memcache.shared.SystemMonitorDto;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

public class MemcacheTest {

	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(
			new LocalDatastoreServiceTestConfig());

	@Before
	public void setUp() throws Exception {
		helper.setUp();
	}

	@After
	public void tearDown() throws Exception {
		helper.tearDown();
	}
	//@Test
	public void test1() {
		System.out.print(MonitorMemcache.getCount());
		MonitorMemcache.put(Key.create(Key.SYSTEM_MONITOR_STORE, MonitorMemcache.getCount()), "test");
		System.out.print(MonitorMemcache.getCount());
		Object obj = MonitorMemcache.get(Key.create(Key.SYSTEM_MONITOR_STORE, MonitorMemcache.getCount()));
		System.out.print(obj.toString());
	}
	@Test
	public void test() {
		MonitorMemcache.changeFlag(false);
		 System.out.print(MonitorMemcache.isFlagChange());
		SystemMonitorDAO sysDao = new SystemMonitorDaoJDOImpl();
		SystemMonitor[] sysList = null;
		try {
			sysDao.addnewSystem(new SystemMonitor("yoyo", "yoyo.com",
					"1.2.2.4", true));
			sysDao.addnewSystem(new SystemMonitor("yoyo1", "yoyo1.com",
					"1.2.2.4", true));
			sysDao.addnewSystem(new SystemMonitor("yoyo2", "yoyo2.com",
					"1.2.2.4", true));
			sysList = sysDao.listSystems(false);
			System.out.println("#1 Length: " + sysList.length);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		String sid = "";
		ArrayList<SystemMonitorStore> list = MonitorMemcache
				.getSystemMonitorStore();
		ArrayList<SystemMonitorStore> listTemp = new ArrayList<SystemMonitorStore>();
		System.out.println("Count: " +  MonitorMemcache.getCount());
		if (list != null) {
			for (SystemMonitorStore sms : list) {
				SystemMonitorDto sys = sms.getSysMonitor();
				if (sys != null) {
					System.out.println(sys.getId() + " # " + sys.getName());	
					sid = sys.getId();
					sys.setName("test1");
					sms.setSysMonitor(sys);
				} else {
					System.out.println("null");
				}
				listTemp.add(sms);
			}
		}
		MonitorMemcache.putSystemMonitorStore(list);
		MonitorMemcache.putSystemMonitorStore(listTemp);
		System.out.println("Count: " +  MonitorMemcache.getCount());
		try {
			sysDao.deleteSystembyID(sid);
			sysList = sysDao.listSystems(false);
			System.out.println("#2 Length: " + sysList.length);
		} catch (Exception e) {
			//
		}
		list = MonitorMemcache
				.getSystemMonitorStore();
		
		if (list != null) {
			for (SystemMonitorStore sms : list) {
				SystemMonitorDto sys = sms.getSysMonitor();
				if (sys != null) {
					System.out.println(sys.getId() + " # " + sys.getName());	
					sid = sys.getId();
				} else {
					System.out.println("null");
				}
				
			}
		}
		
		
		
		
	}

}
