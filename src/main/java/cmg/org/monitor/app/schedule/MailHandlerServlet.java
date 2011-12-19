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
			
			saveJDO(o, contentType);

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
	public void saveJDO(Object o,String contentType) throws MessagingException, IOException{
		if (contentType.equals("txt/xml")) {
			if (o instanceof InputStream) {
				log.log(Level.INFO, "This is just an input stream");
				InputStream is = (InputStream) o;
				try {
					//do anything with data
					String data = convertInputStreamtoString(is);
					log.log(Level.INFO, data);
					/*log.log(Level.INFO, readXml(data));*/
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
						//do anything with data
						String data = (String) obj;
						log.log(Level.INFO, "contentmail :is html");
						log.log(Level.INFO, "contentmail :" + data);
					}
				}
			}
			if (o instanceof InputStream) {
				log.log(Level.INFO, "This is just an input stream");
				InputStream is = (InputStream) o;
				try {
					//do anything with data
					String data = convertInputStreamtoString(is);
					log.log(Level.INFO, data);
					/*log.log(Level.INFO, readXml(data));*/
				} catch (Exception e) {
					log.log(Level.INFO, e.getMessage());
				}
			}
		} else {
			if (o instanceof Multipart) {
				Multipart mp = (Multipart) o;
				int count = mp.getCount();
				for (int i = 0; i < count; i++) {
					if (mp.getBodyPart(i).getContent() instanceof String) {
						log.log(Level.INFO, "contentmail :" + i);
						Object obj = mp.getBodyPart(i).getContent();
						String tp = mp.getBodyPart(i).getContentType();
						//do anything with data
						String data = (String) obj;
						log.log(Level.INFO, "contentmail type :" + tp);
						log.log(Level.INFO, "contentmail :" + data);
					} else {
						log.log(Level.INFO, "contentmail :wrong");
					}
				}
			}
		}
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


/*	public static String readXml(String is)
			throws ParserConfigurationException, SAXException, IOException {

		DocumentBuilderFactory factory = null;
		DocumentBuilder builder = null;
		Document ret = null;
		try {
			factory = DocumentBuilderFactory.newInstance();
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			log.log(Level.INFO, e.getMessage()+ "zeo");
		}
		try {
			ByteArrayInputStream bi = new ByteArrayInputStream(is.getBytes());
			ret = builder.parse(bi);
		} catch (SAXException e) {
			log.log(Level.INFO, e.getMessage() + "one");
		} catch (IOException e) {
			log.log(Level.INFO, e.getMessage() +"two");
		}
		ret.getDocumentElement().normalize();
		String data = ret.getDocumentElement().getNodeName();
		return data;
	}*/
}
