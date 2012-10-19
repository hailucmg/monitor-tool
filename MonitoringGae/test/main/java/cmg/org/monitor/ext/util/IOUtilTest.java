/**
 * Copyright (c) CMG Ltd All rights reserved.
 *
 * This software is the confidential and proprietary information of CMG
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with CMG.
 */

package cmg.org.monitor.ext.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import cmg.org.monitor.ext.model.MailContent;
import cmg.org.monitor.util.shared.MonitorConstant;

/** 
 * DOCME
 * 
 * @Creator Hai Lu
 * @author $Author$
 * @version $Revision$
 * @Last changed: $LastChangedDate$
 */

public class IOUtilTest {

	/**
	 * Test method for {@link cmg.org.monitor.ext.util.IOUtil#readResource(java.lang.String)}.
	 */
	@Test
	public void testReadResource() {
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
			System.out.println(mail.getBody());
			MailAsync mailUtil = new MailAsync(new String[] {"hai.lu@c-mg.com", "lan.ta@c-mg.com", "long.nguyen@c-mg.com"}, mail);
			
			mailUtil.run();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
