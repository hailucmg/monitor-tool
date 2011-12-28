package cmg.org.monitor.app.schedule;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cmg.org.monitor.dao.MailMonitorDAO;
import cmg.org.monitor.dao.impl.MailMonitorDaoImpl;
import cmg.org.monitor.entity.shared.MailMonitor;
import cmg.org.monitor.ext.util.MonitorUtil;
import cmg.org.monitor.services.email.MailService;

public class AlertMailHandlerServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7599950554394002744L;
	
	private static final Logger logger = Logger.getLogger(AlertMailHandlerServlet.class
			.getCanonicalName());

	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException {
		try {			
			// BEGIN LOG
			long start = System.currentTimeMillis();
			logger.log(Level.INFO, MonitorUtil.parseTime(start, true)
					+ " -> START: Receive mail ... ");
			// BEGIN LOG
			MailMonitorDAO mailDAO = new MailMonitorDaoImpl();
			MailMonitor mail = MailService.receiveMail(req.getInputStream());
			logger.log(Level.INFO, mail.toString());			
			// END LOG
			long end = System.currentTimeMillis();
			logger.log(Level.INFO, MonitorUtil.parseTime(end, true)
					+ " -> END: Receive mail. Time executed: "
					+ (end - start) + " ms.");
			// END LOG
		} catch (Exception e) {
			logger.log(Level.WARNING,
					"Failure in receiving email : " + e.getMessage());
		}
	}
}
