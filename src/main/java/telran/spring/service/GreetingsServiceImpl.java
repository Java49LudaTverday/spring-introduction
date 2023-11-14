package telran.spring.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import telran.exceptions.NotFoundException;
import telran.spring.Person;
import telran.spring.controller.GreetingsController;

@Service
@Slf4j
public class GreetingsServiceImpl implements GreetingService {
	Map<Long, Person> persons = new HashMap<>();
	@Value(value = "${app.greeting.message:Hello}")
	String greetingMessage;
	@Value("${app.unknown.name:unknown guest}")
	String unknownName;
	@Value("${app.file.name:persons.data}")
	String fileName;

	@Override
	public String getGreetings(long id) {
		Person person = persons.get(id);
		String name = "";
		if (person == null) {
			name = unknownName;
			log.warn("person with id {} not found", id);
		} else {
			name = person.name();
			log.debug("person name is {}", name);
		}
		return String.format("%s, %s", greetingMessage, name);
	}

	@Override
	public Person getPerson(long id) {
		Person person = persons.get(id);
		if (person == null) {
			log.warn("person with id {} not found", id);
		} else {
			log.debug("persons with id {} exists", id);
		}
		return person;
	}

	@Override
	public Person addPerson(Person person) {
		Long id = person.id();
		if (persons.containsKey(id)) {
			throw new IllegalStateException(String.format("person with id %d already exists", id));
		}
		persons.put(id, person);
		log.debug("person with id {} has been saved", id);
		return person;
	}

	@Override
	public List<Person> getPersonsByCity(String city) {
		log.debug("method: get Person by city, received {}", city);
		return persons.values().stream().filter(p -> p.city().equals(city)).toList();
	}

	@Override
	public Person deletePerson(long id) {
		if (!persons.containsKey(id)) {
			throw new NotFoundException(String.format("person with id %d doesn't exist", id));
		}
		Person person = persons.remove(id);
		log.debug("person with id {} has been removed", person.id());
		return person;
	}

	@Override
	public Person updatePerson(Person newPerson) {
		log.debug("method: update Person, received {}", newPerson);
		long id = newPerson.id();
		if (!persons.containsKey(id)) {
			throw new NotFoundException(String.format("person with id %d doesn't exist", id));
		}
		persons.put(newPerson.id(), newPerson);
		log.debug("person with id {} has been update", newPerson.id());
		return newPerson;
	}

	@PostConstruct
	void restoreFrom() {

		restore(fileName);

	}

	@PreDestroy
	void saveToFile() {
		save(fileName);

	}

	@Override
	public void save(String fileName) {
		// saving persons data into ObjectOutputStream
		try(ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(fileName))){
			List<Person> personsForSave = new ArrayList<Person>(persons.values());
			log.trace(String.format("Persons data for saving $s", personsForSave));
			output.writeObject(personsForSave);
			log.info("Persons data have been saved");
		} catch (FileNotFoundException e) {
			throw new  NotFoundException(String.format("Fail %s is not exist", fileName));
		} catch (IOException e) {
			throw new RuntimeException("Can`t be saved");
		} 
		
	}

	@Override
	public void restore(String fileName) {
		// restoring from file using ObjectInputStream
		if(Files.exists(Path.of(fileName))){
		try(ObjectInputStream input = new ObjectInputStream(new FileInputStream(fileName))){
			List<Person> personsList = (ArrayList<Person>) input.readObject();
			log.trace(String.format("Person data for restore $s", personsList));
			personsList.stream().forEach(this::addPerson);
			log.info("Restored from file");
		} catch (FileNotFoundException e) {
			throw new  NotFoundException(String.format("Fail %s is not exist", fileName));
		} catch (Exception e) {
			throw new RuntimeException("Can`t be restore");
		}		
	}
	}
}

