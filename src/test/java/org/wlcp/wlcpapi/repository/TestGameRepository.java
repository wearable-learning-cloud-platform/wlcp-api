package org.wlcp.wlcpapi.repository;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.wlcp.wlcpapi.datamodel.master.Game;
import org.wlcp.wlcpapi.datamodel.master.Username;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class TestGameRepository {

	@Autowired
	private GameRepository gameRepository;
	
	@Autowired
	private UsernameRepository usernameRepository;
	
	@Test
	public void testGameRepositoryFindByUsername() {
		Username username = createUsername();
		Game game = createBasicTestGame();
		game.setUsername(username);
		gameRepository.save(game);
		List<Game> games = gameRepository.findByUsername(username);
		Assertions.assertEquals(games.size(), 1);
	}
	
	private Username createUsername() {
		Username username = new Username("userid", "", "", "", "");
		usernameRepository.save(username);
		return username;
	}
	
	private Game createBasicTestGame() {
		return new Game("gameId", 3, 3, new Username("","","","",""), true, 0, 0, 0, false);
	}
}
