package org.wlcp.wlcpapi.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.wlcp.wlcpapi.datamodel.master.Username;
import org.wlcp.wlcpapi.repository.UsernameRepository;

@Controller
@RequestMapping("/usernameController")
public class UsernameController {
	
	@Autowired
	private UsernameRepository usernameRepository;
	
	@GetMapping(value="/getUsername/{usernameId}")
	public ResponseEntity<Username> getUsername(@PathVariable String usernameId) {
		Optional<Username> username = usernameRepository.findById(usernameId);
		return new ResponseEntity<Username>(username.isPresent() ? username.get() : null, HttpStatus.OK);
	}

}
