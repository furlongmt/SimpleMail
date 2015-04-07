package login;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import javax.mail.*;
import javax.mail.Flags.Flag;
import javax.mail.internet.MimeMultipart;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.text.BadLocationException;

public class EmailMainScreen {
	
    private static JFrame frame = new JFrame("Email");
    private static JTextPane textPane;
    private static Folder[] folders;
    private static String username;
    private static String password;
    private static int menuHeight = 0;
    private static Font fontBold;
    private static JTable table;
    private static InboxTable model;
    private static JTextField field;
    
    static {
    	 URL fontUrl;
         fontBold = Font.getFont("FreeSans");
 		try {
 			fontUrl = new URL("http://ff.static.1001fonts.net/o/p/open-sans.regular.ttf");
 	        fontBold = Font.createFont(Font.TRUETYPE_FONT, fontUrl.openStream());
 	        fontBold = fontBold.deriveFont(Font.PLAIN,12);
 	        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
 	        ge.registerFont(fontBold);
 		} catch (Exception e1) {
 			fontBold = Font.getFont("FreeSans");
 		}
        fontBold = new Font(fontBold.getFontName(),Font.BOLD,13);
         
        EmailClient app = EmailClient.getInstance();
         
        model = new InboxTable(app.readEmails("All Mail",username,password));
        table = new JTable(model);
    }
    
    private static void contactsButton() {
    	final JButton contact = new JButton("Contacts");
    	
    	contact.setSize(200, 200);
    	table.add(contact);
    	
    	contact.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub	    		
	    		JFrame contactFrame = ContactTableFrame.getInstance();
	    		
	    		contactFrame.setVisible(true);
			}
    	   		
    	});
    }
    
