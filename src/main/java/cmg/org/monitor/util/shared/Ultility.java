package cmg.org.monitor.util.shared;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class Ultility {
	public static void main(String[] args) {
		try {
			System.out.println(Integer.toString(getSystemRole("luhonghai@c-mg.vn")));
		} catch (Exception ex) {
			
		}
	}
	public static int getSystemRole(String userId) throws Exception {
		int member = MonitorConstant.ROLE_GUEST;
		if (userId.contains("@c-mg")) {
			boolean checking = true;
			String[] temp = Ultility.listAdmin();
			String[] admins = new String[temp.length];
			for (int i = 0; i < temp.length; i++) {
				admins[0] = temp[i].split(":")[0];
				System.out.println(admins[0].toString());
				if (admins[0].trim().toString().toLowerCase().equals(userId.toLowerCase())) {
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
		allUser = new String[list.size()];
		for (int j = 0; j < list.size(); j++) {
			allUser[j] = list.get(j);

		}
		return allUser;
	}

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
