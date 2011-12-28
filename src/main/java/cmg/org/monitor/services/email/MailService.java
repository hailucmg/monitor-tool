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
import java.util.Locale;
import java.util.Properties;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import cmg.org.monitor.common.Constant;
import cmg.org.monitor.dao.MailMonitorDAO;
import cmg.org.monitor.dao.SystemDAO;
import cmg.org.monitor.dao.impl.AlertDaoImpl;
import cmg.org.monitor.dao.impl.MailMonitorDaoImpl;
import cmg.org.monitor.dao.impl.SystemDaoImpl;
import cmg.org.monitor.entity.shared.AlertMonitor;
import cmg.org.monitor.entity.shared.AlertStoreMonitor;
import cmg.org.monitor.entity.shared.MailConfigMonitor;
import cmg.org.monitor.entity.shared.MailMonitor;
import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.ext.util.MonitorUtil;
import cmg.org.monitor.util.shared.HTMLControl;
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
				feed = mailItemService.batch(MonitorConstant.DOMAIN,
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
		tmp.append("Message-ID: <" + MESSAGE_ID + ">\r\n");
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
		tmp.append(content + "\r\n\r\n");
		return new Rfc822Msg(tmp.toString());
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
			Multipart mp = (Multipart) o;
			int count = mp.getCount();
			for (int i = 0; i < count; i++) {
				if (mp.getBodyPart(i).getContent() instanceof String) {
					Object obj = mp.getBodyPart(i).getContent();
					data = (String) obj;
				}
			}
		} else if (o instanceof InputStream) {
			InputStream is = (InputStream) o;
			try {
				data = convertInputStreamtoString(is);
			} catch (Exception e) {
				logger.log(Level.WARNING,
						" -> ERROR when get content mail - type txt/html. Message: "
								+ e.getMessage());
			}
		} else if (o instanceof String) {
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

	public String createMailContent(ArrayList<AlertStoreMonitor> stores) throws Exception {
		StringBuffer content =new StringBuffer();
		SystemDAO sysDAO = new SystemDaoImpl();
		content.append("<html><head></head><body><div align=\"center\" style=\"COLOR:black;FONT-SIZE:15pt\">Alert Email from C-MG monitor</div>");
		content.append("<div><ul>");
		for(int i =0;i < stores.size();i++){
			AlertStoreMonitor alertstore = stores.get(i);
			SystemMonitor system = sysDAO.getSystemById(alertstore.getSysId());
			ArrayList<AlertMonitor> alerts = (ArrayList<AlertMonitor>) alertstore.getAlerts();
			content.append("<li style=\"COLOR:blue;FONT-SIZE:12pt\">");
			content.append("<a href=\"\">");
			content.append(system.getCode());
			content.append("</a>");
			content.append("<Ol>");
			for(int j = 0; j < alerts.size();j++){
				content.append("<li style=\"COLOR:red;FONT-SIZE:10pt\">");
				content.append("Time:" + alerts.get(j).getTimeStamp().toString());
				content.append("<br>Error:" + alerts.get(j).getError().toString());
				content.append("<br>Detail:" + alerts.get(j).getDescription().toString());
				content.append("</li>");
			}
			content.append("</Ol>");
		}
		content.append("</ul></div>");
		content.append("<div><p style=\"COLOR:black;FONT-SIZE:15pt\"><b>(*)Note:Send me a email like below if you want to config email:</b></p>");
		content.append("<ul><li>inbox=true/false;(choose true if you want us send mail to your inbox else if you chose false our alert email will sent you to the label that name: alert monitor and you can config the label you want in this next step)</li>");
		content.append("<li>starred= true/false;(choose true if you want my mail is starred in your mail)</li>");
		content.append("<li>maskAsUnread=false/true;(choose true if you want my mail mark as unread)</li>");
		content.append("<li>label=alert monitor/or something;(give me a name you want our alert email going to,We will create automatic a space to store this)</li>");
		content.append("</ul></p></div>");
		content.append("<b>------------------------------<wbr>------------------------------<wbr>------------------------------<wbr>-----</b>");
		content.append("<p><b>Monitor C-MG</b></p>");
		content.append("<p>Monitor - Admin<br><span style=\"COLOR:black;FONT-SIZE:8.5pt\"></span></p>");
		content.append("<p><b><span style=\"FONT-FAMILY:'Courier New';COLOR:black\">Claybourne McGregor Consulting Ltd</span></b><span style=\"FONT-FAMILY:'Courier New';COLOR:black;FONT-SIZE:10pt\"></span></p>");
		content.append("<p><span style=\"COLOR:black;FONT-SIZE:8.5pt\">");
		content.append("<img border=\"0\" alt=\"cmg-logo-email\" src=\"\" width=\"255\" height=\"90\">");
		content.append("</span></p></body></html>");
		return content.toString();
	}
}
