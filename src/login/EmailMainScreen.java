//Matthew Furlong and Robert Larsen
package login;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URL;
import java.util.Properties;

import javax.mail.*;
import javax.mail.Flags.Flag;
import javax.mail.internet.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

/**
* 
* @author Matthew Furlong
* @author Rj Larsen
* @see JFrame
* @see JTextPane
* @see Folder
* @see Font
* @see String
* @see JScrollPane
* @see JButton
* @see JLabel
* @see Container
* @see JTextField
* @see JTable
* <p> This class provides the gui for the simple mail application </p>
*/

public class EmailMainScreen {
	
    private static JFrame frame = new JFrame("SimpleMail");
    private static JTextPane textPane;
    private static Folder[] folders;
    private static int menuHeight;
	private static Font fontBold;
    private static String selectedFolder = "All Mail";
    private static JScrollPane scrollPane;
    private static JScrollPane scroll;
    private static JButton compose;
    private static JLabel l_from;
    private static JLabel l_sub;
    private static JButton reply;
    private static JTextField from;
    private static JTextField subject;
    private static Container pane;
    private static JTable table;
    private static JFrame contacts;
    
    /**
     * <p> Statically initializes font for this class
     */
    
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
   }

    private EmailMainScreen(){}
    
    /**
     * 
     * @param folder Array of folders from email client object
     * 
     * <p> Setter function for folder array </p>
     */
    public static void setFolders(Folder[] folder){
    	folders = folder;
    }
	
    /**
     * 
     * @param model final InboxTable 
     * @throws IOException
     * 
     * <p> Creates main gui </p>
     */
    public static void setComponentsPane(final InboxTable model) throws IOException {
    	pane = frame.getContentPane();
    	menuHeight = 2;
        pane.removeAll();
        pane.revalidate();
    	pane.setLayout(null);
        table = new JTable(model);
        l_from = new JLabel("from:");
        l_sub = new JLabel("subject:");
        reply = new JButton("Reply");
        from = new JTextField(20);
        subject = new JTextField(20);
        textPane = new JTextPane();
        scrollPane = new JScrollPane(textPane);
        scroll = new JScrollPane(table);
        compose = new JButton("Compose");
        textPane.setEditable(false);
        from.setEditable(false);
        subject.setEditable(false);
        TableColumn column;
		for(int i = 0; i < InboxTable.COLUMN_COUNT; i++) {
			column = table.getColumnModel().getColumn(i);
			if(i == 0) {
				column.setPreferredWidth(350);
				column.setHeaderValue("Subject");
			}
			if(i == 1) {
				column.setPreferredWidth(250);
				column.setHeaderValue("Sender");
			}
			if(i == 2) {
				column.setPreferredWidth(50);
				column.setHeaderValue("Date");
			}
		}
		
		table.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				if(e.getClickCount() == 2) {
					if(table.getSelectedRow() >= 0 && model.getRow(table.getSelectedRow()) != null){
			    		replyScreen(true, "Reply");
			    	}
				}
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
		
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				setMessage(model, table.getSelectedRow());
				try {
					if(table.getSelectedRow() >= 0 && model.getRow(table.getSelectedRow()) != null){
						model.getRow(table.getSelectedRow()).setFlag(Flag.SEEN, true);
					}
				} catch (MessagingException e1) {}
			}
		});
		
		for(int i = 0; i < folders.length; i++){
			final JButton newb = new JButton(folders[i].getName());
	        newb.setBounds(pane.getInsets().left, pane.getInsets().top + (menuHeight++ * 30), 150, 30);
			pane.add(newb);
			if(newb.getText().compareToIgnoreCase(selectedFolder) == 0){
				newb.setBackground(Color.LIGHT_GRAY);
			} else {
				newb.setBackground(Color.WHITE);
			}
			newb.addActionListener( new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					EmailClient app = EmailClient.getInstance("","");
					selectedFolder = newb.getText();
					try {
						setComponentsPane(new InboxTable(app.readEmails(newb.getText())));
					} catch (IOException e1) {}
				}
			});
		}
		table.setShowHorizontalLines(false);
		table.setShowVerticalLines(false);
        Insets insets = pane.getInsets();
        scrollPane.setBounds(insets.left, insets.top + 400, 800, 200);
        scroll.setBounds(insets.left + 150, insets.top, 650, 350);
        reply.setBounds(insets.left + 10, insets.top + 355, 120, 40);
        l_from.setBounds(insets.left + 150, insets.top + 350, 75, 25);
        l_sub.setBounds(insets.left + 150, insets.top + 375, 75, 25);
        from.setBounds(insets.left + 225, insets.top + 350, 570, 25);
        subject.setBounds(insets.left + 225, insets.top + 375, 570, 25);
		compose.setBounds(insets.left + 25, insets.top + 10, 100, 40);
        URL fontUrl;
        Font font = Font.getFont("FreeSans");
		try {
			fontUrl = new URL("http://ff.static.1001fonts.net/o/p/open-sans.regular.ttf");
	        font = Font.createFont(Font.TRUETYPE_FONT, fontUrl.openStream());
	        font = font.deriveFont(Font.PLAIN,12);
	        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	        ge.registerFont(font);
		} catch (Exception e1) {
			font = Font.getFont("FreeSans");
		}
		compose.addActionListener( new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				getCompose(model);
			}
		});
		reply.addActionListener( new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
		    	if(table.getSelectedRow() >= 0 && model.getRow(table.getSelectedRow()) != null){
		    		replyScreen(true, "Reply");
		    	}
			}
		});
        pane.setBackground(new Color(240,255,255));
        reply.setFont(font);
        from.setBackground(Color.WHITE);
        subject.setBackground(Color.WHITE);
        reply.setBackground(new Color(179,212,230));
		compose.setBackground(new Color(255,50,50));
		compose.setForeground(Color.WHITE);
		pane.add(compose);
        pane.add(scrollPane);
        pane.add(scroll);
        pane.add(reply);
        pane.add(l_from);
        pane.add(from);
        pane.add(l_sub);
        pane.add(subject);
        
    }
    
    /**
     * 
     * @param model final InboxTable 
     * 
     * <p> This function opens the screen to compose a message upon pressing the compose button</p>
     */
    private static void getCompose(final InboxTable model){
    	if(contacts != null){
    		contacts.setVisible(true);
    		return;
    	}
    	Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    	contacts = new JFrame("Compose");
    	contacts.setSize(400, 200);
    	contacts.setLocation(dim.width/2-contacts.getSize().width/2, dim.height/2-contacts.getSize().height/2);
    	JPanel pane = new JPanel();
    	JButton nc = new JButton("New Contact");
    	JButton ec = new JButton("Existing Contact");
    	contacts.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		pane.setBounds(contacts.getBounds());
		contacts.add(pane);
		BorderLayout layout = new BorderLayout();
		pane.setLayout(layout);
		nc.setPreferredSize(new Dimension(200, 200));
		ec.setPreferredSize(new Dimension(200, 200));
		pane.add(nc,BorderLayout.LINE_START);
		pane.add(ec,BorderLayout.LINE_END);
		contacts.setVisible(true);
		nc.addActionListener( new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				replyScreen(false,"Compose");
				contacts.setVisible(false);
			}
		});
		ec.addActionListener( new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				ContactsScreen.getInstance(model).setVisible(true);
				contacts.setVisible(false);
			}
		});
    }
    
    /**
     * 
     * @param model InboxTable
     * @param row integer from tables selected row
     * 
     * <p> This function displays the message into the lower text field of the gui after parsing it properly </p>
     */
    private static void setMessage(InboxTable model, int row) {
    	final StringBuilder sb = new StringBuilder();
		String fromStr = "";
		Address lastAddr = new InternetAddress();
		try {
			if(row >= 0 && model.getRow(row) == null){}
			else if(row >= 0 &&model.getRow(row).getContent() instanceof String){
				for(Address a : model.getRow(row).getFrom()){
					if(a != lastAddr){
						fromStr += a.toString() + "; ";
						lastAddr = a;
					}
				}
				from.setText(fromStr);
				subject.setText(model.getRow(row).getSubject());
				sb.append(model.getRow(row).getContent());
			}
			else if(row >= 0){
			MimeMultipart mmp = (MimeMultipart) model.getRow(row).getContent();
			for (int i=0; i<mmp.getCount(); i++) {
			    BodyPart mbp = mmp.getBodyPart(i);
			    if (mbp.getContent() instanceof String)
			    {
			    	for(Address a : model.getRow(row).getFrom()){
						if(a != lastAddr){
							fromStr += a.toString() + "; ";
							lastAddr = a;
						}
					}
					from.setText(fromStr);
					subject.setText(model.getRow(row).getSubject());
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
			        for(Address a : model.getRow(row).getFrom()){
						if(a != lastAddr){
							fromStr += a.toString() + "; ";
							lastAddr = a;
						}
					}
					from.setText(fromStr);
					subject.setText(model.getRow(row).getSubject());
			        sb.append(bstr.toString());
			    }
			}
		}
		} catch (Exception e) {}
		textPane.setText(sb.toString());
		if(textPane.getText().equals("")){
			from.setText(sb.toString());
			subject.setText(sb.toString());
		}
		if(textPane.getText().startsWith("Content-Type")){
			textPane.setText(textPane.getText().substring(textPane.getText().indexOf("\n") + 5));
		}
		textPane.setCaretPosition(0);
	}

    /**
     * 
     * @param un username for email
     * @param pw password for email
     * 
     *<p> This function displays the email client main screen and hides the login screen </p>
     */
    public static void show(String un, String pw) {
    	Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        LoginScreen.hide();
        frame.setSize(800,600);
    	frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
        frame.setVisible(true);
    }
    
    /**
     * 
     * @param a recipient of message
     * 
     * <p> This function opens the gui to reply to a message </p>
     */
    public static void sendTo(String a){
    	replyScreen(false, "Compose", a);
    }
    
    /**
     * 
     * @param checkReply checks whether it is a reply or not
     * @param intent Title of frame and string displayed in textfield
     * 
     *  
     */
    private static void replyScreen(boolean checkReply, String intent){
    	replyScreen(checkReply, intent, "");
    }
    
    /**
     * 
     * @param checkReply checks whether it is a reply or not
     * @param intent Title of frame and string displayed in textfield
     * @param recip To whome the message is being sent
     * 
     * <p> This message creates the gui to reply to a message which is then displayed by the sendTo function </p>
     * 
     */
    private static void replyScreen(boolean checkReply, String intent, String recip) {
    	final EmailClient app = EmailClient.getInstance("","");
		try {
	    	Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
			final JFrame newFrame = new JFrame(intent);
			newFrame.setSize(400, 400);
	    	newFrame.setLocation(dim.width/2-newFrame.getSize().width/2, dim.height/2-newFrame.getSize().height/2);
			newFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			newFrame.getContentPane().setBackground(new Color(240,255,255));
			Container contentPane = newFrame.getContentPane();
			SpringLayout layout = new SpringLayout();
			contentPane.setLayout(layout);		
			JLabel to = new JLabel("To: ");
			final JTextField field = new JTextField(25);
			to.setFont(fontBold);
			JLabel subject = new JLabel("Subject: ");
			final JTextField subject_field = new JTextField(25);
			subject.setFont(fontBold);
			InboxTable model = (InboxTable)table.getModel();
			if(checkReply == true && table.getSelectedRow() >= 0 && model.getRow(table.getSelectedRow()) != null) {
				Message msg = model.getRow(table.getSelectedRow()).reply(true);
				subject_field.setText(msg.getSubject());
				field.setText(msg.getRecipients(Message.RecipientType.TO)[0].toString());
	    	}
			else if(recip != ""){
				field.setText(recip);
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
					area.setText("");
				}
				@Override
				public void mouseEntered(MouseEvent e) {}
				@Override
				public void mouseExited(MouseEvent e) {}
				@Override
				public void mousePressed(MouseEvent e) {}
				@Override
				public void mouseReleased(MouseEvent e) {}
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
