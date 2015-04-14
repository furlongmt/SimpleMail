package login;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import javax.swing.*;
import javax.swing.table.*;

@SuppressWarnings("serial")
public class ContactsScreen extends JFrame {
	
	public List<Person> contacts;
	
	public ContactsScreen(TableModel model){
		super("Contacts");
		setSize(500, 500);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    	setLocation(dim.width/2-getSize().width/2, dim.height/2-getSize().height/2);
		try {
			FileInputStream in = new FileInputStream("person.ser");
			ObjectInputStream data = new ObjectInputStream(in);
			contacts = (List) data.readObject();
			data.close();
			in.close();
		} catch(EOFException ef) {
			System.out.println("EOFException found");
		} catch(IOException f) {
			f.printStackTrace();
		} catch (ClassNotFoundException el) {
			el.printStackTrace();
		}
		Object[][] data = new Object[contacts.size()][5];
		for(int i = 0;i < contacts.size();i++){
			data[i][0] = contacts.get(i).getFirst();
			data[i][1] = contacts.get(i).getLast();
			data[i][2] = contacts.get(i).getEmail();
			data[i][3] = contacts.get(i).getAddress();
			data[i][4] = contacts.get(i).getNumber();
		}
		Object[] col = {"First Name","Last Name","Email","Address","Phone number"};
		final JTable table = new JTable(new DefaultTableModel(data, col));
		JScrollPane scroll = new JScrollPane(table);
		JButton addb = new JButton("+");
		JButton removeb = new JButton("-");
		JButton composeb = new JButton("Compose");
		JPanel pane = new JPanel();
		
        Insets insets = getInsets();
		pane.setBounds(insets.left, insets.top, 500, 500);
        pane.setBackground(new Color(240,255,255));
		insets = pane.getInsets();
		scroll.setBounds(insets.left, insets.top, 500, 400);
		addb.setBounds(insets.left + 50, insets.top + 400, 50, 50);
		removeb.setBounds(insets.left + 100, insets.top + 400, 50, 50);
		composeb.setBounds(insets.left + 200, insets.top + 425, 100, 50);
		add(pane);
		pane.add(scroll);
		pane.add(addb);
		pane.add(removeb);
		pane.add(composeb);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		
		addb.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				contacts.add(new Person("First","Last","Email","Address", "Phone"));
				DefaultTableModel model = (DefaultTableModel) table.getModel();
				model.addRow(new Object[]{"First","Last","Email","Address", "Phone"});
			}
		});
		
		removeb.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				contacts.remove(table.getSelectedRow());
				DefaultTableModel model = (DefaultTableModel) table.getModel();
				model.removeRow(table.getSelectedRow());
			}
		});
		
		composeb.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				int row = table.getSelectedRow();
				EmailMainScreen.sendTo(contacts.get(row).getEmail());
				hideWindow();
			}
		});
		
		table.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					int row = table.getSelectedRow();
					contacts.get(row).setFirst(table.getValueAt(row, 0));
					contacts.get(row).setLast(table.getValueAt(row, 1));
					contacts.get(row).setEmail(table.getValueAt(row, 2));
					contacts.get(row).setAddress(table.getValueAt(row, 3));
					contacts.get(row).setNumber(table.getValueAt(row, 4));
				}
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

					data.writeObject(contacts);
					
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
	}
	
	private void hideWindow(){
		setVisible(false);
	}
	
}
