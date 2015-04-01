package login;

import java.awt.*;
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
	
    private static void addComponentsToPane(Container pane, final InboxTable model) throws IOException {
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

		table.setShowHorizontalLines(false);
		table.setShowVerticalLines(false);
		
        Insets insets = pane.getInsets();
        scrollPane.setBounds(insets.left, insets.top + 400, 800, 200);
        scroll.setBounds(insets.left + 150, insets.top, 650, 400);

        pane.setBackground(new Color(240,255,255));
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

    public static void createAndShowLogin(InboxTable model) {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        try {
			addComponentsToPane(frame.getContentPane(),model);
		} catch (IOException e) {
			e.printStackTrace();
		}
        frame.setSize(800,600);
        frame.setVisible(true);
    }
}
