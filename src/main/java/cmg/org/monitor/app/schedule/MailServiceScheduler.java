package cmg.org.monitor.app.schedule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cmg.org.monitor.dao.AlertDao;
import cmg.org.monitor.dao.SystemDAO;
import cmg.org.monitor.dao.impl.AlertDaoImpl;
import cmg.org.monitor.dao.impl.SystemDaoImpl;
import cmg.org.monitor.entity.shared.AlertStoreMonitor;
import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.ext.util.MonitorUtil;
import cmg.org.monitor.services.MonitorService;

public class MailServiceScheduler extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1934785013663672044L;
	
	private static final Logger logger = Logger.getLogger(MailServiceScheduler.class
			.getCanonicalName());

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		doPost(req, resp);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		doSchedule();
	}
	
	private void doSchedule() {
		//BEGIN LOG
		long start = System.currentTimeMillis();
		logger.log(Level.INFO, MonitorUtil.parseTime(start, true)
				+ " -> START: Scheduled send alert mail ...");
		//BEGIN LOG
		
		SystemDAO sysDAO = new SystemDaoImpl();
		AlertDao alertDAO = new AlertDaoImpl();
		ArrayList<SystemMonitor> systems = sysDAO.listSystemsFromMemcache(false);
		if (systems != null && systems.size() > 0) {
			AlertStoreMonitor alertStore = alertDAO.getLastestAlertStore(systems.get(0));
		} else {
			logger.log(Level.INFO, "NO SYSTEM FOUND");
		}
		
		//END LOG
		long end = System.currentTimeMillis();
		long time = end - start;
		logger.log(Level.INFO, MonitorUtil.parseTime(end, true)
				+ " -> END: Scheduled send alert mail. Time executed: " + time + " ms");
		//END LOG
		
	}
}
