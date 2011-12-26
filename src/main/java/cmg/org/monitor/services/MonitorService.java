package cmg.org.monitor.services;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import cmg.org.monitor.dao.MailMonitorDAO;
import cmg.org.monitor.dao.SystemDAO;
import cmg.org.monitor.dao.impl.MailMonitorDaoImpl;
import cmg.org.monitor.dao.impl.SystemDaoImpl;
import cmg.org.monitor.entity.shared.MailMonitor;
import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.exception.MonitorException;
import cmg.org.monitor.ext.util.MonitorParser;
import cmg.org.monitor.ext.util.MonitorUtil;
import cmg.org.monitor.ext.util.URLMonitor;
import cmg.org.monitor.util.shared.MonitorConstant;

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
	public synchronized void monitor() throws MonitorException {
		SystemDAO systemDao = new SystemDaoImpl();
		ArrayList<SystemMonitor> systems = null;
		try {
			systems = systemDao.listSystems(false);
		} catch (Exception me) {
			logger.log(Level.INFO, " -> ERROR when list all system. Message: "
					+ me.getMessage());
		}
		if (systems != null && systems.size() > 0) {
			SystemMonitor tempSys = null;
			// loop all system
			for (SystemMonitor aSystem : systems) {
				if (!aSystem.isActive()) {
					logger.log(
							Level.INFO,
							MonitorUtil.parserTime(System.currentTimeMillis(),
									true)
									+ "The system "
									+ aSystem.toString()
									+ " is existed but is not active. "
									+ " The monitor skips this system now");
				} else {
					logger.log(
							Level.INFO,
							MonitorUtil.parserTime(System.currentTimeMillis(),
									true)
									+ "START monitoring system "
									+ aSystem.toString());
					logger.log(Level.INFO, "System url: " + aSystem.getUrl());
					logger.log(Level.INFO,
							"Remote url: " + aSystem.getRemoteUrl());

					try {
						String infoContent = null;
						if (MonitorConstant.SMTP_PROTOCOL.equals(aSystem
								.getProtocol())) {
							MailMonitorDAO mailDAO = new MailMonitorDaoImpl();
							MailMonitor mail = mailDAO.getMailMonitor(aSystem
									.getEmailRevice());
							if (mail != null) {
								infoContent = mail.getContent();
							}
						} else {
							URLMonitor urlMonitor = new URLMonitor();
							infoContent = urlMonitor.retrievesContent(aSystem
									.getRemoteUrl());
						}// if-else

						if (infoContent == null || infoContent.equals("")) {
							aSystem.setStatus(false);
							// TODO put alert to store
							logger.log(
									Level.WARNING,
									MonitorUtil.parserTime(
											System.currentTimeMillis(), true)
											+ "Fetch content of data from "
											+ aSystem.toString()
											+ ": NO CONTENT FOUND");
						} else {
							logger.log(
									Level.INFO,
									MonitorUtil.parserTime(
											System.currentTimeMillis(), true)
											+ "Fetch content of data from "
											+ aSystem.toString()
											+ ": \r\n"
											+ infoContent);
							MonitorParser parser = new MonitorParser();
							aSystem = parser.parserData(infoContent, aSystem);
						}// if-else

					} catch (Exception e) {
						aSystem.setStatus(false);
						// TODO put alert to store
						logger.log(Level.INFO, " ->ERROR: when revice content"
								+ e.getMessage());
					}
					
				}// if-else
				try {
					systemDao.updateSystem(aSystem);
				} catch (Exception ex) {
					logger.log(
							Level.SEVERE,
							" -> ERROR: cannot update system. Message: "
									+ ex.getMessage());
				}
			}// for
		} else {
			logger.log(Level.INFO,
					MonitorUtil.parserTime(System.currentTimeMillis(), true)
							+ " -> END Monitoring: No system found!");
		}
	}
}
