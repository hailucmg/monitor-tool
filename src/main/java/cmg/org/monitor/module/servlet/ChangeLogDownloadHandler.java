/**
 * Copyright (c) CMG Ltd All rights reserved.
 *
 * This software is the confidential and proprietary information of CMG
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with CMG.
 */

package cmg.org.monitor.module.servlet;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cmg.org.monitor.dao.AccountSyncLogDAO;
import cmg.org.monitor.dao.impl.AccountSyncLogDaoImpl;
import cmg.org.monitor.entity.shared.AccountSyncLog;

/**
 * DOCME
 * 
 * @Creator Hai Lu
 * @author $Author$
 * @version $Revision$
 * @Last changed: $LastChangedDate$
 */

public class ChangeLogDownloadHandler extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6495149781448537302L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String adminAccount = request.getParameter("id");
		if (adminAccount == null) {
			adminAccount = "";
		}
		String fileName = "log.txt";
		int length = 0;

		byte[] byteBuffer = new byte[1024];

		AccountSyncLogDAO logDao = new AccountSyncLogDaoImpl();
		List<AccountSyncLog> list = null;
		StringBuffer log = new StringBuffer();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
		try {
			list = logDao.listLog(adminAccount, 200);
			if (list != null && list.size() > 0) {
				for (AccountSyncLog l : list) {
					log.append("\n####### " + sdf.format(l.getTimestamp())
							+ " #######\n");
					log.append(l.getLog());
				}
			} else {
				log.append("No log found");
			}
		} catch (Exception e) {
			log.append("Error. Message: " + e.getMessage());
		}

		ByteArrayInputStream bis = new ByteArrayInputStream(log.toString()
				.getBytes("UTF-8"));
		ServletOutputStream sos = response.getOutputStream();
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment; filename=\""
				+ fileName + "\"");
		response.setContentLength(bis.available());
		while ((length = bis.read(byteBuffer)) != -1) {
			sos.write(byteBuffer, 0, length);
		}
		bis.close();
		sos.close();
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		doGet(request, response);
	}
}
