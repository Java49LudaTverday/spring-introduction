package telran.spring.controller;

import java.util.HashSet;
import java.util.List;

import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.exceptions.NotFoundException;
import telran.spring.Person;
import telran.spring.service.GreetingService;
import telran.spring.service.IdName;

@RestController
@RequestMapping("greetings")
@RequiredArgsConstructor
@Slf4j
public class GreetingsController {
	final GreetingService greetingsService;
	
	@PostMapping
	Person addPerson (@RequestBody @Valid Person person) {
		log.debug("method: add person, received {}", person);
		return greetingsService.addPerson(person);
	}
	@PutMapping
	Person updatePerson (@RequestBody @Valid Person person) {
		log.debug("method: update person, received {}", person);
		return greetingsService.updatePerson(person);
	}
	@GetMapping("/getPerson/{id}")
	Person getPerson (@PathVariable long id) {
		log.debug("method: get person, received {}", id);
		Person person = greetingsService.getPerson(id);
		if(person == null) {
			log.warn("method: get person, not found the person with id {}", id);
			throw new NotFoundException(String.format("Not found the person with id %d", id));
		}
		return person;
	}
	@DeleteMapping("{id}")
	Person deletePerson (@PathVariable long id) {
		log.debug("method: delete person, received {}", id);
		return greetingsService.deletePerson(id);
	}
	@GetMapping("/getByCity/{city}")
	List<Person> getPersosByCity (@PathVariable String city){
		log.debug("method: get person by city, received {}", city);
		List<Person> result =  greetingsService.getPersonsByCity(city);
		if(result.isEmpty()) {
			log.warn("received empty list for city: {}", city);
		} else {
			log.trace("result is {}", result);
		}
		return result;
	}	

	@GetMapping("{id}")
	String getGreetings(@PathVariable long id) {
		log.debug("method: get greetings, received id {}", id);
		return greetingsService.getGreetings(id);
	}
	

}