/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * 
 * Copyright (c) CMG Ltd All rights reserved.

 *
 * This software is the confidential and proprietary information of CMG
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with CMG.
 */
package cmg.org.monitor.ext.model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.activation.DataSource;

/**
 * A simple class for loading java.util.Properties backed by .properties files
 * deployed as classpath resources. See individual methods for details.
 * 
 * @Creator Lan Ta
 * @author $$Author: longnguyen $$
 * @version $$Revision: 1717 $$
 * @Lastchanged: $$LastChangedDate: 2012-07-20 10:45:42 +0700 (Fri, 20 Jul 2012) $$
 */
public class HTMLDATASource implements DataSource {

	private final String html;

	public HTMLDATASource(String htmlString) {
		html = htmlString;
	}

	// Return html string in an InputStream.
	// A new stream must be returned each time.
	@Override
	public InputStream getInputStream() throws IOException {
		if (html == null)
			throw new IOException("Null HTML");
		return new ByteArrayInputStream(html.getBytes());
	}

	@Override
	public OutputStream getOutputStream() throws IOException {
		throw new IOException("This DataHandler cannot write HTML");
	}

	@Override
	public String getContentType() {
		return "text/html";
	}

	@Override
	public String getName() {
		return "JAF text/html dataSource to send e-mail only";
	}
}
