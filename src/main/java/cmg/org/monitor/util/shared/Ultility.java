package cmg.org.monitor.util.shared;


import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import cmg.org.monitor.entity.shared.SystemMonitor;

import com.google.gdata.client.appsforyourdomain.AppsGroupsService;
import com.google.gdata.data.appsforyourdomain.generic.GenericFeed;
import com.google.gdata.data.appsforyourdomain.provisioning.UserFeed;



public class Ultility {
	//UserService us = UserServiceFactory.getUserService();
	//URL ul = new URL ("https://apps-apis.google.com/a/feeds/domain/user/2.0/c-mg.vn");
	//UserFeed fedd = us.getFeed (ul);
	//fedd.get(1).getName();
	//fedd.get(4).getAddress();
	

	public static GenericFeed listGroup() throws Exception{
		return null;
	
		
	}
	 public static UserFeed listAdmin() throws Exception{
		return null;
		
		 
	 }
	 
	 public static UserFeed listUser() throws Exception{
		 UserFeed list = null;
		
		
		 return list;
	
	 }
	 
	public static String getIpbyUrl(String url) throws Exception{
		String ip="";
		try {
			
			InetAddress addr = InetAddress.getByName(url);
			byte[] ipAddr = addr.getAddress();
			for(int i=0;i<ipAddr.length;i++){
				if(i>0){
					ip+=".";
				}
			ip+=ipAddr[i]&0xFF;
			}
		
		} catch (UnknownHostException e) {
			throw e;
		}
		return ip;
	}
	

	

	@SuppressWarnings("unchecked")
	public static String createSID() throws Exception{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		String SID = null;
		String[] names = null;
		List<String> list;
		Query q = pm.newQuery("select name from " + SystemMonitor.class.getName());
		try {
	
			list = (List<String>) q.execute();
			if(list.size() == 0){
				SID = "S001";
			}else{
				names = new String[list.size()];
				for(int i = 0 ; i <list.size() ; i++){
					names[i] = list.get(i);
				}
				if(names.length > 0 && names.length < 9){
					int number = names.length + 1;
					SID = "S00"+number;
				}else if(names.length > 9 && names.length <98){
					int number = names.length + 1;
					SID = "S0"+number;				
				}else if(names.length > 98 && names.length < 998){
					int number = names.length + 1;
					SID = "S"+number;
					
				}
			}

			
		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
		finally{
			q.closeAll();
			pm.close();
		}
			
			
		return SID;
	}
}
