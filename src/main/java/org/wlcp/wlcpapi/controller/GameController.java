package org.wlcp.wlcpapi.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.wlcp.wlcpapi.archive.repository.GameSaveRepository;
import org.wlcp.wlcpapi.datamodel.enums.SaveType;
import org.wlcp.wlcpapi.datamodel.master.Game;
import org.wlcp.wlcpapi.datamodel.master.GameSave;
import org.wlcp.wlcpapi.dto.CopyRenameDeleteGameDto;
import org.wlcp.wlcpapi.dto.GameDto;
import org.wlcp.wlcpapi.dto.SaveDto;
import org.wlcp.wlcpapi.repository.GameRepository;
import org.wlcp.wlcpapi.service.GameService;

import com.fasterxml.jackson.core.JsonProcessingException;

@Controller
@RequestMapping("/gameController")
@Validated
public class GameController {
	
	@Autowired
	private GameRepository gameRepository;
	
	@Autowired
	private GameService gameService;
	
	@Autowired
	private GameSaveRepository gameSaveRepository;
	
	@GetMapping("/getGames")
	public ResponseEntity<List<GameDto>> getGames() {
		List<Game> games = gameRepository.findAll();
		List<GameDto> returnGames = new ArrayList<GameDto>();
		for(Game game : games) {
			if(game.getVisibility() && !game.getDataLog()) {
				returnGames.add(new GameDto(game.getGameId()));
			}
		}
		return new ResponseEntity<List<GameDto>>(returnGames, HttpStatus.OK);
	}
	
	@GetMapping("/getGame/{gameId}")
	public ResponseEntity<Game> getGame(@PathVariable String gameId) {
		Optional<Game> game = gameRepository.findById(gameId);
		return new ResponseEntity<Game>(game.isPresent() ? game.get() : null, HttpStatus.OK);
	}
	
	@GetMapping(value="/getPrivateGames")
	@ResponseBody
	public ResponseEntity<List<GameDto>> getPrivateGames(@RequestParam("usernameId") @Valid @NotBlank String usernameId) {
		return new ResponseEntity<List<GameDto>>(gameService.getPrivateGames(usernameId), HttpStatus.OK);
	}
	
	@GetMapping(value="/getPublicGames")
	@ResponseBody
	public ResponseEntity<List<GameDto>> getPublicGames() {
		return new ResponseEntity<List<GameDto>>(gameService.getPublicGames(), HttpStatus.OK);
	}
	
	@GetMapping(value="/getGameHistory")
    @ResponseBody()
	public ResponseEntity<List<GameSave>> getGameHistory(@RequestParam("gameId") @Valid @NotBlank String gameId, @RequestParam("saveType") SaveType saveType) {
		return new ResponseEntity<List<GameSave>>(gameSaveRepository.findByMasterGameIdAndType(gameId, saveType), HttpStatus.OK);
	}
	
	@GetMapping(value="/loadGame")
    @ResponseBody()
	public ResponseEntity<Game> loadGame(@RequestParam("gameId") @Valid @NotBlank String gameId) {
		return new ResponseEntity<Game>(gameService.loadGame(gameId), HttpStatus.OK);
	}
	
	@GetMapping(value="/loadGameVersion")
    @ResponseBody()
	public ResponseEntity<Game> loadGameVersion(@RequestParam("gameId") @Valid @NotBlank String gameId, @RequestParam("saveType") SaveType saveType, @RequestParam("version") @Valid @NotBlank String version) {
		return new ResponseEntity<Game>(gameService.loadGameVersion(gameId, saveType, version), HttpStatus.OK);
	}
	
	@PostMapping(value="/saveGame")
	@ResponseBody
	public ResponseEntity<Game> saveGame(@RequestBody SaveDto saveDto) {
		return new ResponseEntity<Game>(gameService.saveGame(saveDto), HttpStatus.OK);
	}
	
	@PostMapping(value="/copyGame")
	@ResponseBody
	public ResponseEntity<Game> copyGame(@RequestBody CopyRenameDeleteGameDto copyRenameDeleteGameDto) {
		return new ResponseEntity<Game>(gameService.copyGame(copyRenameDeleteGameDto), HttpStatus.OK);
	}
	
	@PostMapping(value="/renameGame")
	@ResponseBody
	public ResponseEntity<Game> renameGame(@RequestBody CopyRenameDeleteGameDto copyRenameDeleteGameDto) {
		return new ResponseEntity<Game>(gameService.renameGame(copyRenameDeleteGameDto), HttpStatus.OK);
	}
	
	@PostMapping(value="/deleteGame")
	@ResponseBody
	public ResponseEntity<String> deleteGame(@RequestBody CopyRenameDeleteGameDto copyRenameDeleteGameDto) {
		gameService.deleteGame(copyRenameDeleteGameDto);
		return new ResponseEntity<String>("{}", HttpStatus.OK);
	}
	
	@PostMapping(value="/importJSONGame")
	@Transactional
	public ResponseEntity<String> importJSONGame(@RequestParam("file") MultipartFile file) throws IOException {
		
		gameService.importGame(file);
		
		return new ResponseEntity<String>("Import Success!", HttpStatus.OK);
	}
	
	@GetMapping(value="/exportJSONGame", produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional
	public ResponseEntity<String> exportJSONGame(@RequestParam("gameId") String gameId) throws JsonProcessingException {
		
		HttpHeaders responseHeaders = new HttpHeaders();
	    responseHeaders.add("content-disposition", "attachment; filename=" + gameId + ".wlcpx");

		return new ResponseEntity<String>(gameService.exportGame(gameId), responseHeaders, HttpStatus.OK);
	}
	
}
