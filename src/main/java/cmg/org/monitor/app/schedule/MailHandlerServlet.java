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

import cmg.org.monitor.dao.impl.SystemMonitorDaoJDOImpl;
import cmg.org.monitor.ext.model.shared.SystemDto;

@SuppressWarnings("serial")
public class MailHandlerServlet extends HttpServlet {
	private static final Logger log = Logger.getLogger(MailHandlerServlet.class
			.getName());
	private static final SystemMonitorDaoJDOImpl system = new SystemMonitorDaoJDOImpl();

	/* private static SystemDto sys; */

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		doPost(req, resp);

	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		try {

			Properties props = new Properties();
			Session session = Session.getDefaultInstance(props, null);
			MimeMessage message = new MimeMessage(session, req.getInputStream());
			Address[] add = message.getFrom();
			InternetAddress from = (InternetAddress) add[0];
			String sender = from.getAddress();
			log.log(Level.INFO, "getting mail from:" + sender);
			String subject = message.getSubject();
			log.log(Level.INFO, "Got an email. Subject = " + subject);
			String contentType = message.getContentType();
			log.log(Level.INFO, "Email Content Type : " + contentType);
			Object o = message.getContent();
			SystemDto sys = new SystemDto();
			parseEmail(o, contentType, sys);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.log(Level.INFO,
					"Failure in receiving email : " + e.getMessage());
		}
	}

	/**
	 * @param o
	 * @param contentType
	 * @throws MessagingException
	 * @throws IOException
	 */
	public void parseEmail(Object o, String contentType, SystemDto sysDTO)
			throws MessagingException, IOException {
		String data = null;
		if (contentType.equals("txt/xml")) {
			if (o instanceof InputStream) {
				log.log(Level.INFO,
						"This is just an input stream content type xml");
				InputStream is = (InputStream) o;
				try {
					data = convertInputStreamtoString(is);
					log.log(Level.INFO, data);
				} catch (Exception e) {
					log.log(Level.INFO, e.getMessage());
				}
			}
		} else if (contentType.equals("txt/html")) {
			if (o instanceof Multipart) {
				Multipart mp = (Multipart) o;
				int count = mp.getCount();
				for (int i = 0; i < count; i++) {
					if (mp.getBodyPart(i).getContent() instanceof String) {
						log.log(Level.INFO, "contentmail :" + i);
						Object obj = mp.getBodyPart(i).getContent();
						// do anything with data
						data = (String) obj;

					}
				}
			}
			if (o instanceof InputStream) {
				log.log(Level.INFO,
						"This is just an input stream of content html");
				InputStream is = (InputStream) o;
				try {
					// do anything with data
					data = convertInputStreamtoString(is);

					/* log.log(Level.INFO, readXml(data)); */
				} catch (Exception e) {
					log.log(Level.INFO, e.getMessage());
				}
			}
		} else if (contentType.toLowerCase().startsWith("multipart")) {
			if (o instanceof Multipart) {
				Multipart mp = (Multipart) o;
				int count = mp.getCount();
				for (int i = 0; i < count; i++) {
					if (mp.getBodyPart(i).getContent() instanceof String) {
						data = (String) mp.getBodyPart(i).getContent();
					} else {
						log.log(Level.INFO, "contentmail :wrong");
					}
					log.log(Level.INFO, "contentmail :" + data);
				}
			}
		}

		/*
		 * MailStoreDAO mailStore = new MailStoreDaoJDO(); MailStoreDto mailDto
		 * = new MailStoreDto(); mailDto.setContent(data);
		 * mailDto.setTimeStamp(new Date()); log.log(Level.INFO, "Set content");
		 * if (sysDTO != null) { log.log(Level.INFO, "Get list of system");
		 * mailStore.addMail(mailDto, sysDTO); } log.log(Level.INFO,
		 * "Save STMP email content successfully");
		 */
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

	/*
	 * public static boolean checkMail(String senderMail) throws Exception {
	 * SystemMonitor[] list = system.listSystems(false); if (list.length > 0) {
	 * for (int i = 0; i < list.length; i++) { if
	 * (list[i].getEmail().toLowerCase().equals(senderMail)) { sys = new
	 * SystemDto(); sys.setId(list[i].getId());
	 * sys.setGroupEmail(list[i].getGroupEmail()); return true; } } } else {
	 * return false; } return false; }
	 */

}
