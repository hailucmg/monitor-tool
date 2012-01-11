package cmg.org.monitor.ext.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cmg.org.monitor.util.shared.Constant;

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
public class MonitorUtil {
	/** Represent digit pattern value */
	public static final String DIGIT_PATTERN = "\\d+";
	public static String ARROW_STRING = " -> ";

	public static String FREE_MEMORY = "freeMemory";
	public static String TOTAL_MEMORY = "totalMemory";
	public static String MAX_MEMORY = "maxMemory";
	public static String USED_MEMORY = "memoryUsed";

	private static final Logger logger = Logger.getLogger(MonitorUtil.class
			.getCanonicalName());

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
	

	public static String parseTime(long millis, boolean addArrow) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss:SSS");
		return sdf.format(millis) + (addArrow ? ARROW_STRING : "");
	}

	public static String parseTime(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss:SSS");
		return sdf.format(date);
	}
	public static String parseTimeEmail(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		return sdf.format(date);
	}

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
	 * Extract digit number from a string value
	 * 
	 * @param str
	 * @return String value
	 */
	public static String extractDigit(String str) {
		Matcher m = Pattern.compile(DIGIT_PATTERN).matcher(str);
		if (m.find())
			return m.group(0);
		return null;
	}

}
