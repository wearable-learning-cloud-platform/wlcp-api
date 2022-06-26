package org.wlcp.wlcpapi.service.impl;

import static org.wlcp.wlcpapi.helper.HelperMethods.deepCopy;
import static org.wlcp.wlcpapi.helper.HelperMethods.md5;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.wlcp.wlcpapi.archive.repository.ArchiveGameRepository;
import org.wlcp.wlcpapi.archive.repository.ArchiveUsernameRepository;
import org.wlcp.wlcpapi.archive.repository.GameSaveRepository;
import org.wlcp.wlcpapi.datamodel.enums.SaveType;
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
import org.wlcp.wlcpapi.helper.HelperMethods;
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
	public Game loadGameVersion(String gameId) {
		Game game = archiveGameRepository.findById(gameId).get();
		Hibernate.initialize(game.getStates());
		Hibernate.initialize(game.getConnections());
		Hibernate.initialize(game.getTransitions());
		return game;
	}
	
	@Override
	public Game saveGame(SaveDto saveDto) {
		switch(saveDto.gameSave.getType()) {
		case NEW_GAME:
			gameExists(saveDto.game.getGameId());
		case REVERT_ARCHIVED:
		case MANUAL:
		case RUN_AND_DEBUG:
		case AUTO:
			Game game = gameRepository.save(saveDto.game);
			archiveGame(saveDto);
			return game;
		//case AUTO:
			//archiveGame(saveDto);
			//return null;
		default:
			return null;
		}
	}
	
	@Override
	@Transactional("archiveTransactionManager")
	public Game revertGame(CopyRenameDeleteGameDto copyRenameDeleteGameDto) {
		Game copiedGame = deepCopyWithoutSave(copyRenameDeleteGameDto.oldGameId, copyRenameDeleteGameDto.newGameId, copyRenameDeleteGameDto.usernameId, true, archiveGameRepository);//deepCopyWithoutSaveArchive(copyRenameDeleteGameDto);
		gameRepository.deleteById(copyRenameDeleteGameDto.newGameId);
		GameSave gameSave = new GameSave(copyRenameDeleteGameDto.newGameId, copyRenameDeleteGameDto.oldGameId, SaveType.REVERT_ARCHIVED, "Reverting game.");
		gameSave = gameSaveRepository.save(gameSave);
		return gameRepository.save(copiedGame);
	}
	
	@Transactional("archiveTransactionManager")
	private void archiveGame(SaveDto saveDto) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ObjectOutputStream outputStrm;
		try {
			outputStrm = new ObjectOutputStream(outputStream);
			outputStrm.writeObject(saveDto.game);
		} catch (IOException e) {
			return;
		}
		String s = outputStream.toString();
		String md5 = HelperMethods.md5(s);
		
		String name = System.currentTimeMillis() + " " + saveDto.gameSave.getDescription() + " " + saveDto.gameSave.getType() + " " + saveDto.game.getGameId() + " " + md5;
		for(int i = 0; i < new Random().nextInt(32) + 1; i++) {
			name = HelperMethods.md5(name);
		}
		
		GameSave gameSave = new GameSave(saveDto.game.getGameId(), saveDto.game.getGameId() + " " + name, saveDto.gameSave.getType(), saveDto.gameSave.getDescription());
		gameSave = gameSaveRepository.save(gameSave);
		if(!archiveUsernameRepository.existsById(saveDto.game.getUsername().getUsernameId())) { archiveUsernameRepository.save(saveDto.game.getUsername()); }
		Game copiedGame = deepCopyGame(saveDto.game.getGameId(), saveDto.game.getGameId() + " " + name, saveDto.game.getUsername(), saveDto.game.getVisibility(), saveDto.game);
		archiveGameRepository.save(copiedGame);
	}
	
	@Override
	@Transactional
	public Game copyGame(CopyRenameDeleteGameDto copyRenameDeleteGameDto) {
		gameExists(copyRenameDeleteGameDto.newGameId);
		return deepCopyGame(copyRenameDeleteGameDto.oldGameId, copyRenameDeleteGameDto.newGameId, copyRenameDeleteGameDto.usernameId, copyRenameDeleteGameDto.visibility, gameRepository);
	}
	
	@Transactional(transactionManager="archiveTransactionManager")
	public Game copyArchivedGame(CopyRenameDeleteGameDto copyRenameDeleteGameDto) {
		gameExists(copyRenameDeleteGameDto.newGameId);
		Game game = deepCopyWithoutSave(copyRenameDeleteGameDto.oldGameId, copyRenameDeleteGameDto.newGameId, copyRenameDeleteGameDto.usernameId, copyRenameDeleteGameDto.visibility, archiveGameRepository);
		return gameRepository.save(game);
	}

	@Override
	@Transactional
	public Game renameGame(CopyRenameDeleteGameDto copyRenameDeleteGameDto) {
		gameExists(copyRenameDeleteGameDto.newGameId);
		Game game = gameRepository.findById(copyRenameDeleteGameDto.oldGameId).get();
		
		if(game.getUsername().getUsernameId().equals(copyRenameDeleteGameDto.usernameId)) {
			Game copiedGame = deepCopyGame(copyRenameDeleteGameDto.oldGameId, copyRenameDeleteGameDto.newGameId, copyRenameDeleteGameDto.usernameId, game.getVisibility(), gameRepository);
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
		deleteArchivedGames(game.getGameId());
		gameRepository.delete(game);	
	}
	
	private void deleteArchivedGames(String gameId) {
		List<GameSave> gameSaves = gameSaveRepository.findByMasterGameId(gameId);
		Collections.reverse(gameSaves);
		for(GameSave gameSave : gameSaves) {
			archiveGameRepository.deleteById(gameSave.getReferenceGameId());
			gameSaveRepository.delete(gameSave);
		}

	}
	
	public Game deepCopyGame(String gameId, String newGameId, String usernameId, Boolean visibility, JpaRepository<Game, String> gameRepository) {		
		return gameRepository.save(deepCopyWithoutSave(gameId, newGameId, usernameId, visibility, gameRepository));
	}
	
	public Game deepCopyWithoutSave(String gameId, String newGameId, String usernameId, Boolean visibility, JpaRepository<Game, String> gameRepository) {
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
	
	private boolean gameExists(String gameId) {
		if(gameRepository.findById(gameId).isPresent()) {
			//Create new already exists
			throw new RuntimeException("Game Already Exists!");
		} else {
			return false;	
		}
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
