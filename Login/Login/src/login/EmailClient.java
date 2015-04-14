//Matthew Furlong and Robert Larsen
package login;

import java.io.Serializable;
import java.util.*;

import javax.mail.*;
import javax.mail.internet.*;

public class EmailClient implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1193542647330340001L;
	private static String password = "";
	private static String email = ""; 
	private final static String google_host = "smtp.gmail.com";
	private static Message[] messages;
	private static Folder[] fs;
	private static Session session;
	private static Properties props;
	private static EmailClient app;
	
	private EmailClient() {
		props = new Properties();
	    props.put("mail.smtp.host", google_host);
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.starttls.enable", "true");
	    props.put("mail.smtp.port", "587");
	    
	    session = Session.getInstance(props, new javax.mail.Authenticator() {
    		protected PasswordAuthentication getPasswordAuthentication() {
    				return new PasswordAuthentication(email, password);
    		}
		});
	}
	
	public static EmailClient getInstance() {
		if(app == null) {
			app = new EmailClient();
		}
		
		return app;
	}
	
	public void sendMessage(String subject, String body, String recipient) {
		try {
	        Message msg = new MimeMessage(session);
	        msg.setFrom(new InternetAddress(email));
	        msg.setRecipients(Message.RecipientType.TO,
	                          InternetAddress.parse(recipient));
	        msg.setSubject(subject);
	        msg.setSentDate(new Date());
	        msg.setText(body);
	        Transport.send(msg, email, password);
	        
	        System.out.println("done");
	    } catch (MessagingException e) {
	        System.out.println("The email failed due to the following exception: " + e);
	    }
	}
	
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
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return messages;
	}
}


