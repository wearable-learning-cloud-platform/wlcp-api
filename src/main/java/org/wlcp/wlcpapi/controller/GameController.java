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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wlcp.wlcpapi.datamodel.master.Game;
import org.wlcp.wlcpapi.dto.CopyRenameDeleteGameDto;
import org.wlcp.wlcpapi.dto.GameDto;
import org.wlcp.wlcpapi.dto.GenericResponse;
import org.wlcp.wlcpapi.repository.GameRepository;
import org.wlcp.wlcpapi.service.GameService;

@Controller
@RequestMapping("/gameController")
public class GameController {
	
	@Autowired
	private GameRepository gameRepository;
	
	@Autowired
	private GameService gameService;
	
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
	
	@GetMapping("/getGame/{gameId}")
	public ResponseEntity<Game> getGame(@PathVariable String gameId) {
		Optional<Game> game = gameRepository.findById(gameId);
		return new ResponseEntity<Game>(game.isPresent() ? game.get() : null, HttpStatus.OK);
	}
	
	@GetMapping(value="/getPrivateGames")
	@ResponseBody
	public ResponseEntity<GenericResponse> getPrivateGames(@RequestParam("usernameId") String usernameId) {
		return new ResponseEntity<GenericResponse>(new GenericResponse("Message", gameService.getPrivateGames(usernameId)), HttpStatus.OK);
	}
	
	@GetMapping(value="/getPublicGames")
	@ResponseBody
	public ResponseEntity<GenericResponse> getPublicGames() {
		return new ResponseEntity<GenericResponse>(new GenericResponse("Message", gameService.getPublicGames()), HttpStatus.OK);
	}
	
	@GetMapping(value="/loadGame")
    @ResponseBody()
	public ResponseEntity<GenericResponse> loadGame(@RequestParam("gameId") String gameId) {
		return new ResponseEntity<GenericResponse>(new GenericResponse("Message", gameService.loadGame(gameId)), HttpStatus.OK);
	}
	
	@PostMapping(value="/saveGame")
	@ResponseBody
	public ResponseEntity<GenericResponse> createGame(@RequestBody Game game) { 
		return new ResponseEntity<GenericResponse>(new GenericResponse("Message", gameService.saveGame(game)), HttpStatus.OK);
	}
	
	@PostMapping(value="/copyGame")
	@ResponseBody
	public ResponseEntity<GenericResponse> copyGame(@RequestBody CopyRenameDeleteGameDto copyRenameDeleteGameDto) {
		String message = gameService.copyGame(copyRenameDeleteGameDto);
		if(message.equals("")) {
			return new ResponseEntity<GenericResponse>(new GenericResponse(message, new String()), HttpStatus.OK);
		} else {
			return new ResponseEntity<GenericResponse>(new GenericResponse(message, new String()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	@PostMapping(value="/renameGame")
	@ResponseBody
	public ResponseEntity<GenericResponse> renameGame(@RequestBody CopyRenameDeleteGameDto copyRenameDeleteGameDto) {
		gameService.renameGame(copyRenameDeleteGameDto);
		return new ResponseEntity<GenericResponse>(new GenericResponse("Message", new String()), HttpStatus.OK);
	}
	
	@PostMapping(value="/deleteGame")
	@ResponseBody
	public ResponseEntity<GenericResponse> deleteGame(@RequestBody CopyRenameDeleteGameDto copyRenameDeleteGameDto) {
		String message = gameService.deleteGame(copyRenameDeleteGameDto);
		if(message.equals("")) {
			return new ResponseEntity<GenericResponse>(new GenericResponse(message, new String()), HttpStatus.OK);
		} else {
			return new ResponseEntity<GenericResponse>(new GenericResponse(message, new String()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}
