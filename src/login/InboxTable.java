//Matthew Furlong and Robert Larsen
package login;

import java.awt.Container;
import java.awt.Font;
import java.text.SimpleDateFormat;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.table.AbstractTableModel;

/**
* 
* @author Matthew Furlong
* @author Rj Larsen

* <p> This class provides data for the table in the email client </p>
*/

public class InboxTable extends AbstractTableModel {

	private static final long serialVersionUID = 5497563449061855719L;
	public static final int COLUMN_COUNT = 3;
	private Message[] inbox;
	
	/**
	 * 
	 * @param messages Array of messages for InboxTable
	 * 
	 * <p> This constructor sets the messages to be displayed in the table </p>
	 */
	public InboxTable(Message[] messages) {
		inbox = messages;
	}
	
	/**
	 * <p> This method overrides AbstractTableModel and returns the number of columns
	 */
	@Override
	public int getColumnCount() {
		return COLUMN_COUNT;
	}
	
	/**
	 * <p> This method overrides AbstractTableModel and returns the number of rows
	 */
	@Override
	public int getRowCount() {
		return (inbox.length == 0? 1:inbox.length);
	}

	/**
	 * @param row the row selected
	 * @param col the column selected
	 * 
	 * <p> This function overrides AbstractTableModel and displays the necessary data in the JTable</p>
	 */
	@Override
	public Object getValueAt(int row, int col) {
		String first = "<html><b>";
		String last = "</b></html>";
		row = inbox.length - 1 - row;
		try {
			if(col == 0) {
				if(inbox.length == 0){
					return "No messages";
				}
				if(inbox[row].isSet(Flag.SEEN))
					return inbox[row].getSubject();
				else
					return first + inbox[row].getSubject() + last;
			}
			if(col == 1) {
				if(inbox.length == 0){
					return " ";
				}
				if(inbox[row].isSet(Flag.SEEN))
					return inbox[row].getFrom()[0];
				else
					return first + inbox[row].getFrom()[0] + last;
			}
			if(col == 2) {
				if(inbox.length == 0){
					return " ";
				}
				if(inbox[row].isSet(Flag.SEEN))
					return new SimpleDateFormat("MM/dd").format(inbox[row].getReceivedDate());
				else
					return first + new SimpleDateFormat("MM/dd").format(inbox[row].getReceivedDate()) + last;
			}
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * 
	 * @param row row of table
	 * @return Message at row selected
	 */
	public Message getRow(int row) {
		if(inbox.length == 0){
			return null;
		}
		return inbox[(inbox.length - 1) - row];
	}

}
