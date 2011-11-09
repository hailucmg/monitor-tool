package cmg.org.monitor.util.shared;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import java_cup.emit;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import cmg.org.monitor.entity.shared.SystemMonitor;



public class Ultility {
	public static final int ROLE_ADMIN = 0x001;
	public static final int ROLE_NORMAL_USER = 0x002;
	public static final int ROLE_GUEST = 0x003;
	public static int getSystemRole(String userId) throws Exception{
		int member = ROLE_GUEST;
		 if(userId.contains("@c-mg")){
			boolean checking = true;
			String[] temp = Ultility.listAdmin();
			String[] admins = new String[temp.length];
			for(int i = 0; i< temp.length;i++){
				admins[i] = temp[i].split(":")[0];
				if(admins[i].equals(userId)){
					member = ROLE_ADMIN;
					checking = false;
				}
			}
			if(checking = true){
				String[] temp1  = Ultility.listUser();
				String[] users = new String[temp1.length];
				for(int k = 0;k < temp1.length;k++){
					users[k]=temp1[k].split(":")[0];
					if(users[k].equals(userId)){
						member = ROLE_NORMAL_USER;
					}
				}
				
			}
		}
		return member;
		
	}
	
	public static String[] listGroup() throws Exception {
		Appforyourdomain client = new Appforyourdomain(MonitorConstant.ADMIN_EMAIL,
				MonitorConstant.ADMIN_PASSWORD, MonitorConstant.Domain);

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
		Appforyourdomain client = new Appforyourdomain(MonitorConstant.ADMIN_EMAIL,
				MonitorConstant.ADMIN_PASSWORD, MonitorConstant.Domain);
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
		Appforyourdomain client = new Appforyourdomain(MonitorConstant.ADMIN_EMAIL,
				MonitorConstant.ADMIN_PASSWORD, MonitorConstant.Domain);
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

	@SuppressWarnings("unchecked")
	public static String createSID() throws Exception {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		String SID = null;
		String[] names = null;
		List<String> list;
		Query q = pm.newQuery("select name from "
				+ SystemMonitor.class.getName());
		try {

			list = (List<String>) q.execute();
			if (list.size() == 0) {
				SID = "S001";
			} else {
				names = new String[list.size()];
				for (int i = 0; i < list.size(); i++) {
					names[i] = list.get(i);
				}
				if (names.length > 0 && names.length < 9) {
					int number = names.length + 1;
					SID = "S00" + number;
				} else if (names.length > 9 && names.length < 98) {
					int number = names.length + 1;
					SID = "S0" + number;
				} else if (names.length > 98 && names.length < 998) {
					int number = names.length + 1;
					SID = "S" + number;

				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		} finally {
			q.closeAll();
			pm.close();
		}

		return SID;
	}
}
