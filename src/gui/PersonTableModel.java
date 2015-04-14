package gui;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import model.Person;
import model.EmploymentCategory;

public class PersonTableModel extends AbstractTableModel {

	private List<Person> db;
	private String[] columnames = { "ID", "Name", "Occupation", "Age Category",
			"Employment Category", "US Citizen?", "Tax ID"};
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		switch(columnIndex) {
		case 1:
			return true;
		case 4:
			return true;
		case 5:
			return true;
		default:
			return false;
		}
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		switch (columnIndex) {
		case 0:
			return Integer.class;
		case 1:
			return String.class;
		case 2:
			return String.class;
		case 3:
			return String.class;
		case 4:
			return EmploymentCategory.class;
		case 5:
			return Boolean.class;
		case 6:
			return String.class;
		default:
			return null;
		}

	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		if(db == null) {
			return;
		}
		
        Person person = db.get(rowIndex);
        
		switch(columnIndex) {
		case 1:
			person.setName((String)aValue);
			break;
		case 4:
			person.setEmpCat((EmploymentCategory)aValue);
			break;
		case 5:
			person.setUsCitizen((Boolean)aValue);
		default:
			return;
		}
		
	}

	@Override
	public String getColumnName(int column) {
		return columnames[column];
	}

	public void setData(List<Person> db) {
		this.db = db;
	}

	@Override
	public int getRowCount() {
		return db.size();
	}

	@Override
	public int getColumnCount() {
		// hardcode? why?
		return 7;
	}

	@Override
	public Object getValueAt(int row, int col) {
		// TODO Auto-generated method stub
		Person person = db.get(row);

		switch (col) {
		case 0:
			return person.getId();
		case 1:
			return person.getName();
		case 2:
			return person.getOccupation();
		case 3:
			return person.getAgeCategory();
		case 4:
			return person.getEmpCat();
		case 5:
			return person.isUsCitizen();
		case 6:
			return person.getTaxId();
		}

		return null;
	}

}
