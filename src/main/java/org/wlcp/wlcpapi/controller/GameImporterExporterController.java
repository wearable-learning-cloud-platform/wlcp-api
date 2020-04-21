package org.wlcp.wlcpapi.controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.wlcp.wlcpapi.datamodel.master.Game;
import org.wlcp.wlcpapi.datamodel.master.transition.KeyboardInput;
import org.wlcp.wlcpapi.datamodel.master.transition.SequenceButtonPress;
import org.wlcp.wlcpapi.datamodel.master.transition.Transition;
import org.wlcp.wlcpapi.repository.GameRepository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/gameImporterExporterController")
public class GameImporterExporterController {
	
	@Autowired
	private GameRepository gameRepository;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@PostMapping(value="/importGame")
	@Transactional
	public ResponseEntity<String> importGame(@RequestParam("file") MultipartFile file) throws IOException {
	
		ObjectInputStream in = new WlcpObjectInputStream(file.getInputStream());
		Game game = null;
		try {
			game = (Game) in.readObject();
		} catch (ClassNotFoundException e) {
			in.close();
			e.printStackTrace();
			return new ResponseEntity<String>("Error importing!", HttpStatus.INTERNAL_SERVER_ERROR);
		}
			
		in.close();
		
		for(Transition transition : game.getTransitions()) {
			for(Entry<String, SequenceButtonPress> entry : transition.getSequenceButtonPresses().entrySet()) {
				entry.getValue().setSequenceButtonPressId(transition.getTransitionId() + "_" + entry.getValue().getScope().toLowerCase().replace(" ", "_"));
			}
			for(Entry<String, KeyboardInput> entry : transition.getKeyboardInputs().entrySet()) {
				entry.getValue().setKeyboardInputId(transition.getTransitionId() + "_" + entry.getValue().getScope().toLowerCase().replace(" ", "_"));
			}
		}
		
		gameRepository.save(game);
		
		return new ResponseEntity<String>("Import Success!", HttpStatus.OK);
	}
	
	@PostMapping(value="/importJSONGame")
	@Transactional
	public ResponseEntity<String> importJSONGame(@RequestParam("file") MultipartFile file) throws IOException {
		
		Game game = objectMapper.readValue(file.getBytes(), Game.class);
		
		gameRepository.save(game);
		
		return new ResponseEntity<String>("Import Success!", HttpStatus.OK);
	}
	
	@GetMapping(value="/exportJSONGame", produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional
	public ResponseEntity<String> exportJSONGame(@RequestParam("gameId") String gameId) throws JsonProcessingException {
		
		Game game = gameRepository.findById(gameId).get();
		
		String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(game);
		
		HttpHeaders responseHeaders = new HttpHeaders();
	    responseHeaders.add("content-disposition", "attachment; filename=" + game.getGameId() + ".wlcpx");

		return new ResponseEntity<String>(json, responseHeaders, HttpStatus.OK);
	}

}
