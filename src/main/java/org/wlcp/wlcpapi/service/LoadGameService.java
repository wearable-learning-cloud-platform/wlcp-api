package org.wlcp.wlcpapi.service;

import java.util.List;

import org.wlcp.wlcpapi.datamodel.master.Game;
import org.wlcp.wlcpapi.dto.GameDto;

public interface LoadGameService {

	public List<GameDto> getPrivateGames(String usernameId);
	
	public List<GameDto> getPublicGames();
	
	public Game loadGame(String gameId);
	
}
