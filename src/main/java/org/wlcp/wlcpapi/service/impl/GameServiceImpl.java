package org.wlcp.wlcpapi.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.wlcp.wlcpapi.datamodel.master.Game;
import org.wlcp.wlcpapi.datamodel.master.Username;
import org.wlcp.wlcpapi.datamodel.master.connection.Connection;
import org.wlcp.wlcpapi.datamodel.master.state.OutputState;
import org.wlcp.wlcpapi.datamodel.master.state.StartState;
import org.wlcp.wlcpapi.datamodel.master.state.State;
import org.wlcp.wlcpapi.datamodel.master.transition.KeyboardInput;
import org.wlcp.wlcpapi.datamodel.master.transition.SequenceButtonPress;
import org.wlcp.wlcpapi.datamodel.master.transition.Transition;
import org.wlcp.wlcpapi.dto.CopyRenameDeleteGameDto;
import org.wlcp.wlcpapi.dto.GameDto;
import org.wlcp.wlcpapi.repository.GameRepository;
import org.wlcp.wlcpapi.repository.UsernameRepository;
import org.wlcp.wlcpapi.service.GameService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class GameServiceImpl implements GameService {
	
	@Autowired
	private GameRepository gameRepository;
	
	@Autowired
	private UsernameRepository usernameRepository;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Override
	public List<GameDto> getPrivateGames(String usernameId) {
		Username username = new Username();
		username.setUsernameId(usernameId);
		List<Game> games = gameRepository.findByUsername(username);
		List<GameDto> returnGames = new ArrayList<GameDto>();
		for(Game game : games) {
			if(!game.getDataLog()) {
				returnGames.add(new GameDto(game.getGameId()));
			}
		}
		return returnGames;
	}

	@Override
	public List<GameDto> getPublicGames() {
		List<Game> games = gameRepository.findAll();
		List<GameDto> returnGames = new ArrayList<GameDto>();
		for(Game game : games) {
			if(game.getVisibility() && !game.getDataLog()) {
				returnGames.add(new GameDto(game.getGameId()));
			}
		}
		return returnGames;
	}
	
	@Override
	public Game loadGame(String gameId) {
		return gameRepository.findById(gameId).get();
	}
	
	@Override
	public Game saveGame(Game game) {
		return gameRepository.save(game);
	}
	
	@Override
	@Transactional
	public Game copyGame(CopyRenameDeleteGameDto copyRenameDeleteGameDto) {
		return deepCopyGame(copyRenameDeleteGameDto.oldGameId, copyRenameDeleteGameDto.newGameId, copyRenameDeleteGameDto.usernameId, copyRenameDeleteGameDto.visibility);
	}

	@Override
	@Transactional
	public Game renameGame(CopyRenameDeleteGameDto copyRenameDeleteGameDto) {
		Game game = gameRepository.findById(copyRenameDeleteGameDto.oldGameId).get();
		
		if(game.getUsername().getUsernameId().equals(copyRenameDeleteGameDto.usernameId)) {
			Game copiedGame = deepCopyGame(copyRenameDeleteGameDto.oldGameId, copyRenameDeleteGameDto.newGameId, copyRenameDeleteGameDto.usernameId, game.getVisibility());
			gameRepository.delete(game);
			return copiedGame;
		} else {
			return null;
		}
	}

	@Override
	@Transactional
	public void deleteGame(CopyRenameDeleteGameDto copyRenameDeleteGameDto) {
		Game game = gameRepository.findById(copyRenameDeleteGameDto.oldGameId).get();
		gameRepository.delete(game);	
	}
	
	private Game deepCopyGame(String gameId, String newGameId, String usernameId, Boolean visibility) {
		Game game = gameRepository.findById(gameId).get();
		Username username = usernameRepository.findById(usernameId).get();
		
		//Initialize Lazy Load Proxy's
		//This should and can be done generically and recursively. Temporary for now.
		Hibernate.initialize(game.getStates());
		Hibernate.initialize(game.getConnections());
		Hibernate.initialize(game.getTransitions());
		
		for(State state : game.getStates()) {
			Hibernate.initialize(state.getInputConnections());
			Hibernate.initialize(state.getOutputConnections());
			if(state instanceof OutputState) {
				Hibernate.initialize(((OutputState) state).getDisplayText());
				Hibernate.initialize(((OutputState) state).getPictureOutputs());
			}
		}
		
		for(Connection connection : game.getConnections()) {
			Hibernate.initialize(connection.getConnectionFrom());
			Hibernate.initialize(connection.getConnectionTo());
		}
		
		for(Transition transition : game.getTransitions()) {
			Hibernate.initialize(transition.getActiveTransitions());
			Hibernate.initialize(transition.getSingleButtonPresses());
			Hibernate.initialize(transition.getSequenceButtonPresses());
			Hibernate.initialize(transition.getKeyboardInputs());
			for(Entry<String, SequenceButtonPress> entry : transition.getSequenceButtonPresses().entrySet()) {
				Hibernate.initialize(entry.getValue().getSequences());
			}
			for(Entry<String, KeyboardInput> entry : transition.getKeyboardInputs().entrySet()) {
				Hibernate.initialize(entry.getValue().getKeyboardInputs());
			}
		}
		
		Game copiedGame = (Game) deepCopy(game);
		copiedGame.setGameId(newGameId);
		copiedGame.setUsername(username);
		copiedGame.setVisibility(visibility);
		
		for(State state : copiedGame.getStates()) {
			state.setGame(copiedGame);
			if(state instanceof StartState) {
				state.setStateId(state.getStateId().replace(md5(gameId) + "_start", md5(newGameId) + "_start"));
			} else if(state instanceof OutputState) {
				state.setStateId(state.getStateId().replace(md5(gameId) +  "_state_", md5(newGameId) +  "_state_"));
			}
			
		}
		
		for(Connection connection : copiedGame.getConnections()) {
			connection.setGame(copiedGame);
			connection.setConnectionId(connection.getConnectionId().replace(md5(gameId) +  "_connection_", md5(newGameId) +  "_connection_"));
		}
		
		for(Transition transition : copiedGame.getTransitions()) {
			transition.setGame(copiedGame);
			transition.setTransitionId(transition.getTransitionId().replace(md5(gameId) +  "_transition_", md5(newGameId) +  "_transition_"));
			for(Entry<String, SequenceButtonPress> entry : transition.getSequenceButtonPresses().entrySet()) {
				entry.getValue().setSequenceButtonPressId(transition.getTransitionId() + "_" + entry.getValue().getScope().toLowerCase().replace(" ", "_"));
			}
			for(Entry<String, KeyboardInput> entry : transition.getKeyboardInputs().entrySet()) {
				entry.getValue().setKeyboardInputId(transition.getTransitionId() + "_" + entry.getValue().getScope().toLowerCase().replace(" ", "_"));
			}
		}
		
		return gameRepository.save(copiedGame);
		
	}
	
	private static Object deepCopy(Object object) {
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			ObjectOutputStream outputStrm = new ObjectOutputStream(outputStream);
			outputStrm.writeObject(object);
			ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
			ObjectInputStream objInputStream = new ObjectInputStream(inputStream);
			return objInputStream.readObject();
		} catch (Exception e) {
			throw new RuntimeException("There was an error with the deep copy.");
		}
	}
	
	private static String md5(String input) {
		MessageDigest m = null;
		try {
			m = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		m.reset();
		m.update(input.getBytes());
		byte[] digest = m.digest();
		BigInteger bigInt = new BigInteger(1,digest);
		String hashtext = bigInt.toString(16);
		// Now we need to zero pad it if you actually want the full 32 chars.
		while(hashtext.length() < 32 ){
		  hashtext = "0"+hashtext;
		}
		return hashtext;
	}

	@Override
	public void importGame(MultipartFile file) {
		Game game = null;
		try {
			game = objectMapper.readValue(file.getBytes(), Game.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		gameRepository.save(game);
	}

	@Override
	public String exportGame(String gameId) {
		Game game = gameRepository.findById(gameId).get();
		try {
			return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(game);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
