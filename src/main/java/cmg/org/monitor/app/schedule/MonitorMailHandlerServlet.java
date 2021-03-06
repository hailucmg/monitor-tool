/*
 * Copyright (c) CMG Ltd All rights reserved.
 *
 * This software is the confidential and proprietary information of CMG
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with CMG.
 */
package cmg.org.monitor.app.schedule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cmg.org.monitor.dao.MailMonitorDAO;
import cmg.org.monitor.dao.UtilityDAO;
import cmg.org.monitor.dao.impl.MailMonitorDaoImpl;
import cmg.org.monitor.dao.impl.UtilityDaoImpl;
import cmg.org.monitor.entity.shared.MailConfigMonitor;
import cmg.org.monitor.entity.shared.MailMonitor;
import cmg.org.monitor.ext.model.shared.UserMonitor;
import cmg.org.monitor.ext.util.MonitorUtil;
import cmg.org.monitor.services.email.MailService;
import cmg.org.monitor.util.shared.Constant;

/**
 * The Class MonitorMailHandlerServlet.
 */
@SuppressWarnings("serial")
public class MonitorMailHandlerServlet extends HttpServlet {
	
	/** The Constant logger. */
	private static final Logger logger = Logger
			.getLogger(MonitorMailHandlerServlet.class.getName());

	/* private static SystemDto sys; */

	/**
	 * (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse) 
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		doPost(req, resp);
	}

	/**
	 * (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse) 
	 */
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
			logger.log(Level.INFO, mail.toString());
			mailDAO.putMailMonitor(mail);	
			
			UtilityDAO utilDAO = new UtilityDaoImpl();
			ArrayList<UserMonitor> users = utilDAO.listAllUsers();
			// check the sender of email.
			logger.log(Level.INFO, " START check mail content for MailConfigMonitor");
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
			
			// END LOG
			long end = System.currentTimeMillis();
			logger.log(Level.INFO, MonitorUtil.parseTime(end, true)
					+ " -> END: Receive mail. Time executed: "
					+ (end - start) + " ms.");
			// END LOG
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(Level.WARNING,
					"Failure in receiving email : " + e.getMessage());
		}
	}

	

}
