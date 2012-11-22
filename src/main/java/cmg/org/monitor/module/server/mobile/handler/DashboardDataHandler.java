/**
 * Copyright (c) CMG Ltd All rights reserved.
 *
 * This software is the confidential and proprietary information of CMG
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with CMG.
 */

package cmg.org.monitor.module.server.mobile.handler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.ext.model.shared.MonitorMessage;
import cmg.org.monitor.module.server.MonitorGwtServiceImpl;

/** 
 * DOCME
 * 
 * @Creator Hai Lu
 * @author $Author$
 * @version $Revision$
 * @Last changed: $LastChangedDate$
 */

public class DashboardDataHandler extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7331712821078280168L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		response.setHeader("Expires", "Tue, 03 Jul 2001 06:00:00 GMT"); 
		response.setHeader("Last-Modified", new Date().toString()); 
		response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0, post-check=0, pre-check=0"); 
		response.setHeader("Pragma", "no-cache"); 	
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		Gson gson = new Gson();
		
		MonitorGwtServiceImpl service = new MonitorGwtServiceImpl();
		SystemMonitor[] list = service.listSystems();
		if (list != null && list.length > 0) {
			out.print(gson.toJson(list));
		} else {
			MonitorMessage mes = new MonitorMessage("No system found");
			out.print(gson.toJson(mes));
		}
		out.close();
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		doGet(request, response);
	}
}
