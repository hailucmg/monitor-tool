package cmg.org.monitor.util.shared;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    
	

	
	/**
	 * Extract digit number from a string value
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
     * @param  strDateValue string value.
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
            validDate= formatter.parse(strDateValue);

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
		if (userId.contains("@c-mg")) {
			boolean checking = true;
			String[] temp = Ultility.listAdmin();
			String[] admins = new String[temp.length];
			for (int i = 0; i < temp.length; i++) {
				admins[0] = temp[i].split(":")[0];
				if (admins[0].trim().toString().toLowerCase()
						.equals(userId.toLowerCase())) {
					member = MonitorConstant.ROLE_ADMIN;
					checking = false;
				}
			}
			if (checking == true) {
				String[] temp1 = Ultility.listUser();
				String[] users = new String[temp1.length];
				for (int k = 0; k < temp1.length; k++) {
					users[0] = temp1[k].split(":")[0];
					if (users[0].toLowerCase().equals(userId.toLowerCase())) {
						member = MonitorConstant.ROLE_NORMAL_USER;
					}
				}

			}
		}

		return member;

	}

	/**
	 * @return all group in your domain
	 * @throws Exception
	 */
	public static String[] listGroup() throws Exception {
		Appforyourdomain client = new Appforyourdomain(
				MonitorConstant.ADMIN_EMAIL, MonitorConstant.ADMIN_PASSWORD,
				MonitorConstant.DOMAIN);

		String[] groups = null;
		try {
			groups = client.listGroup();
		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
		return groups;

	}

	/**
	 * @return all admin in domain
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
	 * @return all normal user in domain
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
		for(int a = 0 ; a < list.size();a++){
			for(int b = 1 ; b <list.size();b++){
				if(list.get(a).split(":")[0].equals(list.get(b).split(":")[0])){
					String newUser = list.get(a).toString()+","+ list.get(b).split(":")[1];
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

}
