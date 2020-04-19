package org.wlcp.wlcpapi.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wlcp.wlcpapi.datamodel.master.Game;
import org.wlcp.wlcpapi.datamodel.master.Username;
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

	@Override
	public Game createGame(CreateGameDto createGameDto) {
		
		Optional<Username> username = usernameRepository.findById(createGameDto.username.getUsernameId());
		
		Game game = new Game(createGameDto.gameId, createGameDto.teamCount, createGameDto.playersPerTeam, username.get(), createGameDto.visibility, createGameDto.stateIdCount, createGameDto.transitionIdCount, createGameDto.connectionIdCount, createGameDto.dataLog);
		
		return gameRepository.save(game);
	}

	@Override
	public Game saveGame(Game game) {
		return gameRepository.save(game);
	}

}
