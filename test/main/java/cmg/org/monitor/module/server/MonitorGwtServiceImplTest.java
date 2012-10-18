
/*
 * Copyright (c) CMG Ltd All rights reserved.

 *
 * This software is the confidential and proprietary information of CMG
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with CMG.
 */
package cmg.org.monitor.module.server;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cmg.org.monitor.entity.shared.GoogleAccount;
import cmg.org.monitor.entity.shared.LinkDefaultMonitor;
import cmg.org.monitor.services.LinkService;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

/**
 * DOCME *
 * 
 * @author ISTC *
 * @version .Revision: # .Date:Sep 28, 2012*
 */
public class MonitorGwtServiceImplTest {
    private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

    @Before
    public void setUp() throws Exception {
	helper.setUp();

    }

    @After
    public void tearDown() throws Exception {
	helper.tearDown();
    }

    /**
     * Test method for
     * {@link cmg.org.monitor.module.server.MonitorGwtServiceImpl#syncAccount(cmg.org.monitor.entity.shared.GoogleAccount)}
     * .
     */
    @Test
    public void testSyncAccount() {

	MonitorGwtServiceImpl asd = new MonitorGwtServiceImpl();
	asd.inviteUser3rd(new String[] {"luhonghai@gmail.com"}, "null");
    }

}
