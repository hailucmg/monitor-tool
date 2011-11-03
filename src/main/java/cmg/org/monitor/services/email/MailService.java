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
import cmg.org.monitor.ext.model.dto.SystemDto;

/**
 * @author lamphan
 *
 */
public class MailService {
	
	/** */
	public static String ALERT_NAME = " alert report ";
	
	/** */
	private static final Logger logger = Logger.getLogger(AlertDaoJDOImpl.class
			.getName());
	

    /**
     * @param systemDto
     * @throws AddressException
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    public void sendAlertMail(SystemDto systemDto) throws AddressException, MessagingException , UnsupportedEncodingException {
    	Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);
		String msgBody = "This is monitoring alert email.";

		try {
			StringBuilder strBuild = new StringBuilder(msgBody);
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress("lam.phan@c-mg.com",
					"Monitor Administrator"));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(
					"monitor@c-mg.vn", "Mr. Lam"));
			msg.setSubject(systemDto.getName() + ALERT_NAME);
			msg.setText(strBuild.append(systemDto.getName()).append("is under alert ").toString());
			Transport.send( msg);

		} catch (AddressException ae) {
			logger.warning("Address exception occurrence due to :"+ae.getCause().getMessage());
			throw new AddressException(ae.getCause().getMessage());
		} catch (MessagingException me) {
			logger.warning("Messaging exception occurrence due to :"+me.getCause().getMessage());
			throw new MessagingException(me.getCause().getMessage());
		} catch (UnsupportedEncodingException uee) {
			logger.warning("Unsupported encoding exception due to :"+uee.getCause().getMessage());
			throw new UnsupportedEncodingException(uee.getCause().getMessage());
			
		} catch(Exception e) { 
			logger.log(Level.SEVERE, e.getCause().getMessage()); 
			}
		
    }
}
