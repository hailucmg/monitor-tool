/**
 * Copyright (c) CMG Ltd All rights reserved.
 *
 * This software is the confidential and proprietary information of CMG
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with CMG.
 */

package cmg.org.monitor.ext.util;

import java.util.List;
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

public class MailUtil {
	private static final Logger logger = Logger.getLogger(MailUtil.class
			.getName());

	
	public static String getInviteMailSubject() {
		return "Monitor Invited Request";
	}
	
	public static String getInviteMailContent() {
		return "";
	}
	
	/**
	 * 
	 * @param recipients
	 * @param subject
	 * @param body
	 * @return the log
	 */
	public static String send(List<String> recipients, String subject, String body) {		
		if (recipients == null || recipients.size() == 0) {
			return "No recipient found.";
		}
		long start = System.currentTimeMillis();
		StringBuffer sb = new StringBuffer();
		Properties props = new Properties();
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
			Transport.send(msg);
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
		return sb.toString();
	}
}
