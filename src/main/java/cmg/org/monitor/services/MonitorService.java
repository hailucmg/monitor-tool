package cmg.org.monitor.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import cmg.org.monitor.dao.SystemMonitorDAO;
import cmg.org.monitor.dao.impl.SystemMonitorDaoJDOImpl;
import cmg.org.monitor.exception.MonitorException;
import cmg.org.monitor.ext.model.URLPageObject;
import cmg.org.monitor.ext.model.shared.SystemDto;
import cmg.org.monitor.ext.util.URLMonitor;

public class MonitorService {

	private final String RUNNING = "running";
	private final String FAILED = "failed";
	public static String MONITOR_URL = "https://ukpensionsint.bp.com/content/cmg_monitor.html";
	public static String MONITOR_URL_TEST = "http://c-mg.vn:81/bpg/content/monitortest.html";
	private static final Logger logger = Logger.getLogger(MonitorService.class
			.getCanonicalName());

	// Sample url data
	private static final String[] urls = new String[] {
			"http://c-mg.vn:81/bpg/content/monitortest.html",
			"https://ukpensionsint.bp.com/content/cmg_monitor.html" };

	// Sample status of systems
	private static final String[] statuses = new String[] { "true", "true" };

	private static final String[] names = new String[] { "PL monitor",
			"BPS Monitor" };
	private static final String[] groupEmails = new String[] { "monitor@c-mg.vn",
			"lam.phan@c-mg.vn" };

	/**
	 * Get a list of testing datas
	 * 
	 * @return
	 */
	static List<SystemDto> makeStaticDatas() {
		SystemDto systemDto = new SystemDto();
		List<SystemDto> systems = new ArrayList<SystemDto>();
		for (int i = 0; i < urls.length; ++i) {
			systemDto.setUrl(urls[i]);
			systemDto.setIsActive(Boolean.parseBoolean(statuses[i]));
			systemDto.setName(names[i]);
			systemDto.setGroupEmail(groupEmails[i]);
			systems.add(systemDto);
		}
		return systems;
	}

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
		// List<Node> nodes = nodeService.getNodes();
		logger.info("Begin monitoring... ");

		SystemMonitorDAO systemDao = new SystemMonitorDaoJDOImpl();
		List<SystemDto> systems =  
				makeStaticDatas();

		URLMonitor urlMonitor = null;
		Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
		// for (Node node : nodes) {
		int errorCount = 0;

		// boolean projRunning = false;
		// projs = projService.getProjects(node.getNode_id());

		// Initializes monitor object list
		URLPageObject obj = null;
		int continueCount = 0;
		for (SystemDto aSystem : systems) {
			if (!aSystem.getIsActive()) {
				logger.info("The system " + aSystem.getName()
						+ " is existed but is not active. "
						+ "The monitor skips this project now");
				continueCount++;

				continue;
			}
			logger.info("Project url: " + aSystem.getUrl());
			logger.info("Project name: " + aSystem.getName());

			// Initiates monitor and do task
			String running = "";
			try {
				urlMonitor = new URLMonitor();
				urlMonitor.setTimeStamp(timeStamp);
				obj = urlMonitor.generateInfo(aSystem);
				running = (obj == null) ? FAILED : RUNNING;
				logger.info("System '" + aSystem.getName() + "/" + "' is "
						+ running);

				// Adds systems to list
				objList.add(obj);
			} catch (MonitorException me) {
				try {
					// logger.info("Try to update project " + proj.getName()
					// + "'s status to false.");
					// running = FAILED;
					// updateProjectStatus(proj, false);
					obj = null;
				} catch (Exception e) {
					// logger.error("The monitoring failed, try to update"
					// + " project's status but not success");
				}
				// logger.error(me.getMessage());
			}
			if (running.equals(FAILED)) {
				errorCount++;
			} // if
		} // for
			// if ((projs != null) && (projs.size() > 0)) {
			// try {
			// if (errorCount == (projs.size() - continueCount)) {
		logger.info("Update node's status to false");
		// updateNode(node, false);
		// } else {
		logger.info("Update node's status to true");
		// updateNode(node, true);
		// }
		// } catch (Exception e) {
		// logger.error("Cannot update the status of node: "
		// + node.getName() + ", error: " + e.getMessage());
		// }
		// }
		// } // for
		// logger.info("Finished monitoring, object size: " + objList.size());

		return objList;
	}
}
