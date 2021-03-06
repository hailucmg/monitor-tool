/*
 * Copyright (c) CMG Ltd All rights reserved.
 *
 * This software is the confidential and proprietary information of CMG
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with CMG.
 */
package cmg.org.monitor.dao;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cmg.org.monitor.dao.impl.SystemDaoImpl;
import cmg.org.monitor.entity.shared.ChangeLogMonitor;
import cmg.org.monitor.entity.shared.NotifyMonitor;
import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.ext.model.shared.MonitorContainer;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

/**
 * The Class SystemDAOTest.
 */
public class SystemDAOTest {
	
	/** The helper. */
	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(
			new LocalDatastoreServiceTestConfig());
	
	/**
	 * Sets the up.
	 *
	 * @throws Exception the exception
	 */
	@Before
	public void setUp() throws Exception {
		helper.setUp();
	}

	/**
	 * Tear down.
	 *
	 * @throws Exception the exception
	 */
	@After
	public void tearDown() throws Exception {
		helper.tearDown();
	}

	/**
	 * Test get notify option.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testGetNotifyOption() throws Exception {
		SystemDAO sysDAO = new SystemDaoImpl();
		try {
			for(int i =0 ; i < 1 ; i++){
				SystemMonitor system = new SystemMonitor();
				system.setName("n"+i);
				NotifyMonitor nm = new NotifyMonitor();
				nm.setJVM(true);
				nm.setNotifyCpu(true);
				system.setNotify(nm);
				if(i == 0){
					i = 1;
				}
				String remoteURL = "aa" +i;
				system.setRemoteUrl(remoteURL);
				String code = "S00"+i;
				system.setActive(true);
				system.setDeleted(false);
				sysDAO.addSystem(system,code);
			}
			NotifyMonitor nm = sysDAO.getNotifyOption("S001");
			if(nm.isJVM()){
				System.out.println("true");
			}
			List<SystemMonitor> sysList = sysDAO.listSystems(false);
			SystemMonitor sys1 = sysList.get(0);
			sys1.setIp("1.1.1.1");
			NotifyMonitor nm1 = new NotifyMonitor();
			sys1.setNotify(nm1);
			boolean update = sysDAO.updateSystem(sys1, true);
//			boolean setcount = sysDAO.setCountChangeLog("S001");
			int countSid = sysDAO.getCounterChangeLog("S001");
			System.out.println(countSid);
			//int countChangelog = sysDAO.getCountAllChangeLog();

			//ArrayList<ChangeLogMonitor> clm = sysDAO.listChangeLog();
			//System.out.println(clm.get(0).getDescription());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	
}
