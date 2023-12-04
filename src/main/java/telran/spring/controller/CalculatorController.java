package telran.spring.controller;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.exceptions.NotFoundException;
import telran.spring.dto.OperationData;
import telran.spring.service.CalculatorService;

@RestController
@RequestMapping("calculator")
@RequiredArgsConstructor
@Getter
@Slf4j
public class CalculatorController {
	final List<CalculatorService> calculatorService;
	Map<String, CalculatorService> servicesMap;
	@PostConstruct
	void createServicesMap() {
		servicesMap = calculatorService.stream().collect(Collectors
				.toMap(service -> service.getCalculationType() ,service -> service)) ;
		if(servicesMap.isEmpty()) {
			log.warn("servicesMap is empty");
		} else {
		log.trace("method: createServiceMap, servicesMap was created {}", servicesMap);
		}
	}
	
	@PostMapping
	String calculate(@RequestBody @Valid OperationData operationData) {
		log.debug("method: calculate, operationData: {},  was recieved", operationData);
		String type = operationData.type();
		CalculatorService  calculatorService = servicesMap.get(type);
		log.debug("method: calculate, calculatorService: {} has been received  ", calculatorService);
		if(calculatorService == null) {
			throw new NotFoundException(String.format("type %s not found", type));
		}
		return calculatorService.calculate(operationData);
	}
	
}
