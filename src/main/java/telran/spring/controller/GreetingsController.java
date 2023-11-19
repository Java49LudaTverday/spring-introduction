package telran.spring.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.spring.Person;
import telran.spring.service.GreetingService;


@RestController
@RequestMapping("greetings")
@RequiredArgsConstructor
@Slf4j
public class GreetingsController {
	final GreetingService greetingsService;
	@GetMapping("{id}")
	String getGreetings(@PathVariable long id) {
		log.debug("method: getGreetings, received id {}", id);
		return greetingsService.getGreetings(id);
	}
	@PostMapping
	Person addPerson(@RequestBody @Valid Person person) {
		log.debug("method: addPerson, received {}", person);
		return greetingsService.addPerson(person);
	}
	@PutMapping
	Person updatePerson(@RequestBody @Valid Person person) {
		log.debug("method: updatePerson, received {}", person);
		return greetingsService.updatePerson(person);
	}
	@DeleteMapping("{id}")
	Person deletePerson(@PathVariable long id) {
		log.debug("method: deletePerson, received id {}", id);
		return greetingsService.deletePerson(id);
	}
	@GetMapping("city/{city}")
	List<Person> getPersonsByCity(@PathVariable String city) {
		log.debug("method: getPersonsByCity, received city {}", city);
		List<Person> result =  greetingsService.getPersonsByCity(city);
		
		if(result.isEmpty()) {
			log.warn("received empty list for city: {}", city);
		} else {
			log.trace("result is {}", result);
		}
		return result;
	}
	@GetMapping("id/{id}")
	Person getPerson(@PathVariable long id) {
		log.debug("method: getPerson, received id {}", id);
		return greetingsService.getPerson(id);
	}
	
}