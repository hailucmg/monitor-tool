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

import cmg.org.monitor.exception.MonitorException;
import cmg.org.monitor.ext.model.shared.UserDto;

public class Ultility {

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
	 * @return all normal user in domain
	 * @throws Exception
	 */
	public static String[] listUser(String groupId) throws Exception {
		Appforyourdomain client = new Appforyourdomain(
				MonitorConstant.ADMIN_EMAIL, MonitorConstant.ADMIN_PASSWORD,
				MonitorConstant.DOMAIN);
		List<String> list = new ArrayList<String>();
		String[] users = null;
		String[] allUser = null;
		try {
			users = client.listUser(groupId);
			if (users != null) {
				for (int j = 0; j < users.length; j++) {
					list.add(users[j].toString());
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		for (int a = 0; a < list.size(); a++) {
			for (int b = 1; b < list.size(); b++) {
				if (list.get(a).split(":")[0].equals(list.get(b).split(":")[0])) {
					String newUser = list.get(a).toString() + ","
							+ list.get(b).split(":")[1];
					list.add(a, newUser);
					list.remove(b);
				}
			}
		}
		allUser = new String[list.size()];
		for (int j = 0; j < list.size(); j++) {
			allUser[j] = list.get(j);
		}

		return allUser;
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
		int member = MonitorConstant.ROLE_GUEST;
		String domain = userId.split("@")[1];
		if (domain.contains("c-mg")) {
			boolean checking = true;
			ArrayList<String> admin = Ultility.allAdmin();
			ArrayList<String> user = Ultility.allUser();
			for (int i = 0; i < admin.size(); i++) {
				if (admin.get(i).trim().toLowerCase()
						.equals(userId.trim().toLowerCase())) {
					member = MonitorConstant.ROLE_ADMIN;
					checking = false;
					break;
				}
			}
			if (checking) {
				for (int j = 0; j < user.size(); j++) {
					if (user.get(j).trim().toLowerCase()
							.equals(userId.trim().toLowerCase())) {
						member = MonitorConstant.ROLE_NORMAL_USER;
						break;
					}
				}

			}
		} else {
			member = MonitorConstant.ROLE_GUEST;
		}
		return member;

	}

	/**
	 * @return all group in your domain
	 * @throws Exception
	 */

	public static String[] listGroup() throws Exception {
		Appforyourdomain client_com = new Appforyourdomain(
				MonitorConstant.ADMIN_EMAIL_DotCom,
				MonitorConstant.ADMIN_PASSWORD_DotCom,
				MonitorConstant.DOMAIN_DotCom);
		String[] groups_com = null;
		try{
			groups_com = client_com.listGroupDotCom();
		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
		return groups_com;

	}

	/**
	 * @return all admin in domain_Dotvn
	 * @throws Exception
	 */
	public static String[] listAdmin() throws Exception {
		Appforyourdomain client = new Appforyourdomain(
				MonitorConstant.ADMIN_EMAIL, MonitorConstant.ADMIN_PASSWORD,
				MonitorConstant.DOMAIN);
		String[] admins = null;
		String[] ids = null;
		try {
			ids = client.listGroupID();
			admins = client.listAdmin(ids);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return admins;

	}

	/**
	 * @return all admin in domain_DotCom
	 * @throws Exception
	 */
	public static String[] listAdminDotCom() throws Exception {
		Appforyourdomain client = new Appforyourdomain(
				MonitorConstant.ADMIN_EMAIL_DotCom,
				MonitorConstant.ADMIN_PASSWORD_DotCom,
				MonitorConstant.DOMAIN_DotCom);
		String[] admins = null;
		String[] ids = null;
		try {
			ids = client.listGroupID();
			admins = client.listAdminDotCom(ids);
		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
		return admins;

	}

	/**
	 * @return all normal user in domain_dotcom
	 * @throws Exception
	 */
	public static String[] listUserDotCom() throws Exception {
		Appforyourdomain client = new Appforyourdomain(
				MonitorConstant.ADMIN_EMAIL_DotCom,
				MonitorConstant.ADMIN_PASSWORD_DotCom,
				MonitorConstant.DOMAIN_DotCom);
		List<String> list = new ArrayList<String>();
		String[] ids = null;
		String[] users = null;
		String[] allUser = null;
		try {
			ids = client.listGroupID();
			for (int i = 0; i < ids.length; i++) {
				users = client.listUserDotCom(ids[i]);
				if (users != null) {
					for (int j = 0; j < users.length; j++) {
						list.add(users[j].toString());
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		// tim kiem user trung nhau va loai bo
		for (int a = 0; a < list.size(); a++) {
			for (int b = 1; b < list.size(); b++) {
				if (list.get(a).split(":")[0].equals(list.get(b).split(":")[0])) {
					String newUser = list.get(a).toString() + ","
							+ list.get(b).split(":")[1];
					list.add(a, newUser);
					list.remove(b);
				}
			}
		}
		allUser = new String[list.size()];
		for (int j = 0; j < list.size(); j++) {
			allUser[j] = list.get(j);
		}

		return allUser;
	}

	/**
	 * @return all normal user in domain_dotvn
	 * @throws Exception
	 */
	public static String[] listUser() throws Exception {
		Appforyourdomain client = new Appforyourdomain(
				MonitorConstant.ADMIN_EMAIL, MonitorConstant.ADMIN_PASSWORD,
				MonitorConstant.DOMAIN);
		List<String> list = new ArrayList<String>();
		String[] ids = null;
		String[] users = null;
		String[] allUser = null;
		try {
			ids = client.listGroupID();
			for (int i = 0; i < ids.length; i++) {
				users = client.listUser(ids[i]);
				if (users != null) {
					for (int j = 0; j < users.length; j++) {
						list.add(users[j].toString());
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		for (int a = 0; a < list.size(); a++) {
			for (int b = 1; b < list.size(); b++) {
				if (list.get(a).split(":")[0].equals(list.get(b).split(":")[0])) {
					String newUser = list.get(a).toString() + ","
							+ list.get(b).split(":")[1];
					list.add(a, newUser);
					list.remove(b);
				}
			}
		}
		allUser = new String[list.size()];
		for (int j = 0; j < list.size(); j++) {
			allUser[j] = list.get(j);
		}

		return allUser;
	}

	/**
	 * @param url
	 * @return ip
	 * @throws Exception
	 */
	public static String getIpbyUrl(String url) throws Exception {
		String ip = "";
		try {

			InetAddress addr = InetAddress.getByName(url);
			byte[] ipAddr = addr.getAddress();
			for (int i = 0; i < ipAddr.length; i++) {
				if (i > 0) {
					ip += ".";
				}
				ip += ipAddr[i] & 0xFF;
			}

		} catch (UnknownHostException e) {
			throw e;
		}
		return ip;
	}

	/**
	 * @return allAdmin in two domain
	 * @throws Exception
	 */
	public static ArrayList<String> allAdmin() throws Exception {
		ArrayList<String> admin = new ArrayList<String>();
		String[] admin_vn = Ultility.listAdmin();
		String[] admin_dotCom = Ultility.listAdminDotCom();
		for (int i = 0; i < admin_vn.length; i++) {
			String email = admin_vn[i].split(":")[0].trim();
			admin.add(email);
		}
		for (int j = 0; j < admin_dotCom.length; j++) {
			String email = admin_dotCom[j].split(":")[0].trim();
			admin.add(email);
		}
		return admin;
	}

	/**
	 * @return allUser in two domain
	 * @throws Exception
	 */
	public static ArrayList<String> allUser() throws Exception {
		ArrayList<String> user = new ArrayList<String>();
		String[] user_vn = Ultility.listUser();
		String[] user_dotCom = Ultility.listUserDotCom();
		for (int i = 0; i < user_vn.length; i++) {
			String email = user_vn[i].split(":")[0].trim();
			user.add(email);
		}
		for (int j = 0; j < user_dotCom.length; j++) {
			String email = user_dotCom[j].split(":")[0];
			user.add(email);
		}

		return user;
	}

	public static Map<String, UserDto> listAllUser() throws Exception {
		String[] admins_vn = null;
		String[] users_vn = null;
		Map<String, UserDto> listAllUser = new HashMap<String, UserDto>();
		Map<String, UserDto> listUser_vn = new HashMap<String, UserDto>();
		try {

			admins_vn = Ultility.listAdmin();
			for (int i = 0; i < admins_vn.length; i++) {
				String[] temp = admins_vn[i].split(":");
				UserDto u = new UserDto();
				u.setUsername(temp[0].split("@")[0]);
				u.setEmail(temp[0]);
				u.setGroup(temp[1]);
				listUser_vn.put(u.getEmail().toString().trim(), u);
			}

			users_vn = Ultility.listUser();

			for (int j = 0; j < users_vn.length; j++) {

				String[] temp = users_vn[j].split(":");
				UserDto u = new UserDto();
				u.setUsername(temp[0].split("@")[0]);
				u.setEmail(temp[0]);
				u.setGroup(temp[1]);

				if (!listUser_vn.containsKey(u.getEmail().toString().trim())) {
					listUser_vn.put(u.getEmail(), u);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		String[] admins_com = null;
		String[] users_com = null;
		Map<String, UserDto> listUser_com = new HashMap<String, UserDto>();
		try {

			admins_com = Ultility.listAdminDotCom();
			for (int i = 0; i < admins_com.length; i++) {
				String[] temp = admins_com[i].split(":");
				UserDto u = new UserDto();
				u.setUsername(temp[0].split("@")[0]);
				u.setEmail(temp[0]);
				u.setGroup(temp[1]);
				listUser_com.put(u.getEmail().toString().trim(), u);
			}

			users_com = Ultility.listUserDotCom();

			for (int j = 0; j < users_com.length; j++) {

				String[] temp = users_com[j].split(":");
				UserDto u = new UserDto();
				u.setUsername(temp[0].split("@")[0]);
				u.setEmail(temp[0]);
				u.setGroup(temp[1]);

				if (!listUser_com.containsKey(u.getEmail().toString().trim())) {
					listUser_com.put(u.getEmail(), u);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		listAllUser.putAll(listUser_com);
		listAllUser.putAll(listUser_vn);
		return listAllUser;
	}
}
