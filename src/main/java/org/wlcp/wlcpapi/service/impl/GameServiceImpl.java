package org.wlcp.wlcpapi.service.impl;

import static org.wlcp.wlcpapi.helper.HelperMethods.deepCopy;
import static org.wlcp.wlcpapi.helper.HelperMethods.md5;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.wlcp.wlcpapi.archive.repository.GameSaveRepository;
import org.wlcp.wlcpapi.archive.repository.ArchiveGameRepository;
import org.wlcp.wlcpapi.archive.repository.ArchiveUsernameRepository;
import org.wlcp.wlcpapi.datamodel.master.Game;
import org.wlcp.wlcpapi.datamodel.master.GameSave;
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
import org.wlcp.wlcpapi.dto.SaveDto;
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
	private GameSaveRepository gameSaveRepository;
	
	@Autowired
	private ArchiveGameRepository archiveGameRepository;
	
	@Autowired
	private ArchiveUsernameRepository archiveUsernameRepository;
	
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
	@Transactional("archiveTransactionManager")
	public Game loadGameVersion(String gameId, String version) {
		GameSave gameSave = gameSaveRepository.findByMasterGameIdAndVersion(gameId, Integer.valueOf(version));
		Game game = archiveGameRepository.findById(gameSave.getReferenceGameId()).get();
		Hibernate.initialize(game.getStates());
		Hibernate.initialize(game.getConnections());
		Hibernate.initialize(game.getTransitions());
		return game;
	}
	
	@Override
	public Game saveGame(SaveDto saveDto) {
		if(saveDto.game.getStates().size() == 0 && saveDto.game.getConnections().size() == 0 && saveDto.game.getTransitions().size() == 0) {
			if(!gameRepository.findById(saveDto.game.getGameId()).isPresent()) {
				Game game = gameRepository.save(saveDto.game);
				archiveGame(saveDto);
				return game;
			} else {
				//Create new already exists
				throw new RuntimeException("Game Already Exists!");
			}
		} else {
			Game game = gameRepository.save(saveDto.game);
			archiveGame(saveDto);
			return game;
		}
	}
	
	@Transactional("archiveTransactionManager")
	private void archiveGame(SaveDto saveDto) {
		int version = gameSaveRepository.max(saveDto.game.getGameId()) == null ? 0 : gameSaveRepository.max(saveDto.game.getGameId()) + 1;
		GameSave gameSave = new GameSave(saveDto.game.getGameId(), saveDto.game.getGameId() + " " + version, saveDto.gameSave.getType(), version, saveDto.gameSave.getDescription());
		gameSave = gameSaveRepository.save(gameSave);
		if(!archiveUsernameRepository.existsById(saveDto.game.getUsername().getUsernameId())) { archiveUsernameRepository.save(saveDto.game.getUsername()); }
		Game copiedGame = deepCopyWithoutSave(saveDto.game.getGameId(), saveDto.game.getGameId() + " " + gameSave.getVersion(), saveDto.game.getUsername().getUsernameId(), saveDto.game.getVisibility());
		archiveGameRepository.save(copiedGame);
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
	
	public Game deepCopyGame(String gameId, String newGameId, String usernameId, Boolean visibility) {		
		return gameRepository.save(deepCopyWithoutSave(gameId, newGameId, usernameId, visibility));
	}
	
	public Game deepCopyWithoutSave(String gameId, String newGameId, String usernameId, Boolean visibility) {
		Game game = gameRepository.findById(gameId).get();
		
		Hibernate.initialize(game.getStates());
		Hibernate.initialize(game.getConnections());
		Hibernate.initialize(game.getTransitions());
		
		Username username = usernameRepository.findById(usernameId).get();
		
		Game copiedGame = deepCopyGame(gameId, newGameId, username, visibility, game);
		
		return copiedGame;
	}
	
	public Game deepCopyGame(String gameId, String newGameId, Username username, Boolean visibility, Game game) {
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
		return copiedGame;
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
