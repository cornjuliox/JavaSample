package testdatabase;

import java.sql.SQLException;

import model.AgeCategory;
import model.Database;
import model.EmploymentCategory;
import model.Person;
import model.Gender;

public class TestDatabase {

	public static void main(String[] args) {
		System.out.println("Database test running...");
		Database db = new Database();
		
		try {
			db.connect();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		db.addPerson(new Person("Joe", "pimp", AgeCategory.adult, EmploymentCategory.employed, "777", true, Gender.male));
		db.addPerson(new Person("Sue", "goalie", AgeCategory.adult, EmploymentCategory.selfEmployed, null, false, Gender.female));
		
		try {
			System.out.println("Attempting save.");
			db.save();
			System.out.println("Save successful!");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		try {
			db.load();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Can't load from database for some reason.");
			e.printStackTrace();
		}
		
		System.out.println("Disconnecting.");
		db.disconnect();
	}
}
