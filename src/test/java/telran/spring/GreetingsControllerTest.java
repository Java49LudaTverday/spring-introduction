package telran.spring;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import telran.spring.controller.GreetingsController;
import telran.spring.service.GreetingService;

@WebMvcTest
public class GreetingsControllerTest {
	@Autowired
	//this annotation allows dependency 
	//injection inside following field
	GreetingsController controller;
	@MockBean
	GreetingService service;//unreal service
	@Autowired
	MockMvc mockMvc;//imitator of Web Server
	Person personNormal = new Person(123, "Vasya", "Rehovot",
			"rehovotVasya@gmail.com", "054-1234567");
	Person personWrongPhone = new Person(124,"Vasya","Rehovot",
			"rehovotVasya@gmail.com", "054-12345");
	Person personWrongEmail = new Person(125,"Vasya","Rehovot",
			"rehovotVasya", "054-1234567");
	Person personWrongName = new Person(123, "vasyA", "Rehovot",
			"rehovotVasya@gmail.com", "054-1234567");
	Person personEmptyCity = new Person(123, "Vasya", "",
			"rehovotVasya@gmail.com", "054-1234567");
	Person personForUpdate = new Person(123, "Vasya", "TelAviv",
			"rehovotVasya@gmail.com", "054-1234567");
	@Autowired
	ObjectMapper objectMapper;
	
	@Test
	void loadControllerTest() {
		assertNotNull(controller);
		assertNotNull(service);
		assertNotNull(mockMvc);
		assertNotNull(objectMapper);
	}
	
	@Test
	void normalFlowAddPerson() throws Exception{
		mockMvc.perform(post("http://localhost:8080/greetings")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(personNormal)))
		.andDo(print()).andExpect(status().isOk());
	}
	
	@Test
	void wrongFlowAddPerson() throws Exception{
		String responseWrongPhone = mockMvc.perform(post("http://localhost:8080/greetings")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(personWrongPhone)))
				.andDo(print())
		.andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();
		assertEquals("not Israel mobile phone", responseWrongPhone);
		
		String responseWrongEmail = mockMvc.perform(post("http://localhost:8080/greetings")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(personWrongEmail))).andDo(print())
		.andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();
		assertEquals("must be a well-formed email address", responseWrongEmail);
		
		String responseWrongName = mockMvc.perform(post("http://localhost:8080/greetings")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(personWrongName))).andDo(print())
		.andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();
		assertEquals("must match \"[A-Z][a-z]{2,}\"", responseWrongName);
		
		String responseEmptyCity = mockMvc.perform(post("http://localhost:8080/greetings")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(personEmptyCity))).andDo(print())
		.andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();
		assertEquals("must not be empty", responseEmptyCity);
		
	}
	
	@Test
	void normalFlowUpdatePerson() throws Exception {
		String reponse = mockMvc.perform(put("http://localhost:8080/greetings")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(personForUpdate)))
				.andDo(print()).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
	}

	@Test
	void normalFlowGetPerson() throws Exception {
		
		Long id = personNormal.id();
		when(service.getPerson(id)).thenReturn(personNormal);
		String response = mockMvc.perform(get("http://localhost:8080/greetings/getPerson/{id}", 123))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
		
		Person personResponse = objectMapper.readValue(response, Person.class);
		 assertThat(personResponse).isEqualTo(personNormal);
	}
	
	@Test 
	void normalFlowDeletePerson() throws Exception {
		Long id = personNormal.id();
		when(service.deletePerson(id)).thenReturn(personNormal);
		
	String response =	mockMvc.perform(delete("http://localhost:8080/greetings/123"))
			.andDo(print())
			.andExpect(status().isOk())
			.andReturn().getResponse().getContentAsString();
	
	Person personResponse = objectMapper.readValue(response, Person.class);
	 assertThat(personResponse).isEqualTo(personNormal);
		
	}
	@Test 
	void normalFlowGetGreeting() throws Exception{
		Long id = personNormal.id();
		when(service.getGreetings(id)).thenReturn("Hi, Vasya"); 
		
	String response = mockMvc.perform(get("http://localhost:8080/greetings/{id}", id))
		.andDo(print()).andExpect(status().isOk())
		.andReturn().getResponse().getContentAsString();
	     assertEquals("Hi, Vasya", response);
	}

}
