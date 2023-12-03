package telran.spring.tests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.test.web.servlet.MockMvc;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import telran.spring.controller.CalculatorController;
import telran.spring.dto.OperationData;
import telran.spring.service.CalculatorService;

@Service
class TestService implements CalculatorService {

	@Override
	public String calculate(OperationData operationData) {
		if(operationData.operation().equals("add")) {
			throw new IllegalArgumentException("unsupported operation");
		}
		return "test";
	}

	@Override
	public String getCalculationType() {
		
		return "test";
	}
	
}
public class YuryTestController {


@WebMvcTest
class CalculatorControllerTest {
	@Autowired
	CalculatorController controller;
	@Autowired
	MockMvc mockMvc;
	
	
	OperationData operationDataNormal = new OperationData("test", "test", "test", "test");
	OperationData operationDataWrongTypeOperation = new OperationData("xx", "test", "test", "test");
	OperationData operationDataNotFoundTypeOperation = new OperationData("xxx", "test", "test", "test");
	OperationData operationDataWrongOperation = new OperationData("test", null, "test", "test");
	OperationData operationDataWrongOperand = new OperationData("test", "test", null, null);
	OperationData operationDataUnsupprtedOperation = new OperationData("test", "add", "xxx", null);
	@Autowired
	private ObjectMapper objectMapper;
	@Test
	void contextLoads() {
		assertNotNull(controller);
	}
	
	@Test
	void mapCreationTest() {
		CalculatorService service = controller.getServicesMap().get("test");
		assertEquals("test", service.getCalculationType());
		
	}
	@Test
	void normalFlowTest() throws  Exception {
		String response = mockMvc
				.perform(post("http://localhost:8080/calculator").contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(operationDataNormal)))
				.andDo(print()).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		assertEquals("test", response);
	}
	@Test
	void wrongTypeTest() throws  Exception {
		String response = mockMvc
				.perform(post("http://localhost:8080/calculator").contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(operationDataWrongTypeOperation)))
				.andDo(print()).andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();
		assertEquals("wrong type format", response);
	}
	@Test
	void WrongOperationTest() throws  Exception {
		String response = mockMvc
				.perform(post("http://localhost:8080/calculator").contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(operationDataWrongOperation)))
				.andDo(print()).andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();
		assertEquals("operation must not be empty", response);
	}
	@Test
	void WrongOperandTest() throws  Exception {
		String response = mockMvc
				.perform(post("http://localhost:8080/calculator").contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(operationDataWrongOperand)))
				.andDo(print()).andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();
		assertEquals("operand1 must not be empty", response);
	}
	@Test
	void UnsupportedOperationTest() throws  Exception {
		String response = mockMvc
				.perform(post("http://localhost:8080/calculator").contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(operationDataUnsupprtedOperation)))
				.andDo(print()).andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();
		assertEquals("unsupported operation", response);
	}
	@Test
	void notFoundTypeTest() throws  Exception {
		String response = mockMvc
				.perform(post("http://localhost:8080/calculator").contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(operationDataNotFoundTypeOperation)))
				.andDo(print()).andExpect(status().isNotFound()).andReturn().getResponse().getContentAsString();
		assertEquals("type xxx not found", response);
	}
}
}