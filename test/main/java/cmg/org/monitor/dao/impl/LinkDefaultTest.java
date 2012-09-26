
/*
 * Copyright (c) CMG Ltd All rights reserved.

 *
 * This software is the confidential and proprietary information of CMG
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with CMG.
 */
package cmg.org.monitor.dao.impl;

import static org.junit.Assert.*;
import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cmg.org.monitor.dao.UtilityDAO;
import cmg.org.monitor.entity.shared.LinkDefaultMonitor;
import cmg.org.monitor.services.LinkService;
import cmg.org.monitor.util.shared.Constant;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

/**
 * DOCME *
 * 
 * @author ISTC *
 * @version .Revision: # .Date:Sep 25, 2012*
 */
public class LinkDefaultTest {
    private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
    LinkService linkService = new LinkService();
    LinkDefaultMonitor link;
    @Before
    public void setUp() throws Exception {
	helper.setUp();
	//link = new LinkDefaultMonitor();
	//link.setLinkContent("http://c-mg.vn:8180/URLRedirector/handler");
	//linkService.updateLink(link);
    }

    @After
    public void tearDown() throws Exception {
	helper.tearDown();
    }

    //@Test
    public void test() {
	try{
	    LinkDefaultMonitor link1 = linkService.getLink();
	    assertNotNull(link1);
	    assertNotNull(link);
	    assertEquals(link1.getLinkContent(), link.getLinkContent());
	    System.out.println(link1.getLinkContent());
	    System.out.println(link.getLinkContent());
	}catch (Exception ex){
	    ex.printStackTrace();
	}
    }
    
    //@Test
    public void test1() {
	try{
	    LinkDefaultMonitor link1 = linkService.getLink();
	    link1.setLinkContent("http://c-mg.vn:8888/URLRedirector");
	    
	    linkService.updateLink(link1);
	    LinkDefaultMonitor link2 = linkService.getLink();
	    assertEquals(link1.getLinkContent(), link2.getLinkContent());
	    System.out.println(link2.getLinkContent());
	}catch (Exception ex){
	    ex.printStackTrace();
	}
    }
    
    @Test
    public void test2() {
	try{
	   UtilityDAO ultility = new UtilityDaoImpl();
	   String str = ultility.getLinkDefault();
	   assertEquals(Constant.REDIRECTOR_WORKER_URL, str);
	   ultility.putLinkDefault("http://c-mg.com.vn");
	   String link = ultility.getLinkDefault();
	   assertEquals("http://c-mg.com.vn", link);
	   
	}catch (Exception ex){
	    ex.printStackTrace();
	}
    }

}
