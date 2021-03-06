package cmg.org.monitor.memcache;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheService.SetPolicy;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

public class MonitorMemcache {

	private static final String MEMCACHE_NAMESPACE = "monitor_tool_mig";
	/**
	 * @param key
	 *            The key of store
	 * @param obj
	 *            Object which want to store
	 */
	public static void put(Key key, Object obj) {
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService(MEMCACHE_NAMESPACE);
		System.out.println(key.toString());
		syncCache.put(key.toString(), obj, null, SetPolicy.SET_ALWAYS);
	}

	/**
	 * @param key
	 *            The key of store
	 * @return Object in store. Return null if not found
	 */
	public static Object get(Key key) {
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService(MEMCACHE_NAMESPACE);
		System.out.println(key.toString());
		return syncCache.get(key.toString());
	}

	/**
	 * @param key
	 * 			The key of store
	 * @return true if object deleted successfully
	 */
	public static boolean delete(Key key) {
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService(MEMCACHE_NAMESPACE);
		return syncCache.delete(key.toString());
	}

}
