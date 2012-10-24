/**
 * Copyright (c) CMG Ltd All rights reserved.
 *
 * This software is the confidential and proprietary information of CMG
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with CMG.
 */

package cmg.org.monitor.ext.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

/** 
 * DOCME
 * 
 * @Creator Hai Lu
 * @author $Author$
 * @version $Revision$
 * @Last changed: $LastChangedDate$
 */

public class IOUtilTest {
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

	/**
	 * Test method for {@link cmg.org.monitor.ext.util.IOUtil#readResource(java.lang.String)}.
	 */
	@Test
	public void testReadResource() {
		new Date(System.currentTimeMillis());
		/*
		String a;
		try {
			a = IOUtil.readResource(IOUtil.CMG_SIGNATURE_TEMPLATE_PATH);
			
			MailContent mail = IOUtil.getMailTemplate("invite-monitor-user");
			Map<String, String> map = new HashMap<String, String>();
			map.put("PROJECT_NAME", MonitorConstant.PROJECT_NAME);
			map.put("PROJECT_HOST_NAME", MonitorConstant.PROJECT_HOST_NAME);
			map.put("QRCODE_LINK", "http://qr.kaywa.com/?s=8&d=http%3A%2F%2Fmo.c-mg.com.vn");
			mail.setMap(map);
			mail.init();
			MailAsync mailUtil = new MailAsync(new String[] {"hai.lu@c-mg.com", "lan.ta@c-mg.com", "long.nguyen@c-mg.com"}, mail);
			
			mailUtil.run();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/

		Date now = new Date(System.currentTimeMillis());
		Date dtLondon = DateTimeUtils.convertDateWithZone(DateTime.now().toDate());
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT+1"));
		System.out.println("London:" + sdf.format(dtLondon));
		System.out.println("Here: " + sdf.format(now));		
		//Date abc = new Date(dtLondon.getMillis());
		
		//System.out.println(abc + "/" + dtLondon.getHourOfDay());
	}

}
