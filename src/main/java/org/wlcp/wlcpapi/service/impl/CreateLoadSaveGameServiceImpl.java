package org.wlcp.wlcpapi.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wlcp.wlcpapi.datamodel.master.Game;
import org.wlcp.wlcpapi.datamodel.master.Username;
import org.wlcp.wlcpapi.datamodel.master.connection.Connection;
import org.wlcp.wlcpapi.datamodel.master.state.State;
import org.wlcp.wlcpapi.datamodel.master.transition.Transition;
import org.wlcp.wlcpapi.dto.CreateGameDto;
import org.wlcp.wlcpapi.repository.GameRepository;
import org.wlcp.wlcpapi.repository.UsernameRepository;
import org.wlcp.wlcpapi.service.CreateLoadSaveGameService;

@Service
@Transactional
public class CreateLoadSaveGameServiceImpl implements CreateLoadSaveGameService {
	
	@Autowired
	private UsernameRepository usernameRepository;
	
	@Autowired
	private GameRepository gameRepository;
	
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Game createGame(CreateGameDto createGameDto) {
		
		Optional<Username> username = usernameRepository.findById(createGameDto.username.getUsernameId());
		
		Game game = new Game(createGameDto.gameId, createGameDto.teamCount, createGameDto.playersPerTeam, username.get(), createGameDto.visibility, createGameDto.stateIdCount, createGameDto.transitionIdCount, createGameDto.connectionIdCount, createGameDto.dataLog);
		
		return gameRepository.save(game);
	}

	@Override
	public Game saveGame(Game game) {
		for(State state : game.getStates()) {
			state.setGame(game);
			entityManager.merge(state);
		}
		for(Connection connection : game.getConnections()) {
			connection.setGame(game);
			entityManager.merge(connection);
		}
		for(Transition transition : game.getTransitions()) {
			transition.setGame(game);
			entityManager.merge(transition);
		}
		game = entityManager.merge(game);
		return game;
	}

}
