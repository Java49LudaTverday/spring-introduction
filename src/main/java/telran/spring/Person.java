package telran.spring;

import java.util.Objects;

import jakarta.validation.constraints.*;


public record Person(long id,@Pattern(regexp = "[A-Z][a-z]{2,}") String name,
		@NotEmpty String city,/*TODO validation annotation*/ String email,
		/*TODO Israel mobile phone validation*/  String phone) {

	

}
