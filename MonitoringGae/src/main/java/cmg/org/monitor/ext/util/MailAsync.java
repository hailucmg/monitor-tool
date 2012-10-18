/**
 * Copyright (c) CMG Ltd All rights reserved.
 *
 * This software is the confidential and proprietary information of CMG
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with CMG.
 */

package cmg.org.monitor.ext.util;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import cmg.org.monitor.util.shared.MonitorConstant;

/**
 * DOCME
 * 
 * @Creator Hai Lu
 * @author $Author$
 * @version $Revision$
 * @Last changed: $LastChangedDate$
 */

public class MailAsync extends Thread {
	private static final Logger logger = Logger.getLogger(MailAsync.class
			.getName());

	
	public static String getInviteMailSubject() {
		return "Monitor Invited Request";
	}
	
	public static String getInviteMailContent() {
		return "";
	}
	private String[] recipients;
	private String subject;
	private String body;
	public MailAsync(String[] recipients, String subject, String body) {
		this.setRecipients(recipients);
		this.setSubject(subject);
		this.setBody(body);
	}
	
	
	@Override
	public void run() {
		send(recipients, subject, body);
	}
	/**
	 * 
	 * @param recipients
	 * @param subject
	 * @param body
	 * @return the log
	 */
	public String send(String[] recipients, String subject, String body) {		
		if (recipients == null || recipients.length == 0) {
			return "No recipient found.";
		}
		long start = System.currentTimeMillis();
		StringBuffer sb = new StringBuffer();
		Properties props = new Properties();
		if (MonitorConstant.DEBUG) {
			props.put("mail.smtp.host", "smtp.gmail.com");
			props.put("mail.smtp.socketFactory.port", "465");
			props.put("mail.smtp.socketFactory.class",
			"javax.net.ssl.SSLSocketFactory");
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.port", "465");			
		}
		
		Session session = Session.getDefaultInstance(props, null);
		int problem = 0;
		Message msg = new MimeMessage(session);
		try {
			msg.setFrom(new InternetAddress(
					MonitorConstant.ALERT_MAIL_SENDER_NAME));
		} catch (Exception ex) {
			problem++;
			logger.log(Level.SEVERE,
					"Cannot not set sender. Message: " + ex.getMessage());			
		}
		for (String rec : recipients) {
			sb.append("\nAdd recipient " + rec + " ... ");
			try {			
				msg.addRecipient(Message.RecipientType.TO, new InternetAddress(
						rec));				
				sb.append("DONE.");
			} catch (Exception ex) {
				problem++;
				sb.append("FAIL. Message: "  + ex.getMessage());
			}
		}
		sb.append("\n" + "Start sendmail ...");
		try {
			
			msg.setSubject(subject);
			msg.setText(body);
			if (MonitorConstant.DEBUG) {
				Transport transport = session.getTransport("smtp");
				transport.connect("smtp.gmail.com", MonitorConstant.SITES_USERNAME, MonitorConstant.SITES_PASSWORD);
				transport.sendMessage(msg, msg.getAllRecipients());
				transport.close();
			} else {
				Transport.send(msg);
			}
			sb.append("DONE.");
		} catch (Exception ex) {
			problem++;
			logger.log(Level.SEVERE, "Cannot sendmail. Message: " + ex.getMessage());
			sb.append("FAIL. Message: " + ex.getMessage());
		} finally {
			long end = System.currentTimeMillis();
			long total = end - start;
			sb.append("\n" + "Sendmail completed with " + problem
				+ " problem"+(problem > 1 ? "s" : "")+". Time executed: " + total + " ms");
		}
		System.out.println(sb.toString());
		return sb.toString();
	}

	/** 
	 * @return the recipients 
	 */
	public String[] getRecipients() {
		return recipients;
	}

	/** 
	 * @param recipients the recipients to set 
	 */
	
	public void setRecipients(String[] recipients) {
		this.recipients = recipients;
	}

	/** 
	 * @return the subject 
	 */
	public String getSubject() {
		return subject;
	}

	/** 
	 * @param subject the subject to set 
	 */
	
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/** 
	 * @return the body 
	 */
	public String getBody() {
		return body;
	}

	/** 
	 * @param body the body to set 
	 */
	
	public void setBody(String body) {
		this.body = body;
	}
}
