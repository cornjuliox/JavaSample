package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Database {
	private Connection con;
	private List<Person> people;

	public Database() {
		people = new LinkedList<Person>();
	}

	public void connect() throws Exception {

		if (con != null) {
			return;
		}
		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Driver found! Initializing connection");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			throw new Exception("Driver not found! Check path and try again!");
		}
		String connectionUrl = "jdbc:mysql://localhost:3306/swingtest";
		con = DriverManager.getConnection(connectionUrl, "root", "jrZw0mc01.");
		if (con == null) {
			System.out
					.println("Connection failed! Check database settings and try again.");
		} else {
			System.out.println("Connected to: " + connectionUrl);
		}
	}

	public void disconnect() {
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println("Connection can't be closed, try again.");
			}
		}
	}

	public void save() throws SQLException {

		String checkSql = "SELECT COUNT(*) as count from people where id=?";
		String insertSql = "insert into people (id, name, age, employment_status, tax_id, us_citizen, gender, occupation) values (?, ?, ?, ?, ?, ?, ?, ?)";
		String updateSql = "update people set name=?, age=?, employment_status=?, tax_id=?, us_citizen=?, gender=?, occupation=? where id=?";

		PreparedStatement checkStmt = con.prepareStatement(checkSql);
		PreparedStatement insertStmt = con.prepareStatement(insertSql);
		PreparedStatement updateStmt = con.prepareStatement(updateSql);
		
		for (Person person : people) {
			// insert in DB if not there,
			// otherwise update.
			int id = person.getId();
			String name = person.getName();
			
			String occupation = person.getOccupation();
			AgeCategory ageCat = person.getAgeCategory();
			EmploymentCategory empCat = person.getEmpCat();
			String taxId = person.getTaxId();
			boolean isCitizen = person.isUsCitizen();
			Gender gender = person.getGender();
			checkStmt.setInt(1, id);

			ResultSet checkResult = checkStmt.executeQuery();
			checkResult.next();

			int count = checkResult.getInt(1);
			if (count == 0) {
				// insert
				System.out.println("Inserting person with ID: " + id);
				int col = 1;
				
				insertStmt.setInt(col++, id);
				insertStmt.setString(col++, name);
				insertStmt.setString(col++, ageCat.name());
				insertStmt.setString(col++, empCat.name());
				insertStmt.setString(col++, taxId);
				insertStmt.setBoolean(col++, isCitizen);
				insertStmt.setString(col++, gender.name());
				insertStmt.setString(col++, occupation);
				insertStmt.executeUpdate();
				
			} else {
				// update
				System.out.println("Updating person with ID: " + id);
				int col = 1;
			
				updateStmt.setString(col++, name);
				updateStmt.setString(col++, ageCat.name());
				updateStmt.setString(col++, empCat.name());
				updateStmt.setString(col++, taxId);
				updateStmt.setBoolean(col++, isCitizen);
				updateStmt.setString(col++, gender.name());
				updateStmt.setString(col++, occupation);
				updateStmt.setInt(col++, id);
				updateStmt.executeUpdate();
			}
			
		}
        checkStmt.close();
		insertStmt.close();
		updateStmt.close();
	}
	
	public void load() throws SQLException {
		people.clear();
	
		String selectSql = "select id, name, age, employment_status, tax_id, us_citizen, gender, occupation from people order by name";
		Statement selectStatement = con.createStatement();
		
		ResultSet results = selectStatement.executeQuery(selectSql);
		
		while(results.next()) {
			int id = results.getInt("id");
			
			String name = results.getString("name");
			String age = results.getString("age");
			String employment = results.getString("employment_status");
			String tax = results.getString("tax_id");
			boolean usCitizen = results.getBoolean("us_citizen");
			String gender = results.getString("gender");
			String occupation = results.getString("occupation");
			
			Person person = new Person(id, name, occupation, AgeCategory.valueOf(age), EmploymentCategory.valueOf(employment), tax, usCitizen, Gender.valueOf(gender));
			people.add(person);
			
			System.out.println(person);
		}
		results.close();
		selectStatement.close();
	}

	public void addPerson(Person person) {
		people.add(person);
	}

	public List<Person> getPeople() {
		return Collections.unmodifiableList(people);
	}

	public void saveToFile(File file) throws IOException {
		// russian doll nesting.
		FileOutputStream fos = new FileOutputStream(file);
		ObjectOutputStream oos = new ObjectOutputStream(fos);

		Person[] persons = people.toArray(new Person[people.size()]);

		oos.writeObject(persons);

		oos.close();
	}

	public void loadFromFile(File file) throws IOException {
		FileInputStream fis = new FileInputStream(file);
		ObjectInputStream ois = new ObjectInputStream(fis);

		try {
			Person[] persons = (Person[]) ois.readObject();
			people.clear();
			people.addAll(Arrays.asList(persons));

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ois.close();
	}

	public void removePerson(int index) {
		people.remove(index);
	}
}