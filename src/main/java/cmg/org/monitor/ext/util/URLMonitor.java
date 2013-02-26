/*
 * Copyright (c) CMG Ltd All rights reserved.
 *
 * This software is the confidential and proprietary information of CMG
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with CMG.
 */
package cmg.org.monitor.ext.util;

import static com.google.appengine.api.urlfetch.FetchOptions.Builder.allowTruncate;

import java.net.URL;
import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.appengine.api.urlfetch.HTTPHeader;
import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;

import cmg.org.monitor.ext.util.HttpUtils.Page;
import cmg.org.monitor.util.shared.Constant;

/**
 * Please enter a short description for this class.
 * 
 * <p>
 * Optionally, enter a longer description.
 * </p>
 * 
 * @author Lam phan
 * @version 1.0.6 June 11, 2008
 */
public class URLMonitor {
	/** the charset static property. */
	public static final String CHARSET = "charset=";

	/** the charset pattern property. */
	public static final Pattern CHARSET_PATTERN = Pattern.compile(";\\s*"
			+ CHARSET + "(.*)$");

	/** Alert name */
	public static String ALERT_NAME = " alert report ";

	/** Declare email address */
	public static String EMAIL_ADMINISTRATOR = "lam.phan@c-mg.com";

	/** Time format value */
	private static String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss.S";

	/** Time instance */
	private Timestamp timeStamp;

	private static String ERROR = "ERROR";

	/** Log object. */
	private static final Logger logger = Logger.getLogger(URLMonitor.class
			.getCanonicalName());

	/**
	 * Default constructor.<br>
	 */
	public URLMonitor() {
		super();
	}

	public static String retrievesContentWithFetchService(String remoteUrl)
			throws Exception {
		URLFetchService service = URLFetchServiceFactory.getURLFetchService();
		try {
			String charset = Constant.ENCODING_ISO_8859_1;
			String content_type = "";
			URL remoteURL = new URL(remoteUrl);
			HTTPRequest request = new HTTPRequest(remoteURL, HTTPMethod.GET,
					allowTruncate().doNotFollowRedirects()
							.doNotValidateCertificate().setDeadline(new Double(60 * 5)));
			HTTPResponse res = service.fetch(request);
			List<HTTPHeader> headers = res.getHeaders();
			if (headers != null && !headers.isEmpty()) {
				for (HTTPHeader header : headers) {
					if (header.getName().equalsIgnoreCase("content-type")) {
						content_type = header.getValue();
					}
				}
			}
			if (content_type != null && content_type.length() > 0) {
				Matcher content_type_matcher = CHARSET_PATTERN
						.matcher(content_type);
				if (content_type_matcher.find()) {
					charset = content_type_matcher.group(1);
				}
			}

			byte[] content = res.getContent();

			if (content == null) {
				return "";
			} else {
				return new String(content, charset);
			}
		} catch (Exception ex) {
			logger.log(Level.WARNING, "Error. Message: " + ex.getMessage());
			return "";
		}
	}

	public static String retrievesContent(String remoteUrl) throws Exception {
		String webContent = "";
		// Processes page
		Page page = null;
		boolean isError = false;
		try {
			page = HttpUtils.retrievePage(remoteUrl);
			logger.log(
					Level.INFO,
					"The website has been retrieved, content type: "
							+ page.getContentType());
			if ((page == null) || (page.getContent() == null)) {
				if ((ConnectionUtil.internetAvail()) && (!isError)) {
					// Prints out
					logger.log(Level.SEVERE,
							"The system can't fetch content of data from the following url : \r\n"
									+ remoteUrl);
				} else {
					logger.info("Internet connection failed");
				}
			} else if ((page.getContent().equals(ERROR))
					|| (page.getContent().equals(MonitorUtil.getErrorContent()))) {

			} else {
				webContent = page.getContent();
			}
		} catch (Exception mx) {
			try {
				if (ConnectionUtil.internetAvail()) {
					isError = true;
				} else {

				}
			} catch (Exception e) {
			}

		}
		return webContent;
	}

}
