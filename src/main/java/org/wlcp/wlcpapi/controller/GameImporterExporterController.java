package org.wlcp.wlcpapi.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
import org.wlcp.wlcpapi.datamodel.master.Username;
import org.wlcp.wlcpapi.datamodel.master.connection.Connection;
import org.wlcp.wlcpapi.datamodel.master.state.OutputState;
import org.wlcp.wlcpapi.datamodel.master.state.PictureOutput;
import org.wlcp.wlcpapi.datamodel.master.state.State;
import org.wlcp.wlcpapi.datamodel.master.state.StateType;
import org.wlcp.wlcpapi.datamodel.master.transition.KeyboardInput;
import org.wlcp.wlcpapi.datamodel.master.transition.SequenceButtonPress;
import org.wlcp.wlcpapi.datamodel.master.transition.Transition;
import org.wlcp.wlcpapi.service.impl.CreateLoadSaveGameServiceImpl;

@Controller
@RequestMapping("/gameImporterExporterController")
public class GameImporterExporterController {
	
	@PersistenceContext
	EntityManager entityManager;
	
	@Autowired
	private CreateLoadSaveGameServiceImpl createLoadSaveGameServiceImpl;
	
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
				entry.getValue().setSequenceButtonPressId(null);
			}
			for(Entry<String, KeyboardInput> entry : transition.getKeyboardInputs().entrySet()) {
				entry.getValue().setKeyboardInputId(null);
			}
		}
		
		entityManager.persist(game);
		
		return new ResponseEntity<String>("Import Success!", HttpStatus.OK);
	}
	
	@PostMapping(value="/importGame2")
	@Transactional
	public ResponseEntity<String> importGame2(@RequestParam("file") MultipartFile file) throws IOException {
	
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
		
		Game game2 = new Game(game.getGameId(), 0, 0, entityManager.find(Username.class, game.getUsername().getUsernameId()), false, false);
		entityManager.merge(game2);
		
		for(State state : game.getStates()) {
			state.setInputConnections(null);
			state.setOutputConnections(null);
			switch(state.getStateType()) {
			case OUTPUT_STATE:
				Map<String, String> displayText = new HashMap<String, String>();
				for(Entry<String, String> entry : ((OutputState) state).getDisplayText().entrySet()) {
					displayText.put(entry.getKey(), entry.getValue());
				}
				((OutputState) state).setDisplayText(displayText);
				((OutputState) state).setPictureOutputs(new HashMap<String, PictureOutput>());
				break;
			}
		}
		
		createLoadSaveGameServiceImpl.saveGame(game);
		
		in = new WlcpObjectInputStream(file.getInputStream());
		try {
			game = (Game) in.readObject();
			for(State state : game.getStates()) {
				switch(state.getStateType()) {
				case OUTPUT_STATE:
					Map<String, String> displayText = new HashMap<String, String>();
					for(Entry<String, String> entry : ((OutputState) state).getDisplayText().entrySet()) {
						displayText.put(entry.getKey(), entry.getValue());
					}
					((OutputState) state).setDisplayText(displayText);
					((OutputState) state).setPictureOutputs(new HashMap<String, PictureOutput>());
					break;
				}
			}
			createLoadSaveGameServiceImpl.saveGame(game);
			in.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

//		entityManager.merge(game);
//		entityManager.flush();
//		entityManager.clear();
		
		return new ResponseEntity<String>("Import Success!", HttpStatus.OK);
	}

	@GetMapping(value="/exportGame", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@Transactional
	public ResponseEntity<byte[]> exportGame(@RequestParam("gameId") String gameId) {
		
		Game game = entityManager.getReference(Game.class, gameId);
		game.hashCode();
		game.getStates().hashCode();
		game.getTransitions().hashCode();
		game.getConnections().hashCode();
		
		for(State state : game.getStates()) {
			if(state.getStateType().equals(StateType.OUTPUT_STATE)) {
				((OutputState) state).getDisplayText().hashCode();
			}
		}
		
		for(Transition transition : game.getTransitions()) {
			transition.getActiveTransitions().hashCode();
			transition.getSingleButtonPresses().hashCode();
			transition.getSequenceButtonPresses().hashCode();
			for(Entry<String, SequenceButtonPress> entry : transition.getSequenceButtonPresses().entrySet()) {
				entry.getValue().getSequences().hashCode();
			}
			transition.getKeyboardInputs().hashCode();
			for(Entry<String, KeyboardInput> entry : transition.getKeyboardInputs().entrySet()) {
				entry.getValue().getKeyboardInputs().hashCode();
			}
		}
		
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ObjectOutput outputObject = null;
		try {
			outputObject = new ObjectOutputStream(byteArrayOutputStream);
			outputObject.writeObject(game);
			outputObject.flush();
		} catch (IOException e) {
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}  
		
		HttpHeaders responseHeaders = new HttpHeaders();
	    responseHeaders.add("content-disposition", "attachment; filename=" + game.getGameId() + ".wlcpx");

		return new ResponseEntity<byte[]>(byteArrayOutputStream.toByteArray(), responseHeaders, HttpStatus.OK);
	}

}
