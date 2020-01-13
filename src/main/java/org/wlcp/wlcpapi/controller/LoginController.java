package org.wlcp.wlcpapi.controller;

import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

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
	public ResponseEntity<Boolean> login(HttpServletResponse response, @RequestBody UsernameDto usernameDto) { 
		Optional<Username> username = usernameRepository.findById(usernameDto.username);
		if(username.isPresent()) {
			Cookie cookie = new Cookie("wlcp.userSession", username.get().getUsernameId());
			cookie.setMaxAge(15 * 60);
			cookie.setPath("/");
			cookie.setHttpOnly(false);
			response.addCookie(cookie);
			return new ResponseEntity<Boolean>(true, HttpStatus.OK);
		}
		else {
			return new ResponseEntity<Boolean>(false, HttpStatus.OK);
		}
	}
}