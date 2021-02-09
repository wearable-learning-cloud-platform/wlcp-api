package org.wlcp.wlcpapi.service.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.wlcp.wlcpapi.datamodel.master.Username;
import org.wlcp.wlcpapi.dto.UserRegistrationDto;
import org.wlcp.wlcpapi.repository.UsernameRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TestUsernameServiceImpl {

	@InjectMocks
	private UsernameServiceImpl usernameServiceImpl;
	
	@Mock
	private UsernameRepository usernameRepository;
	
	@Mock
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Test
	public void testRegisterUserUsernameDoesNotExist() {
		when(usernameRepository.findById(any(String.class))).thenReturn(Optional.empty());
		when(usernameRepository.save(any(Username.class))).thenReturn(new Username());
		Username username = usernameServiceImpl.registerUser(createUserRegistrationDto());
		Assertions.assertNotNull(username);
	}
	
	@Test
	public void testRegisterUserUsernameAlreadyExists() {
		Assertions.assertThrows(RuntimeException.class, () -> {
			when(usernameRepository.findById(any(String.class))).thenReturn(Optional.of(new Username()));
			usernameServiceImpl.registerUser(createUserRegistrationDto());
		});
	}
	
	@Test
	public void testLoadUserByUsernameUsernameDoesNotExist() {
		when(usernameRepository.findById(any(String.class))).thenReturn(Optional.of(new Username("test", "test", "test", "test", "test")));
		UserDetails userDetails = usernameServiceImpl.loadUserByUsername("");
		Assertions.assertNotNull(userDetails);
	}
	
	@Test
	public void testLoadUserByUsernameUsernameAlreadyExists() {
		Assertions.assertThrows(UsernameNotFoundException.class, () -> {
			when(usernameRepository.findById(any(String.class))).thenReturn(Optional.empty());
			usernameServiceImpl.loadUserByUsername("");
		});
	}
	
	private UserRegistrationDto createUserRegistrationDto() {
		UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
		userRegistrationDto.usernameId = "";
		userRegistrationDto.password = "";
		userRegistrationDto.firstName = "";
		userRegistrationDto.lastName = "";
		return userRegistrationDto;
	}
}
