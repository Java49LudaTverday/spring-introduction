package telran.spring.service;

import java.util.*;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import telran.exceptions.NotFoundException;
import telran.spring.Person;
import telran.spring.controller.GreetingsController;

@Service
@Slf4j
public class GreetingsServiceImpl implements GreetingService {
	Map<Long, Person> persons = new HashMap<>();
	
	@Override
	public String getGreetings(long id) {
		log.debug("method: get greetings, received {}", id);
		Person person = persons.get(id);
		String name = person.name();
		return "Hello " + name;
	}

	@Override
	public Person getPerson(long id) {
		log.debug("method: get Person, received {}", id);
		return persons.get(id);
	}
	
	@Override
	public Person addPerson(Person person) {
		log.debug("method: add Person, received {}", person);
		Long id = person.id();
		if(persons.containsKey(id)) {
			throw new IllegalStateException(String.format("person with id %d already exists", id));
		}
	persons.put(id, person);
		return person;
	}

	@Override
	public List<Person> getPersonsByCity(String city) {
		log.debug("method: get Person by city, received {}", city);
		return persons.values().stream().filter(p -> p.city().equals(city)).toList();
	}

	@Override
	public Person deletePerson(long id) {
		log.debug("method: delete Person, received {}", id);
		if(!persons.containsKey(id)) {
			throw new NotFoundException(String.format("person with id %d doesn't exist", id));
		}
		
		return persons.remove(id);
	}

	@Override
	public Person updatePerson(Person newPerson) {
		log.debug("method: update Person, received {}", newPerson);
		long id = newPerson.id();
		if(!persons.containsKey(id)) {
			throw new NotFoundException(String.format("person with id %d doesn't exist", id));
		}
		persons.put(newPerson.id(), newPerson);
		return newPerson;
	}
	
	
	
	
}
