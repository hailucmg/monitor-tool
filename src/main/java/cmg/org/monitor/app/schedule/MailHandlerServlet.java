package cmg.org.monitor.app.schedule;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.Address;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class MailHandlerServlet extends HttpServlet {
	private static final Logger log = Logger.getLogger(MailHandlerServlet.class
			.getName());

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		doPost(req, resp);

	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		try {

			Properties props = new Properties();
			Session session = Session.getDefaultInstance(props, null);
			log.log(Level.INFO, "start getting mail");
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
			if (o instanceof Multipart) {
				Multipart mp = (Multipart) o;
				int count = mp.getCount();
				for (int i = 0; i < count; i++) {
					if (mp.getBodyPart(i).getContent() instanceof String) {
						log.log(Level.INFO, "contentmail :" + i);
						Object obj = mp.getBodyPart(i).getContent();
						String data = (String) obj;
						log.log(Level.INFO, "contentmail :" + data);
						if (data.trim().endsWith("</html>")) {
							log.log(Level.INFO, "contentmail :end");
							saveJDO(data);
						}
					}

				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.log(Level.INFO,
					"Failure in receiving email : " + e.getMessage());
		}
	}

	private static void saveJDO(String data_html) {

	}

}
