package telran.spring.service;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import telran.spring.controller.CalculatorController;
import telran.spring.dto.OperationData;
@Service
@Slf4j
public class ArithmeticCalculatorService implements CalculatorService {

	@Override
	public String calculate(OperationData operationData) {
		double[] operands = getOperands(operationData);
		log.trace("operands was received {}", operands);
		String operation = operationData.operation();
		log.debug("operation {} was received", operation );
		String result = switch(operation) {
		case "add" -> Double.toString(operands[0] + operands[1]);
		case "subtruct" -> Double.toString(operands[0]-operands[1]);
		case "multiply" -> Double.toString(operands[0]+operands[1]);
		case "divide" -> Double.toString(operands[0]/operands[1]);
		default -> throw new IllegalArgumentException(operation + " unsupported operation");
		};
		log.debug("result has been given: {}", result);
		return result;
	}

	private double[] getOperands(OperationData operationData) {
		try {
		double op1 = Double.parseDouble(operationData.operand1());
		log.debug("operand1 {} was parsed ", op1);
		double op2 = Double.parseDouble(operationData.operand2());
		log.debug("operand2 {} was parsed ", op2);
		return new double[]{op1, op2};
		}catch (Exception ex) {
			throw new IllegalArgumentException("two operands must be the number");
		}	
	}

	@Override
	public String getCalculationType() {
		
		return "arithmetic";
	}

}
