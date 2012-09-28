/**
 * Copyright (c) CMG Ltd All rights reserved.
 *
 * This software is the confidential and proprietary information of CMG
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with CMG.
 */

package cmg.org.monitor.services;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cmg.org.monitor.dao.SystemAccountDAO;
import cmg.org.monitor.dao.impl.SystemAccountDaoImpl;
import cmg.org.monitor.entity.shared.GoogleAccount;
import cmg.org.monitor.entity.shared.SystemUser;
import cmg.org.monitor.module.server.MonitorGwtServiceImpl;
import cmg.org.monitor.util.shared.MonitorConstant;
import cmg.org.monitor.util.shared.SecurityUtil;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.gdata.util.AuthenticationException;
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

public class GoogleAccountServiceTest {
	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(
			new LocalDatastoreServiceTestConfig());
	Gson gson;
	SystemAccountDAO accountDao;
	GoogleAccountService service;
	GoogleAccount adminAcc;

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
	 * {@link cmg.org.monitor.services.GoogleAccountService#sync()}.
	 */
	//@Test
	public void testSync() {
		gson = new Gson();
		accountDao = new SystemAccountDaoImpl();
		adminAcc = new GoogleAccount();
		adminAcc.setDomain(MonitorConstant.DOMAIN);
		adminAcc.setUsername(MonitorConstant.ADMIN_EMAIL_ID);
		adminAcc.setPassword(SecurityUtil
				.encrypt(MonitorConstant.ADMIN_PASSWORD));
		try {
			service = new GoogleAccountService(adminAcc);
		} catch (AuthenticationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String jsonInit = "[{\"id\":\"agR0ZXN0chALEgpTeXN0ZW1Vc2VyGAIM\",\"username\":\"hai.testuser2\",\"email\":\"hai.testuser2@c-mg.vn\",\"domain\":\"c-mg.vn\",\"firstName\":\"Hai Test\",\"lastName\":\"User 2\",\"isSuspended\":false,\"isDomainAdmin\":false},{\"id\":\"agR0ZXN0chALEgpTeXN0ZW1Vc2VyGAMM\",\"username\":\"hai.testuser3\",\"email\":\"hai.testuser3@c-mg.vn\",\"domain\":\"c-mg.vn\",\"firstName\":\"Hai Test\",\"lastName\":\"User3\",\"isSuspended\":false,\"isDomainAdmin\":false},{\"id\":\"agR0ZXN0chALEgpTeXN0ZW1Vc2VyGAQM\",\"username\":\"hai.testuser\",\"email\":\"hai.testuser@c-mg.vn\",\"domain\":\"c-mg.vn\",\"firstName\":\"Hai Test\",\"lastName\":\"User\",\"isSuspended\":false,\"isDomainAdmin\":false},{\"id\":\"agR0ZXN0chALEgpTeXN0ZW1Vc2VyGAUM\",\"username\":\"cmgvietnam\",\"email\":\"cmgvietnam@c-mg.vn\",\"domain\":\"c-mg.vn\",\"firstName\":\"cmg\",\"lastName\":\"vietnam\",\"isSuspended\":false,\"isDomainAdmin\":false},{\"id\":\"agR0ZXN0chALEgpTeXN0ZW1Vc2VyGAYM\",\"username\":\"dao.nguyen\",\"email\":\"dao.nguyen@c-mg.vn\",\"domain\":\"c-mg.vn\",\"firstName\":\"Dao\",\"lastName\":\"Nguyen\",\"isSuspended\":false,\"isDomainAdmin\":false},{\"id\":\"agR0ZXN0chALEgpTeXN0ZW1Vc2VyGAcM\",\"username\":\"dcarlyle\",\"email\":\"dcarlyle@c-mg.vn\",\"domain\":\"c-mg.vn\",\"firstName\":\"Dominic\",\"lastName\":\"Carlyle\",\"isSuspended\":false,\"isDomainAdmin\":true},{\"id\":\"agR0ZXN0chALEgpTeXN0ZW1Vc2VyGAgM\",\"username\":\"Elaine.Dimon\",\"email\":\"Elaine.Dimon@c-mg.vn\",\"domain\":\"c-mg.vn\",\"firstName\":\"Elanine\",\"lastName\":\"Dimon\",\"isSuspended\":false,\"isDomainAdmin\":false},{\"id\":\"agR0ZXN0chALEgpTeXN0ZW1Vc2VyGAkM\",\"username\":\"glo.noreply\",\"email\":\"glo.noreply@c-mg.vn\",\"domain\":\"c-mg.vn\",\"firstName\":\"glo\",\"lastName\":\"test\",\"isSuspended\":false,\"isDomainAdmin\":false},{\"id\":\"agR0ZXN0chALEgpTeXN0ZW1Vc2VyGAoM\",\"username\":\"hai.lu\",\"email\":\"hai.lu@c-mg.vn\",\"domain\":\"c-mg.vn\",\"firstName\":\"Hai\",\"lastName\":\"Lu\",\"isSuspended\":false,\"isDomainAdmin\":false},{\"id\":\"agR0ZXN0chALEgpTeXN0ZW1Vc2VyGAsM\",\"username\":\"hieu.tran\",\"email\":\"hieu.tran@c-mg.vn\",\"domain\":\"c-mg.vn\",\"firstName\":\"Mr\",\"lastName\":\"Hieu\",\"isSuspended\":true,\"isDomainAdmin\":false},{\"id\":\"agR0ZXN0chALEgpTeXN0ZW1Vc2VyGAwM\",\"username\":\"hudsoncmg\",\"email\":\"hudsoncmg@c-mg.vn\",\"domain\":\"c-mg.vn\",\"firstName\":\"hudson\",\"lastName\":\"admin 2\",\"isSuspended\":false,\"isDomainAdmin\":false},{\"id\":\"agR0ZXN0chALEgpTeXN0ZW1Vc2VyGA0M\",\"username\":\"huong.vu\",\"email\":\"huong.vu@c-mg.vn\",\"domain\":\"c-mg.vn\",\"firstName\":\"Huong\",\"lastName\":\"Vu\",\"isSuspended\":false,\"isDomainAdmin\":false},{\"id\":\"agR0ZXN0chALEgpTeXN0ZW1Vc2VyGA4M\",\"username\":\"lam.phan\",\"email\":\"lam.phan@c-mg.vn\",\"domain\":\"c-mg.vn\",\"firstName\":\"Lam\",\"lastName\":\"Phan\",\"isSuspended\":true,\"isDomainAdmin\":false},{\"id\":\"agR0ZXN0chALEgpTeXN0ZW1Vc2VyGA8M\",\"username\":\"lan.ta\",\"email\":\"lan.ta@c-mg.vn\",\"domain\":\"c-mg.vn\",\"firstName\":\"Lan\",\"lastName\":\"Ta\",\"isSuspended\":false,\"isDomainAdmin\":false},{\"id\":\"agR0ZXN0chALEgpTeXN0ZW1Vc2VyGBAM\",\"username\":\"monitor\",\"email\":\"monitor@c-mg.vn\",\"domain\":\"c-mg.vn\",\"firstName\":\"monitor\",\"lastName\":\"admin\",\"isSuspended\":false,\"isDomainAdmin\":true},{\"id\":\"agR0ZXN0chALEgpTeXN0ZW1Vc2VyGBEM\",\"username\":\"ort.noreply\",\"email\":\"ort.noreply@c-mg.vn\",\"domain\":\"c-mg.vn\",\"firstName\":\"ort\",\"lastName\":\"schedule\",\"isSuspended\":false,\"isDomainAdmin\":false},{\"id\":\"agR0ZXN0chALEgpTeXN0ZW1Vc2VyGBIM\",\"username\":\"tuannguyen\",\"email\":\"tuannguyen@c-mg.vn\",\"domain\":\"c-mg.vn\",\"firstName\":\"Tuan\",\"lastName\":\"Nguyen\",\"isSuspended\":false,\"isDomainAdmin\":true}]";
		Type type = new TypeToken<Collection<SystemUser>>() {
		}.getType();
		List<SystemUser> listInit = gson.fromJson(jsonInit, type);
		System.out.println("List USer init size:" + listInit.size());
		try {
			accountDao.createSystemUsers(listInit);
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		List<SystemUser> listOld = new ArrayList<SystemUser>();
		System.out.println("List USer size:" + listOld.size());
		try {
			listOld = accountDao.listAllSystemUser();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (!listOld.isEmpty()) {
			for (SystemUser user: listOld) {
				System.out.println("#### -> " + gson.toJson(user));
			}
		}
		try {
			service.sync();
			System.out.println(service.getLog());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			listOld = accountDao.listAllSystemUser();
			if (!listOld.isEmpty()) {
				System.out.println(gson.toJson(listOld));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("List USer size:" + listOld.size());
		
	}
	
	/**
	 * Test method for
	 * {@link cmg.org.monitor.services.GoogleAccountService#sync()}.
	 * @throws Exception 
	 */
	@Test
	public void testListGoogleAcc() throws Exception {
	    MonitorGwtServiceImpl asd = new MonitorGwtServiceImpl();
	    SystemAccountDAO dao = new SystemAccountDaoImpl();
	    GoogleAccount gg = new GoogleAccount();
	    gg.setDomain("c-mg.com.vn");
	    gg.setUsername("long.nguyen");
	    gg.setPassword("abcdef");
	    dao.createGoogleAccount(gg);
	    GoogleAccount[] list = asd.listAllGoogleAcc();
	    if(list != null){
		System.out.println("yow man");
	    }else{
		System.out.println("bullshit man");
	    }
	}
}
