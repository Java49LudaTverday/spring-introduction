package telran.spring.service;

import telran.spring.dto.OperationData;

public interface CalculatorService {
	String calculate(OperationData operationData);
	String getCalculationType();
}
