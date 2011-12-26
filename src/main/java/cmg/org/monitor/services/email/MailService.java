package cmg.org.monitor.services.email;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import cmg.org.monitor.common.Constant;
import cmg.org.monitor.dao.impl.AlertDaoImpl;
import cmg.org.monitor.entity.shared.MailConfigMonitor;
import cmg.org.monitor.ext.util.MonitorUtil;
import cmg.org.monitor.util.shared.MonitorConstant;

import com.google.gdata.client.appsforyourdomain.migration.MailItemService;
import com.google.gdata.data.appsforyourdomain.migration.Label;
import com.google.gdata.data.appsforyourdomain.migration.MailItemEntry;
import com.google.gdata.data.appsforyourdomain.migration.MailItemFeed;
import com.google.gdata.data.appsforyourdomain.migration.MailItemProperty;
import com.google.gdata.data.appsforyourdomain.migration.Rfc822Msg;
import com.google.gdata.data.batch.BatchStatus;
import com.google.gdata.data.batch.BatchUtils;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

/**
 * @author HongHai, LamPhan
 * @version 1.0
 */
public class MailService {
	public static final String DELIVERED_TO = "admin@c-mg.com";
	public static final String CONTENT_DISPOSITION = "inline";
	public static final String CONTENT_TRANSFER_ENCODING = "7bit";
	public static final String FORMAT = "flowed";
	public static final String CHARSET = "ISO-8859-1";
	public static final String CONTENT_TYPE = "text/html";
	public static final String MIME_VERSION = "1.0";
	public static final String MONITOR_USER_NAME = "Monitor User";
	public static final String MESSAGE_ID = "c8acb6980707161012i5d395392p5a6d8d14a8582613@mail.gmail.com";

	/** Get log instance */
	private static final Logger logger = Logger.getLogger(AlertDaoImpl.class
			.getName());

	private final MailItemService mailItemService;

	public MailService() {
		// Set up the mail item service.
		mailItemService = new MailItemService(MonitorConstant.SITES_APP_NAME);
		try {
			mailItemService.setUserCredentials(MonitorConstant.ADMIN_EMAIL,
					MonitorConstant.ADMIN_PASSWORD);
		} catch (AuthenticationException ae) {
			logger.log(Level.SEVERE,
					" -> ERROR: Sending mail ... AuthenticationException: "
							+ ae.getMessage());
		}
	}

	public boolean sendMail(String subject, String content,
			MailConfigMonitor mailConfig) {
		// BEGIN LOG
		long start = System.currentTimeMillis();
		logger.log(Level.INFO, MonitorUtil.parserTime(start, true)
				+ " -> START: send alert mail to " + mailConfig.getMailId());
		// BEGIN LOG
		boolean b = false;
		if (mailConfig != null) {
			// setup mail item
			MailItemEntry entry = new MailItemEntry();
			// add label
			entry.addLabel(new Label(mailConfig.getLabel()));
			// add property
			if (mailConfig.isInbox()) {
				entry.addMailProperty(MailItemProperty.INBOX);
			}
			if (mailConfig.isStarred()) {
				entry.addMailProperty(MailItemProperty.STARRED);
			}
			if (mailConfig.isMarkAsUnread()) {
				entry.addMailProperty(MailItemProperty.UNREAD);
			}
			// add content
			entry.setRfc822Msg(initRfcContent(subject, content, mailConfig));
			try {
				// send mail
				MailItemFeed feed = new MailItemFeed();
				feed.getEntries().add(entry);
				feed = mailItemService.batch(MonitorConstant.DOMAIN,
						mailConfig.getMailId(), feed);
				MailItemEntry returnedEntry = feed.getEntries().get(0);
				if (BatchUtils.isFailure(returnedEntry)) {
					BatchStatus status = BatchUtils
							.getBatchStatus(returnedEntry);
					logger.log(Level.SEVERE,
							"Entry " + BatchUtils.getBatchId(returnedEntry)
									+ " failed insertion: " + status.getCode()
									+ " " + status.getReason());
					b = false;
				} else {
					b = true;
				}

			} catch (IOException e) {
				logger.log(Level.SEVERE, "Caught IOException: " + e.toString());
			} catch (ServiceException e) {
				logger.log(Level.SEVERE,
						"Caught ServiceException: " + e.toString());
			}
		}// if
			// END LOG
		long end = System.currentTimeMillis();
		logger.log(Level.INFO, MonitorUtil.parserTime(end, true) + " -> END: "
				+ (b ? "Batch send mail succeeded" : "Cannot send email.")
				+ ". Time executed: " + (end - start) + " ms.");
		// END LOG
		return b;
	}

	public Rfc822Msg initRfcContent(String subject, String content,
			MailConfigMonitor mailConfig) {
		Date now = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat(
				Constant.DATE_EMAIL_FORMAT);
		StringBuffer tmp = new StringBuffer();
		tmp.append("Message-ID: <" + MESSAGE_ID + ">\r\n");
		tmp.append("Date: " + formatter.format(now) + "\r\n");
		tmp.append("From: \"" + MonitorConstant.PROJECT_NAME + "\" <"
				+ MonitorConstant.ALERT_MAIL_SENDER_NAME + ">\r\n");
		tmp.append("To: \"" + MONITOR_USER_NAME + "\" <"
				+ mailConfig.getMailId() + ">\r\n");
		tmp.append("Subject: " + subject + " \r\n");
		tmp.append("MIME-Version: " + MIME_VERSION + "\r\n");
		tmp.append("Content-Type: " + CONTENT_TYPE + "; charset=" + CHARSET
				+ "; format=" + FORMAT + "\r\n");
		tmp.append("Content-Transfer-Encoding: " + CONTENT_TRANSFER_ENCODING
				+ "\r\n");
		tmp.append("Content-Disposition: inline\r\n");
		tmp.append("Delivered-To: " + DELIVERED_TO + "\r\n");
		tmp.append(content + "\r\n\r\n");
		return new Rfc822Msg(tmp.toString());
	}

}
