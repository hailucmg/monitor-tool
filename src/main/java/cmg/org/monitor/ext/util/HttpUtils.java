package cmg.org.monitor.ext.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.Cookie;


import cmg.org.monitor.util.shared.Constant;

/**
 * Please enter a short description for this class.
 * 
 * <p>
 * Optionally, enter a longer description.
 * </p>
 * 
 * @author Binh Nguyen and Lam phan
 * @version 1.01 May 02, 2008
 */
public abstract class HttpUtils {

	// ~ Static fields/initializers
	// ---------------------------------------------

	private static final String METHOD_GET = "GET";
	private static final String METHOD_POST = "POST";
	private static final String HEADER_COOKIE = "Cookie";
	private static final String HEADER_CONTENTTYPE = "Content-Type";
	private static final String HEADER_CONTENTLENGTH = "Content-Length";
	private static final String HEADER_SETCOOKIE = "Set-Cookie";
	private static final String CONTENTTYPE_FORM_URLENCODED = "application/x-www-form-urlencoded";

	/** the charset static property. */
	public static final String CHARSET = "charset=";

	/** the charset pattern property. */
	public static final Pattern CHARSET_PATTERN = Pattern.compile(";\\s*"
			+ CHARSET + "(.*)$");

	// ~ Methods
	// ----------------------------------------------------------------

