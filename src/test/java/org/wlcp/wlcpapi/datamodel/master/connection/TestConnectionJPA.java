package org.wlcp.wlcpapi.datamodel.master.connection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.wlcp.wlcpapi.datamodel.master.state.State;
import org.wlcp.wlcpapi.datamodel.master.transition.Transition;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class TestConnectionJPA {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Test
	@Transactional
	public void testWhenCreateNullConnectionExceptionThrown() {
		Assertions.assertThrows(PersistenceException.class, () -> {
			Connection connection = new Connection();
			entityManager.persist(connection);
			entityManager.flush();
		});
	}
	
	@Test
	@Transactional
	public void testCascadingPersistSucceeded() {
		State state = createState();
		Transition transition = createTransition();
		Connection connection = createConnection();
		connection.setConnectionFrom(state);
		connection.setConnectionTo(state);
		connection.setTransition(transition);
		entityManager.persist(connection);
		entityManager.flush();
		connection = entityManager.find(Connection.class, connection.getConnectionId());
		Assertions.assertNotEquals(connection.getConnectionTo(), null);
		Assertions.assertNotEquals(connection.getConnectionFrom(), null);
		Assertions.assertNotEquals(connection.getTransition(), null);
	}
	
	@Test
	@Transactional
	public void testCascadingMergeSucceeded() {
		State state = createState();
		Transition transition = createTransition();
		Connection connection = createConnection();
		connection.setConnectionFrom(state);
		connection.setConnectionTo(state);
		connection.setTransition(transition);
		entityManager.merge(connection);
		entityManager.flush();
		connection = entityManager.find(Connection.class, connection.getConnectionId());
		Assertions.assertNotEquals(connection.getConnectionTo(), null);
		Assertions.assertNotEquals(connection.getConnectionFrom(), null);
		Assertions.assertNotEquals(connection.getTransition(), null);
	}
	
	@Test
	@Transactional
	public void testCascadingRemoveAndOrphanRemovalSucceeded() {
		testCascadingPersistSucceeded();
		Connection connection = entityManager.find(Connection.class, "connectionid");
		entityManager.remove(connection);
		entityManager.flush();
		Assertions.assertEquals(entityManager.createQuery("SELECT e FROM State e", State.class).getResultList().size(), 0);
		Assertions.assertEquals(entityManager.createQuery("SELECT e FROM Transition e", Transition.class).getResultList().size(), 0);
	}
	
	private State createState() {
		State state = new State();
		state.setStateId("stateid");
		return state;
	}
	
	private Connection createConnection() {
		Connection connection = new Connection();
		connection.setConnectionId("connectionid");
		return connection;
	}
	
	private Transition createTransition() {
		Transition transition = new Transition();
		transition.setTransitionId("transitionid");
		return transition;
	}

}
