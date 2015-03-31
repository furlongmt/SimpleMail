package cu.cs.cpsc215.project3;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailClient implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1193542647330340001L;
	private String password = "wordpass1";
	private String email = "cu2150tester@gmail.com"; 
	private final static String google_host = "smtp.gmail.com";
	private ArrayList<Message> messages = new ArrayList<Message>();
	private Folder[] folders;
	
	private Properties setProps() {
		Properties props = new Properties();
	    props.put("mail.smtp.host", google_host);
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.starttls.enable", "true");
	    props.put("mail.smtp.port", "587");
	    
	    return props;
	}
	
	private Session createSession(Properties props) {
		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
    		protected PasswordAuthentication getPasswordAuthentication() {
    				return new PasswordAuthentication(email, password);
    		}
		});
		
		return session;
	}
	
	public void sendMessage(String subject, String body, String recipient) {
	    
	   Properties props = setProps();
	   
	   Session session = createSession(props);
	    
	    
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
	
	public ArrayList<Message> readEmails(String boxName) {
		Properties props = setProps();
		
		try {
			//props.oad(new FileInputStream(new File("")))
			Session session = createSession(props);
			
			Store store = session.getStore("imaps");
			store.connect(google_host, email, password);
			
			/*Folder inbox = store.getFolder("[Gmail]/Sent Mail");
			inbox.open(Folder.READ_ONLY);
			int msgCount = inbox.getMessageCount();
			*/
			
			folders = store.getDefaultFolder().list("*");
			
			//uncomment if you want to see the list of folders
			/*for(Folder folder : folders)
				if ((folder.getType() & javax.mail.Folder.HOLDS_MESSAGES) != 0) {
		            System.out.println(folder.getFullName() + ": " + folder.getMessageCount());
		        }
			*/
			
			for(Folder folder: folders) {
				if(folder.getFullName().toLowerCase().contains(boxName.toLowerCase())) {
					folder.open(Folder.READ_WRITE);
					messages = new ArrayList<Message>(Arrays.asList(folder.getMessages()));
				}
			}
			//System.out.println("Total Message = " + msgCount);
			
			//messages = new ArrayList<Message>(Arrays.asList(inbox.getMessages()));
			
			//System.out.println("---------------");
			
			//for(Message temp: messages)
			//	System.out.println("Mail Subject: " + temp.getSubject());
			
			//inbox.close(true);
			//store.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
//		Collections.sort(messages, new MessageComparator());
		
		Collections.reverse(messages);
		
		return messages;
	}
}


