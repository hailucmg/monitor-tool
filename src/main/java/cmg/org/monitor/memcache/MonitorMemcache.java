package cmg.org.monitor.memcache;

import java.util.ArrayList;

import cmg.org.monitor.dao.SystemMonitorDAO;
import cmg.org.monitor.dao.impl.SystemMonitorDaoJDOImpl;
import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.memcache.shared.SystemMonitorDto;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheService.SetPolicy;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

public class MonitorMemcache {

	private static MemcacheService syncCache = MemcacheServiceFactory
			.getMemcacheService("monitor_system");

	/**
	 * @return Store of System Monitor return null if no system found in
	 *         memcache and JDO
	 */
	public static ArrayList<SystemMonitorStore> getSystemMonitorStore() {
		ArrayList<SystemMonitorStore> smsList = null;
		SystemMonitorStore sms = null;

		ArrayList<SystemMonitorDto> sysList = readSystemMonitorList();

		if (sysList != null) {
			smsList = new ArrayList<SystemMonitorStore>();
			for (SystemMonitorDto sys : sysList) {
				sms = SystemMonitorStore.create();
				sms.setSysMonitor(sys);
				smsList.add(sms);
			}
		}
		return smsList;
	}

	/**
	 * @param sms
	 *            Store all information of monitor system put the store into
	 *            memcache
	 */
	public static void putSystemMonitorStore(
			ArrayList<SystemMonitorStore> smsList){
		if (smsList != null) {
			int count = getCount() + 1;
			// Store count
			setCount(count);
			ArrayList<SystemMonitorDto> sysList = new ArrayList<SystemMonitorDto>();
			for (SystemMonitorStore sms : smsList) {
				sysList.add(sms.getSysMonitor());
				String sid = sms.getSysMonitor().getId();
				// Store alert monitor information
				put(Key.create(Key.ALERT_STORE, count, sid), sms.getAlert());
				// Store CPU & memory information
				put(Key.create(Key.CPU_MEMORY_STORE, count, sid),
						sms.getMemory());
				
				put(Key.create(Key.CPU_STORE, count, sid),
						sms.getCpu());
				// Store File system list information
				put(Key.create(Key.FILE_SYSTEM_STORE, count, sid),
						sms.getFileSysList());
				// Store JVM information
				put(Key.create(Key.JVM_STORE, count, sid), sms.getJvm());
				// Store service list information
				put(Key.create(Key.SERVICE_STORE, count, sid),
						sms.getServiceMonitorList());
			}
			// Store system monitor list
			put(Key.create(Key.SYSTEM_MONITOR_STORE, count), sysList);
		}
	}

	/**
	 * @return system monitor list. Read from JDO if not found in memcache or
	 *         JDO add new one or JDO update system information Return null if
	 *         no system found.
	 */
	private static ArrayList<SystemMonitorDto> readSystemMonitorList() {
		ArrayList<SystemMonitorDto> sysList = null;
		if (isFlagChange()) {
			sysList = readSystemFromJdo();
		} else {
			Object obj = get(Key.create(Key.SYSTEM_MONITOR_STORE,
					getCount()));
			if (obj == null) {
				sysList = readSystemFromJdo();
			} else {				
				sysList = (ArrayList<SystemMonitorDto>) obj;
			}
		}
		return sysList;
	}

	/**
	 * @return the list of system monitor read from JDO. Null if no system found
	 */
	private static ArrayList<SystemMonitorDto> readSystemFromJdo() {
		ArrayList<SystemMonitorDto> list = null;
		try {
			SystemMonitorDAO sysDao = new SystemMonitorDaoJDOImpl();
			SystemMonitor[] sysList = sysDao.listSystems(false);
			SystemMonitorDto sysDto = null;
			if (sysList != null) {
				list = new ArrayList<SystemMonitorDto>();
				for (SystemMonitor sys : sysList) {
					sysDto = new SystemMonitorDto();
					sysDto.setId(sys.getId());
					sysDto.setCode(sys.getCode());
					sysDto.setActive(sys.isActive());
					sysDto.setDeleted(sys.isDeleted());
					sysDto.setEmail(sys.getEmail());
					sysDto.setGroupEmail(sys.getGroupEmail());
					sysDto.setIp(sys.getIp());
					sysDto.setName(sys.getName());
					sysDto.setProtocol(sys.getProtocol());
					sysDto.setRemoteUrl(sys.getRemoteUrl());
					sysDto.setStatus(sys.getStatus());
					sysDto.setUrl(sys.getUrl());
					list.add(sysDto);
				}				
			}
		} catch (Exception ex) {
			// do nothing
		}
		//
		changeFlag(false);
		return list;
	}

	/**
	 * @return count number.
	 */
	protected static int getCount() {
		int count = 0;
		try {
			count = Integer.parseInt(get(Key.create(Key.COUNT_STORE))
					.toString());
		} catch (Exception e) {
			// do nothing
		}
		return count;
	}

	/**
	 * @param count
	 *            store the counting value.
	 */
	protected static void setCount(int count) {
		put(Key.create(Key.COUNT_STORE), count);
	}

	/**
	 * @param key
	 *            The key of store
	 * @param obj
	 *            Object which want to store
	 */
	protected static void put(Key key, Object obj) {
		syncCache.put(key, obj, null, SetPolicy.SET_ALWAYS);
	}

	/**
	 * @param key
	 *            The key of store
	 * @return Object in store. Return null if not found
	 */
	protected static Object get(Key key) {
		return syncCache.get(key);
	}

	/**
	 * @param b
	 *            true when an System Monitor JDO insert or update.
	 */
	protected static void changeFlag(boolean b) {
		put(Key.create(Key.FLAG_STORE), b);
	}

	/**
	 * @return return true if flag not found in mecache or one of system monitor
	 *         is update or memche add new one
	 */
	protected static boolean isFlagChange() {
		boolean b = true;
		if (get(Key.create(Key.FLAG_STORE)) == null) {
			b = true;
		} else {
			try {
				b = Boolean.parseBoolean(get(Key.create(Key.FLAG_STORE))
						.toString());
			} catch (Exception ex) {
			}
		}
		return b;
	}
}
