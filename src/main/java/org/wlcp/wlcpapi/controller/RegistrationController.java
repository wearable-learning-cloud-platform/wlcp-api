package org.wlcp.wlcpapi.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.wlcp.wlcpapi.datamodel.master.Username;
import org.wlcp.wlcpapi.dto.UserRegistrationDto;
import org.wlcp.wlcpapi.repository.UsernameRepository;

@Controller
@RequestMapping("/registrationController")
public class RegistrationController {
	
	@Autowired
	private UsernameRepository usernameRepository;
	
	@PostMapping("/registerUser")
	public ResponseEntity<Boolean> login(@RequestBody UserRegistrationDto userRegistrationDto) { 
		Optional<Username> user = usernameRepository.findById(userRegistrationDto.usernameId);
		if(!user.isPresent()) {
			Username username = new Username(userRegistrationDto.usernameId, userRegistrationDto.password, userRegistrationDto.firstName, userRegistrationDto.lastName, "");
			usernameRepository.save(username);
			return new ResponseEntity<Boolean>(true, HttpStatus.OK);
		} else {
			return new ResponseEntity<Boolean>(false, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
