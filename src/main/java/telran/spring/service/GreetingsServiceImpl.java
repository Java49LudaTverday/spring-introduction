package telran.spring.service;


import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
@Service
public class GreetingsServiceImpl implements GreetingService {
	Map<Long, String> greetingsMap = new HashMap<>(Map.of(123l, "David", 124l, "Sara", 125l, "Rivka"));
	
	@Override
	public String getGreetings(long id) {
		
		String name =  greetingsMap.getOrDefault(id, "Unknown Guest");
		return "Hello " + name;
	}

	@Override
	public String addName(IdName idName) {
		String name = greetingsMap.putIfAbsent(idName.id(), idName.name());
		if(name != null) {
			throw new IllegalStateException(idName.id() + " name with given ID already exist");
		}
		return name;
	}

	@Override
	public String deleteName(long id) {
		String name = greetingsMap.remove(id);
		if(name == null) {
			throw new IllegalStateException(id + " name with given ID not found");
		}
		return name;
	}

	@Override
	public String updateName(IdName idName) {
		if(!greetingsMap.containsKey(idName.id())) {
			throw new IllegalStateException(idName.id() + " name with given ID not found"); 
		}
		greetingsMap.put(idName.id(), idName.name());
		return idName.name();
	}

}


	