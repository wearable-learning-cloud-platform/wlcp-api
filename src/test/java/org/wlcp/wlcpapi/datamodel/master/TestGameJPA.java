package org.wlcp.wlcpapi.datamodel.master;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.wlcp.wlcpapi.datamodel.master.connection.Connection;
import org.wlcp.wlcpapi.datamodel.master.state.State;
import org.wlcp.wlcpapi.datamodel.master.transition.Transition;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class TestGameJPA {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Test
	@Transactional
	public void testWhenCreateNullGameExceptionThrown() {
		Assertions.assertThrows(PersistenceException.class, () -> {
			Game game = new Game();
			entityManager.persist(game);
			entityManager.flush();
		});
	}

	@Test
	@Transactional
	public void testWhenMaxGameIdLengthExceededExceptionThrown() {
		Assertions.assertThrows(PersistenceException.class, () -> {
			Username username = new Username("user","password", "firstname", "lastname", "email");
			entityManager.persist(username);
			Game game = new Game(new String(new byte[41], Charset.forName("UTF-8")), 0, 0, username,
					false, false);
			entityManager.persist(game);
			entityManager.flush();
		});
	}
	
	@Test
	@Transactional
	public void testWhenUsernameIsNullExceptionThrown() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			Game game = createGame(null);
			entityManager.persist(game);
			entityManager.flush();
		});
	}
	
	@Test
	@Transactional
	public void testCascadingPersistSucceeded() {
		Game game = createGame(createUsername());
		List<State> states = new ArrayList<State>();
		states.add(createState());
		game.setStates(states);
		List<Connection> connections = new ArrayList<Connection>();
		connections.add(createConnection());
		game.setConnections(connections);
		Transition transition = createTransition(game.getConnections().get(0));
		List<Transition> transitions = new ArrayList<Transition>();
		transitions.add(transition);
		game.setTransitions(transitions);
		entityManager.persist(game);
		entityManager.flush();
		game = entityManager.find(Game.class, game.getGameId());
		Assertions.assertEquals(game.getStates().size(), 1);
		Assertions.assertEquals(game.getConnections().size(), 1);
		Assertions.assertEquals(game.getTransitions().size(), 1);
	}
	
	@Test
	@Transactional
	public void testCascadingMergeSucceeded() {
		Game game = createGame(createUsername());
		List<State> states = new ArrayList<State>();
		states.add(createState());
		game.setStates(states);
		List<Connection> connections = new ArrayList<Connection>();
		connections.add(createConnection());
		game.setConnections(connections);
		Transition transition = createTransition(game.getConnections().get(0));
		List<Transition> transitions = new ArrayList<Transition>();
		transitions.add(transition);
		game.setTransitions(transitions);
		entityManager.merge(game);
		entityManager.flush();
		game = entityManager.find(Game.class, game.getGameId());
		Assertions.assertEquals(game.getStates().size(), 1);
		Assertions.assertEquals(game.getConnections().size(), 1);
		Assertions.assertEquals(game.getTransitions().size(), 1);
	}
	
	@Test
	@Transactional
	public void testCascadingRemoveAndOrphanRemovalSucceeded() {
		testCascadingPersistSucceeded();
		Game game = entityManager.find(Game.class, "gameid");
		entityManager.remove(game);
		entityManager.flush();
		Assertions.assertEquals(entityManager.createQuery("SELECT e FROM State e", State.class).getResultList().size(), 0);
		Assertions.assertEquals(entityManager.createQuery("SELECT e FROM Connection e", Connection.class).getResultList().size(), 0);
		Assertions.assertEquals(entityManager.createQuery("SELECT e FROM Transition e", Transition.class).getResultList().size(), 0);
	}
	
	private Username createUsername() {
		Username username = new Username("user","password", "firstname", "lastname", "email");
		entityManager.persist(username);
		return username;
	}
	
	private Game createGame(Username username) {
		Game game = new Game("gameid", 0, 0, username, false, false);
		entityManager.persist(username);
		return game;
	}
	
	private State createState() {
		State state = new State();
		state.setStateId("stateid");
		return state;
	}
	
	private Connection createConnection() {
		Connection connection = new Connection();
		connection.setConnectionId("connectionId");
		return connection;
	}
	
	private Transition createTransition(Connection connection) {
		Transition transition = new Transition();
		transition.setTransitionId("transitionid");
		transition.setConnection(connection);
		return transition;
	}
}
