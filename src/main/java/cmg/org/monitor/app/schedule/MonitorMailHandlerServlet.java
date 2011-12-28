package cmg.org.monitor.app.schedule;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.Address;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cmg.org.monitor.dao.MailMonitorDAO;
import cmg.org.monitor.dao.impl.MailMonitorDaoImpl;
import cmg.org.monitor.entity.shared.MailMonitor;
import cmg.org.monitor.ext.util.MonitorUtil;
import cmg.org.monitor.services.email.MailService;

@SuppressWarnings("serial")
public class MonitorMailHandlerServlet extends HttpServlet {
	private static final Logger logger = Logger
			.getLogger(MonitorMailHandlerServlet.class.getName());

	/* private static SystemDto sys; */

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		doPost(req, resp);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		try {			
			// BEGIN LOG
			long start = System.currentTimeMillis();
			logger.log(Level.INFO, MonitorUtil.parseTime(start, true)
					+ " -> START: Receive mail ... ");
			// BEGIN LOG
			MailMonitorDAO mailDAO = new MailMonitorDaoImpl();
			MailMonitor mail = MailService.receiveMail(req.getInputStream());
			mailDAO.putMailMonitor(mail);			
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
