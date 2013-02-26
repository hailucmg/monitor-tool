/*
 * Copyright (c) CMG Ltd All rights reserved.
 *
 * This software is the confidential and proprietary information of CMG
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with CMG.
 */
package cmg.org.monitor.ext.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cmg.org.monitor.util.shared.Constant;


// TODO: Auto-generated Javadoc
/**
 * The Class MonitorUtil.
 */
public class MonitorUtil {
	
	/** Represent digit pattern value. */
	public static final String DIGIT_PATTERN = "\\d+";
	
	/** The arrow string. */
	public static String ARROW_STRING = " -> ";

	/** The free memory. */
	public static String FREE_MEMORY = "freeMemory";
	
	/** The total memory. */
	public static String TOTAL_MEMORY = "totalMemory";
	
	/** The max memory. */
	public static String MAX_MEMORY = "maxMemory";
	
	/** The used memory. */
	public static String USED_MEMORY = "memoryUsed";

	/** The Constant logger. */
	private static final Logger logger = Logger.getLogger(MonitorUtil.class
			.getCanonicalName());

	/**
	 * Parses the date.
	 * 
	 * @param date
	 *            the date
	 * @return the date
	 */
	public static Date parseDate(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		Date d = new Date();
		try {
			d = sdf.parse(date);
		} catch (Exception ex) {
			// do nothing
		}
		return d;
	}
	

	/**
	 * Parses the time.
	 * 
	 * @param millis
	 *            the millis
	 * @param addArrow
	 *            the add arrow
	 * @return the string
	 */
	public static String parseTime(long millis, boolean addArrow) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss:SSS");
		return sdf.format(millis) + (addArrow ? ARROW_STRING : "");
	}

	/**
	 * Parses the time.
	 * 
	 * @param date
	 *            the date
	 * @return the string
	 */
	public static String parseTime(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss:SSS");
		return sdf.format(date);
	}
	
	/**
	 * Parses the time email.
	 * 
	 * @param date
	 *            the date
	 * @return the string
	 */
	public static String parseTimeEmail(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		return sdf.format(date);
	}

	/**
	 * Parses the href.
	 * 
	 * @param inputStr
	 *            the input str
	 * @return the string
	 */
	public static String parseHref(String inputStr) {
		Pattern pattern = Pattern.compile(Constant.PATTERN_HREF);
		Matcher matcher = pattern.matcher(inputStr);
		String matchStr;

		// Checks if existing any string that match with the pattern
		while (matcher.find()) {
			matchStr = matcher.group();
			inputStr = inputStr.replaceAll(matchStr, Constant.BLANK);
		}

		pattern = Pattern.compile(Constant.PATTERN_HREF_A_NAME);
		matcher = pattern.matcher(inputStr);
		// Checks if existing any string that match with the pattern
		while (matcher.find()) {
			matchStr = matcher.group();
			inputStr = inputStr.replaceAll(matchStr, Constant.BLANK);
		}
		return inputStr;
	}

	/**
	 * Gets property.
	 * 
	 * @return the return value
	 */
	public static String getErrorContent() {
		String s = "<html>"
				+ "<head>"
				+ "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">"
				+ "<title>CMG Email Monitor</title>"
				+ "</head><body>ERROR</body></html>";

		return s;
	}

	/**
	 * Gets the ip from url.
	 * 
	 * @param url
	 *            the url
	 * @return the ip from url
	 */
	public static String getIpFromUrl(String url) {
		try {
			InetAddress addr = InetAddress.getByName("microsoft.com");
			return addr.getHostAddress();
		} catch (UnknownHostException uhe) {
			uhe.printStackTrace();
			logger.info("unknow host exception occurrence");
			return null;
		}

	}
	
	/**
	 * Convert memory to string.
	 * 
	 * @param value
	 *            the value
	 * @return the string
	 */
	public static String convertMemoryToString(double value) {
		String temp = "";
		value = value / 1024;
		if (value >= 1024 * 1024) {
			temp = String.format("%.1f",value / (1024 * 1024))
					+ " GB";
		} else if (value >= 1024) {
			temp = String.format("%.1f",value / 1024) + " MB";
		} else {
			temp = String.format("%.1f", value) + " KB";
		}

		return temp;
	}
	
	/**
	 * Extract digit number from a string value.
	 * 
	 * @param str
	 *            the str
	 * @return String value
	 */
	public static String extractDigit(String str) {
		Matcher m = Pattern.compile(DIGIT_PATTERN).matcher(str);
		if (m.find())
			return m.group(0);
		return null;
	}

}