	/**
	 * Extracts only the mime-type from a Content-Type HTTP header. Thus a
	 * header like this: <code>text/html;charset=UTF-8</code> will return:
	 * <code>text/html</code>
	 * 
	 * @param contentType
	 *            the Content-Type header
	 * 
	 * @return the content type header without the charset
	 * 
	 * @since 1.6
	 */
	public static String extractMimeTypeFromContentType(String contentType) {
		int charset_index = contentType.indexOf(CHARSET);
		if (charset_index != -1) {
			char indexed_char;
			do {
				indexed_char = contentType.charAt(--charset_index);
			} while (' ' == indexed_char);
			if (';' == indexed_char) {
				return contentType.substring(0, charset_index);
			}
		}

		return contentType;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param connection
	 *            DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 * 
	 * @throws IOException
	 *             DOCUMENT ME!
	 */
	public static String getConnectionContent(HttpURLConnection connection)
			throws IOException {
		// content character set
		String charset = Constant.ENCODING_ISO_8859_1;
		String content_type = connection.getContentType();
		if (content_type != null) {
			Matcher content_type_matcher = CHARSET_PATTERN
					.matcher(content_type);
			if (content_type_matcher.find()) {
				charset = content_type_matcher.group(1);
			}
		}
		ByteArrayOutputStream byte_output = new ByteArrayOutputStream();
		if (!getConnectionContent(connection, byte_output)) {
			return null;
		}
		byte[] bytes = byte_output.toByteArray();

		return new String(bytes, charset);
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param connection
	 *            DOCUMENT ME!
	 * @param output
	 *            DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 * 
	 * @throws IOException
	 *             DOCUMENT ME!
	 */
	public static boolean getConnectionContent(HttpURLConnection connection,
			OutputStream output) throws IOException {
		InputStream input = null;
		try {
			input = connection.getInputStream();
		} catch (FileNotFoundException e) {
			return false;
		}

		// headers
		Map header_fields = connection.getHeaderFields();

		// content encoding
		List content_encoding = (List) header_fields.get("Content-Encoding");
		if ((content_encoding != null) && content_encoding.contains("gzip")) {
			input = new GZIPInputStream(input);
		}
		BufferedInputStream buffered_input = new BufferedInputStream(input);
		byte[] buffer = new byte[8 * 1024];
		int count = 0;
		do {
			output.write(buffer, 0, count);
			count = buffered_input.read(buffer, 0, buffer.length);
		} while (-1 != count);

		return true;
	}

	/**
	 * Retrieve a page using absolute URL.
	 * 
	 * @param absoluteUrl
	 *            the absolute url
	 * 
	 * @return a page
	 * 
	 * @throws IOException
	 *             if IO exception occurred
	 */
	public static Page retrievePage(String absoluteUrl) throws IOException {
		return retrievePage(absoluteUrl, true);
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param absoluteUrl
	 *            DOCUMENT ME!
	 * @param downloadContent
	 *            DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 * 
	 * @throws IOException
	 *             DOCUMENT ME!
	 */
	public static Page retrievePage(String absoluteUrl, boolean downloadContent)
			throws IOException {
		return new Request(absoluteUrl).retrieve(downloadContent);
	}

	// ~ Inner Classes
	// ----------------------------------------------------------

	public static class Request {
		private String mUrl = null;
		private String mMethod = null;
		private Map<String, String> mHeaders = null;
		private StringBuilder mQueryParams = null;
		private StringBuilder mPostParams = null;

		public Request(String absoluteUrl) {
			if (null == absoluteUrl) {
				absoluteUrl = "";
			}
			mUrl = absoluteUrl;
		}

		public Request method(String method) {
			mMethod = method;

			return this;
		}

		public Request queryParams(String[][] queryParams) {
			if (null != queryParams) {
				if (null == mQueryParams) {
					mQueryParams = new StringBuilder();
				}
				params(mQueryParams, queryParams);
			}

			return this;
		}

		public Request queryParams(Map<String, String[]> queryParams) {
			if (null != queryParams) {
				if (null == mQueryParams) {
					mQueryParams = new StringBuilder();
				}
				params(mQueryParams, queryParams);
			}

			return this;
		}

		public Request queryParam(String key, String value) {
			if (null == mQueryParams) {
				mQueryParams = new StringBuilder();
			}
			param(mQueryParams, key, value);

			return this;
		}

		public Request postParams(String[][] postParams) {
			if (null != postParams) {
				if (null == mPostParams) {
					mPostParams = new StringBuilder();
				}
				params(mPostParams, postParams);
			}

			return this;
		}

		public Request postParams(Map<String, String[]> postParams) {
			if (null != postParams) {
				if (null == mPostParams) {
					mPostParams = new StringBuilder();
				}
				params(mPostParams, postParams);
			}

			return this;
		}

		public Request postParam(String key, String value) {
			if (null == mPostParams) {
				mPostParams = new StringBuilder();
			}
			param(mPostParams, key, value);

			return this;
		}

		private void param(StringBuilder store, String key, String value) {
			if ((null != key) && (null != value)) {
				if (store.length() > 0) {
					store.append("&");
				}
				store.append(StringUtils.encodeUrl(key));
				store.append("=");
				store.append(StringUtils.encodeUrl(value));
			}
		}

		private void params(StringBuilder store, String[][] params) {
			for (String[] param : params) {
				for (int i = 1; i < param.length; i++) {
					param(store, param[0], param[i]);
				}
			}
		}

		private void params(StringBuilder store, Map<String, String[]> params) {
			for (Map.Entry<String, String[]> param_entry : params.entrySet()) {
				for (String value : param_entry.getValue()) {
					param(store, param_entry.getKey(), value);
				}
			}
		}

		public Request headers(String[][] headers) {
			if (null != headers) {
				if (null == mHeaders) {
					mHeaders = new LinkedHashMap<String, String>();
				}
				LinkedHashMap<String, List<String>> header_list_map = new LinkedHashMap<String, List<String>>();
				for (String[] header : headers) {
					if ((null != header[0]) && (null != header[1])) {
						List<String> header_values = header_list_map
								.get(header[0]);
						if (null == header_values) {
							header_values = new ArrayList<String>();
							header_list_map.put(header[0], header_values);
						}
						boolean first = true;
						for (String header_value : header) {
							if (first) {
								first = false;

								continue;
							}
							header_values.add(header_value);
						}
					}
				}
				List<String> headers_values = null;
				for (String headers_name : header_list_map.keySet()) {
					headers_values = header_list_map.get(headers_name);
					mHeaders.put(headers_name,
							StringUtils.join(headers_values, ","));
				}
			}

			return this;
		}

		public Request headers(Map<String, String> headers) {
			if (null != headers) {
				if (null == mHeaders) {
					mHeaders = new LinkedHashMap<String, String>();
				}
				mHeaders.putAll(headers);
			}

			return this;
		}

		public Request header(String name, String content) {
			if ((null != name) && (null != content)) {
				if (null == mHeaders) {
					mHeaders = new LinkedHashMap<String, String>();
				}
				mHeaders.put(name, content);
			}

			return this;
		}

		public Request cookie(String name, String value) {
			String current_cookie_header = null;
			if (mHeaders != null) {
				current_cookie_header = mHeaders.get(HEADER_COOKIE);
			}
			if (null == current_cookie_header) {
				current_cookie_header = "$Version=\"1\"";
			}
			StringBuilder cookie_header = new StringBuilder(
					current_cookie_header);
			cookie_header.append("; ");
			cookie_header.append(name);
			cookie_header.append("=\"");
			cookie_header.append(value);
			cookie_header.append("\"");
			header(HEADER_COOKIE, cookie_header.toString());

			return this;
		}

		public Page retrieve() throws IOException {
			return retrieve(true);
		}

		public Page retrieve(boolean downloadContent) throws IOException {
			return retrieve(downloadContent, null);
		}

		public Page retrieve(boolean downloadContent, OutputStream output)
				throws IOException {
			Page page = null;
			URL url = null;
			HttpURLConnection connection = null;
			try {
				// add the query parameters to the URL if thzy're present
				if ((mQueryParams != null) && (mQueryParams.length() > 0)) {
					StringBuilder buffer = new StringBuilder(mUrl);
					if (-1 == mUrl.indexOf("?")) {
						buffer.append("?");
					} else {
						buffer.append("&");
					}
					buffer.append(mQueryParams.toString());
					mUrl = buffer.toString();
				}

				// create a new URL
				url = new URL(mUrl);

				// setup the request method
				if (null == mMethod) {
					if (mPostParams != null) {
						mMethod = METHOD_POST;

						// put the correct content type for a post request
						if ((null == mHeaders)
								|| !mHeaders.containsKey(HEADER_CONTENTTYPE)) {
							header(HEADER_CONTENTTYPE,
									CONTENTTYPE_FORM_URLENCODED);
						}
					} else {
						mMethod = METHOD_GET;
					}
				}
				if (mPostParams != null) {
					// put the correct content length for post requests
					header(HEADER_CONTENTLENGTH,
							String.valueOf(mPostParams.length()));
				}

				// make untrusted SSL certificates work
				/**
				if (url.getProtocol().equals("https")) {
					try {
						java.security.Security
								.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
						System.setProperty("java.protocol.handler.pkgs",
								"com.sun.net.ssl.internal.www.protocol");

						X509TrustManager tm = new MyX509TrustManager();
						HostnameVerifier hm = new MyHostnameVerifier();
						KeyManager[] km = null;
						TrustManager[] tma = { tm };
						SSLContext sc = SSLContext.getInstance("SSL");
						sc.init(km, tma, new java.security.SecureRandom());
						SSLSocketFactory sf1 = sc.getSocketFactory();
						HttpsURLConnection.setDefaultSSLSocketFactory(sf1);
						HttpsURLConnection.setDefaultHostnameVerifier(hm);
					} catch (KeyManagementException e) {
						IOException e2 = new IOException();
						e2.initCause(e);
						throw e2;
					} catch (NoSuchAlgorithmException e) {
						IOException e2 = new IOException();
						e2.initCause(e);
						throw e2;
					}
				}
				*/
				// setup the connection
				connection = (HttpURLConnection) url.openConnection();
				connection.setConnectTimeout(5 * 60 * 1000);
				connection.setRequestMethod(mMethod);
				connection.setDoInput(true);
				connection.setDoOutput(true);
				connection.setUseCaches(true);

				// put the headers as properties to the connection
				if (mHeaders != null) {
					for (String headers_name : mHeaders.keySet()) {
						connection.setRequestProperty(headers_name,
								mHeaders.get(headers_name));
					}
				}

				// send the form parameters
				if (mPostParams != null) {
					DataOutputStream out = new DataOutputStream(
							connection.getOutputStream());
					out.writeBytes(mPostParams.toString());
					out.flush();
					out.close();
				}

				// download the connection content if that's needed
				String connection_content = null;
				if (downloadContent) {
					if (output != null) {
						getConnectionContent(connection, output);
					} else {
						connection_content = getConnectionContent(connection);
					}
				}

				// construct a new result page from the connection result
				page = new Page(connection_content,
						connection.getHeaderFields(),
						connection.getResponseCode(),
						connection.getResponseMessage());
				connection.disconnect();
			} finally {
				if (null != connection) {
					connection.disconnect();
				}
			}

			return page;
		}
	}

	static class MyHostnameVerifier implements HostnameVerifier {		
		@Override
		public boolean verify(String arg0, SSLSession arg1) {
			return true;
		}
	}

	static class MyX509TrustManager implements X509TrustManager {
		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			return new X509Certificate[0];
		}

		@Override
		public void checkClientTrusted(X509Certificate[] arg0, String arg1)
				throws CertificateException {
		}

		@Override
		public void checkServerTrusted(X509Certificate[] arg0, String arg1)
				throws CertificateException {
			
		}
	}

	public static class Page {
		private String mContent = null;
		private int mResponseCode = -1;
		private String mResponseMessage = null;
		private Map<String, List<String>> mHeaders = null;

		Page(String content, Map<String, List<String>> headers,
				int responseCode, String responseMessage) {
			mContent = content;
			mHeaders = new HashMap<String, List<String>>(
					(Map<String, List<String>>) headers);
			mResponseCode = responseCode;
			mResponseMessage = responseMessage;
		}

		public String getContent() {
			return mContent;
		}

		public Map<String, List<String>> getHeaders() {
			return mHeaders;
		}

		public List<String> getHeader(String name) {
			return mHeaders.get(name);
		}

		public String getHeaderField(String name) {
			List<String> header = getHeader(name);
			if (null == header) {
				return null;
			}

			return header.get(0);
		}

		public int getResponseCode() {
			return mResponseCode;
		}

		public String getResponseMessage() {
			return mResponseMessage;
		}

		public String getContentType() {
			return getHeaderField(HEADER_CONTENTTYPE);
		}

		public boolean checkReceivedCookies(Cookie[] cookies) {
			if (!getHeaders().containsKey(HEADER_SETCOOKIE)) {
				return false;
			}
			HashSet<Cookie> matched_cookies = new HashSet<Cookie>();
			List<String> received_cookies = null;
			received_cookies = getHeaders().get(HEADER_SETCOOKIE);
			for (Cookie cookie : cookies) {
				if (!received_cookies.contains(cookie.getName() + "="
						+ cookie.getValue())) {
					return false;
				}
				if (matched_cookies.contains(cookie)) {
					return false;
				}
				matched_cookies.add(cookie);
			}

			return true;
		}
	}
}
