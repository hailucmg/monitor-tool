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
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;

import cmg.org.monitor.dao.AlertDao;
import cmg.org.monitor.dao.MailMonitorDAO;
import cmg.org.monitor.dao.SystemAccountDAO;
import cmg.org.monitor.dao.SystemDAO;
import cmg.org.monitor.dao.SystemGroupDAO;
import cmg.org.monitor.dao.UtilityDAO;
import cmg.org.monitor.dao.impl.AlertDaoImpl;
import cmg.org.monitor.dao.impl.MailMonitorDaoImpl;
import cmg.org.monitor.dao.impl.SystemAccountDaoImpl;
import cmg.org.monitor.dao.impl.SystemDaoImpl;
import cmg.org.monitor.dao.impl.SystemGroupDaoImpl;
import cmg.org.monitor.dao.impl.UtilityDaoImpl;
import cmg.org.monitor.entity.shared.AlertStoreMonitor;
import cmg.org.monitor.entity.shared.GoogleAccount;
import cmg.org.monitor.entity.shared.MailConfigMonitor;
import cmg.org.monitor.entity.shared.NotifyMonitor;
import cmg.org.monitor.entity.shared.SystemGroup;
import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.entity.shared.SystemUser;
import cmg.org.monitor.ext.model.shared.UserMonitor;
import cmg.org.monitor.ext.util.MailAsync;
import cmg.org.monitor.ext.util.MonitorUtil;
import cmg.org.monitor.services.email.MailService;
import cmg.org.monitor.util.shared.MonitorConstant;

public class MailServiceScheduler extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1934785013663672044L;

	private static final Logger logger = Logger
			.getLogger(MailServiceScheduler.class.getCanonicalName());

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		doPost(req, resp);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		doSchedule();
	}

	/**
	 * Do schedule.
	 */
	public void doSchedule() {
		// BEGIN LOG
		UtilityDAO utilDAO = new UtilityDaoImpl();
		long start = System.currentTimeMillis();
		logger.log(Level.INFO, MonitorUtil.parseTime(start, true)
				+ " -> START: Scheduled send alert mail ...");
		String currentZone = utilDAO.getCurrentTimeZone();
		DateTime dtStart = new DateTime(start);
		dtStart = dtStart.withZone(DateTimeZone.forID(currentZone));
		// BEGIN LOG
		String alertName = MonitorConstant.ALERTSTORE_DEFAULT_NAME + ": "
				+ dtStart.toString(DateTimeFormat.forPattern(MonitorConstant.SYSTEM_DATE_FORMAT));
		SystemAccountDAO accountDao = new SystemAccountDaoImpl();
		SystemDAO sysDAO = new SystemDaoImpl();
		AlertDao alertDAO = new AlertDaoImpl();
		ArrayList<SystemMonitor> systems = sysDAO
				.listSystemsFromMemcache(false);

		MailMonitorDAO mailDAO = new MailMonitorDaoImpl();
		
		SystemGroupDAO groupDao = new SystemGroupDaoImpl();
		if (systems != null && systems.size() > 0) {
			ArrayList<UserMonitor> listUsers = utilDAO.listAllUsers();
			if (listUsers != null && listUsers.size() > 0) {
				for (UserMonitor user : listUsers) {
					for (SystemMonitor sys : systems) {
						try {
							SystemGroup gr = groupDao.getByName(sys
									.getGroupEmail());
							if (gr != null) {
								if (user.checkGroup(gr.getId())) {
									user.addSystem(sys);
								}
							}
						} catch (Exception e) {
							logger.log(Level.WARNING,
									"Error: " + e.getMessage());
						}
					}
				}

				for (UserMonitor user : listUsers) {
					if (user.getSystems() != null
							&& user.getSystems().size() > 0) {
						for (Object tempSys : user.getSystems()) {
							AlertStoreMonitor alertstore = alertDAO
									.getLastestAlertStore((SystemMonitor) tempSys);
							if (alertstore != null) {
								alertstore.setName(alertName);
								alertstore.setTimeStamp(new Date(start));
								NotifyMonitor notify = null;
								try {
									notify = sysDAO
											.getNotifyOption(((SystemMonitor) tempSys)
													.getCode());
								} catch (Exception e) {
								}
								if (notify == null) {
									notify = new NotifyMonitor();
								}
								alertstore.fixAlertList(notify);
								if (alertstore.getAlerts().size() > 0) {
									user.addAlertStore(alertstore);
								}
							}
						}
					}
				}
			}
			List<GoogleAccount> googleAccs = null;
			try {
				googleAccs = accountDao.listAllGoogleAccount();
			} catch (Exception e1) {
				logger.log(Level.WARNING, "Error: " + e1.getMessage());
			}
			if (listUsers != null && listUsers.size() > 0 && googleAccs != null
					&& googleAccs.size() > 0) {
				for (UserMonitor user : listUsers) {
					if (user.getUser().getDomain()
							.equalsIgnoreCase(SystemUser.THIRD_PARTY_USER)) {
						if (user.getStores() != null
								&& user.getStores().size() > 0) {
							MailConfigMonitor config = mailDAO
									.getMailConfig(user.getId());
							MailService mailService = new MailService();
							try {
								String content = mailService.parseContent(
										user.getStores(), config);
								MailAsync mailUtil = new MailAsync(new String[] {user.getId()}, alertName, content);
								mailUtil.run();
								logger.log(Level.INFO, "send mail"
										+ content);
							} catch (Exception e) {
								logger.log(Level.INFO, "Can not send mail"
										+ e.getMessage());
							}

						}
					}

				}
				
				
				for (GoogleAccount gAcc : googleAccs) {
					MailService mailService = new MailService(gAcc);
					for (UserMonitor user : listUsers) {
						if (user.getUser().getDomain()
								.equalsIgnoreCase(gAcc.getDomain())) {
							if (user.getStores() != null
									&& user.getStores().size() > 0) {
								MailConfigMonitor config = mailDAO
										.getMailConfig(user.getId());
								try {
									String content = mailService.parseContent(
											user.getStores(), config);
									mailService.sendMail(alertName, content,
											config);
									logger.log(Level.INFO, "send mail"
											+ content);
								} catch (Exception e) {
									logger.log(Level.INFO, "Can not send mail"
											+ e.getMessage());
								}

							}
						}

					}
				}
				for (SystemMonitor sys : systems) {
					AlertStoreMonitor store = alertDAO
							.getLastestAlertStore(sys);
					alertDAO.putAlertStore(store);
					alertDAO.clearTempStore(sys);
				}

			}
			for (SystemMonitor sys : systems) {
				AlertStoreMonitor asm = alertDAO.getLastestAlertStore(sys);
				if (asm == null) {
					asm = new AlertStoreMonitor();
				}
				asm.setCpuUsage(sys.getLastestCpuUsage());
				asm.setMemUsage(sys.getLastestMemoryUsage());
				asm.setSysId(sys.getId());
				asm.setName(alertName);
				asm.setTimeStamp(new Date(start));
				alertDAO.putAlertStore(asm);
				alertDAO.clearTempStore(sys);
			}
		} else {
			logger.log(Level.INFO, "NO SYSTEM FOUND");
		}

		/*
		 * mailDAO.getMailConfig(maiId); mailService.sendMail(subject, content,
		 * mailConfig);
		 */

		// END LOG
		long end = System.currentTimeMillis();
		long time = end - start;
		logger.log(Level.INFO, MonitorUtil.parseTime(end, true)
				+ " -> END: Scheduled send alert mail. Time executed: " + time
				+ " ms");
		// END LOG

	}
}
