package org.wlcp.wlcpapi.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wlcp.wlcpapi.datamodel.master.Game;
import org.wlcp.wlcpapi.datamodel.master.Username;
import org.wlcp.wlcpapi.dto.GameDto;
import org.wlcp.wlcpapi.repository.GameRepository;
import org.wlcp.wlcpapi.service.LoadGameService;

@Service
public class LoadGameServiceImpl implements LoadGameService {

	@Autowired
	private GameRepository gameRepository;

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

}
