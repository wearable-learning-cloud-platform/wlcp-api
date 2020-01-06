package org.wlcp.wlcpapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wlcp.wlcpapi.datamodel.master.Game;
import org.wlcp.wlcpapi.dto.CreateGameDto;
import org.wlcp.wlcpapi.dto.GenericResponse;
import org.wlcp.wlcpapi.service.impl.CreateLoadSaveGameServiceImpl;

@Controller
@RequestMapping("/gameController")
public class CreateLoadSaveGameController {
	
	@Autowired
	private CreateLoadSaveGameServiceImpl createLoadSaveGameServiceImpl;

	@PostMapping(value="/createGame")
	@ResponseBody
	public ResponseEntity<GenericResponse> createGame(@RequestBody CreateGameDto createGameDto) { 
		return new ResponseEntity<GenericResponse>(new GenericResponse("Message", createLoadSaveGameServiceImpl.createGame(createGameDto)), HttpStatus.OK);
	}
	
	@PostMapping(value="/saveGame")
	@ResponseBody
	public ResponseEntity<GenericResponse> createGame(@RequestBody Game game) { 
		return new ResponseEntity<GenericResponse>(new GenericResponse("Message", createLoadSaveGameServiceImpl.saveGame(game)), HttpStatus.OK);
	}
	
}
