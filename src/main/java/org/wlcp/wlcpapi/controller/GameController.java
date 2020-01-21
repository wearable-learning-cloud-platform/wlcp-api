package org.wlcp.wlcpapi.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.wlcp.wlcpapi.datamodel.master.Game;
import org.wlcp.wlcpapi.dto.GameDto;
import org.wlcp.wlcpapi.dto.GenericResponse;
import org.wlcp.wlcpapi.repository.GameRepository;

@Controller
@RequestMapping("/gameController")
public class GameController {
	
	@Autowired
	private GameRepository gameRepository;
	
	@GetMapping("/getGames")
	public ResponseEntity<GenericResponse> getGames() {
		List<Game> games = gameRepository.findAll();
		List<GameDto> returnGames = new ArrayList<GameDto>();
		for(Game game : games) {
			if(game.getVisibility() && !game.getDataLog()) {
				returnGames.add(new GameDto(game.getGameId()));
			}
		}
		return new ResponseEntity<GenericResponse>(new GenericResponse("", returnGames), HttpStatus.OK);
	}
	
	@GetMapping(value="/getGame/{gameId}")
	public ResponseEntity<Game> getGame(@PathVariable String gameId) {
		Optional<Game> game = gameRepository.findById(gameId);
		return new ResponseEntity<Game>(game.isPresent() ? game.get() : null, HttpStatus.OK);
	}

}
