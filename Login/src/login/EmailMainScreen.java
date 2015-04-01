package login;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.mail.*;
import javax.mail.Flags.Flag;
import javax.mail.internet.MimeMultipart;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

public class EmailMainScreen {
	
    private static JFrame frame = new JFrame("Email");
    private static JTextPane textPane;
    private static Folder[] folders;
    private static String username;
    private static String password;
    private static int menuHeight = 0;
    
    public static void setFolders(Folder[] folder){
    	folders = folder;
    }
	
    public static void setComponentsPane(final InboxTable model) throws IOException {
    	Container pane = frame.getContentPane();
    	menuHeight = 0;
    	pane.removeAll();
    	pane.setLayout(null);
        textPane = new JTextPane();
        JScrollPane scrollPane = new JScrollPane(textPane);
        final JTable table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);

        textPane.setEditable(false);
        
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
		
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				setMessage(model, table.getSelectedRow());
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
			if(folders[i].getName() == model){
				newb.setBackground(Color.WHITE);
			}
			newb.addActionListener( new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					EmailClient app = new EmailClient();
					try {
						setComponentsPane(new InboxTable(app.readEmails(newb.getText(),username,password)));
					} catch (IOException e1) {
					}
				}
			});
			
		}

		table.setShowHorizontalLines(false);
		table.setShowVerticalLines(false);
		
        Insets insets = pane.getInsets();
        scrollPane.setBounds(insets.left, insets.top + 400, 800, 200);
        scroll.setBounds(insets.left + 150, insets.top, 650, 400);

        pane.setBackground(new Color(240,255,255));
        scroll.setBackground(new Color(240,255,255));
        pane.add(scrollPane);
        pane.add(scroll);
        
    }
    
    private static void setMessage(InboxTable model, int row) {
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

    public static void show(String un, String pw) {
    	username = un;
    	password = pw;
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        LoginScreen.hide();
        frame.setSize(800,600);
        frame.setVisible(true);
    }
}
