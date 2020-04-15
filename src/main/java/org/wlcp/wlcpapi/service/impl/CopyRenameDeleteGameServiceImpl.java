package org.wlcp.wlcpapi.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wlcp.wlcpapi.datamodel.master.Game;
import org.wlcp.wlcpapi.datamodel.master.Username;
import org.wlcp.wlcpapi.datamodel.master.connection.Connection;
import org.wlcp.wlcpapi.datamodel.master.state.OutputState;
import org.wlcp.wlcpapi.datamodel.master.state.PictureOutput;
import org.wlcp.wlcpapi.datamodel.master.state.StartState;
import org.wlcp.wlcpapi.datamodel.master.state.State;
import org.wlcp.wlcpapi.datamodel.master.state.StateType;
import org.wlcp.wlcpapi.datamodel.master.transition.KeyboardInput;
import org.wlcp.wlcpapi.datamodel.master.transition.SequenceButtonPress;
import org.wlcp.wlcpapi.datamodel.master.transition.SingleButtonPress;
import org.wlcp.wlcpapi.datamodel.master.transition.Transition;
import org.wlcp.wlcpapi.dto.CopyRenameDeleteGameDto;

@Service
public class CopyRenameDeleteGameServiceImpl implements org.wlcp.wlcpapi.service.CopyRenameDeleteGameService {
	
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	@Transactional
	public String copyGame(CopyRenameDeleteGameDto copyRenameDeleteGameDto) {
		deepCopyGame(copyRenameDeleteGameDto.oldGameId, copyRenameDeleteGameDto.newGameId, copyRenameDeleteGameDto.usernameId);
		return "";
	}

	@Override
	@Transactional
	public String renameGame(CopyRenameDeleteGameDto copyRenameDeleteGameDto) {
		Game game = entityManager.getReference(Game.class, copyRenameDeleteGameDto.oldGameId);
		
		if(game.getUsername().getUsernameId().equals(copyRenameDeleteGameDto.usernameId)) {
			deepCopyGame(copyRenameDeleteGameDto.oldGameId, copyRenameDeleteGameDto.newGameId, copyRenameDeleteGameDto.usernameId);
			entityManager.remove(game);
			return "";
		} else {
			return "You must own the game to rename it!";
		}
	}

	@Override
	@Transactional
	public String deleteGame(CopyRenameDeleteGameDto copyRenameDeleteGameDto) {
		Game game = entityManager.getReference(Game.class, copyRenameDeleteGameDto.oldGameId);
		
		if(game.getUsername().getUsernameId().equals(copyRenameDeleteGameDto.usernameId)) {
			entityManager.remove(game);
			return "";
		} else {
			return "You must own the game to delete it!";
		}	
	}
	
