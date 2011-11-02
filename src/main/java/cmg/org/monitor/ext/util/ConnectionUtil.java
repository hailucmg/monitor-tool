/*
 * Copyright (c) CMG Ltd All rights reserved.
 *
 * This software is the confidential and proprietary information of CMG
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with CMG.
 */
package cmg.org.monitor.ext.util;

import cmg.org.monitor.ext.util.HttpUtils.Page;

/**
 * Please enter a short description for this class.
 * 
 * <p>
 * Optionally, enter a longer description.
 * </p>
 * 
 * @author Lamphan
 * @version 1.0
 */
public class ConnectionUtil {

	/**
	 * documents description.
	 * 
	 * @return the return value
	 */
	public static boolean internetAvail() {
		boolean avail = false;
		Page gPage = null;
		Page mPage = null;
		String googleURL = "http://google.com";
		String microsoft = "http://microsoft.com";
		try {
			gPage = HttpUtils.retrievePage(googleURL);
			mPage = HttpUtils.retrievePage(microsoft);
			if ((gPage != null) || (mPage != null)) {
				avail = true;
			}
		} catch (Exception ex) {
			avail = false;
		}

		return avail;
	}
	
	/**
	 * Check given url.<br>
	 * 
	 * @param url value.
	 * 
	 * @return the return value
	 */
	public boolean isActiveUrl(String url) {
		boolean isAvailable = false;
		Page urlPage = null;
		try {
			urlPage = HttpUtils.retrievePage(url);
			if ((urlPage != null) ) {
				isAvailable = true;
			}
		} catch (Exception ex) {
			isAvailable = false;
		}

		return isAvailable;
	}
}
