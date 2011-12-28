package cmg.org.monitor.app.schedule;

import java.util.ArrayList;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cmg.org.monitor.dao.AlertDao;
import cmg.org.monitor.dao.CpuDAO;
import cmg.org.monitor.dao.FileSystemDAO;
import cmg.org.monitor.dao.JvmDAO;
import cmg.org.monitor.dao.MemoryDAO;
import cmg.org.monitor.dao.ServiceDAO;
import cmg.org.monitor.dao.SystemDAO;
import cmg.org.monitor.dao.impl.AlertDaoImpl;
import cmg.org.monitor.dao.impl.CpuDaoImpl;
import cmg.org.monitor.dao.impl.FileSystemDaoImpl;
import cmg.org.monitor.dao.impl.JvmDaoImpl;
import cmg.org.monitor.dao.impl.MemoryDaoImpl;
import cmg.org.monitor.dao.impl.ServiceDaoImpl;
import cmg.org.monitor.dao.impl.SystemDaoImpl;
import cmg.org.monitor.entity.shared.AlertMonitor;
import cmg.org.monitor.entity.shared.AlertStoreMonitor;
import cmg.org.monitor.entity.shared.CpuMonitor;
import cmg.org.monitor.entity.shared.FileSystemMonitor;
import cmg.org.monitor.entity.shared.JvmMonitor;
import cmg.org.monitor.entity.shared.MemoryMonitor;
import cmg.org.monitor.entity.shared.ServiceMonitor;
import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.ext.util.MonitorUtil;
import cmg.org.monitor.util.shared.MonitorConstant;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

public class GlobalSchedulerTest {
	public static final int TEST_LENGTH = 50;

	/** Log object. */
	private static final Logger logger = Logger
			.getLogger(MailServiceScheduler.class.getCanonicalName());
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

	/*void init() {
		SystemDAO sysDAO = new SystemDaoImpl();
		try {
			sysDAO.addSystem(new SystemMonitor("S001", "Test01", "test01.org",
					"http://localhost:8080/xml_ukpensionsint.bp.com.html", true));
			sysDAO.addSystem(new SystemMonitor("S002", "Test02", "test02.org",
					"http://localhost:8080/html_ukpensionsint.bp.com.html",
					true));
			sysDAO.addSystem(new SystemMonitor("S003", "Test03", "test03.org",
					"", false));
			sysDAO.addSystem(new SystemMonitor("S004", "Test04", "test04.org",
					"http://localhost:8080/html_pensionline.bp.com.html", true));
		} catch (Exception e) {
			e.printStackTrace();
		}
		GlobalScheduler gs = new GlobalScheduler();
		for (int i = 0; i < TEST_LENGTH; i++) {
			gs.doSchedule();
		}
	}*/
	@Test
	public void test01() {
		SystemDAO sysDAO = new SystemDaoImpl();
		SystemMonitor sys = new SystemMonitor();
		sys.setCode("S001");
		sys.setActive(true);
		sys.setName("test1");
		sys.setGroupEmail("admin_monitor");
		sys.setProtocol(MonitorConstant.HTTP_PROTOCOL);
		sys.setRemoteUrl("http://localhost:8080/xml_ukpensionsint.bp.com.html");
		sys.setUrl("ukpension.com");
		sys.setDeleted(false);
		SystemMonitor sys1 = new SystemMonitor();
		sys1.setCode("S002");
		sys1.setActive(true);
		sys1.setName("test2");
		sys1.setGroupEmail("admin_monitor");
		sys1.setProtocol(MonitorConstant.HTTP_PROTOCOL);
		sys1.setRemoteUrl("http://localhost:8080/html_pensionline.bp.com.html");
		sys1.setUrl("pensom.com");
		sys1.setDeleted(false);
		try {
			sysDAO.addSystem(sys);
			sysDAO.addSystem(sys1);
			/*sysDAO.addSystem(new SystemMonitor("S001", "Test01", "test01.org",
					"http://localhost:8080/xml_ukpensionsint.bp.com.html", true));*/
			
			/*
			 * sysDAO.addSystem(new SystemMonitor("S002", "Test02",
			 * "test02.org",
			 * "http://localhost:8080/html_ukpensionsint.bp.com.html", true));
			 * sysDAO.addSystem(new SystemMonitor("S003", "Test03",
			 * "test03.org", "", false)); sysDAO.addSystem(new
			 * SystemMonitor("S004", "Test04", "test04.org",
			 * "http://localhost:8080/html_pensionline.bp.com.html", true));
			 */
		} catch (Exception e) {
			e.printStackTrace();
		}
		GlobalScheduler gs = new GlobalScheduler();
		gs.doSchedule();
		MailServiceScheduler mail = new MailServiceScheduler();
		mail.doSchedule();
	}

