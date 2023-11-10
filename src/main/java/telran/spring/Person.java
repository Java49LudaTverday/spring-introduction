package telran.spring;

import jakarta.validation.constraints.*;


public record Person( long id,@Pattern(regexp = "[A-Z][a-z]{2,}") String name,
		@NotEmpty String city,/*TODO validation annotation*/@Email(message = "Email is not valid", 
		regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$") String email,
		/*TODO Israel mobile phone validation*/ @Pattern(regexp = "^\\+?972-?\\d{2,3}-\\d{7}$", 
		message = "Please enter a valid Israel phone number in format +972-XXX-XXXXXXX") String phone) {

}
