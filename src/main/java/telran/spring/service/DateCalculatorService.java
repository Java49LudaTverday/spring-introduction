package telran.spring.service;


import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import telran.spring.dto.OperationData;
@Service
@Slf4j
public class DateCalculatorService implements CalculatorService {

	@Override
	public String calculate(OperationData operationData) {
		String result = null;
		String operation = operationData.operation();
		LocalDate firstOperandDate = getOperandDate(operationData.operand1());
		log.debug("method: calculate, firstOperandDate {} has been received", firstOperandDate);
		if(operation.contains("between")) {
			LocalDate secondOperandDate = getOperandDate(operationData.operand2());
			log.debug("method:calculate, secondOperandDate {} has been received", secondOperandDate);
			result = getPeriodBetweenDates(operation, firstOperandDate, secondOperandDate);
		} else {
			long secondOperandLong = getOperandLong(operationData.operand2());
			log.debug("method: calculate, secondOperandLong {} has been received", secondOperandLong);
			result = getArithmeticResult(operation,firstOperandDate,secondOperandLong);
		}
		log.debug("method: calculate, result of operation {} is {}", operation, result);
		
		return result;
	}

	private String getPeriodBetweenDates(String operation, LocalDate firstOperandDate, LocalDate secondOperandDate) {
		
		return switch(operation) {
		case "betweenInDays" -> getValueBetweenDates(ChronoUnit.DAYS, firstOperandDate, secondOperandDate);
		case "betweenInYears" -> getValueBetweenDates(ChronoUnit.YEARS, firstOperandDate, secondOperandDate);
		case "betweenInMonths" -> getValueBetweenDates(ChronoUnit.MONTHS, firstOperandDate, secondOperandDate);
		case "betweenDates" -> getPeriod(firstOperandDate, secondOperandDate);
		default -> throw new IllegalArgumentException(operation + " unsupported operation");
		};
	}
	
	private String getArithmeticResult(String operation, LocalDate firstOperandDate, long secondOperandLong) {
		log.debug("recieved operation: {}, firstDate {} , secondOperandLong {}", 
				operation, firstOperandDate, secondOperandLong);
		return switch(operation) {
		case "addDays" -> firstOperandDate.plusDays(secondOperandLong).toString();
		case "addYears" -> firstOperandDate.plusYears(secondOperandLong).toString();
		case "addMonths" -> firstOperandDate.plusMonths(secondOperandLong).toString();
		case "substructDays" -> firstOperandDate.minusDays(secondOperandLong).toString();
		case "substructYears" -> firstOperandDate.minusYears(secondOperandLong).toString();
		case "substructMounths" -> firstOperandDate.minusMonths(secondOperandLong).toString();
		default -> throw new IllegalArgumentException(operation + " unsupported operation");
		};
	}

	private String getValueBetweenDates(ChronoUnit unit, LocalDate firstOperandDate, LocalDate secondOperandDate) {
		long result = Math.abs( unit.between(firstOperandDate, secondOperandDate));
		log.debug("method: getValueBettweenDates, chronoUnit: {}, recieved result: {}", unit, result);
		return String.format("%d %s", result, unit);
	}

	private String getPeriod(LocalDate firstOperandDate, LocalDate secondOperandDate) {
		Period period = Period.between(firstOperandDate, secondOperandDate);
		log.debug("received period between {} and {}",firstOperandDate , secondOperandDate);
		int days = Math.abs(period.getDays());
		int months = Math.abs(period.getMonths());
		int years = Math.abs(period.getYears());
		log.debug("receivde days {}, months {}, years {}", days, months, years);
		return String.format("%d years, %d months, %d days", years, months,days);
	}

	

	private long getOperandLong(String operand) {
		try {
			return Long.parseLong(operand); 
		} catch (NumberFormatException e) {
		throw new IllegalArgumentException("operand2 must be a number");
		}
		
	}

	private LocalDate getOperandDate(String operand) {
		log.debug("get operand as String: {}", operand);
		try {
			return LocalDate.parse(operand);
		} catch (DateTimeParseException e) {
			 throw new IllegalArgumentException("operand must be a date in format YYYY-MM-DD");
		}		
	}

	@Override
	public String getCalculationType() {
		
		return "date";
	}

}