	private void deepCopyGame(String gameId, String newGameId, String usernameId) {
		
		Game game = entityManager.getReference(Game.class, gameId);
		Username username = entityManager.getReference(Username.class, usernameId);
		
		Game newGame = new Game(game.getGameId().replace(gameId, newGameId), game.getTeamCount(), game.getPlayersPerTeam(), username, game.getVisibility(), game.getDataLog());
		newGame.setStateIdCount(game.getStateIdCount());
		newGame.setConnectionIdCount(game.getConnectionIdCount());
		newGame.setTransitionIdCount(game.getTransitionIdCount());
		
		entityManager.persist(newGame);
		
		for(State state : game.getStates()) {
			if(state.getStateType().equals(StateType.START_STATE)) {
				StartState newStartState = new StartState(state.getStateId().replace(gameId, newGameId), newGame, state.getStateType(), state.getPositionX(), state.getPositionY(), new ArrayList<Connection>(), new ArrayList<Connection>());
				newGame.getStates().add(newStartState);
				entityManager.persist(newStartState);
			} else {
				Map<String, String> displayText = new HashMap<String, String>();
				Map<String, PictureOutput> pictureOutputs = new HashMap<String, PictureOutput>();
				for(Entry<String, String> entry : ((OutputState) state).getDisplayText().entrySet()) {
					displayText.put(entry.getKey(), entry.getValue());
				}
				for(Entry<String, PictureOutput> entry :  ((OutputState) state).getPictureOutputs().entrySet()) {
					pictureOutputs.put(entry.getKey(), new PictureOutput(entry.getValue().getUrl(), entry.getValue().getScale()));
				}
				OutputState newOutputState = new OutputState(state.getStateId().replace(gameId, newGameId), newGame, state.getStateType(), state.getPositionX(), state.getPositionY(), new ArrayList<Connection>(), new ArrayList<Connection>(), ((OutputState) state).getDescription(), displayText, pictureOutputs);
				newGame.getStates().add(newOutputState);
				entityManager.persist(newOutputState);
			}
		}
		
		for(Connection connection : game.getConnections()) {
			Connection newConnection = new Connection(connection.getConnectionId().replace(gameId, newGameId), newGame, null, null, connection.getBackwardsLoop(), null);
			newGame.getConnections().add(newConnection);
			entityManager.persist(newConnection);
		}
		
		for(Transition transition : game.getTransitions()) {
			Map<String, SingleButtonPress> singleButtonPresses = new HashMap<String, SingleButtonPress>();
			Map<String, SequenceButtonPress> sequenceButtonPresses = new HashMap<String, SequenceButtonPress>();
			Map<String, KeyboardInput> keyboardInputs = new HashMap<String, KeyboardInput>();
			Transition newTransition = new Transition(transition.getTransitionId().replace(gameId, newGameId), newGame, null, new HashMap<String, String>(transition.getActiveTransitions()), singleButtonPresses, sequenceButtonPresses, keyboardInputs);
			for(Entry<String, SingleButtonPress> entry : transition.getSingleButtonPresses().entrySet()) {
				singleButtonPresses.put(entry.getKey(), new SingleButtonPress(entry.getValue().getButton1(), entry.getValue().getButton2(), entry.getValue().getButton3(), entry.getValue().getButton4()));
			}
			for(Entry<String, SequenceButtonPress> entry : transition.getSequenceButtonPresses().entrySet()) {
				sequenceButtonPresses.put(entry.getKey(), new SequenceButtonPress(newTransition, entry.getValue().getScope(), new ArrayList<String>(entry.getValue().getSequences())));
			}
			for(Entry<String, KeyboardInput> entry : transition.getKeyboardInputs().entrySet()) {
				keyboardInputs.put(entry.getKey(), new KeyboardInput(newTransition, entry.getValue().getScope(), new ArrayList<String>(entry.getValue().getKeyboardInputs())));
			}
			newGame.getTransitions().add(newTransition);
			entityManager.persist(newTransition);
		}
		
		entityManager.merge(newGame);
		
		for(State state : game.getStates()) {
			for(Connection inputConnection : state.getInputConnections()) {
				State newState = entityManager.getReference(State.class, state.getStateId().replace(gameId, newGameId));
				newState.getInputConnections().add(entityManager.getReference(Connection.class, inputConnection.getConnectionId().replace(gameId, newGameId)));
				entityManager.merge(newState);
			}
			for(Connection outputConnection : state.getOutputConnections()) {
				State newState = entityManager.getReference(State.class, state.getStateId().replace(gameId, newGameId));
				newState.getOutputConnections().add(entityManager.getReference(Connection.class, outputConnection.getConnectionId().replace(gameId, newGameId)));
				entityManager.merge(newState);
			}
		}
		
		for(Connection connection : game.getConnections()) {
			Connection newConnection = entityManager.getReference(Connection.class, connection.getConnectionId().replace(gameId, newGameId));
			newConnection.setConnectionFrom(entityManager.find(State.class, connection.getConnectionFrom().getStateId().replace(gameId, newGameId)));
			newConnection.setConnectionTo(entityManager.find(State.class, connection.getConnectionTo().getStateId().replace(gameId, newGameId)));
			if(connection.getTransition() != null) {
				newConnection.setTransition(entityManager.getReference(Transition.class, connection.getTransition().getTransitionId().replace(gameId, newGameId)));
			}
			entityManager.merge(newConnection);
		}
		
		for(Transition transition : game.getTransitions()) {
			Transition newTransition = entityManager.getReference(Transition.class, transition.getTransitionId().replace(gameId, newGameId));
			newTransition.setConnection(entityManager.getReference(Connection.class, transition.getConnection().getConnectionId().replace(gameId, newGameId)));
			entityManager.merge(newTransition);
		}
	}

}