/*   private static void deleteMessage() {
    	final JButton delete = new JButton("Delete");
    	
    	delete.setSize(200, 200);
    	table.add(delete);
    	
    	delete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Message msg = model.getRow(table.getSelectedRow());
    			
				try {
					Folder f = msg.getFolder();
					msg.setFlag(Flag.DELETED, true);
					f.close(false);
					f.open(Folder.READ_WRITE);
				} catch (MessagingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

    			try {
					EmailMainScreen.setComponentsPane(model);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
    			model.fireTableDataChanged();
			}
    	});
    }
   */
    public static void setFolders(Folder[] folder){
    	folders = folder;
    }
    
    public static void setComponentsPane(final InboxTable tablemodel) throws IOException {
    	Container pane = frame.getContentPane();
    	menuHeight = 0;
    	pane.removeAll();
    	pane.setLayout(null);
        textPane = new JTextPane();
        JScrollPane scrollPane = new JScrollPane(textPane);
        table = new JTable(tablemodel);
        JScrollPane scroll = new JScrollPane(table);

        textPane.setEditable(false);
        
        TableColumn column;
		for(int i = 0; i < InboxTable.COLUMN_COUNT; i++) {
			column = table.getColumnModel().getColumn(i);
			if(i == 0) {
				column.setPreferredWidth(350);
				column.setHeaderValue("<html><b>Subject</b></html>");
			}
			if(i == 1) {
				column.setPreferredWidth(250);
				column.setHeaderValue("<html><b>Sender</b></html>");
			}
			if(i == 2) {
				column.setPreferredWidth(50);
				column.setHeaderValue("<html><b>Date</b></html>");
			}
		}
		
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				setMessage(table.getSelectedRow());
				try {
					model.getRow(table.getSelectedRow()).setFlag(Flag.SEEN, true);
				} catch (MessagingException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		for(int i = 0; i < folders.length; i++){
			final JButton newb = new JButton(folders[i].getName());
	        newb.setBounds(pane.getInsets().left, pane.getInsets().top + (menuHeight++ * 30), 150, 30);
			pane.add(newb);
//			if(folders[i].getName() == model){
				newb.setBackground(Color.WHITE);
//			}
			newb.addActionListener( new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					EmailClient app = EmailClient.getInstance();
					try {
						setComponentsPane(new InboxTable(app.readEmails(newb.getText(),username,password)));
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			});
			
		}
		
		table.setShowHorizontalLines(false);
		table.setShowVerticalLines(false);

		//These are the buttons that need to be changed
		//deleteMessage();
		contactsButton();
		//reply();
		newMessage();
		
        Insets insets = pane.getInsets();
        scrollPane.setBounds(insets.left, insets.top + 400, 800, 200);
        scroll.setBounds(insets.left + 150, insets.top, 650, 400);

        pane.setBackground(new Color(240,255,255));
        scroll.setBackground(new Color(240,255,255));
        pane.add(scrollPane);
        pane.add(scroll);
        
       
    }
    
    private static void setMessage(int row) {
    	final StringBuilder sb = new StringBuilder();
		try {
			MimeMultipart mmp = (MimeMultipart) model.getRow(row).getContent();
			for (int i=0; i<mmp.getCount(); i++) {
			    BodyPart mbp = mmp.getBodyPart(i);
			    if (mbp.getContent() instanceof String)
			    {
				    sb.append(mbp.getContent().toString());
			    }
			    else if (mbp.getContent() instanceof Multipart)
			    {
			    	ByteArrayOutputStream bstr = new ByteArrayOutputStream();
			        Multipart mp = (Multipart)mbp.getContent();
			        for(int j = 0; j < mp.getCount();j++){
			        	BodyPart bp = mp.getBodyPart(j);
			        	bp.writeTo(bstr);
			        }
			        sb.append(bstr.toString());
			    }
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		textPane.setText(sb.toString());
		if(textPane.getText().startsWith("Content-Type")){
			textPane.setText(textPane.getText().substring(textPane.getText().indexOf("\n") + 5));
		}
	
		textPane.setCaretPosition(0);
	}
    
    private EmailMainScreen(){
    	
    }
    
    private static void newMessage() {
    	final JButton compose =  new JButton("Compose");
    	
    	compose.setSize(100, 100);
    	table.add(compose);
    	
    	compose.addActionListener( new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				replyScreen(false,"Compose");
			}
		});
    }
    
    private static void reply() {
    	final JButton reply = new JButton("Reply");
    	
    	reply.setSize(50, 50);
    	table.add(reply);
    	
    	reply.addActionListener( new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				replyScreen(true, "Reply");
			}
		});
    }
    
    public static void show(String un, String pw) {
    	username = un;
    	password = pw;
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        LoginScreen.hide();
        frame.setSize(800,600);
        frame.setVisible(true);
    }
    
    private static void replyScreen(boolean checkReply, String intent) {
    	final EmailClient app = EmailClient.getInstance();
		try {
			final Message msg;
			
			final JFrame newFrame = new JFrame(intent);
			newFrame.setSize(400, 400);
			newFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			newFrame.getContentPane().setBackground(new Color(240,255,255));
			//newFrame.s(new Color(240,255,255));

			Container contentPane = newFrame.getContentPane();
			SpringLayout layout = new SpringLayout();
			contentPane.setLayout(layout);
								
			JLabel to = new JLabel("To: ");
			field = new JTextField(25);
			to.setFont(fontBold);			
			
			JLabel subject = new JLabel("Subject: ");
			final JTextField subject_field = new JTextField(25);
			subject.setFont(fontBold);
			
			if(checkReply == true) {
				msg = model.getRow(table.getSelectedRow()).reply(true);
				subject_field.setText(msg.getSubject());
				field.setText(msg.getRecipients(Message.RecipientType.TO)[0].toString());
	    	}
			
			final JTextArea area = new JTextArea(intent, 15, 31);
			JScrollPane pane = new JScrollPane(area);
			area.setEditable(true);
			area.setLineWrap(true);
			area.setWrapStyleWord(true);
			pane.setSize(200, 200);
			pane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			pane.setForeground(Color.WHITE);
			
			area.addMouseListener(new MouseListener() {

				@Override
				public void mouseClicked(MouseEvent arg0) {
					// TODO Auto-generated method stub
					area.setText("");
				}

				@Override
				public void mouseEntered(MouseEvent e) {
					// TODO Auto-generated method stub
				}

				@Override
				public void mouseExited(MouseEvent e) {
					// TODO Auto-generated method stub
				}

				@Override
				public void mousePressed(MouseEvent e) {
					// TODO Auto-generated method stub
				}

				@Override
				public void mouseReleased(MouseEvent e) {
					// TODO Auto-generated method stub
				}
			});
			
			JButton send = new JButton("Send");
			
			send.addActionListener( new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					app.sendMessage(subject_field.getText(), area.getText(), field.getText());
					newFrame.dispose();
				}
				
			});
			
			layout.putConstraint(SpringLayout.WEST, to, 20, SpringLayout.WEST, contentPane);
		    layout.putConstraint(SpringLayout.NORTH, to, 27, SpringLayout.NORTH, contentPane);
		    layout.putConstraint(SpringLayout.NORTH, field, 25, SpringLayout.NORTH, contentPane);
		    layout.putConstraint(SpringLayout.WEST, field, 51, SpringLayout.EAST, to);
		    
		    layout.putConstraint(SpringLayout.WEST, subject, 20, SpringLayout.WEST, contentPane);
		    layout.putConstraint(SpringLayout.WEST, subject_field, 15, SpringLayout.EAST, subject);
		    layout.putConstraint(SpringLayout.NORTH, subject, 20, SpringLayout.SOUTH, to);
		    layout.putConstraint(SpringLayout.NORTH, subject_field, 18, SpringLayout.SOUTH, field);
		    
		    layout.putConstraint(SpringLayout.WEST, pane, 23, SpringLayout.WEST, contentPane);
			layout.putConstraint(SpringLayout.NORTH, pane, 20, SpringLayout.SOUTH, subject);
			
		    layout.putConstraint(SpringLayout.SOUTH, send, 0, SpringLayout.SOUTH, contentPane);
		    layout.putConstraint(SpringLayout.EAST, send, 0, SpringLayout.EAST, contentPane);

		    contentPane.add(pane);
		    contentPane.add(subject);
		    contentPane.add(subject_field);
			contentPane.add(send);
			contentPane.add(to);
			contentPane.add(field);
			newFrame.setVisible(true);
		} catch (MessagingException e1) {
		}
    } 

}
