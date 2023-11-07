package telran.spring.controller;

import java.util.HashSet;
import java.util.List;

import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import telran.spring.Person;
import telran.spring.service.GreetingService;
import telran.spring.service.IdName;

@RestController
@RequestMapping("greetings")
@RequiredArgsConstructor
public class GreetingsController {
	final GreetingService greetingsService;
	//TODO update following control end point 
	//methods for HW#57 according to updated service
	@PostMapping
	Person addPerson (@RequestBody Person person) {
		return greetingsService.addPerson(person);
	}
	@PutMapping
	Person updatePerson (@RequestBody Person person) {
		return greetingsService.updatePerson(person);
	}
	@GetMapping("/getPerson/{id}")
	Person getPerson (@PathVariable long id) {
		return greetingsService.getPerson(id);
	}
	@DeleteMapping("{id}")
	Person deletePerson (@PathVariable long id) {
		return greetingsService.deletePerson(id);
	}
	@GetMapping("/getByCity/{city}")
	List<Person> getPersosByCity (@PathVariable String city){
		return greetingsService.getPersonsByCity(city);
	}	

	@GetMapping("{id}")
	String getGreetings(@PathVariable long id) {
		return greetingsService.getGreetings(id);
	}
	

}