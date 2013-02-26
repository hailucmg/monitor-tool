package cmg.org.monitor.app.schedule;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cmg.org.monitor.dao.UtilityDAO;
import cmg.org.monitor.dao.impl.UtilityDaoImpl;
import cmg.org.monitor.ext.util.MonitorUtil;
import cmg.org.monitor.services.SitesHelper;
import cmg.org.monitor.util.shared.MonitorConstant;

public class GatherScheduler extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8020875152319434942L;
	/** The log of application */
	private static final Logger logger = Logger.getLogger(GatherScheduler.class
			.getCanonicalName());

	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException {
		
		doSchedule();
	}

	void doSchedule() {
		try {
			// BEGIN LOG
			long start = System.currentTimeMillis();
			logger.log(Level.INFO, MonitorUtil.parseTime(start, true)
					+ " -> START: Scheduled gather system data ...");
			// BEGIN LOG

			UtilityDAO utilDAO = new UtilityDaoImpl();

			utilDAO.putAboutContent(SitesHelper
					.getSiteEntryContent(MonitorConstant.SITES_ABOUT_CONTENT_ID));

			utilDAO.putHelpContent(SitesHelper
					.getSiteEntryContent(MonitorConstant.SITES_HELP_CONTENT_ID));
			
			utilDAO.putGroups();
			
			utilDAO.putAllUsers();

			// END LOG
			long end = System.currentTimeMillis();
			long time = end - start;
			logger.log(Level.INFO, MonitorUtil.parseTime(end, true)
					+ " -> END: Scheduled gather system data. Time executed: "
					+ time + " ms");
			// END LOG
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.log(Level.SEVERE,
					" ->ERROR: When Scheduled gather system data. Message: "
							+ ex.getMessage());
		}
	}
}
