package org.wlcp.wlcpapi.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wlcp.wlcpapi.datamodel.master.Username;
import org.wlcp.wlcpapi.dto.UsernameDto;
import org.wlcp.wlcpapi.repository.UsernameRepository;

@Controller
@RequestMapping("/userController")
public class LoginController {

	@Autowired
	private UsernameRepository usernameRepository;

	@PostMapping(value="/userLogin")
	@ResponseBody
	public ResponseEntity<Boolean> login(@RequestBody UsernameDto usernameDto) { 
		Optional<Username> username = usernameRepository.findById(usernameDto.username);
		if(username.isPresent()) {
			return new ResponseEntity<Boolean>(true, HttpStatus.OK);
		}
		else {
			return new ResponseEntity<Boolean>(false, HttpStatus.OK);
		}
	}
}