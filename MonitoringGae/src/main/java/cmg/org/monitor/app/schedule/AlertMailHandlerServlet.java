package cmg.org.monitor.app.schedule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cmg.org.monitor.common.Constant;
import cmg.org.monitor.dao.MailMonitorDAO;
import cmg.org.monitor.dao.UtilityDAO;
import cmg.org.monitor.dao.impl.MailMonitorDaoImpl;
import cmg.org.monitor.dao.impl.UtilityDaoImpl;
import cmg.org.monitor.entity.shared.MailConfigMonitor;
import cmg.org.monitor.entity.shared.MailMonitor;
import cmg.org.monitor.ext.model.shared.UserMonitor;
import cmg.org.monitor.ext.util.MonitorUtil;
import cmg.org.monitor.services.email.MailService;

public class AlertMailHandlerServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7599950554394002744L;

	private static final Logger logger = Logger
			.getLogger(AlertMailHandlerServlet.class.getCanonicalName());

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
			
			MailMonitor mail = MailService.receiveMail(req.getInputStream());
			
			MailMonitorDAO mailDAO = new MailMonitorDaoImpl();
			UtilityDAO utilDAO = new UtilityDaoImpl();
			ArrayList<UserMonitor> users = utilDAO.listAllUsers();
			// check the sender of email.
			boolean check = false;
			for (UserMonitor user : users) {
				if (user.getId().equalsIgnoreCase(mail.getSender())) {
					check = true;
					break;
				}
			}
			
			if (check) {
				MailConfigMonitor mailConfig = mailDAO.getMailConfig(mail.getSender().toLowerCase().trim());
				String content = mail.getContent();	
				// START check Inbox
				Matcher matcher = Pattern.compile(Constant.PATTERN_MAIL_INBOX_ON).matcher(
						content.toLowerCase());
				if (matcher.find()) {
					if (mailConfig == null) {
						mailConfig = new MailConfigMonitor();
					}
					mailConfig.setInbox(true);
					logger.log(Level.INFO, "Change inbox to ON");
				}
				
				matcher = Pattern.compile(Constant.PATTERN_MAIL_INBOX_OFF).matcher(
						content.toLowerCase());
				if (matcher.find()) {
					if (mailConfig == null) {
						mailConfig = new MailConfigMonitor();
					}
					mailConfig.setInbox(false);
					logger.log(Level.INFO, "Change inbox to OFF");
				}				
				// END check Inbox
				
				// START check Starred
				matcher = Pattern.compile(Constant.PATTERN_MAIL_STARRED_ON).matcher(
						content.toLowerCase());
				if (matcher.find()) {
					if (mailConfig == null) {
						mailConfig = new MailConfigMonitor();
					}
					mailConfig.setStarred(true);
					logger.log(Level.INFO, "Change starred to ON");
				}
				
				matcher = Pattern.compile(Constant.PATTERN_MAIL_STARRED_OFF).matcher(
						content.toLowerCase());
				if (matcher.find()) {
					if (mailConfig == null) {
						mailConfig = new MailConfigMonitor();
					}
					mailConfig.setStarred(false);
					logger.log(Level.INFO, "Change starred to OFF");
				}
				// END check Starred
				
				//START check mark as unread
				matcher = Pattern.compile(Constant.PATTERN_MAIL_MARK_AS_UNREAD_ON).matcher(
						content.toLowerCase());
				if (matcher.find()) {
					if (mailConfig == null) {
						mailConfig = new MailConfigMonitor();
					}
					mailConfig.setMarkAsUnread(true);
					logger.log(Level.INFO, "Change mark as unread to ON");
				}
				
				matcher = Pattern.compile(Constant.PATTERN_MAIL_MARK_AS_UNREAD_OFF).matcher(
						content.toLowerCase());
				if (matcher.find()) {
					if (mailConfig == null) {
						mailConfig = new MailConfigMonitor();
					}
					mailConfig.setMarkAsUnread(false);
					logger.log(Level.INFO, "Change mark as unread to OFF");
				}
				// START check label
				matcher = Pattern.compile(Constant.PATTERN_MAIL_LABEL).matcher(
						content);
				if (matcher.find()) {
					if (mailConfig == null) {
						mailConfig = new MailConfigMonitor();
					}
					mailConfig.setLabel(matcher.group(5));
					logger.log(Level.INFO, "Change label to " + mailConfig.getLabel());
				}
				// END check label
				
				mailDAO.putMailConfig(mailConfig);
			}
			logger.log(Level.INFO, mail.toString());
			// END LOG
			long end = System.currentTimeMillis();
			logger.log(Level.INFO, MonitorUtil.parseTime(end, true)
					+ " -> END: Receive mail. Time executed: " + (end - start)
					+ " ms.");
			// END LOG
		} catch (Exception e) {
			logger.log(Level.WARNING,
					"Failure in receiving email : " + e.getMessage());
		}
	}
}
