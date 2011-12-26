package cmg.org.monitor.app.schedule;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cmg.org.monitor.dao.MailMonitorDAO;
import cmg.org.monitor.dao.impl.MailMonitorDaoImpl;
import cmg.org.monitor.dao.impl.SystemDaoImpl;
import cmg.org.monitor.entity.shared.MailMonitor;
import cmg.org.monitor.ext.util.MonitorUtil;
import cmg.org.monitor.memcache.Key;
import cmg.org.monitor.memcache.MonitorMemcache;

@SuppressWarnings("serial")
public class MonitorMailHandlerServlet extends HttpServlet {
	private static final Logger logger = Logger
			.getLogger(MonitorMailHandlerServlet.class.getName());
	private static final SystemDaoImpl system = new SystemDaoImpl();

	/* private static SystemDto sys; */

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		doPost(req, resp);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		try {			
			// BEGIN LOG
			long start = System.currentTimeMillis();
			logger.log(Level.INFO, MonitorUtil.parserTime(start, true)
					+ " -> START: Receive mail ... ");
			// BEGIN LOG
			MailMonitorDAO mailDAO = new MailMonitorDaoImpl();
			MailMonitor mail = new MailMonitor();
			Properties props = new Properties();
			Session session = Session.getDefaultInstance(props, null);
			MimeMessage message = new MimeMessage(session, req.getInputStream());
			Address[] add = message.getFrom();
			InternetAddress from = (InternetAddress) add[0];
			
			mail.setSender(from.getAddress());
			logger.log(Level.INFO, "From:" + mail.getSender());
			
			mail.setSubject(message.getSubject());
			logger.log(Level.INFO, "Subject: " + mail.getSubject());
			
			mail.setContentType(message.getContentType());
			logger.log(Level.INFO, "Content Type: " + mail.getContentType());
			
			Object o = message.getContent();
			mail.setContent(getMaiContent(o));
			logger.log(Level.INFO, "Content: " + mail.getContent());
			
			mailDAO.putMailMonitor(mail);
			
			// END LOG
			long end = System.currentTimeMillis();
			logger.log(Level.INFO, MonitorUtil.parserTime(end, true)
					+ " -> END: Receive mail. Time executed: "
					+ (end - start) + " ms.");
			// END LOG
		} catch (Exception e) {
			logger.log(Level.WARNING,
					"Failure in receiving email : " + e.getMessage());
		}
	}

	public String getMaiContent(Object o)
			throws MessagingException, IOException {

		String data = null;
		if (o instanceof Multipart) {
			Multipart mp = (Multipart) o;
			int count = mp.getCount();
			for (int i = 0; i < count; i++) {
				if (mp.getBodyPart(i).getContent() instanceof String) {
					Object obj = mp.getBodyPart(i).getContent();
					data = (String) obj;
				}
			}
		} else if (o instanceof InputStream) {
			InputStream is = (InputStream) o;
			try {
				data = convertInputStreamtoString(is);
			} catch (Exception e) {
				logger.log(Level.WARNING,
						" -> ERROR when get content mail - type txt/html. Message: "
								+ e.getMessage());
			}
		} else if (o instanceof String) {
			data = (String) o;
		}
		return data;
	}

	/**
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public String convertInputStreamtoString(InputStream is) throws IOException {
		if (is != null) {
			Writer write = new StringWriter();
			char[] buffer = new char[1024];
			try {
				Reader reader = new BufferedReader(new InputStreamReader(is,
						"UTF-8"));
				int n;
				while ((n = reader.read(buffer)) != -1) {
					write.write(buffer, 0, n);
				}
			} finally {
				is.close();
			}
			return write.toString();
		} else {
			return null;
		}
	}

}
