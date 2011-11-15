package cmg.org.monitor.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import cmg.org.monitor.dao.SystemMonitorDAO;
import cmg.org.monitor.dao.impl.SystemMonitorDaoJDOImpl;
import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.exception.MonitorException;
import cmg.org.monitor.ext.model.URLPageObject;
import cmg.org.monitor.ext.model.shared.SystemDto;
import cmg.org.monitor.ext.util.URLMonitor;

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

		SystemMonitorDAO systemDao = new SystemMonitorDaoJDOImpl();
		List<SystemMonitor> systems = new ArrayList<SystemMonitor>();
		try {
		   systems = Arrays.asList(systemDao.listSystems(false));
		} catch(Exception me) { 
			logger.log(Level.SEVERE, me.getCause().getMessage());
			}
		URLMonitor urlMonitor = null;
		Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
		@SuppressWarnings("unused")
		int errorCount = 0;

		// Initializes monitor object list
		URLPageObject obj = null;
		int continueCount = 0;
		SystemDto aSysDto;
		for (SystemMonitor aSystem : systems) {
			
			aSysDto = aSystem.toDTO();
			if (!aSysDto.getIsActive()) {
				logger.info("The system " + aSysDto.getName()
						+ " is existed but is not active. "
						+ "The monitor skips this system now");
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
				logger.info("System '" + aSystem.getName() + "/" + "' is "
						+ running);

				// Add systems to list
				objList.add(obj);
			} catch (MonitorException me) {
				logger.log(Level.SEVERE, me.getCause().getMessage());
				throw me;
				
			}
			if (running.equals(FAILED)) {
				errorCount++;
			} // if
		} // for
		logger.info("Finished monitoring, object size: " + objList.size());

		return objList;
	}
}
