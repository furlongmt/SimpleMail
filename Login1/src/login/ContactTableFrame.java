package login;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpringLayout;
import javax.swing.table.TableColumn;

public class ContactTableFrame extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static int ROW_PLACE = 3;
	private static int ROW_FINAL = 4;
	private static JTable table;
//	private List<Person> people = new ArrayList<Person>();
	private static ContactTableFrame database;
	private static ContactTableModel model = new ContactTableModel();
	private static Font fontBold;
	
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
	
	public static ContactTableFrame getInstance() {
		if(database == null) {
			database = new ContactTableFrame("Contacts");
		}
		return database;
	}
	
	public ContactTableModel getModel() {
		return model;
	}
	
	private ContactTableFrame(String title) {
		super(title);
		this.setSize(800, 600);
		
		//close window when you click 'x'
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
		
		//create table and add it to content pane
		table = new JTable(model);
		JScrollPane scroll = new JScrollPane(table);
		
		scroll.setPreferredSize(new Dimension(getWidth(), getHeight() - 100));
        scroll.setBackground(new Color(240,255,255));
        
		TableColumn column;
		for(int i = 0; i < ContactTableModel.COLUMN_COUNT; i++) {
			column = table.getColumnModel().getColumn(i);
			if(i == 0) {
				column.setPreferredWidth(125);
				column.setHeaderValue("<html><b>First Name</b></html>");
			}
			if(i == 1) {
				column.setPreferredWidth(125);
				column.setHeaderValue("<html><b>Last Name</b></html>");
			}
			if(i == 2) {
				column.setPreferredWidth(200);
				column.setHeaderValue("<html><b>Email</b></html>");
			}
			if(i == 3) {
				column.setPreferredWidth(125);
				column.setHeaderValue("<html><b>Phone Number</b></html>");
			}
			if(i == 4) {
				column.setPreferredWidth(250);
				column.setHeaderValue("<html><b>Address</b></html>");
			}
			if(i == 5) {
				column.setPreferredWidth(75);
				column.setHeaderValue("<html><b>Reply</b></html>");
			}
		}
		
		Container pane = this.getContentPane();
		
		SpringLayout layout = new SpringLayout();
		pane.setLayout(layout);
		
		JButton add_button = new JButton("Add");
		add_button.setSize(100, 50);
		add_button.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				model.addPerson(new Person("First","Last","Email","Address", "Phone"), ROW_PLACE++, ROW_FINAL++);
			}
		});
		
		JButton rem_button = new JButton("Remove");
		rem_button.setSize(100, 50);
		rem_button.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				model.deletePerson(table.getSelectedRow() , ROW_PLACE--, ROW_FINAL--);
			}
		});
		
		table.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				int key = e.getKeyCode();
				
				if(key == KeyEvent.VK_ENTER) {
					replyScreen();
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		this.addWindowListener(new WindowListener () {
	
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				try {
					FileOutputStream out = new FileOutputStream("person.ser");
					ObjectOutputStream data = new ObjectOutputStream(out);

					data.writeObject(model.getPeople());
					
					data.close();
					out.close();
				} catch(IOException f) {
					f.printStackTrace();
				}
			}
			
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
			}
			
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
				try {
					FileInputStream in = new FileInputStream("person.ser");
					ObjectInputStream data = new ObjectInputStream(in);

					model.setPeople((List) data.readObject());
					
					data.close();
					in.close();
				} catch(EOFException ef) {
					System.out.println("EOFException found");
				} catch(IOException f) {
					f.printStackTrace();
				} catch (ClassNotFoundException el) {
					// TODO Auto-generated catch block
					el.printStackTrace();
				} 

			}
			
			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}


			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}


			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub
			}
		});
		
		layout.putConstraint(SpringLayout.WEST, scroll, 0, SpringLayout.WEST, pane);
	    layout.putConstraint(SpringLayout.NORTH, scroll, 0, SpringLayout.NORTH, pane);
	    layout.putConstraint(SpringLayout.NORTH, add_button, 25, SpringLayout.SOUTH, scroll);
	    layout.putConstraint(SpringLayout.EAST, add_button, 0, SpringLayout.EAST, pane);
	    
	    layout.putConstraint(SpringLayout.NORTH, rem_button, 25, SpringLayout.SOUTH, scroll);
	    layout.putConstraint(SpringLayout.WEST, rem_button, 50, SpringLayout.WEST, pane);
	    

		pane.add(scroll);
		pane.add(rem_button);
		pane.add(add_button);
	}
	
	private static void replyScreen() {
    	final EmailClient app = EmailClient.getInstance();
    	
		final Message msg;
		
		final JFrame newFrame = new JFrame("To " + model.getRow(table.getSelectedRow()).getFirst() + " " + model.getRow(table.getSelectedRow()).getLast());
		newFrame.setSize(400, 400);
		newFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		newFrame.getContentPane().setBackground(new Color(240,255,255));
		//newFrame.s(new Color(240,255,255));

		Container contentPane = newFrame.getContentPane();
		SpringLayout layout = new SpringLayout();
		contentPane.setLayout(layout);
							
		JLabel to = new JLabel("To: ");
		final JTextField field = new JTextField(25);
		to.setFont(fontBold);	
		field.setText(model.getRow(table.getSelectedRow()).getEmail());
		
		JLabel subject = new JLabel("Subject: ");
		final JTextField subject_field = new JTextField(25);
		subject.setFont(fontBold);
			
		final JTextArea area = new JTextArea("Hello " + model.getRow(table.getSelectedRow()).getFirst() + ",", 15, 31);
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
	}
}
