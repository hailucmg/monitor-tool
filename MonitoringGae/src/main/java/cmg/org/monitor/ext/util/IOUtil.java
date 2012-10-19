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

import cmg.org.monitor.ext.model.MailContent;

/**
 * DOCME.
 *
 * @Creator Hai Lu
 * @author $Author$
 * @version $Revision$
 * @Last changed: $LastChangedDate$
 */

public class IOUtil {
	
	/** The Constant SUBJECT_FILE_EXTENSION. */
	public static final String SUBJECT_FILE_EXTENSION = ".subject";
	
	/** The Constant BODY_FILE_EXTENSION. */
	public static final String BODY_FILE_EXTENSION = ".html";	
	
	/** The Constant CMG_SIGNATURE_TEMPLATE_PATH. */
	public static final String CMG_SIGNATURE_TEMPLATE_PATH = "/cmg/org/monitor/mail/template/cmg-signature.html";
	
	/** The Constant TEMPLATE_PATH. */
	public static final String TEMPLATE_PATH = "/cmg/org/monitor/mail/template/";
	
	/**
	 * Read resource.
	 *
	 * @param path the path
	 * @return the string
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static String readResource(String path) throws IOException {
		try {
			return FileUtils.readFileToString(FileUtils
					.toFile(IOUtil.class.getResource(path)));			
		} catch (IOException e) {
			throw e;
		}
	}
	
	/**
	 * Gets the mail template.
	 *
	 * @param name the name
	 * @return the mail template
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static MailContent getMailTemplate(String name) throws IOException {
		try {
			String subject = readResource(TEMPLATE_PATH + name + SUBJECT_FILE_EXTENSION);
			String body = readResource(TEMPLATE_PATH + name + BODY_FILE_EXTENSION);
			String signature = readResource(CMG_SIGNATURE_TEMPLATE_PATH);
			return new MailContent(subject, body + signature);
		} catch (IOException e) {
			throw e;
		}
	}

}
