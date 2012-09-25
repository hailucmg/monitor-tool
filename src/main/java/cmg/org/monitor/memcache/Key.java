package cmg.org.monitor.memcache;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Key implements IsSerializable {
	/**
	 * 
	 */

	private static final long serialVersionUID = 1L;

	public static final int FILE_SYSTEM_STORE = 0x101;
	public static final int CPU__STORE = 0x102;
	public static final int MEMORY_STORE = 0x103;
	public static final int ALERT_STORE = 0x104;
	public static final int JVM_STORE = 0x105;
	public static final int SERVICE_STORE = 0x106;
	public static final int SYSTEM_MONITOR_STORE = 0x107;
	
	public static final int ALERT_TEMP_STORE = 0x108;
	public static final int MAIL_STORE = 0x109;
	public static final int MAIL_CONFIG_STORE = 0x110;
	
	public static final int ABOUT_CONTENT = 0x111;
	public static final int HELP_CONTENT = 0x112;
	
	public static final int LIST_GROUP = 0x113;
	public static final int LIST_ALL_USERS = 0x114;
	public static final int LIST_USERS_IN_GROUP = 0x115;
	public static final int CHANGE_LOG = 0x116;
	public static final int CHANGE_LOG_COUNT = 0x117;
	public static final int TOKEN_SITES = 0x018;
	public static final int TOKEN_MAIL= 0x019;
	public static final int TOKEN_GROUP = 0x020;
	private int type;

	private String sid;
	
	private String options;
	
	private int memType;

	protected Key() {

	}
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(type);
		sb.append("-");
		sb.append(sid);
		sb.append("-");
		sb.append(options);
		sb.append("-");
		sb.append(memType);
		return sb.toString();
	}

	public static Key create(int type) {
		Key key = new Key();
		key.type = type;
		return key;
	}

	public static Key create(int type, String sid) {
		Key key = create(type);
		key.setSid(sid);
		return key;
	}
	
	public static Key create(int type, String sid, String options) {
		Key key = create(type, sid);
		key.setOptions(options);
		return key;
	}
	
	public static Key create(int type, String sid, int memType) {
		Key key = create(type, sid);
		key.setMemType(memType);
		return key;
	}

	protected String getSid() {
		return sid;
	}

	protected void setSid(String sid) {
		this.sid = sid;
	}

	protected int getType() {
		return type;
	}

	protected void setType(int type) {
		this.type = type;
	}

	public String getOptions() {
		return options;
	}

	public void setOptions(String options) {
		this.options = options;
	}

	public int getMemType() {
		return memType;
	}

	public void setMemType(int memType) {
		this.memType = memType;
	}
}
