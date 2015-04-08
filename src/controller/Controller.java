package controller;

import gui.FormEvent;

import java.io.File;
import java.io.IOException;
import java.util.List;

import model.AgeCategory;
import model.Database;
import model.EmploymentCategory;
import model.Gender;
import model.Person;

public class Controller {
	Database db = new Database(); 
	
	public List<Person> getPeople() {
		return db.getPeople();
	}
	
	public void connect() {
	}
	
	public void save() {
	}
	
	public void refresh() {
	}
	
	public void addPerson(FormEvent ev) {
		String name = ev.getName();
		String occupation = ev.getOccupation();
		int ageCatId = ev.getAgeCategory();
		String empCat = ev.getEmploymentCategory();
		boolean usCitizen = ev.isUsCitizen();
		String taxId = ev.getTaxId();
		String gender = ev.getGender();
		
		AgeCategory ageCategory = null;
		switch(ageCatId) {
		case 0:
			ageCategory = AgeCategory.child;
			break;
		case 1:
			ageCategory = AgeCategory.adult;
			break;
		case 2:
			ageCategory = AgeCategory.senior;
			break;
		}
		
		EmploymentCategory empCategory;
		if(empCat.equals("Employed")) {
			empCategory = EmploymentCategory.employed;
		}
		if(empCat.equals("Self-employed")) {
			empCategory = EmploymentCategory.selfEmployed;
		}
		if(empCat.equals("unemployed")) {
			empCategory = EmploymentCategory.unemployed;
		}
		else {
			empCategory = EmploymentCategory.other;
		}
		
		Gender genderCat;
		if(gender.equals("male")) {
			genderCat = Gender.male;
		}
		else {
			genderCat = Gender.female;
		}
		
		Person person = new Person(name, occupation, ageCategory, empCategory, taxId, usCitizen, genderCat);
		
		db.addPerson(person);
	
	}
	
	public void removePerson(int index) {
		db.removePerson(index);
	}
	
	public void saveToFile(File file) throws IOException {
		db.saveToFile(file);
	}
	
	public void loadFromFile(File file) throws IOException {
		db.loadFromFile(file);
	}
}
