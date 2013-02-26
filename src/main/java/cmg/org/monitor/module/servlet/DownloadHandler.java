/**
 * Copyright (c) CMG Ltd All rights reserved.
 *
 * This software is the confidential and proprietary information of CMG
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with CMG.
 */

package cmg.org.monitor.module.servlet;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** 
 * DOCME
 * 
 * @Creator Hai Lu
 * @author $Author$
 * @version $Revision$
 * @Last changed: $LastChangedDate$
 */

public class DownloadHandler extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {		
		String fileName = "proxy-link.zip";
		int length = 0;
		byte[] byteBuffer = new byte[1024];
		
		InputStream is = this.getClass().getResourceAsStream("/cmg/org/monitor/content/download/proxy-link.zip");		
		ServletOutputStream sos = response.getOutputStream();
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment; filename=\""
				+ fileName + "\"");
		response.setContentLength(is.available());
		while ((length = is.read(byteBuffer)) != -1) {
			sos.write(byteBuffer, 0, length);
		}
		is.close();
		sos.close();
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		doGet(request, response);
	}
}
