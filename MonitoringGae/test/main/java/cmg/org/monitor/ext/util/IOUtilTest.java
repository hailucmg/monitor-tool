/**
 * Copyright (c) CMG Ltd All rights reserved.
 *
 * This software is the confidential and proprietary information of CMG
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with CMG.
 */

package cmg.org.monitor.ext.util;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

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
			System.out.println(a);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
