package cu.cs.cpsc215.project3;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Flags.Flag;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;

public class InboxTable extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5497563449061855719L;
	public static final int COLUMN_COUNT = 3;
	private ArrayList<Message> inbox;
	
	public InboxTable(ArrayList<Message> messages) {
		inbox = new ArrayList<Message>(messages);
	}
	
	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return COLUMN_COUNT;
	}

	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return inbox.size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		// TODO Auto-generated method stub
		String first = "<html><b>";
		String last = "</b></html>";
		
		try {
			if(col == 0) {
				if(inbox.get(row).isSet(Flag.SEEN))
					return inbox.get(row).getSubject();
				else
					return first + inbox.get(row).getSubject() + last;
			}
			if(col == 1) {
				if(inbox.get(row).isSet(Flag.SEEN))
					return inbox.get(row).getFrom()[0];
				else
					return first + inbox.get(row).getFrom()[0] + last;
			}
			if(col == 2) {
				if(inbox.get(row).isSet(Flag.SEEN))
					return new SimpleDateFormat("MM/dd").format(inbox.get(row).getReceivedDate());
				else
					return first + new SimpleDateFormat("MM/dd").format(inbox.get(row).getReceivedDate()) + last;
			}
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public Message getRow(int row) {
		return inbox.get(row);
	}

}
