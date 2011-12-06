package cmg.org.monitor.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import cmg.org.monitor.exception.MonitorException;
import cmg.org.monitor.ext.model.URLPageObject;
import cmg.org.monitor.ext.util.URLMonitor;
import cmg.org.monitor.memcache.MonitorMemcache;
import cmg.org.monitor.memcache.SystemMonitorStore;
import cmg.org.monitor.memcache.shared.SystemMonitorDto;

public class MonitorService {
	/** Show status of monitor */
	private static String RUNNING = "running";
	
	/** Show status of monitor */
	private static String FAILED = "failed";
	
	/** Default monitor logger */
	private static final Logger logger = Logger.getLogger(MonitorService.class
			.getCanonicalName());

	/**
	 * Monitor the node/projects.
	 * 
	 * @return the return value
	 * 
	 * @throws MonitorException
	 *             if the monitoring failed
	 */
	public synchronized List<URLPageObject> monitor() throws MonitorException {
		List<URLPageObject> objList = new ArrayList<URLPageObject>();

		// Gets system time
		logger.info("Begin monitoring... ");
		
		List<SystemMonitorStore> systemMonitorCaches = (List<SystemMonitorStore>)MonitorMemcache.getSystemMonitorStore();
		
		
		URLMonitor urlMonitor = null;
		Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
		@SuppressWarnings("unused")
		int errorCount = 0;

		// Initializes monitor object list
		URLPageObject obj = null;
		int continueCount = 0;
		SystemMonitorDto aSysDto ;
		for (SystemMonitorStore aSystem : systemMonitorCaches) {
			aSysDto = aSystem.getSysMonitor();
			if (!aSysDto.isActive()) {
				logger.info("The system " + aSysDto.getName()
						+ " is existed but is not active. "
						+ " The monitor skips this system now");
				continueCount++;

				continue;
			}
			logger.info("Project url: " + aSysDto.getUrl());
			logger.info("System name: " + aSysDto.getName());
			logger.info("Number of system are monitoring : " + continueCount);
			// Initiates monitor and do task
			String running = "";
			try {
				urlMonitor = new URLMonitor();
				urlMonitor.setTimeStamp(timeStamp);
				obj = urlMonitor.generateInfo(aSysDto);
				running = (obj == null) ? FAILED : RUNNING;
				

				// Add systems to list
				objList.add(obj);
			} catch (MonitorException me) {
				logger.log(Level.SEVERE, me.getCause().getMessage());
				throw me;
			} catch(Exception e) {
				logger.log(Level.SEVERE, e.getMessage());
			}
			if (running.equals(FAILED)) {
				errorCount++;
			} // if
		} // for
		MonitorMemcache.increaseCount();
		logger.info("Finished monitoring, object size: " + objList.size());

		return objList;
	}
}
