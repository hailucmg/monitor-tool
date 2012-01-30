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

public class TokenScheduler extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -441314521680111158L;

	private static final Logger logger = Logger.getLogger(TokenScheduler.class
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
			long start = System.currentTimeMillis();
			logger.log(Level.INFO, MonitorUtil.parseTime(start, true)
					+ " -> START: Scheduled token begin ...");
			// BEGIN LOG
			UtilityDAO uDAO = new UtilityDaoImpl();
			uDAO.putTokenMail();
			uDAO.putTokenSite();
			// END LOG
			long end = System.currentTimeMillis();
			long time = end - start;
			logger.log(Level.INFO, MonitorUtil.parseTime(end, true)
					+ " -> END: Scheduled token. Time executed: " + time
					+ " ms");
			// END LOG
		} catch (Exception ex) {
			logger.log(
					Level.SEVERE,
					" ->ERROR: When Scheduled token. Message: "
							+ ex.getMessage());
		}
	}

}
