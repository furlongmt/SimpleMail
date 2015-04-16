package login;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.*;

/**
 * <p> This class provides the gui for the contacts screen </p>
 * @author Matthew Furlong
 * @author Rj Larsen
 * @see JFrame
 * @see List
 */
public class ContactsScreen extends JFrame {
	
	private static final long serialVersionUID = 2701350043254429795L;
	public List<Person> contacts;
	private static ContactsScreen myInstance;
	
	/**
	 * <p> This function gets the static ContactsScreen instance</p>
	 * @param model - TableModel for the contacts screen table
	 * @return ContactsScreen
	 * @see TableModel
	 */
	public static ContactsScreen getInstance(TableModel model){
		if(myInstance == null){
			myInstance = new ContactsScreen(model);
		}
		return myInstance;
	}
	
	/**
	 * <p> This function creates the window for a ContactsScreen</p>
	 * @param model - TableModel for the contacts screen table
	 * @see TableModel
	 * @see Dimension
	 * @see ArrayList
	 * @see Person
	 * @see FileInputStream
	 * @see ObjectInputStream
	 * @see JTable
	 * @see DefaultTableModel
	 * @see JScrollPane
	 * @see JButton
	 * @see JPanel
	 * @see JLabel
	 * @see Insets
	 * @see MouseAdapter
	 * @see FocusListener
	 * @see KeyListener
	 * @see WindowListener
	 * @see IOException
	 * @see EOFException
	 * @see ClassNotFoundException
	 * @see Exception
	 */
	@SuppressWarnings("unchecked")
	private ContactsScreen(TableModel model){
		super("Contacts");
		setSize(500, 505);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    	setLocation(dim.width/2-getSize().width/2, dim.height/2-getSize().height/2);
		try {
			FileInputStream in = new FileInputStream("person.ser");
			ObjectInputStream data = new ObjectInputStream(in);
			contacts = (ArrayList<Person>) data.readObject();
			data.close();
			in.close();
		} catch(EOFException ef) {
			System.out.println("End of File found");
		} catch(IOException f) {
			contacts = new ArrayList<Person>();
			contacts.add(new Person("","","","", ""));
		} catch (ClassNotFoundException el) {
			System.out.println("Class not found");
		} catch (Exception eft) {
			System.out.println("Coult not load data file");
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
		JLabel l_warn = new JLabel("Please hit ENTER to save changes in a box");
        Insets insets = getInsets();
		pane.setBounds(insets.left, insets.top, 500, 500);
        pane.setBackground(new Color(240,255,255));
		insets = pane.getInsets();
		scroll.setBounds(insets.left, insets.top, 500, 400);
		addb.setBounds(insets.left + 50, insets.top + 400, 50, 50);
		removeb.setBounds(insets.left + 100, insets.top + 400, 50, 50);
		composeb.setBounds(insets.left + 200, insets.top + 425, 100, 50);
		l_warn.setBounds(insets.left + 50, insets.top + 440, l_warn.getSize().width, l_warn.getSize().height);
		add(pane);
		pane.add(scroll);
		pane.add(addb);
		pane.add(removeb);
		pane.add(composeb);
		pane.add(l_warn);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		addb.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				contacts.add(new Person("First","Last","Email","Address", "Phone"));
				DefaultTableModel model = (DefaultTableModel) table.getModel();
				model.addRow(new Object[]{"First","Last","Email","Address", "Phone"});
				table.setRowSelectionInterval(model.getRowCount() - 1, model.getRowCount() - 1);
			}
		});
		removeb.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				int row = table.getSelectedRow();
				if(row >= 0){
					contacts.remove(row);
					DefaultTableModel model = (DefaultTableModel) table.getModel();
					model.removeRow(row);
				}
			}
		});
		composeb.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				int row = table.getSelectedRow();
				if(row >= 0){
					EmailMainScreen.sendTo(contacts.get(row).getEmail());
					hideWindow();
				}
			}
		});
		table.addFocusListener(new FocusListener(){
			@Override
			public void focusGained(FocusEvent e) {}
			@Override
			public void focusLost(FocusEvent e) {
				for(int row = 0; row < table.getRowCount();row++){
					contacts.get(row).setFirst(table.getValueAt(row, 0));
					contacts.get(row).setLast(table.getValueAt(row, 1));
					contacts.get(row).setEmail(table.getValueAt(row, 2));
					contacts.get(row).setAddress(table.getValueAt(row, 3));
					contacts.get(row).setNumber(table.getValueAt(row, 4));
				}
			}
		});
		table.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {}
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
			public void keyTyped(KeyEvent e) {}
		});
		this.addWindowListener(new WindowListener(){
			@Override
			public void windowOpened(WindowEvent e) {}
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					FileOutputStream out = new FileOutputStream("person.ser");
					ObjectOutputStream data = new ObjectOutputStream(out);
					data.writeObject(contacts);
					data.close();
					out.close();
				} catch(IOException f) {}
			}
			@Override
			public void windowClosed(WindowEvent e) {}
			@Override
			public void windowIconified(WindowEvent e) {}
			@Override
			public void windowDeiconified(WindowEvent e) {}
			@Override
			public void windowActivated(WindowEvent e) {}
			@Override
			public void windowDeactivated(WindowEvent e) {}
		});
	}
	
	/**
	 * <p>This function hides the ContactsScreen</p>
	 * @see FileOutputStream
	 * @see ObjectOutputStream
	 * @see IOException
	 */
	private void hideWindow(){
		try {
			FileOutputStream out = new FileOutputStream("person.ser");
			ObjectOutputStream data = new ObjectOutputStream(out);
			data.writeObject(contacts);
			data.close();
			out.close();
		} catch(IOException f) {}
		setVisible(false);
	}
	
}