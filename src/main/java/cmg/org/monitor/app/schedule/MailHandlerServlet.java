package cmg.org.monitor.app.schedule;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
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
		/*log.log(Level.INFO, "doget getting mail");*/
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
			// get email of sender
			String sender = from.getAddress();
			Object o = message.getContent();
			if(o instanceof Multipart){
				Multipart mp = (Multipart) o;
				int count = mp.getCount();
				for (int i = 0; i < count; i++) {
					if(mp.getBodyPart(i).getContent() instanceof String){
						if(mp.getBodyPart(i).isMimeType("text/plain")){
							saveJDO(mp.getBodyPart(i));
						}	
					}
					
				}
			}	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.log(Level.WARNING,
					"Failure in receiving email : " + e.getMessage());
		}
	}

	private static void saveJDO(Part p) throws IOException,
			MessagingException {
		String o = (String) p.getContent();
		System.out.println(o.toString());
	}
}
