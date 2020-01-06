package org.wlcp.wlcpapi.service;

import org.wlcp.wlcpapi.datamodel.master.Game;
import org.wlcp.wlcpapi.dto.CreateGameDto;

public interface CreateLoadSaveGameService {
	
	public Game createGame(CreateGameDto createGameDto);
	
	public Game saveGame(Game game);

}
