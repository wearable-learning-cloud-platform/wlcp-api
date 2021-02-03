package org.wlcp.wlcpapi.datamodel.master.state;

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

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class TestStateJPA {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Test
	@Transactional
	public void testWhenCreateNullStateExceptionThrown() {
		Assertions.assertThrows(PersistenceException.class, () -> {
			State state = new State();
			entityManager.persist(state);
			entityManager.flush();
		});
	}
	
	@Test
	@Transactional
	public void testCascadingPersistSucceeded() {
		State state = createState();
		Connection connection = createConnection();
		state.getInputConnections().add(connection);
		state.getOutputConnections().add(connection);
		entityManager.persist(state);
		entityManager.flush();
		state = entityManager.find(State.class, state.getStateId());
		Assertions.assertEquals(state.getInputConnections().size(), 1);
		Assertions.assertEquals(state.getOutputConnections().size(), 1);
	}
	
	@Test
	@Transactional
	public void testCascadingMergeSucceeded() {
		State state = createState();
		Connection connection = createConnection();
		state.getInputConnections().add(connection);
		state.getOutputConnections().add(connection);
		entityManager.merge(state);
		entityManager.flush();
		state = entityManager.find(State.class, state.getStateId());
		Assertions.assertEquals(state.getInputConnections().size(), 1);
		Assertions.assertEquals(state.getOutputConnections().size(), 1);
	}
	
	@Test
	@Transactional
	public void testCascadingRemoveAndOrphanRemovalSucceeded() {
		testCascadingPersistSucceeded();
		State state = entityManager.find(State.class, "stateid");
		entityManager.remove(state);
		entityManager.flush();
		Assertions.assertEquals(entityManager.createQuery("SELECT e FROM Connection e", Connection.class).getResultList().size(), 0);
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

}
