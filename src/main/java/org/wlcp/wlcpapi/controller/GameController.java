package org.wlcp.wlcpapi.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.wlcp.wlcpapi.datamodel.master.Game;
import org.wlcp.wlcpapi.repository.GameRepository;

@Controller
@RequestMapping("/gameController")
public class GameController {
	
	@Autowired
	private GameRepository gameRepository;
	
	@GetMapping(value="/getGame/{gameId}")
	public ResponseEntity<Game> getGame(@PathVariable String gameId) {
		Optional<Game> game = gameRepository.findById(gameId);
		return new ResponseEntity<Game>(game.isPresent() ? game.get() : null, HttpStatus.OK);
	}

}
