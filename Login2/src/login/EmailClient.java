package login;

import java.io.Serializable;
import java.util.*;

import javax.mail.*;
import javax.mail.internet.*;

/**
 * <p> This class handles the backend support for the simple mail client </p>
 * @author Matthew Furlong
 * @author Rj Larsen
 * @see Folder
 * @see Message
 * @see String
 * @see Session
 * @see Properties
 */
public class EmailClient implements Serializable {

	private static final long serialVersionUID = 1193542647330340001L;
	private static String password = "";
	private static String email = ""; 
	private static String google_host = "smtp.gmail.com";
	private static Message[] messages;
	private static Folder[] fs;
	private static Session session;
	private static Properties props;
	private static EmailClient app;
	
	/**
	 * <p> This implementation uses a private constructor to create 
	 * a singleton pattern. It creates a session and adds the necessary
	 * attributes to the properties variable of the class </p>
	 * @param - host the host server
	 * @param - port the host port
	 */
	
	private EmailClient(String host, String port) {
		google_host = host;
		props = new Properties();
		props.put("mail.smtp.host", google_host);
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.port", port);
	    session = Session.getInstance(props, new javax.mail.Authenticator() {
	    	protected PasswordAuthentication getPasswordAuthentication() {
	    		return new PasswordAuthentication(email, password);
	    	}
		});
	}
	
	/**
	 * @param - host used to specify host for constructor
	 * @param - port used to specify port for constructor
	 * @return EmailClient app
	 */
	public static EmailClient getInstance(String host, String port) {
		if(app == null) {
			app = new EmailClient(host, port);
		}
		return app;
	}
	
	/**
	 * <p> This function will send a message using the already 
	 * preconfigured host and port</p>
	 * @param subject - subject of the message
	 * @param body - body text for message
	 * @param recipient - to whom the message will be sent
	 */
	public void sendMessage(String subject, String body, String recipient) {
		try {
	        Message msg = new MimeMessage(session);
	        msg.setFrom(new InternetAddress(email));
	        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
	        msg.setSubject(subject);
	        msg.setSentDate(new Date());
	        msg.setText(body);
	        Transport.send(msg, email, password);
	        System.out.println("done");
	    } catch (MessagingException e) {
	        System.out.println("The email failed due to the following exception: " + e);
	    }
	}
	
	/**
	 * <p> Checks to ensure that the email credentials are correct </p>
	 * @param un - username for email account
	 * @param pw - password for email account
	 * @return boolean
	 */
	public static boolean validCreds(String un,String pw){
		email = un;
		password = pw;
		try{
			Store store = session.getStore("imaps");
			store.connect(google_host, un, pw);
			store.close();
		} catch(Exception ex) {
			return false;
		}
		return true;
	}
	
	/**
	 * <p> This function reads the emails of particular gmail folder
	 * and returns these emails in an array</p>
	 * @param boxName - name of email folder to be read
	 * @return Array of type Message
	 * @see Message
	 */
	public Message[] readEmails(String boxName) {
		try {
			Store store = session.getStore("imaps");
			store.connect(google_host, email, password);
			fs = store.getFolder("[Gmail]").list();
			EmailMainScreen.setFolders(fs);
			Folder folder = store.getFolder("[Gmail]").getFolder(boxName);
			if(folder != null){
				folder.open(Folder.READ_WRITE);
				messages = folder.getMessages();
			}
		} catch (MessagingException me) {
			System.out.println("Server temporarily unavailable. Please try again.");
		} catch (Exception e) {}
		return messages;
	}
}



