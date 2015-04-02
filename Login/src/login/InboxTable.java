package login;

import java.text.SimpleDateFormat;
import javax.mail.Flags.Flag;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.swing.table.AbstractTableModel;

public class InboxTable extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5497563449061855719L;
	public static final int COLUMN_COUNT = 3;
	private Message[] inbox;
	
	public InboxTable(Message[] messages) {
		inbox = messages;
	}
	
	@Override
	public int getColumnCount() {
		return COLUMN_COUNT;
	}

	@Override
	public int getRowCount() {
		return (inbox.length == 0? 1:inbox.length);
	}

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
	
	public Message getRow(int row) {
		if(inbox.length == 0){
			return null;
		}
		return inbox[inbox.length - 1 - row];
	}

}
