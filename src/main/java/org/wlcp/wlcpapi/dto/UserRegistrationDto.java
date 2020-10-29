package org.wlcp.wlcpapi.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class UserRegistrationDto {

	@NotNull
	@NotEmpty
	public String usernameId;
	
	@NotNull
	@NotEmpty
	public String password;
	
	@NotNull
	@NotEmpty
	public String firstName;
	
	@NotNull
	@NotEmpty
	public String lastName;
	
}