	/*@Test
	public void test() {
		SystemDAO sysDAO = new SystemDaoImpl();
		try {
			sysDAO.addSystem(new SystemMonitor("S001", "Test01", "test01.org",
					"http://localhost:8080/xml_ukpensionsint.bp.com.html", true));
			sysDAO.addSystem(new SystemMonitor("S002", "Test02", "test02.org",
					"http://localhost:8080/html_ukpensionsint.bp.com.html",
					true));
			sysDAO.addSystem(new SystemMonitor("S003", "Test03", "test03.org",
					"", false));
			sysDAO.addSystem(new SystemMonitor("S004", "Test04", "test04.org",
			// "http://localhost:8080/html_pensionline.bp.com.html"
					"http://localhost:8080/html_pensom.html", true));
		} catch (Exception e) {
			e.printStackTrace();
		}
		AlertDao alertDAO = new AlertDaoImpl();
		CpuDAO cpuDAO = new CpuDaoImpl();
		FileSystemDAO fileSysDAO = new FileSystemDaoImpl();
		JvmDAO jvmDAO = new JvmDaoImpl();
		MemoryDAO memDAO = new MemoryDaoImpl();
		ServiceDAO serviceDAO = new ServiceDaoImpl();
		SystemMonitor tempSys = null;
		GlobalScheduler gs = new GlobalScheduler();
		for (int i = 0; i < TEST_LENGTH; i++) {
			gs.doSchedule();			
		}
		
		
		try {
			ArrayList<SystemMonitor> sysList = sysDAO.listSystems(false);
			if (sysList != null) {
				if (sysList.size() > 0) {
					tempSys = sysList.get(0);
					for (SystemMonitor sys : sysList) {
						System.out
								.println("=========================================");
						System.out.println(sys.toString());
						ArrayList<CpuMonitor> cpuList = cpuDAO.listCpu(sys);
						System.out.println("CPU List Size: "
								+ ((cpuList == null) ? 0 : cpuList.size()));
						ArrayList<JvmMonitor> jvmList = jvmDAO.listJvm(sys);
						System.out.println("JVM List Size: "
								+ ((jvmList == null) ? 0 : jvmList.size()));
						ArrayList<MemoryMonitor> memList = memDAO.listMemory(
								sys, MemoryMonitor.MEM);
						System.out.println("Memory MEM List Size: "
								+ ((memList == null) ? 0 : memList.size()));
						ArrayList<MemoryMonitor> swapList = memDAO.listMemory(
								sys, MemoryMonitor.SWAP);
						System.out.println("Memory SWAP List Size: "
								+ ((swapList == null) ? 0 : swapList.size()));
						ArrayList<ServiceMonitor> serviceList = serviceDAO
								.listService(sys);
						System.out.println("Service List Size: "
								+ ((serviceList == null) ? 0 : serviceList
										.size()));
						ArrayList<FileSystemMonitor> fileSysList = fileSysDAO
								.listFileSystems(sys);
						System.out.println("Filesystem List Size: "
								+ ((fileSysList == null) ? 0 : fileSysList
										.size()));
					}
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		if (tempSys != null) {
			AlertStoreMonitor storeTemp = alertDAO.getLastestAlertStore(tempSys);
			alertDAO.putAlertStore(storeTemp);
			alertDAO.clearTempStore(tempSys);
			ArrayList<AlertStoreMonitor> storeList = alertDAO
					.listAlertStore(tempSys.getId());
			if (storeList != null) {
				System.out.println("=========== ALERT ZONE ==========");
				System.out.println(tempSys + " -> Alert Store Size: "
						+ storeList.size());
				for (AlertStoreMonitor store : storeList) {
					ArrayList<AlertMonitor> alertList = store.getAlerts();
					if (alertList != null) {
						System.out.println("List alert of store. Size: " + alertList.size());
						for (AlertMonitor alert : alertList) {
							System.out.println(MonitorUtil.parseTime(alert.getTimeStamp())
									+ " -> " + alert.getError() + " : " + alert.getDescription());
						}
					}
				}
			}
		}
	}*/

}
