package login;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;


public class ContactTableModel extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Person> list = new ArrayList<Person>();
	public static final int COLUMN_COUNT = 5;
	
	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return COLUMN_COUNT;
	}

	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		// TODO Auto-generated method stub
		if(col == 0)
			return list.get(row).getFirst();
		else if(col == 1)
			return list.get(row).getLast();
		else if(col == 2)
			return list.get(row).getEmail();
		else if(col == 3)
			return list.get(row).getNumber();
		else if(col == 4)
			return list.get(row).getAddress();
		else
			return null;
		
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return true;
	}
	
	@Override
	public void setValueAt(Object string, int row, int col) {
		if(col == 0)
			list.get(row).setFirst(string);
		else if(col == 1)
			list.get(row).setLast(string);
		else if(col == 2)
			list.get(row).setEmail(string);
		else if(col == 3)
			list.get(row).setNumber(string);
		else if(col == 4)
			list.get(row).setAddress(string);
		fireTableDataChanged();
	}
	
	public void addPerson(Person person, int first_row, int last_row) {
		list.add(person);
		fireTableRowsInserted(first_row, last_row);
	}
	
	public void deletePerson(int row, int first_row, int final_row) {
		list.remove(row);
		fireTableRowsDeleted(first_row, final_row);
	}
	
	public Person getRow(int row) {
		return list.get(row);
	}
	
	public List<Person> getPeople() {
		return list;
	}
	
	public void setPeople(List<Person> peeps) {
		list = peeps;
	}
}
