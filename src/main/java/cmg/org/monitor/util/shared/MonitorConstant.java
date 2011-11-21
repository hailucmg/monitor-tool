package cmg.org.monitor.util.shared;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public final class MonitorConstant {
	public static final String PROJECT_NAME = MonitorConstant.property()[3];
	public static final String VERSION = MonitorConstant.property()[4];
	public static final String RELEASED_ON = MonitorConstant.property()[5];

	public static final String DONE_MESSAGE = MonitorConstant.property()[6];
	// time to refresh data for all module
	public static final int REFRESH_RATE = Integer.parseInt(MonitorConstant.property()[7]);

	public static final int REDIRECT_WAIT_TIME = Integer.parseInt(MonitorConstant.property()[8]);

	/**
	 * return domain
	 */
	public static final String DOMAIN = MonitorConstant.property()[0];

	/**
	 * return admin email
	 */
	public static final String ADMIN_EMAIL = MonitorConstant.property()[1];

	/**
	 * return admin password
	 */
	public static final String ADMIN_PASSWORD = MonitorConstant.property()[2];

	/**
	 * 
	 */
	public static final int ROLE_ADMIN = Integer.parseInt(MonitorConstant.property()[9]);
	/**
	 * 
	 */
	public static final int ROLE_NORMAL_USER = Integer.parseInt(MonitorConstant.property()[10]);
	/**
	 * 
	 */
	public static final int ROLE_GUEST = Integer.parseInt(MonitorConstant.property()[11]);
	
	
	/**
	 * 
	 */
	public static final String SITES_USERNAME =  MonitorConstant.property()[12];
	/**
	 * 
	 */
	public static final String SITES_PASSWORD = MonitorConstant.property()[13];
	/**
	 * 
	 */
	public static final String SITES_CONTENT_FEED_URL = MonitorConstant.property()[14];
	/**
	 * 
	 */
	public static final String SITES_APP_NAME = MonitorConstant.property()[15];
	/**
	 * 
	 */
	public static final String SITES_HELP_CONTENT_ID = MonitorConstant.property()[16];
	/**
	 * 
	 */
	public static final String SITES_ABOUT_CONTENT_ID = MonitorConstant.property()[17];

	
	/**
	 * @return all property for domain
	 */
	public static String[] property() {
		String[] pro = new String[18];
		File file = new File("war/WEB-INF/config-MonitorConstant.xml");
		FileInputStream fileInput;
		try {
			fileInput = new FileInputStream(file);
			Properties properties = new Properties();
			properties.loadFromXML(fileInput);
			fileInput.close();
			pro[0] = properties.getProperty("DOMAIN");
			pro[1] = properties.getProperty("ADMIN_EMAIL");
			pro[2] = properties.getProperty("ADMIN_PASSWORD");
			pro[3] = properties.getProperty("PROJECT_NAME");
			pro[4] = properties.getProperty("VERSION");
			pro[5] = properties.getProperty("RELEASED_ON");
			pro[6] = properties.getProperty("DONE_MESSAGE");
			pro[7] = properties.getProperty("REFRESH_RATE");
			pro[8] = properties.getProperty("REDIRECT_WAIT_TIME");
			pro[9] = properties.getProperty("ROLE_ADMIN");
			pro[10] = properties.getProperty("ROLE_NORMAL_USER");
			pro[11] = properties.getProperty("ROLE_GUEST");
			pro[12] = properties.getProperty("SITES_USERNAME");
			pro[13] = properties.getProperty("SITES_PASSWORD");
			pro[14] = properties.getProperty("SITES_CONTENT_FEED_URL");
			pro[15] = properties.getProperty("SITES_APP_NAME");
			pro[16] = properties.getProperty("SITES_HELP_CONTENT_ID");
			pro[17] = properties.getProperty("SITES_ABOUT_CONTENT_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return pro;
	}
	
/*	public static void main(String[] arg){
		String[] pro1 = MonitorConstant.property();
		for(int i = 0; i < pro1.length;i++){
			System.out.println(pro1[i]);
		}
	}*/


}
