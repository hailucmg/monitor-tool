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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cmg.org.monitor.dao.InviteUserDAO;
import cmg.org.monitor.dao.SystemAccountDAO;
import cmg.org.monitor.dao.impl.InviteUserDaoImpl;
import cmg.org.monitor.dao.impl.SystemAccountDaoImpl;
import cmg.org.monitor.entity.shared.InvitedUser;
import cmg.org.monitor.entity.shared.SystemRole;
import cmg.org.monitor.entity.shared.SystemUser;
import cmg.org.monitor.ext.util.MonitorUtil;
import cmg.org.monitor.services.MonitorLoginService;

public class GatherScheduler extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8020875152319434942L;
	/** The log of application */
	private static final Logger logger = Logger.getLogger(GatherScheduler.class
			.getCanonicalName());

	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException {

		doSchedule();
	}

	/**
	 * Do schedule.
	 */
	void doSchedule() {
		try {
			// BEGIN LOG
			long start = System.currentTimeMillis();
			logger.log(Level.INFO, MonitorUtil.parseTime(start, true)
					+ " -> START: Scheduled gather system data ...");
			// BEGIN LOG
			SystemAccountDAO accountDao = new SystemAccountDaoImpl();
			InviteUserDAO userDao = new InviteUserDaoImpl();
			logger.log(Level.INFO, MonitorUtil.parseTime(start, true)
					+ " -> init DAO");
			if (MonitorLoginService.temp3rdUsers != null) {
				synchronized (MonitorLoginService.temp3rdUsers) {
					logger.log(Level.INFO, MonitorUtil.parseTime(start, true)
							+ " -> sync list");
					if (MonitorLoginService.temp3rdUsers.size() > 0) {
						logger.log(Level.INFO, MonitorUtil.parseTime(start, true)
								+ " -> init temp list");
						List<InvitedUser> inviteList = new ArrayList<InvitedUser>();
						for (Object u : MonitorLoginService.temp3rdUsers) {
							boolean check = false;
							InvitedUser temp = (InvitedUser) u;
							for (InvitedUser iu : inviteList) {
								if (iu.getEmail().equalsIgnoreCase(
										temp.getEmail())) {
									check = true;
									break;
								}
							}
							if (!check) {
								inviteList.add(temp);
							}
						}
						logger.log(Level.INFO, MonitorUtil.parseTime(start, true)
								+ " -> start check");
						if (!inviteList.isEmpty()) {
							List<SystemUser> listUsers = accountDao
									.listAllSystemUser(false);

							List<InvitedUser> list = userDao
									.list3rdUserFromMemcache();
							logger.log(Level.INFO, MonitorUtil.parseTime(start, true)
									+ " -> init list system user, 3rd User");
							for (InvitedUser iu : inviteList) {
								boolean check = false;
								if (listUsers != null && listUsers.size() > 0) {
									for (SystemUser u : listUsers) {
										if (u.getEmail().equalsIgnoreCase(
												iu.getEmail())) {
											check = true;
											break;
										}
									}
								}
								if (!check) {
									logger.log(Level.INFO, MonitorUtil.parseTime(start, true)
											+ " -> start check invite list");
									if (list != null && list.size() > 0) {
										for (InvitedUser u : list) {
											if (u.getEmail().equalsIgnoreCase(
													iu.getEmail())) {
												u.setStatus(InvitedUser.STATUS_ACTIVE);
												SystemUser user = new SystemUser();
												user.setFirstName(u
														.getFirstName());
												user.setLastName(u
														.getLastName());
												user.setUsername(u.getEmail());
												user.setEmail(u.getEmail());
												user.setDomain(SystemUser.THIRD_PARTY_USER);
												user.addUserRole(SystemRole.ROLE_USER);
												user.setGroupIDs(u
														.getGroupIDs());
												accountDao.createSystemUser(
														user, true);
												userDao.active3rdUser(u
														.getEmail());
												break;
											}
										}
									}
								}
							}
							accountDao.initSystemUserMemcache();
							userDao.storeList3rdUserToMemcache(list);
							MonitorLoginService.temp3rdUsers = new ArrayList<InvitedUser>();
						}

					}
				}
			}

			// utilDAO.putGroups();

			// utilDAO.putAllUsers();

			// END LOG
			long end = System.currentTimeMillis();
			long time = end - start;
			logger.log(Level.INFO, MonitorUtil.parseTime(end, true)
					+ " -> END: Scheduled gather system data. Time executed: "
					+ time + " ms");
			// END LOG
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.log(Level.SEVERE,
					" ->ERROR: When Scheduled gather system data. Message: "
							+ ex.getMessage());
		}
	}
}
