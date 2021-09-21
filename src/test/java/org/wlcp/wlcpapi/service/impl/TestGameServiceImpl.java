package org.wlcp.wlcpapi.service.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.wlcp.wlcpapi.datamodel.master.Game;
import org.wlcp.wlcpapi.datamodel.master.Username;
import org.wlcp.wlcpapi.datamodel.master.connection.Connection;
import org.wlcp.wlcpapi.datamodel.master.state.OutputState;
import org.wlcp.wlcpapi.datamodel.master.state.StartState;
import org.wlcp.wlcpapi.datamodel.master.transition.KeyboardInput;
import org.wlcp.wlcpapi.datamodel.master.transition.SequenceButtonPress;
import org.wlcp.wlcpapi.datamodel.master.transition.Transition;
import org.wlcp.wlcpapi.dto.CopyRenameDeleteGameDto;
import org.wlcp.wlcpapi.repository.GameRepository;
import org.wlcp.wlcpapi.repository.UsernameRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TestGameServiceImpl {

	@Mock
	private GameRepository gameRepository;
	
	@Mock
	private UsernameRepository usernameRepository;

	@InjectMocks
	private GameServiceImpl gameService;

	@Test
	public void testGetPrivateGamesNotDataLog() {

		List<Game> gameList = new ArrayList<Game>();
		gameList.add(createBasicTestGame());

		when(gameRepository.findByUsername(any(Username.class))).thenReturn(gameList);

		gameService.getPrivateGames("");
	}

	@Test
	public void testGetPrivateGamesDataLog() {

		List<Game> gameList = new ArrayList<Game>();
		gameList.add(createBasicTestGame());
		gameList.get(0).setDataLog(true);

		when(gameRepository.findByUsername(any(Username.class))).thenReturn(gameList);

		gameService.getPrivateGames("");
	}

	@Test
	public void testGetPublicGamesVisible() {

		List<Game> gameList = new ArrayList<Game>();
		gameList.add(createBasicTestGame());

		when(gameRepository.findAll()).thenReturn(gameList);

		gameService.getPublicGames();
	}

	@Test
	public void testGetPublicGamesNotVisible() {
		List<Game> gameList = new ArrayList<Game>();
		gameList.add(createBasicTestGame());
		gameList.get(0).setVisibility(false);

		when(gameRepository.findAll()).thenReturn(gameList);

		gameService.getPublicGames();
	}
	
	@Test
	public void testLoadGameSuccess() {
		when(gameRepository.findById(any(String.class))).thenReturn(Optional.of(new Game()));
		Game game = gameService.loadGame("");
		Assertions.assertNotNull(game);
	}
	
	@Test
	public void testSaveGameSuccess() {
		when(gameRepository.save(any(Game.class))).thenReturn(new Game());
		gameService.saveGame(new Game());
	}
	
	@Test
	public void testCopyGameSuccess() {
		CopyRenameDeleteGameDto copyRenameDeleteGameDto = new CopyRenameDeleteGameDto();
		copyRenameDeleteGameDto.newGameId = "newGameId";
		copyRenameDeleteGameDto.oldGameId = "gameId";
		copyRenameDeleteGameDto.usernameId = "";
		copyRenameDeleteGameDto.visibility = true;
		when(gameRepository.findById(copyRenameDeleteGameDto.oldGameId)).thenReturn(Optional.of(createAdvanceTestGame()));
		when(gameRepository.findById(copyRenameDeleteGameDto.newGameId)).thenReturn(Optional.empty());
		when(usernameRepository.findById(any(String.class))).thenReturn(Optional.of(new Username()));
		gameService.copyGame(copyRenameDeleteGameDto);
	}
	
	@Test
	public void testRenameGameSuccess() {
		CopyRenameDeleteGameDto copyRenameDeleteGameDto = new CopyRenameDeleteGameDto();
		copyRenameDeleteGameDto.newGameId = "newGameId";
		copyRenameDeleteGameDto.oldGameId = "gameId";
		copyRenameDeleteGameDto.usernameId = "";
		copyRenameDeleteGameDto.visibility = true;
		when(gameRepository.findById(copyRenameDeleteGameDto.oldGameId)).thenReturn(Optional.of(createAdvanceTestGame()));
		when(gameRepository.findById(copyRenameDeleteGameDto.newGameId)).thenReturn(Optional.empty());
		when(usernameRepository.findById(any(String.class))).thenReturn(Optional.of(new Username()));
		gameService.renameGame(copyRenameDeleteGameDto);
	}
	
	@Test
	public void testDeleteGameSuccess() {
		CopyRenameDeleteGameDto copyRenameDeleteGameDto = new CopyRenameDeleteGameDto();
		copyRenameDeleteGameDto.newGameId = "newGameId";
		copyRenameDeleteGameDto.oldGameId = "gameId";
		copyRenameDeleteGameDto.usernameId = "";
		copyRenameDeleteGameDto.visibility = true;
		when(gameRepository.findById(any(String.class))).thenReturn(Optional.of(new Game()));
		gameService.deleteGame(copyRenameDeleteGameDto);
	}
	
	private Game createBasicTestGame() {
		return new Game("gameId", 3, 3, new Username("","","","",""), true, 0, 0, 0, false);
	}
	
	private Game createAdvanceTestGame() {
		Game game = createBasicTestGame();
		StartState startState = new StartState();
		startState.setStateId(md5(game.getGameId()) + "_start");
		OutputState outputState = new OutputState();
		outputState.setStateId(md5(game.getGameId()) + "_state_");
		Connection connection = new Connection();
		connection.setConnectionId(md5(game.getGameId()) + "_connection_");
		Transition transition = new Transition();
		transition.setTransitionId(md5(game.getGameId()) + "_transition_");
		SequenceButtonPress sequenceButtonPress = new SequenceButtonPress();
		sequenceButtonPress.setScope("");
		sequenceButtonPress.getSequences().add("");
		KeyboardInput keyboardInput = new KeyboardInput();
		keyboardInput.setScope("");
		keyboardInput.getKeyboardInputs().add("");
		transition.getSequenceButtonPresses().put("scope", sequenceButtonPress);
		transition.getKeyboardInputs().put("scope", keyboardInput);
		game.getStates().add(startState);
		game.getStates().add(outputState);
		game.getConnections().add(connection);
		game.getTransitions().add(transition);
		return game;
		
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

}
