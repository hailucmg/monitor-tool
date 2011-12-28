package cmg.org.monitor.util.shared;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cmg.org.monitor.dao.UtilityDAO;
import cmg.org.monitor.dao.impl.UtilityDaoImpl;
import cmg.org.monitor.exception.MonitorException;
import cmg.org.monitor.ext.model.shared.UserDto;
import cmg.org.monitor.ext.model.shared.UserMonitor;

public class Utility {

	/** Represent standard date format */
	public static final String STANDARD_FORMAT_DATE = "yyyy-mm-dd";

	/** Represent common date format */
	public static final String COMMON_FORMAT_DATE = "dd/mm/yyyy";

	/** symbol value */
	public static final char UPPER_LINE = '-';

	/** Forward slash */
	public static final char SLASH = '/';

	/** Represent digit pattern value */
	public static final String DIGIT_PATTERN = "\\d+";

	public static double percentageForJVM(double d1, double d2)
			throws MonitorException {
		if (d2 == 0)
			throw new MonitorException("do not divide by zero number");
		double percent = Math.floor(d1 / d2 * 100);
		return percent;
	}

	

	/**
	 * Extract digit number from a string value
	 * 
	 * @param str
	 * @return String value
	 */
	public static String extractDigit(String str) {
		Pattern p = Pattern.compile(DIGIT_PATTERN);
		Matcher m = p.matcher(str);
		if (m.find())
			return m.group();
		return null;
	}

	/**
	 * Function parse String value to Date type.<br>
	 * 
	 * @param strDateValue
	 *            string value.
	 * @return java.utit.Date type.
	 */
	public static Date isValidFormat(String strDateValue) {

		// Default format
		DateFormat formatter = new SimpleDateFormat();
		Date validDate = null;
		try {

			// Find character in given string value
			if (strDateValue.charAt(4) == UPPER_LINE) {
				formatter = new SimpleDateFormat(STANDARD_FORMAT_DATE);
			}
			if (strDateValue.charAt(2) == SLASH) {
				formatter = new SimpleDateFormat(COMMON_FORMAT_DATE);
			}
			validDate = formatter.parse(strDateValue);

			return validDate;
		} catch (ParseException pe) {
			return null;
		}
	}

	/**
	 * @param userId
	 * @return role of user
	 * @throws Exception
	 */
	public static int getSystemRole(String userId) throws Exception {
		int role = MonitorConstant.ROLE_GUEST;
		UtilityDAO utilDAO = new UtilityDaoImpl();		
		ArrayList<UserMonitor> users = utilDAO.listAllUsers();
		if (users != null && users.size() > 0) {
			for (UserMonitor user : users) {
				if (user.getId().equalsIgnoreCase(userId)) {
					role = user.getRole();
					break;
				}
			}
		}
		return role;
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
	
}
