package org.wlcp.wlcpapi.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.wlcp.wlcpapi.datamodel.master.Username;
import org.wlcp.wlcpapi.dto.UserRegistrationDto;
import org.wlcp.wlcpapi.repository.UsernameRepository;
import org.wlcp.wlcpapi.service.UsernameService;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Controller
@RequestMapping("/usernameController")
public class UsernameController {
	
	@Autowired
	private UsernameRepository usernameRepository;
	
	@Autowired
	private UsernameService usernameService;
	
	@GetMapping(value="/getUsername/{usernameId}")
	public ResponseEntity<Username> getUsername(@PathVariable String usernameId) {
		Optional<Username> username = usernameRepository.findById(usernameId);
		if(username.isPresent()) {
			username = Optional.of(new Username(username.get().getUsernameId(), null, null, null, null));
		}
		return new ResponseEntity<Username>(username.isPresent() ? username.get() : null, HttpStatus.OK);
	}
	
	@PostMapping("/registerUser")
	public ResponseEntity<String> registerUser(@RequestBody @Valid UserRegistrationDto userRegistrationDto) { 
		usernameService.registerUser(userRegistrationDto);
		return new ResponseEntity<String>("{}", HttpStatus.OK);
	}

}
