package telran.spring;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;

import telran.exceptions.NotFoundException;
import telran.spring.service.GreetingService;

@SpringBootTest
//defining order of the tests by @Order
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GreetingServiceTest {
	@Autowired
GreetingService service;
	Person personNormal = new Person(123, "Vasya", "Rehovot",
			"rehovotVasya@gmail.com", "054-1234567");
	Person personNormalUpdated = new Person(123, "Vasya", "Lod",
			"rehovotVasya@gmail.com", "054-1234567");
	Person personNotFound = new Person(500, "Vasya", "Rehovot",
			"rehovotVasya@gmail.com", "054-1234567");
	@BeforeAll
	static void deleteFile() throws IOException {
		Files.deleteIfExists(Path.of("test.data"));
	}
	@Test
	@Order(1)
	void loadApplicationContextTest() {
		assertNotNull(service);
	}
	
	@Test
	@Order(2)
	void addPersonNormalTest() {
		assertEquals(personNormal, service.addPerson(personNormal));
	}
	
	@Test
	@Order(3)
	void addPersonAlreadyExistsTest() {
		assertThrowsExactly(IllegalStateException.class, () -> service.addPerson(personNormal));
	}
	@Test
	@Order(4)
	void updatePersonNormalTest() {
		assertEquals(personNormalUpdated, service.updatePerson(personNormalUpdated));
	}
	@Test
	@Order(5)
		void getPersonTest() {
			assertEquals(personNormalUpdated, service.getPerson(123));
		}
	@Test
	@Order(6)
	void getPersonNotFound() {
		assertNull(service.getPerson(500));
	}
	@Test
	@Order(7)
	void updateNotExistsTest() {
		assertThrowsExactly(NotFoundException.class, ()-> service.updatePerson(personNotFound));
	}
	
	@DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
	@Test
	@Order(8)
	void persistenceTest() {
		assertEquals(personNormalUpdated, service.getPerson(123));
	}
}


