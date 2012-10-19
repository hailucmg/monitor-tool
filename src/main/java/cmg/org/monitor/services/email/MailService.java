package cmg.org.monitor.services.email;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import cmg.org.monitor.dao.InviteUserDAO;
import cmg.org.monitor.dao.SystemAccountDAO;
import cmg.org.monitor.dao.SystemDAO;
import cmg.org.monitor.dao.impl.AlertDaoImpl;
import cmg.org.monitor.dao.impl.InviteUserDaoImpl;
import cmg.org.monitor.dao.impl.SystemAccountDaoImpl;
import cmg.org.monitor.dao.impl.SystemDaoImpl;
import cmg.org.monitor.entity.shared.AlertMonitor;
import cmg.org.monitor.entity.shared.AlertStoreMonitor;
import cmg.org.monitor.entity.shared.GoogleAccount;
import cmg.org.monitor.entity.shared.InvitedUser;
import cmg.org.monitor.entity.shared.MailConfigMonitor;
import cmg.org.monitor.entity.shared.MailMonitor;
import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.entity.shared.SystemRole;
import cmg.org.monitor.entity.shared.SystemUser;
import cmg.org.monitor.ext.util.MailAsync;
import cmg.org.monitor.ext.util.MonitorUtil;
import cmg.org.monitor.memcache.Key;
import cmg.org.monitor.memcache.MonitorMemcache;
import cmg.org.monitor.services.GoogleAccountService;
import cmg.org.monitor.util.shared.Constant;
import cmg.org.monitor.util.shared.HTMLControl;
import cmg.org.monitor.util.shared.MonitorConstant;
import cmg.org.monitor.util.shared.SecurityUtil;

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
	public static final String CHARSET = "UTF-8";
	public static final String CONTENT_TYPE = "text/html";
	public static final String MIME_VERSION = "1.0";
	public static final String MONITOR_USER_NAME = "Monitor User";
	public static final String MESSAGE_ID = "c8acb6980707161012i5d395392p5a6d8d14a8582613@mail.gmail.com";

	/** Get log instance */
	private static final Logger logger = Logger.getLogger(AlertDaoImpl.class
			.getName());

	private MailItemService mailItemService;

	GoogleAccount adminAcc;

	public MailService() {

	}

	public MailService(GoogleAccount acc) {
		adminAcc = acc;
		mailItemService = new MailItemService(
				GoogleAccountService.APPLICATION_NAME);
		if (adminAcc.getToken() != null && adminAcc.getToken().length() > 0) {
			mailItemService.setUserToken(acc.getToken());
		} else {
			try {
				mailItemService.setUserCredentials(adminAcc.getUsername() + "@"
						+ adminAcc.getDomain(),
						SecurityUtil.decrypt(adminAcc.getPassword()));
			} catch (AuthenticationException ae) {
				logger.log(Level.SEVERE,
						" -> ERROR: Sending mail ... AuthenticationException: "
								+ ae.getMessage());
			}
		}

	}

	public boolean sendMail(String subject, String content,
			MailConfigMonitor mailConfig) {
		// BEGIN LOG
		long start = System.currentTimeMillis();
		logger.log(Level.INFO, MonitorUtil.parseTime(start, true)
				+ " -> START: send alert mail to " + mailConfig.getMailId(true));
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
				feed = mailItemService.batch(adminAcc.getDomain(),
						mailConfig.getMailId(false), feed);
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
		logger.log(Level.INFO, MonitorUtil.parseTime(end, true) + " -> END: "
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
		tmp.append("Message-ID: <>\r\n");
		tmp.append("Date: " + formatter.format(now) + "\r\n");
		tmp.append("From: \"" + MonitorConstant.PROJECT_NAME + "\" <"
				+ MonitorConstant.ALERT_MAIL_SENDER_NAME + ">\r\n");
		tmp.append("To: \"" + MONITOR_USER_NAME + "\" <"
				+ mailConfig.getMailId(true) + ">\r\n");
		tmp.append("Subject: " + subject + " \r\n");
		tmp.append("MIME-Version: " + MIME_VERSION + "\r\n");
		tmp.append("Content-Type: " + CONTENT_TYPE + "; charset=" + CHARSET
				+ "; format=" + FORMAT + "\r\n");
		tmp.append("Content-Transfer-Encoding: " + CONTENT_TRANSFER_ENCODING
				+ "\r\n");
		tmp.append("Content-Disposition: inline\r\n");
		tmp.append("Delivered-To: " + DELIVERED_TO + "\r\n");
		tmp.append("\r\n");
		tmp.append(content + "\r\n");
		tmp.append("\r\n");
		String contentOut = tmp.toString();
		String randomFactor = Integer.toString(100000 + (new Random())
				.nextInt(900000));
		contentOut = contentOut.replace("Message-ID: <", "Message-ID: <"
				+ randomFactor);

		return new Rfc822Msg(contentOut);
	}

	public static MailMonitor receiveMail(InputStream is)
			throws MessagingException, IOException {
		MailMonitor mail = new MailMonitor();
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);
		MimeMessage message = new MimeMessage(session, is);
		Address[] add = message.getFrom();
		InternetAddress from = (InternetAddress) add[0];
		mail.setSender(from.getAddress());
		logger.log(Level.INFO, "From:" + mail.getSender());

		mail.setSubject(message.getSubject());
		logger.log(Level.INFO, "Subject: " + mail.getSubject());

		mail.setContentType(message.getContentType());
		logger.log(Level.INFO, "Content Type: " + mail.getContentType());

		mail.setContent(MailService.parseMailContent(message.getContent()));
		logger.log(Level.INFO, "Content: "
				+ (mail.getContent() == null ? "null" : mail.getContent()));
		return mail;
	}

	public static String parseMailContent(Object o) throws MessagingException,
			IOException {

		String data = null;
		if (o instanceof Multipart) {
			logger.log(Level.INFO, "Parse mail. Object instanceof Multipart.");
			Multipart mp = (Multipart) o;
			int count = mp.getCount();
			for (int i = 0; i < count; i++) {
				if (mp.getBodyPart(i).getContent() instanceof String) {
					Object obj = mp.getBodyPart(i).getContent();
					data = (String) obj;
				}
			}
		} else if (o instanceof InputStream) {
			logger.log(Level.INFO, "Parse mail. Object instanceof InputStream.");
			InputStream is = (InputStream) o;
			try {
				data = convertInputStreamtoString(is);
			} catch (Exception e) {
				logger.log(Level.WARNING,
						" -> ERROR when get content mail - type txt/html. Message: "
								+ e.getMessage());
			}
		} else if (o instanceof String) {
			logger.log(Level.INFO, "Parse mail. Object instanceof String.");
			data = (String) o;
		}
		return data;
	}

	/**
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public static String convertInputStreamtoString(InputStream is)
			throws IOException {
		if (is != null) {
			Writer write = new StringWriter();
			char[] buffer = new char[1024];
			try {
				Reader reader = new BufferedReader(new InputStreamReader(is,
						"UTF-8"));
				int n;
				while ((n = reader.read(buffer)) != -1) {
					write.write(buffer, 0, n);
				}
			} finally {
				is.close();
			}
			return write.toString();
		} else {
			return null;
		}
	}

	public String parseContent(ArrayList<AlertStoreMonitor> stores,
			MailConfigMonitor mailConfig) {
		SystemDAO sysDao = new SystemDaoImpl();
		SystemMonitor sys = null;

		StringBuffer sb = new StringBuffer();
		sb.append("<p><img src=\"http://"
				+ MonitorConstant.PROJECT_HOST_NAME
				+ "/images/logo/c-mg_logo.png\" width=\"200px\" height=\"80px\"/></p>");
		sb.append("<ol style=\"margin: 0 0 0 -15px; padding: 0;color: #686868;\">");
		for (AlertStoreMonitor store : stores) {
			try {
				sys = sysDao.getSystemById(store.getSysId());
			} catch (Exception e) {
				logger.log(Level.SEVERE, " ERROR when get system by id.");
				return null;
			}
			sb.append("<li><h4><a style=\"color: #686868;\" href=\"http://"
					+ MonitorConstant.PROJECT_HOST_NAME + "/Index.html"
					+ HTMLControl.HTML_SYSTEM_STATISTIC_NAME + "/"
					+ sys.getId() + "\" >" + sys + "</a></h4>");
			sb.append("<p><a href=\"http://"
					+ MonitorConstant.PROJECT_HOST_NAME + "/Index.html"
					+ HTMLControl.HTML_SYSTEM_DETAIL_NAME + "/" + sys.getId()
					+ "\" title=\"View details.\">");

			sb.append("<img src=\"http://"
					+ MonitorConstant.PROJECT_HOST_NAME
					+ "/images/icon/details.png\" title=\"View details.\" alt=\"View details.\"/></a>");
			sb.append("&nbsp;&nbsp;");
			sb.append("<a href=\"http://" + MonitorConstant.PROJECT_HOST_NAME
					+ "/Index.html" + HTMLControl.HTML_SYSTEM_STATISTIC_NAME
					+ "/" + sys.getId() + "\" title=\"View statistic.\">");
			sb.append("<img src=\"http://"
					+ MonitorConstant.PROJECT_HOST_NAME
					+ "/images/icon/statistic.png\"  title=\"View statistic.\" alt=\"View statistic.\"/></a></p>");

			String mes = "";
			if (sys.getHealthStatus().equals(SystemMonitor.STATUS_DEAD)) {
				mes = "System is not working.";
			} else if (sys.getHealthStatus().equals(SystemMonitor.STATUS_BORED)) {
				mes = "Insufficient data.";
			} else if (sys.getHealthStatus().equals(SystemMonitor.STATUS_SMILE)) {
				mes = "All is working correctly.";
			}

			sb.append("<table style=\"margin: 0; padding: 0;\"><tr><td style=\"background: #F1F1F1; color: #686868; font-weight: bold; height: 24px; font-size: 12px; padding: 0 10px 0 2px;\">Current Health Status: </td> ");
			sb.append("<td style=\"color: #686868; font-weight: bold; height: 24px; font-size: 12px;\"><img src=\"http://"
					+ MonitorConstant.PROJECT_HOST_NAME
					+ "/images/icon/"
					+ sys.getHealthStatus()
					+ "_status_icon.png\" title=\""
					+ mes + "\" alt=\"" + mes + "\"/></td></tr>");
			if (sys.getProtocol().equals(MonitorConstant.HTTP_PROTOCOL)) {
				sb.append("<tr><td style=\"background: #F1F1F1; color: #686868; font-weight: bold; height: 24px; font-size: 12px; padding: 0 10px 0 2px;\">Remote URL: </td><td style=\"color: #686868; font-weight: bold; height: 24px; font-size: 12px;\">"
						+ sys.getRemoteUrl() + "</td></tr>");
			} else {
				sb.append("<tr><td style=\"background: #F1F1F1; color: #686868; font-weight: bold; height: 24px; font-size: 12px; padding: 0 10px 0 2px;\">Remote Email: </td><td style=\"color: #686868; font-weight: bold; height: 24px; font-size: 12px;\"> "
						+ sys.getEmailRevice() + "</td></tr>");
			}
			sb.append("<tr><td style=\"background: #F1F1F1; color: #686868; font-weight: bold; height: 24px; font-size: 12px; padding: 0 10px 0 2px;\">IP Address: </td><td style=\"color: #686868; font-weight: bold; height: 24px; font-size: 12px;\"> "
					+ sys.getIp() + "</td></tr></table><br/>");
			sb.append("<table  style=\"margin: 0; padding: 0;width: 100%;\"><tbody><tr style=\"background: #868484;color: #F1F1F1;text-align: center;\"><th style=\"width: 30px;\">No.</th><th style=\"width: 70px;\">Time</th><th style=\"width: 50px;\">Severity</th><th>Title</th><th>Description</th></tr>");
			ArrayList<AlertMonitor> alerts = store.getAlerts();
			if (alerts != null && alerts.size() > 0) {
				for (int i = 0; i < alerts.size(); i++) {
					sb.append("<tr "
							+ (i % 2 == 0 ? ""
									: "style=\"background:#f1f1f1;\"")
							+ "><td style=\"padding:5px;color: #686868;\">"
							+ (i + 1) + "</td>");
					sb.append("<td style=\"padding:5px;color: #686868;\">"
							+ MonitorUtil.parseTimeEmail(alerts.get(i)
									.getTimeStamp()) + "</td>");
					if (alerts.get(i).getType() == AlertMonitor.CANNOT_GATHER_DATA
							|| alerts.get(i).getType() == AlertMonitor.SERVICE_ERROR_STATUS) {
						sb.append("<td style=\"padding:5px;\"><span style=\"font-weight: bold;color: red;\">Critical</span></td>");
					} else {
						sb.append("<td style=\"padding:5px;color: #686868;\">High</td>");
					}

					sb.append("<td style=\"padding:5px;color: #686868;\">"
							+ alerts.get(i).getError() + "</td>");
					sb.append("<td style=\"padding:5px;color: #686868;\">"
							+ alerts.get(i).getDescription() + "</td></tr>");
				}
			}// if
			sb.append("</tbody></table><hr/></li>");
		}// for

		sb.append("</ol><p style=\"font-size: 0.9em;color: #686868;\">You will continue to receive regular notification alerts until the issue is resolved (default is set to 30 minutes), we recommend creating a filter in your email client to capture all alerts into one folder for easy access.</p>");
		sb.append("<p style=\"font-size: 0.9em;color: #686868;\">");
		sb.append("inbox : " + (mailConfig.isInbox() ? "on" : "off") + "<br/>");
		sb.append("starred : " + (mailConfig.isStarred() ? "on" : "off")
				+ "<br/>");
		sb.append("markAsUnread : "
				+ (mailConfig.isMarkAsUnread() ? "on" : "off") + "<br/>");
		sb.append("label : \"" + mailConfig.getLabel() + "\"<br/>");
		sb.append("</p>");
		sb.append("<p style=\"font-size: 0.9em;color: #686868;\">--------------------------------------------------------------------------------</p>");
		sb.append("<p style=\"color: #686868;\"><i>Thanks and Best Regards</i><br/><br/>");
		sb.append("<b>ADMIN-MONITOR</b></p>");
		return sb.toString();
	}

	@Deprecated
	public static String createMailContent(ArrayList<AlertStoreMonitor> stores)
			throws Exception {
		String content = "";
		SystemDAO sysDAO = new SystemDaoImpl();
		content += "<html><HEAD align=\"center\">ALERT MAIL<HEAD><body>";
		content += "<OL>";
		for (int i = 0; i < stores.size(); i++) {
			AlertStoreMonitor alertstore = stores.get(i);
			SystemMonitor system = sysDAO.getSystemById(alertstore.getSysId());
			ArrayList<AlertMonitor> alerts = (ArrayList<AlertMonitor>) alertstore
					.getAlerts();
			content += "<LI>";
			content += HTMLControl.getLinkSystemStatistic(system);
			for (int j = 0; j < alerts.size(); j++) {
				if (j > 0) {
					content += "<br>";
				}
				content += "<UL>Time "
						+ MonitorUtil.parseTimeEmail(alerts.get(j)
								.getTimeStamp()) + "</UL>";
				content += "<UL>Error " + alerts.get(j).getError() + "</UL>";
				content += "<UL>Detail " + alerts.get(j).getDescription()
						+ "</UL>";

			}
			content += "</LI>";
		}

		content += "</OL>";
		content += "<p>(*)Send me a mail to config our alert email coming to your inbox like below</p>";
		content += "<p>inbox value - on|off;<br>";
		content += "starred  value - on|off;<br>";
		content += "markAsUnread value - on|off;<br>";
		content += "label value - Monitor Alert; </p>";
		content += "<p><or><li><i>With the choosen of inbox you can choose on or off if you want or don't our alert email sending to your inbox !  </i></li>";
		content += "<li><i>With the choosen of starred you can choose on or off if you want or don't alert email is starred in your mail !  </i></li>";
		content += "<li><i>With the choosen of markAsUnread you can choose on or off if you want or don't alert email is marked !  </i></li>";
		content += "<li><i>With the choosen of label,you can create any thing to set up a label that our alert email sending to this!</i></li></or></p>";
		content += "<p>----------------------------------------------------------------------------------------------------------- </p>";
		content += "<p><i>Thank and Best Regard</i></p>";
		content += "<p><strong> ADMIN-MONITOR</strong></p>";
		content += "<p><img src=\"" + MonitorConstant.IMAGES_FOR_EMAIL
				+ "\" width=\"255\" height=\"90\" /> </p>";
		content += "</body></html>";
		return content;
	}

	public boolean requestPermission(String mail, String firstName,
			String lastName, String description) {
		boolean check = false;
		InviteUserDAO userDao = new InviteUserDaoImpl();
		SystemAccountDAO accountDao = new SystemAccountDaoImpl();
		InvitedUser user = new InvitedUser();
		user.setEmail(mail);
		user.setFirstName(firstName);
		user.setLastName(lastName);		
		user.setStatus(InvitedUser.STATUS_REQUESTING);
		try {
			check = userDao.create3rdUser(user);			
		} catch (Exception ex) {
			logger.log(
					Level.SEVERE,
					"Error when requestPermission. Message: "
							+ ex.getMessage());
		}
		List<SystemUser> listActiveUser = null;
		List<String> adminEmails = new ArrayList<String>();
		try {
			listActiveUser = accountDao.listAllSystemUser(false);
			for (SystemUser u : listActiveUser) {
				if (u.checkRole(SystemRole.ROLE_ADMINISTRATOR)) {
					adminEmails.add(u.getEmail());
				}
			}
		} catch (Exception e) {
			//
		}
		if (adminEmails != null && adminEmails.s)
		MailAsync mailUtil = new MailAsync(adminEmails, "Request Permission", "");
		return check;
	}

	public boolean inviteUsers(String[] recipients, String[] groupIDs) {
		boolean check = false;
		if (recipients != null && recipients.length > 0) {
			InviteUserDAO userDao = new InviteUserDaoImpl();
			SystemAccountDAO accountDao = new SystemAccountDaoImpl();
			List<SystemUser> listActiveUser = null;
			try {
				listActiveUser = accountDao.listAllSystemUser(false);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			List<String> temp = new ArrayList<String>();
			for (String rec : recipients) {
				rec = rec.trim();
				boolean isValid = true;
				if (listActiveUser != null && listActiveUser.size() > 0) {
					for (SystemUser sUser : listActiveUser) {
						if (sUser.getEmail().trim().equalsIgnoreCase(rec)) {
							isValid = false;
							break;
						}
					}
				}
				if (isValid) {
					InvitedUser user = new InvitedUser();
					user.setEmail(rec.trim());
					user.setGroupIDs(groupIDs);
					user.setStatus(InvitedUser.STATUS_PENDING);
					try {
						userDao.create3rdUser(user);
						temp.add(rec.trim());
					} catch (Exception ex) {
						logger.log(
								Level.SEVERE,
								"Error when inviteUsers. Message: "
										+ ex.getMessage());
					}
				}
			}
			if (!temp.isEmpty()) {
				String[] newList = new String[temp.size()];
				temp.toArray(newList);
				MailAsync mailUtil = new MailAsync(newList,
						MailAsync.getInviteMailSubject(),
						MailAsync.getInviteMailContent());
				mailUtil.run();
				check = true;
				userDao.initList3rdUser();
			}

		}
		return check;
	}

}
