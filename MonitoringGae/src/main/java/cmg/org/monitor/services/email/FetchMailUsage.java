package cmg.org.monitor.services.email;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.SubjectTerm;

public class FetchMailUsage {

    public static void main(String[] args) {

    	
    	String host = "c-mg.info";
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
    	    Message[] message = folder.getMessages();
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
    	    	webContent = "ERROR";
    	    } //
    	} catch (Exception ex) {
    		System.err.println("Error while reading email content, error details: " + ex.getMessage());
    	}
    	
    	
//        // SUBSTITUTE YOUR ISP's POP3 SERVER HERE!!!
//        String host = "c-mg.info";
//        // SUBSTITUTE YOUR USERNAME AND PASSWORD TO ACCESS E-MAIL HERE!!!
//        String user = "cmgadmin@" + host;
//        String password = "W3lc0m3@";
//        // SUBSTITUTE YOUR SUBJECT SUBSTRING TO SEARCH HERE!!!
//        String subjectSubstringToSearch = "Test E-Mail through Java";
//
//        // Get a session.  Use a blank Properties object.
//        Session session = Session.getInstance(new Properties());
//        //Session session = Session.getDefaultInstance(new Properties(), null);
//        try {
//
//            // Get a Store object
//            Store store = session.getStore("pop3");
//            store.connect(host, user, password);
//
//            // Get "INBOX"
//            Folder fldr = store.getFolder("INBOX");
//            fldr.open(Folder.READ_WRITE);
//            int count = fldr.getMessageCount();
//            System.out.println(count  + " total messages");
//
//            // Message numebers start at 1
//            for(int i = 1; i <= count; i++) {
//								// Get  a message by its sequence number
//                Message m = fldr.getMessage(i);
//
//                // Get some headers
//                Date date = m.getSentDate();
//                Address [] from = m.getFrom();
//                String subj = m.getSubject();
//                String mimeType = m.getContentType();
//                System.out.println(date + "\t" + from[0] + "\t" +
//                                    subj + "\t" + mimeType);
//            }
//
//            // Search for e-mails by some subject substring
//            String pattern = subjectSubstringToSearch;
//            SubjectTerm st = new SubjectTerm(pattern);
//            // Get some message references
//            Message [] found = fldr.search(st);
//
//            System.out.println(found.length +
//                                " messages matched Subject pattern \"" +
//                                pattern + "\"");
//
//            for (int i = 0; i < found.length; i++) {
//                Message m = found[i];
//                // Get some headers
//                Date date = m.getSentDate();
//                Address [] from = m.getFrom();
//                String subj = m.getSubject();
//                String mimeType = m.getContentType();
//                System.out.println(date + "\t" + from[0] + "\t" +
//                                    subj + "\t" + mimeType);
//
//                Object o = m.getContent();
//                if (o instanceof String) {
//                    System.out.println("**This is a String Message**");
//                    System.out.println((String)o);
//                }
//                else if (o instanceof Multipart) {
//                    System.out.print("**This is a Multipart Message.  ");
//                    Multipart mp = (Multipart)o;
//                    int count3 = mp.getCount();
//                    System.out.println("It has " + count3 +
//                        " BodyParts in it**");
//                    for (int j = 0; j < count3; j++) {
//                        // Part are numbered starting at 0
//                        BodyPart b = mp.getBodyPart(j);
//                        String mimeType2 = b.getContentType();
//                        System.out.println( "BodyPart " + (j + 1) +
//                                            " is of MimeType " + mimeType);
//
//                        Object o2 = b.getContent();
//                        if (o2 instanceof String) {
//                            System.out.println("**This is a String BodyPart**");
//                            System.out.println((String)o2);
//                        }
//                        else if (o2 instanceof Multipart) {
//                            System.out.print(
//                                "**This BodyPart is a nested Multipart.  ");
//                            Multipart mp2 = (Multipart)o2;
//                            int count2 = mp2.getCount();
//                            System.out.println("It has " + count2 +
//                                "further BodyParts in it**");
//                        }
//                        else if (o2 instanceof InputStream) {
//                            System.out.println(
//                                "**This is an InputStream BodyPart**");
//                        }
//                    } //End of for
//                }
//                else if (o instanceof InputStream) {
//                    System.out.println("**This is an InputStream message**");
//                    InputStream is = (InputStream)o;
//                    // Assumes character content (not binary images)
//                    int c;
//                    while ((c = is.read()) != -1) {
//                        System.out.write(c);
//                    }
//                }
//
//                // Uncomment to set "delete" flag on the message
//                //m.setFlag(Flags.Flag.DELETED,true);
//
//            } //End of for
//
//            // "true" actually deletes flagged messages from folder
//            fldr.close(true);
//            store.close();
//
//        }
//        catch (MessagingException mex) {
//            // Prints all nested (chained) exceptions as well
//            mex.printStackTrace();
//        }
//        catch (IOException ioex) {
//            ioex.printStackTrace();
//        }

    }


} //End of class
