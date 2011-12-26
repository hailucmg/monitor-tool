/*
 * Copyright (c) CMG Ltd All rights reserved.
 *
 * This software is the confidential and proprietary information of CMG
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with CMG.
 */
package cmg.org.monitor.ext.util;

import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;

import cmg.org.monitor.ext.util.HttpUtils.Page;

/**
 * Please enter a short description for this class.
 * 
 * <p>
 * Optionally, enter a longer description.
 * </p>
 * 
 * @author Lam phan
 * @version 1.0.6 June 11, 2008
 */
public class URLMonitor {

	/** Alert name */
	public static String ALERT_NAME = " alert report ";

	/** Declare email address */
	public static String EMAIL_ADMINISTRATOR = "lam.phan@c-mg.com";

	/** Time format value */
	private static String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss.S";

	/** Time instance */
	private Timestamp timeStamp;

	private static String ERROR = "ERROR";

	/** Log object. */
	private static final Logger logger = Logger.getLogger(URLMonitor.class
			.getCanonicalName());

	/**
	 * Default constructor.<br>
	 */
	public URLMonitor() {
		super();
	}

	public String retrievesContent(String remoteUrl) {
		String webContent = "";
		// Processes page
		Page page = null;
		boolean isError = false;
		try {
			page = HttpUtils.retrievePage(remoteUrl);			
			logger.log(
					Level.INFO,
					"The website has been retrieved, content type: "
							+ page.getContentType());
			if ((page == null) || (page.getContent() == null)) {
				if ((ConnectionUtil.internetAvail()) && (!isError)) {
					// Prints out
					logger.log(Level.SEVERE, "The system can't fetch content of data from the following url : \r\n"
							 + remoteUrl);
				} else {
					logger.info("Internet connection failed");
				}
			} else if ((page.getContent().equals(ERROR))
					|| (page.getContent().equals(MonitorUtil.getErrorContent()))) {
				logger.log(Level.SEVERE, "The system contains error information from the following url : \r\n"
						+ remoteUrl);
			} else {
				webContent = page.getContent();
			}
		} catch (Exception mx) {
			logger.log(Level.SEVERE, " -> ERROR: retrieve url. Message: " + mx.getMessage());
			try {
				if (ConnectionUtil.internetAvail()) {
					logger.log(Level.SEVERE, "The system can not achieve data from following url :\r\n"
							+ remoteUrl);
					isError = true;
				} else {
					logger.log(Level.SEVERE, "Internet connection failed");
				}
			} catch (Exception e) {
				logger.log(Level.SEVERE, "The monitoring failed, try to update"
						+ " project's status but not success, error details: " + e.getMessage());
			}
		}		
		return webContent;
	}

}
