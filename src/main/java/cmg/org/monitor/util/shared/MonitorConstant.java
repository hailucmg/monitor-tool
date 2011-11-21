package cmg.org.monitor.util.shared;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public final class MonitorConstant {
	public static final String PROJECT_NAME = "CMG Health Monitor System";
	public static final String VERSION = "1.0";
	public static final String RELEASED_ON = "01/11/2011";
	
	
	public static final String DONE_MESSAGE = "Add a system successfully";	
	// time to refresh data for all module
	public static final int REFRESH_RATE = 1800000;
	
	public static final int REDIRECT_WAIT_TIME = 5000;
	
	public static final String DOMAIN = "c-mg.vn";
	
	public static final String ADMIN_EMAIL = "monitor@c-mg.vn";
	
	public static final String ADMIN_PASSWORD = "31102011";
	
	public static final int ROLE_ADMIN = 0x001;
	public static final int ROLE_NORMAL_USER = 0x002;
	public static final int ROLE_GUEST = 0x003;
	
	/// Information google sites content
	public static final String SITES_USERNAME = "hai.lu@c-mg.com";
	public static final String SITES_PASSWORD = "123hurricane";
	public static final String SITES_CONTENT_FEED_URL = "https://sites.google.com/feeds/content/c-mg.com/monitor-tool/";
	public static final String SITES_APP_NAME = "cmg-monitor-tool";
	public static final String SITES_HELP_CONTENT_ID = "76479283222030903";
	public static final String SITES_ABOUT_CONTENT_ID = "1702220121101760304";

}
