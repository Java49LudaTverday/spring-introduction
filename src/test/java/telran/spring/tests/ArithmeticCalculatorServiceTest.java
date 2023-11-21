package telran.spring.tests;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import telran.spring.dto.OperationData;
import telran.spring.service.ArithmeticCalculatorService;

@SpringBootTest
public class ArithmeticCalculatorServiceTest {
	
	@Autowired
	ArithmeticCalculatorService service;
	
	OperationData arithAddRequest = new OperationData("arithmetic", "add", "2", "2");
	OperationData arithSubtructRequest = new OperationData("arithmetic", "subtruct", "2", "2");
	OperationData arithDivideRequest = new OperationData("arithmetic", "divide", "2", "2");
	OperationData arithMultiplyRequest = new OperationData("arithmetic", "multyply", "2", "2");
    OperationData arithWrongOperand1AddRequest = new OperationData("arithmetic", "add", "abc", "2");
    OperationData arithUnknownOperationRequest = new OperationData("arithmetic", "adding", "abc", "2");
	
    @Test
	void loadApplicationContextTest() {
		assertNotNull(service);
	}
	@Test
	void getOperationTypeTest() {
		assertEquals("arithmetic", service.getCalculationType());
	}
	@Test
	void calculateNormalRequestTest() {
		assertEquals("4.0", service.calculate(arithAddRequest));
		assertEquals("0.0", service.calculate(arithSubtructRequest));
		assertEquals("1.0", service.calculate(arithDivideRequest));
		assertEquals("4.0", service.calculate(arithMultiplyRequest));
	}
	@Test
	void calculateBadRequestTest() {
		assertThrowsExactly(IllegalArgumentException.class,
				() -> service.calculate(arithWrongOperand1AddRequest));
		assertThrowsExactly(IllegalArgumentException.class, 
				() -> service.calculate(arithUnknownOperationRequest));
	}

}
