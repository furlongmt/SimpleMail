package cu.cs.cpsc215.project3;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.mail.BodyPart;
import javax.mail.Flags.Flag;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumn;

public class EmailFrame extends JFrame implements TableModelListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7504192480955713515L;
	private static final int width = 800;
	private static final int height = 600;
	private static final int MAX_TABLE_WIDTH = width / 2;
	private static final int MAX_TABLE_HEIGHT = height / 2;
	//private JTextPane textArea;
	private JTextPane textPane;
	private JScrollPane scrollPane;
	private JTable table;
	
	public EmailFrame(InboxTable model) {
		super("SimpleMail");
		//this.setLocationRelativeTo(null);
		tableSetup(model);
		messageViewSetup();
		setup();
	}
	
	public void setMessage(InboxTable model, int row) {
		String msg = "";
		
		try {
			Multipart multipart = (Multipart) model.getRow(row).getContent();
			
			for(int j = 0; j < multipart.getCount(); j++) {
				
				BodyPart body = multipart.getBodyPart(j);
				
				
				
				msg += body.getContent().toString();
			}
			
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		textPane.setText(msg);
	}
	
	public void messageViewSetup() {
		textPane = new JTextPane();
		scrollPane = new JScrollPane(textPane);
		
		scrollPane.setPreferredSize(new Dimension(width, height/2));

		textPane.setContentType("text/html");
		//textPane.setText(msg);
		textPane.setEditable(false);
		
		this.getContentPane().add(scrollPane, BorderLayout.SOUTH);
	}
	
	private void tableSetup(final InboxTable model) {
		table = new JTable(model);
		JScrollPane scroll = new JScrollPane(table);
		
		TableColumn column;
		for(int i = 0; i < InboxTable.COLUMN_COUNT; i++) {
			column = table.getColumnModel().getColumn(i);
			if(i == 0) {
				column.setPreferredWidth(width / 2);
				column.setHeaderValue("Subject");
			}
			if(i == 1) {
				column.setPreferredWidth(width / 9);
				column.setHeaderValue("Sender");
			}
			if(i == 2) {
				column.setPreferredWidth(width / 9);
				column.setHeaderValue("Date");
			}
		}

		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub
				System.out.println(table.getSelectedRow());
				setMessage(model, table.getSelectedRow());
				try {
					model.getRow(table.getSelectedRow()).setFlag(Flag.SEEN, true);
				} catch (MessagingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		table.setShowHorizontalLines(false);
		table.setShowVerticalLines(false);
		
		table.setPreferredSize(new Dimension(width, MAX_TABLE_HEIGHT));
		
		this.getContentPane().add(scroll, BorderLayout.NORTH);
	}
	
	private void setup() {
		centerApp();
		this.pack();
		this.setLayout(new BorderLayout());
		this.setSize(width, height);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	private void centerApp() {
		Dimension windowSize = getSize();
		GraphicsEnvironment graphics = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Point center = graphics.getCenterPoint();
		
		int x = center.x - windowSize.width / 2 - width / 2;
		int y = center.y - windowSize.height / 2 - height / 2;
		
		this.setLocation(x,y);
	}

	@Override
	public void tableChanged(TableModelEvent e) {
		// TODO Auto-generated method stub
		
	}
}
