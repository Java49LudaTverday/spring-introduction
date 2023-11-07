package telran.spring.service;

import java.util.*;
import org.springframework.stereotype.Service;
import telran.spring.Person;

@Service
public class GreetingsServiceImpl implements GreetingService {
	Map<Long, String> greetingsMap = new HashMap<>();
	Map<Long, Person> persons = new HashMap<>();
	Map<String, List<Person>> personsByCity = new HashMap<>();
	
	@Override
	public String getGreetings(long id) {

		String name = greetingsMap.getOrDefault(id, "Unknown Guest");
		return "Hello " + name;
	}

	// HW#57
	@Override
	public Person getPerson(long id) {

		return persons.get(id);
	}
	
	@Override
	public Person addPerson(Person person) {
		Person result = null;
		if (persons.putIfAbsent(person.id(), person) == null) {
			addPersonByCity(person);
			addToGreetingsMap(person);
			result = person;
		}
		return result;
	}

	private void addToGreetingsMap(Person person) {
		greetingsMap.put(person.id(), person.name());
		
	}

	private void addPersonByCity(Person person) {
		List<Person> persons = personsByCity.get(person.city());
		if (persons == null) {
			persons = new ArrayList<>();
		}
		persons.add(person);
		personsByCity.put(person.city(), persons);
	}

	@Override
	public List<Person> getPersonsByCity(String city) {

		return personsByCity.get(city);
	}

	@Override
	public Person deletePerson(long id) {
		Person person = persons.remove(id);
		removeFromPersonsCity(person);
		removeFromGreetingsMap(person);
		return person;
	}

	private void removeFromGreetingsMap(Person person) {
		greetingsMap.remove(person.id());
		
	}

	private void removeFromPersonsCity(Person person) {
		List<Person> persons = personsByCity.get(person.city());
		if (persons != null) {
			persons.remove(person);
		}
	}

	@Override
	public Person updatePerson(Person newPerson) {
		Person beforeUpdate = persons.put(newPerson.id(), newPerson);
		removeFromPersonsCity(beforeUpdate);
		addPersonByCity(newPerson);
		if(!newPerson.name().equals(beforeUpdate.name())) {
			removeFromGreetingsMap(beforeUpdate);
			addToGreetingsMap(newPerson);
		}
		return beforeUpdate;
	}
	
	
	
	
}
