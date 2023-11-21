package telran.spring.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import telran.spring.dto.OperationData;
import telran.spring.service.DateCalculatorService;

@SpringBootTest
public class DateCalculatorServiceTest {
	@Autowired
	DateCalculatorService service;
	
	OperationData addDaysRequest = new OperationData("date", "addDays", "2023-01-15", "2");
	OperationData substructDaysRequest = new OperationData("date", "substructDays", "2023-01-15", "2");
	OperationData betweenInDaysRequest = new OperationData("date", "betweenInDays", "2023-01-15", "2023-01-14");
	OperationData betweenInYearsRequest = new OperationData("date", "betweenInYears", "2024-01-15", "2023-01-15");
    OperationData addDaysWrongRequest = new OperationData("date", "addDays", "2023-01-15", "abc");
    OperationData betweenInDaysWrongRequest = new OperationData("date", "betweenInDays", "2023-01-15", "abc");
    
    @Test
	void loadApplicationContextTest() {
		assertNotNull(service);
	}
    
    @Test
	void getOperationTypeTest() {
		assertEquals("date", service.getCalculationType());
	}
    
    @Test
	void calculateNormalRequestTest() {
		assertEquals("2023-01-17", service.calculate(addDaysRequest));
		assertEquals("2023-01-13", service.calculate(substructDaysRequest));
		assertEquals("1 Days", service.calculate(betweenInDaysRequest));
		assertEquals("1 Years", service.calculate(betweenInYearsRequest));

	}
    @Test
	void calculateBadRequestTest() {
		assertThrowsExactly(IllegalArgumentException.class,
				() -> service.calculate(addDaysWrongRequest));
		assertThrowsExactly(IllegalArgumentException.class, 
				() -> service.calculate(betweenInDaysWrongRequest));
	}
}
