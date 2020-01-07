package org.wlcp.wlcpapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.wlcp.wlcpapi.datamodel.master.Username;
import org.wlcp.wlcpapi.repository.UsernameRepository;

@RestController
@RequestMapping("/test")
public class TestController {
	
	@Autowired
	private UsernameRepository usernameRepository;
	
	@GetMapping()
	public void test() {
		
	}
	
	@GetMapping(value="/user")
	public void user(@RequestParam("usernameId") String usernameId) {
		Username username = new Username();
		username.setUsernameId(usernameId);
		usernameRepository.save(username);
	}

}
