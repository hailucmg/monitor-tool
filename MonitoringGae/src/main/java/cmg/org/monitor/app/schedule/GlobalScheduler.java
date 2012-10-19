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
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cmg.org.monitor.ext.util.MonitorUtil;
import cmg.org.monitor.services.MonitorService;

/**
 * The Class GlobalScheduler.
 *
 * @author Lamphan
 * @version 1.0
 */
public class GlobalScheduler extends HttpServlet {

	/** Default UUID value. */
	private static final long serialVersionUID = -5005043235328590690L;

	/** The log of application. */
	private static final Logger logger = Logger.getLogger(GlobalScheduler.class
			.getCanonicalName());

	/**
	 * (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse) 
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	/**
	 * (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse) 
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException {		
		doSchedule();
	}

	/**
	 * Main schedule method.<br>
	 * The method is executed by cron job task.
	 */
	public void doSchedule() {
		try {
			//BEGIN LOG
			long start = System.currentTimeMillis();
			logger.log(Level.INFO, MonitorUtil.parseTime(start, true)
					+ " -> START: Scheduled monitoring ...");
			//BEGIN LOG
			
			MonitorService.monitor();	
			
			//END LOG
			long end = System.currentTimeMillis();
			long time = end - start;
			logger.log(Level.INFO, MonitorUtil.parseTime(end, true)
					+ " -> END: Scheduled monitoring. Time executed: " + time + " ms");
			//END LOG
		} catch (Exception ex) {
			logger.log(Level.SEVERE," ->ERROR: When Scheduled monitoring. Message: " + ex.getMessage());
		}
	}

}
