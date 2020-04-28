package org.wlcp.wlcpapi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wlcp.wlcpapi.datamodel.master.Game;
import org.wlcp.wlcpapi.repository.GameRepository;
import org.wlcp.wlcpapi.service.SaveGameService;

@Service
@Transactional
public class SaveGameServiceImpl implements SaveGameService {
	
	@Autowired
	private GameRepository gameRepository;

	@Override
	public Game saveGame(Game game) {
		return gameRepository.save(game);
	}

}
