package cmg.org.monitor.services.email;

import java.util.Properties;
import java.util.logging.Logger;

import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;

import cmg.org.monitor.ext.util.URLMonitor;
import cmg.org.monitor.util.shared.Constant;

public class FetchMailUsage {
	
	/** */
	private static Properties props = System.getProperties();
	private static String USER_PROPERTY = "com.mail.username";
	
	/** Log object. */
	private static final Logger logger = Logger.getLogger(URLMonitor.class
			.getCanonicalName());
	
	public static String fetchSMTPEmail() {
		String mailUserName  = props.getProperty(USER_PROPERTY);
		String host = "c-mg.info";
		
		//String username = mailUserName + host;
		String username = "cmgadmin@" + host;
		String password = "W3lc0m3@";
		String webContent = null;

		try {
			// SimpleDateFormat formatter = new SimpleDateFormat( "yyyy.MM.dd G
			// 'at' HH:mm:ss z");

			// Generate empty properties
			Properties props = new Properties();

			// Get session
			Session mailSession = Session.getDefaultInstance(props, null);

			// Get the store
			Store store = mailSession.getStore("pop3");
			store.connect(host, username, password);

			// Get folder
			Folder folder = store.getFolder("INBOX");
			folder.open(Folder.READ_ONLY);

    	    // Get directory
    	    Message[] message = folder.getMessages(0, folder.getNewMessageCount());
    	    
    	    long max = 0;
    	    for (int i = 0, n = message.length; i < n; i++) {
    	        // Processes multiple part
    	        Object content = message[i].getContent();
    	        if (content instanceof Multipart) {
    	            // Multipart
    	            Multipart multipart = (Multipart) message[i].getContent();
    	            for (int x = 0; x < multipart.getCount(); x++) {
    	                BodyPart bodyPart = multipart.getBodyPart(x);
    	                String disposition = bodyPart.getDisposition();
    	                if ((disposition != null)
    	                        && (disposition.equals(BodyPart.ATTACHMENT))) {
    	                   
    	                    DataHandler handler = bodyPart.getDataHandler();
    	                 
    	                } else {
    	                    content = bodyPart.getContent().toString();
    	                }
    	            }
    	        } else if (content instanceof Part) {
    	            // text/html
    	        } else {
    	            webContent = content.toString();
    	            long curTime = message[i].getSentDate().getTime();
    	            if (i < (message.length - 1)) {
    	                max = curTime;
    	                webContent = content.toString();
    	                if (message[i + 1].getSentDate().getTime() > max) {
    	                    max = message[i + 1].getSentDate().getTime();
    	                    webContent = message[i + 1].getContent()
    	                                               .toString();
    	                }
    	            }
    	        }
    	    }
    	    // Close connection
    	    folder.close(false);
    	    store.close();
    	    
    	    // Process for content
    	    if (webContent == null) {
    	    	// Loads an empty page
    	    	webContent = Constant.ERROR;
    	    	
    	    } 

		} catch (Exception ex) {
			webContent = Constant.ERROR;
			logger.info("Error while reading email content, error details: " + ex.getCause().getMessage());
//			System.err
//					.println("Error while reading email content, error details: "
//							+ ex.getMessage());
			
		}
		return webContent; 
	
	}
	
	public static void main(String[] args) {
		FetchMailUsage fm = new FetchMailUsage();
		fm.fetchSMTPEmail();
	}
}
