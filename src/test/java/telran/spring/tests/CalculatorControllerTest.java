package telran.spring.tests;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import telran.spring.controller.CalculatorController;
import telran.spring.dto.OperationData;
import telran.spring.service.ArithmeticCalculatorService;
import telran.spring.service.CalculatorService;
import telran.spring.service.DateCalculatorService;

@WebMvcTest
public class CalculatorControllerTest {
	@Autowired
	CalculatorController calculatorController;
	@MockBean
	List<CalculatorService> calculatorService;	
	@Autowired
	MockMvc mockMvc;	
	@Autowired
	ObjectMapper objectMapper;
	
     OperationData arithAddRequest = new OperationData("arithmetic", "add", "2", "2");
     OperationData arithWrongOperand1AddRequest = new OperationData("arithmetic", "add", "abc", "2");
     OperationData arithUnknownTypeRequest = new OperationData("aritmetic", "add", "abc", "2");
     OperationData dateAddDaysRequest = new OperationData("date", "addDays", "2023-01-15", "2");
     OperationData dateAddDaysWrongRequest = new OperationData("date", "addDays", "2023-01-15", "abc");
	@Test
	void applicationContextTest() {
		assertNotNull(calculatorController);
		assertNotNull(mockMvc);
		assertNotNull(objectMapper);
		assertNotNull(calculatorService);
	}
	
	@Test
	void calculatorsMapTest() {
		CalculatorService arithmetic = calculatorController.getServicesMap().get("arithmetic");
		assertEquals("arithmetic", arithmetic.getCalculationType());
	}

	@Test
	void calculateArithmeticAddService() throws Exception {
		mockMvc.perform(post("http://localhost:8080/calculator").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(arithAddRequest))).andDo(print()).andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();

	}
	@Test
	void calculateArithmeticWrongAddService() throws Exception {
		String response = mockMvc.perform(post("http://localhost:8080/calculator").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(arithWrongOperand1AddRequest)))
		.andDo(print()).andExpect(status().isBadRequest())
				.andReturn().getResponse().getContentAsString();
		assertEquals("two operands must be the number", response);

	}
	@Test
	void calculateArithmeticUnknownTypeService() throws Exception {
		String response = mockMvc.perform(post("http://localhost:8080/calculator").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(arithUnknownTypeRequest)))
		.andDo(print()).andExpect(status().isNotFound())
				.andReturn().getResponse().getContentAsString();
		assertEquals("type aritmetic not found", response);

	}
	@Test
	void calculateAddDaysDateService() throws Exception {
		mockMvc.perform(post("http://localhost:8080/calculator").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dateAddDaysRequest)))
		        .andDo(print()).andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
	}
	@Test
	void calculateAddDaysDateServiceWrongRequest() throws Exception {
		String response = mockMvc.perform(post("http://localhost:8080/calculator").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dateAddDaysWrongRequest)))
		        .andDo(print()).andExpect(status().isBadRequest())
				.andReturn().getResponse().getContentAsString();
		assertEquals("operand2 must be a number", response );
	}

}
