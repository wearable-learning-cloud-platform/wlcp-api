package org.wlcp.wlcpapi.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.wlcp.wlcpapi.datamodel.master.Username;
import org.wlcp.wlcpapi.dto.UserRegistrationDto;

public interface UsernameService extends UserDetailsService {
	
	Username registerUser(UserRegistrationDto usernameRegistrationDto);

}
