package org.wlcp.wlcpapi.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map.Entry;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import org.wlcp.wlcpapi.repository.GameRepository;
import org.wlcp.wlcpapi.repository.UsernameRepository;

@Service
public class CopyRenameDeleteGameServiceImpl implements org.wlcp.wlcpapi.service.CopyRenameDeleteGameService {
	
	@Autowired
	private GameRepository gameRepository;
	
	@Autowired
	private UsernameRepository usernameRepository;

	@Override
	@Transactional
	public String copyGame(CopyRenameDeleteGameDto copyRenameDeleteGameDto) {
		deepCopyGame(copyRenameDeleteGameDto.oldGameId, copyRenameDeleteGameDto.newGameId, copyRenameDeleteGameDto.usernameId);
		return "";
	}

	@Override
	@Transactional
	public String renameGame(CopyRenameDeleteGameDto copyRenameDeleteGameDto) {
		Game game = gameRepository.findById(copyRenameDeleteGameDto.oldGameId).get();
		
		if(game.getUsername().getUsernameId().equals(copyRenameDeleteGameDto.usernameId)) {
			deepCopyGame(copyRenameDeleteGameDto.oldGameId, copyRenameDeleteGameDto.newGameId, copyRenameDeleteGameDto.usernameId);
			gameRepository.delete(game);
			return "";
		} else {
			return "You must own the game to rename it!";
		}
	}

	@Override
	@Transactional
	public String deleteGame(CopyRenameDeleteGameDto copyRenameDeleteGameDto) {
		Game game = gameRepository.findById(copyRenameDeleteGameDto.oldGameId).get();
		
		if(game.getUsername().getUsernameId().equals(copyRenameDeleteGameDto.usernameId)) {
			gameRepository.delete(game);
			return "";
		} else {
			return "You must own the game to delete it!";
		}	
	}
	
	private void deepCopyGame(String gameId, String newGameId, String usernameId) {
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
		
		for(State state : copiedGame.getStates()) {
			state.setGame(copiedGame);
			if(state instanceof StartState) {
				state.setStateId(state.getStateId().replace(gameId + "_start", newGameId + "_start"));
			} else if(state instanceof OutputState) {
				state.setStateId(state.getStateId().replace(gameId +  "_state_", newGameId +  "_state_"));
			}
			
		}
		
		for(Connection connection : copiedGame.getConnections()) {
			connection.setGame(copiedGame);
			connection.setConnectionId(connection.getConnectionId().replace(gameId +  "_connection_", newGameId +  "_connection_"));
		}
		
		for(Transition transition : copiedGame.getTransitions()) {
			transition.setGame(copiedGame);
			transition.setTransitionId(transition.getTransitionId().replace(gameId +  "_transition_", newGameId +  "_transition_"));
			for(Entry<String, SequenceButtonPress> entry : transition.getSequenceButtonPresses().entrySet()) {
				entry.getValue().setSequenceButtonPressId(transition.getTransitionId() + "_" + entry.getValue().getScope().toLowerCase().replace(" ", "_"));
			}
			for(Entry<String, KeyboardInput> entry : transition.getKeyboardInputs().entrySet()) {
				entry.getValue().setKeyboardInputId(transition.getTransitionId() + "_" + entry.getValue().getScope().toLowerCase().replace(" ", "_"));
			}
		}
		
		gameRepository.save(copiedGame);
		
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
			e.printStackTrace();
			return null;
		}
	}

}
