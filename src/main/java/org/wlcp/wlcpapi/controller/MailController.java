package org.wlcp.wlcpapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wlcp.wlcpapi.service.MailService;

@Controller
@RequestMapping("/mailController")
@Profile("ecs")
public class MailController {
	
	@Autowired
	private MailService mailService;
	
	@PostMapping("/postMail")
	@ResponseBody
	public ResponseEntity<String> postMail(@RequestParam String name, @RequestParam String email, @RequestParam String subject, @RequestParam String message) {
		if(mailService.handlePostMail(name, email, subject, message)) {
			return new ResponseEntity<String>("Success", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("Failure", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}
