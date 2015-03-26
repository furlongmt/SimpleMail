package cu.cs.cpsc215.project3;

import java.util.ArrayList;
import java.util.Collections;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.swing.table.AbstractTableModel;

public class InboxTable extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5497563449061855719L;
	private static final int COLUMN_COUNT = 3;
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
		try {
			if(col == 0)
				return inbox.get(row).getSubject();
			if(col == 1)
				return inbox.get(row).getFrom();
			if(col == 2)
				return inbox.get(row).getReceivedDate();
		} catch (MessagingException e) {
			return null;
		}
		
		return null;
	}
	
	public Message getRow(int row) {
		return inbox.get(row);
	}

}
