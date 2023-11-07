package telran.spring.controller;

import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import telran.spring.service.GreetingService;
import telran.spring.service.IdName;

@RestController
@RequestMapping("greetings")
@RequiredArgsConstructor
public class GreetingsController {
	final GreetingService greetingsService;
	
	@GetMapping("{id}")
	String getGreetings(@PathVariable long id) {
		return greetingsService.getGreetings(id);
	}
	//TODO update following control end point 
	//methods for HW#57 according to updated service
	@PostMapping
	String addName(@RequestBody IdName idName) {;
	return greetingsService.addName(idName);
	}
	@PutMapping
	String updateName(@RequestBody IdName idName) {
		return greetingsService.updateName(idName);
	}
	@DeleteMapping("{id}")
	String deleteName (long id) {
		return greetingsService.deleteName(id);
	}
	//TODO 
	//getMapping
	//end point for getting person 
	//by id and getting persons by city see service

}