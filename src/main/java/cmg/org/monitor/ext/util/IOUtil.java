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

import org.apache.commons.io.FileUtils;

/**
 * DOCME
 * 
 * @Creator Hai Lu
 * @author $Author$
 * @version $Revision$
 * @Last changed: $LastChangedDate$
 */

public class IOUtil {
	public static final String CMG_SIGNATURE_TEMPLATE_PATH = "/cmg/org/monitor/mail/template/cmg-signature.html";	

	public static String readResource(String path) throws IOException {
		try {
			return FileUtils.readFileToString(FileUtils
					.toFile(IOUtil.class.getResource(path)));			
		} catch (IOException e) {
			throw e;
		}
	}

}
