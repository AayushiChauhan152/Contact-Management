package com.scm.forms;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Userform {

	@NotBlank(message = "username is required")
	@Size(min = 3, max = 12, message = "username must be between 3-12 characters!")
	private String name; 
	
	@NotBlank(message = "password is required")
	private String password;
	
	@Email(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$")
	private String email;
	
	@Size(min = 10, max = 11)
	private String phone;
	
	private String about;

}
