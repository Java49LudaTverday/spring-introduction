package telran.spring.service;

import java.util.List;

import telran.spring.Person;

public interface GreetingService {
	String getGreetings(long id);
	// TODO For HW#57
	// Update and add: 
	Person getPerson(long id); // if have`t as null
	Person addPerson(Person person);	
	List<Person> getPersonsByCity(String city);
	Person deletePerson(long id);
	Person updatePerson(Person person);
	
	
}
