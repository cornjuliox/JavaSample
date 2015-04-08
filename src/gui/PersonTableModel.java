package gui;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import model.Person;

public class PersonTableModel extends AbstractTableModel {

	private List<Person> db;
	private String[] columnames = { "ID", "Name", "Occupation", "Age Category",
			"Employment Category", "US Citizen?", "Tax ID"};

	@Override
	public String getColumnName(int column) {
		// TODO Auto-generated method stub
		return columnames[column];
	}

	public void setData(List<Person> db) {
		this.db = db;
	}

	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return db.size();
	}

	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
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