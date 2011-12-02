package cmg.org.monitor.memcache;

import java.io.Serializable;

public class Key implements Serializable {
	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	
	protected static final int FILE_SYSTEM_STORE = 0x001;
	protected static final int CPU_MEMORY_STORE = 0x002;
	protected static final int ALERT_STORE = 0x003;
	protected static final int JVM_STORE = 0x004;
	protected static final int SERVICE_STORE = 0x005;
	protected static final int SYSTEM_MONITOR_STORE = 0x006;
	protected static final int COUNT_STORE = 0x007;
	protected static final int FLAG_STORE = 0x008;
	protected static final int CPU_STORE = 0x009;
	
	private int type;
	
	private int count;
	
	private String sid;

	protected Key() {
		
	}
	
	protected static Key create(int type) {
		Key key = new Key();
		key.type = type;
		return key;
	}
	
	protected static Key create(int type, int count) {
		Key key = create(type);
		key.count = count;
		return key;
	}
	
	protected static Key create(int type, int count, String sid) {
		Key key = create(type, count);
		key.sid = sid;
		return key;
	}

	protected int getCount() {
		return count;
	}

	protected void setCount(int count) {
		this.count = count;
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
}
