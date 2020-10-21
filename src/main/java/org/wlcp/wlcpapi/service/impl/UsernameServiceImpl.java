package org.wlcp.wlcpapi.service.impl;

import static java.util.Collections.emptyList;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.wlcp.wlcpapi.datamodel.master.Username;
import org.wlcp.wlcpapi.dto.UserRegistrationDto;
import org.wlcp.wlcpapi.repository.UsernameRepository;
import org.wlcp.wlcpapi.service.UsernameService;

@Service
public class UsernameServiceImpl implements UsernameService {
	
	@Autowired
	private UsernameRepository usernameRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Override
	public Username registerUser(UserRegistrationDto userRegistrationDto) {
		Optional<Username> username = usernameRepository.findById(userRegistrationDto.usernameId);
		if(!username.isPresent()) {
			Username user = new Username(userRegistrationDto.usernameId, bCryptPasswordEncoder.encode(userRegistrationDto.password), userRegistrationDto.firstName, userRegistrationDto.lastName, "");
			return usernameRepository.save(user);
		} else {
			throw new RuntimeException("That username already exists!"); 
		}
	}

	@Override
	public UserDetails loadUserByUsername(String usernameId) throws UsernameNotFoundException {
		Optional<Username> username = usernameRepository.findById(usernameId);
		if(username.isPresent()) {
			return new User(username.get().getUsernameId(), username.get().getPassword(), emptyList());
		} else {
			throw new UsernameNotFoundException(usernameId);
		}
	}

}
