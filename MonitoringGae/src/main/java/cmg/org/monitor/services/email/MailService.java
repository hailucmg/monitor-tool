package cmg.org.monitor.services.email;

import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import cmg.org.monitor.dao.impl.AlertDaoJDOImpl;
import cmg.org.monitor.ext.model.Component;
import cmg.org.monitor.ext.model.shared.SystemDto;
/**
 * @author lamphan
 * @version 1.0
 */
public class MailService {

	/** Alert name */
	public static String ALERT_NAME = " alert report ";

	/** Declare email address */
	public static String EMAIL_ADMINISTRATOR = "lam.phan@c-mg.com";

	/** Get log instance */
	private static final Logger logger = Logger.getLogger(AlertDaoJDOImpl.class
			.getName());

	/**
	 * Send alert email function.
	 * 
	 * @param systemDto Data transfer object.
	 * @throws AddressException
	 * @throws MessagingException
	 * @throws UnsupportedEncodingException
	 */
	public void sendAlertMail(SystemDto systemDto, String messageError) throws AddressException,
			MessagingException, UnsupportedEncodingException {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		try {
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(EMAIL_ADMINISTRATOR,
					systemDto.getName()+" Monitor system"));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(
					systemDto.getGroupEmail(), "CMG monitor email"));
			msg.setSubject(systemDto.getName() + ALERT_NAME);
			msg.setText(messageError);
			Transport.send(msg);

		} catch (AddressException ae) {
			logger.warning("Address exception occurrence due to :"
					+ ae.getCause().getMessage());
			throw new AddressException(ae.getCause().getMessage());
		} catch (MessagingException me) {
			logger.warning("Messaging exception occurrence due to :"
					+ me.getCause().getMessage());
			throw new MessagingException(me.getCause().getMessage());
		} catch (UnsupportedEncodingException uee) {
			logger.warning("Unsupported encoding exception due to :"
					+ uee.getCause().getMessage());
			throw new UnsupportedEncodingException(uee.getCause().getMessage());

		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getCause().getMessage());
		}

	}
	
	/**
	 * Send alert email function with parameters<br>.
	 * 
	 * @param systemDto Data transfer object.
	 * @throws AddressException
	 * @throws MessagingException
	 * @throws UnsupportedEncodingException
	 */
	public void sendAlertMail(Component component, SystemDto systemDto) throws AddressException,
			MessagingException, UnsupportedEncodingException {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);
		String msgBody = component.getError();

		try {
			
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(EMAIL_ADMINISTRATOR,
					systemDto.getName()+" Monitor system"));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(
					systemDto.getGroupEmail(), "Cmg monitor email"));
			msg.setSubject(systemDto.getName() + ALERT_NAME);
			msg.setText(msgBody);
			Transport.send(msg);
		
			// log any exception and throw it's reason
		} catch (AddressException ae) {
			logger.warning("Address exception occurrence due to :"
					+ ae.getCause().getMessage());
			throw new AddressException(ae.getCause().getMessage());
		} catch (MessagingException me) {
			logger.warning("Messaging exception occurrence due to :"
					+ me.getCause().getMessage());
			throw new MessagingException(me.getCause().getMessage());
		} catch (UnsupportedEncodingException uee) {
			logger.warning("Unsupported encoding exception due to :"
					+ uee.getCause().getMessage());
			throw new UnsupportedEncodingException(uee.getCause().getMessage());

		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getCause().getMessage());
		}

	}
	
	
}
