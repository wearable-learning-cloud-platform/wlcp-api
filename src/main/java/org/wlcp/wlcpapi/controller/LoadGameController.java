package org.wlcp.wlcpapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wlcp.wlcpapi.dto.GenericResponse;
import org.wlcp.wlcpapi.service.impl.LoadGameServiceImpl;

@Controller
@RequestMapping("/loadGameController")
public class LoadGameController {
	
	@Autowired
	public LoadGameServiceImpl loadGameServiceImpl;

	@GetMapping(value="/getPrivateGames")
	@ResponseBody
	public ResponseEntity<GenericResponse> getPrivateGames(@RequestParam("usernameId") String usernameId) {
		return new ResponseEntity<GenericResponse>(new GenericResponse("Message", loadGameServiceImpl.getPrivateGames(usernameId)), HttpStatus.OK);
	}
	
	@GetMapping(value="/getPublicGames")
	@ResponseBody
	public ResponseEntity<GenericResponse> getPublicGames() {
		return new ResponseEntity<GenericResponse>(new GenericResponse("Message", loadGameServiceImpl.getPublicGames()), HttpStatus.OK);
	}
	
	@GetMapping(value="/loadGame")
    @ResponseBody()
	public ResponseEntity<GenericResponse> loadGame(@RequestParam("gameId") String gameId) {
		return new ResponseEntity<GenericResponse>(new GenericResponse("Message", loadGameServiceImpl.loadGame(gameId)), HttpStatus.OK);
	}
}
