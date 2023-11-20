package telran.spring.dto;
import jakarta.validation.constraints.*;
public record OperationData(@NotEmpty @Pattern( regexp = "[a-z]{3,5}", message="wrong type format")String type,
		@NotEmpty String operation,
		String operand1, String operand2) {

}
