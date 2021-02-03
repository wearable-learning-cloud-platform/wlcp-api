package org.wlcp.wlcpapi.datamodel.master.transition;

import java.util.ArrayList;

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
public class TestTransitionJPA {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Test
	@Transactional
	public void testWhenCreateNullTransitionExceptionThrown() {
		Assertions.assertThrows(PersistenceException.class, () -> {
			Transition transition = new Transition();
			entityManager.persist(transition);
			entityManager.flush();
		});
	}
	
	@Test
	@Transactional
	public void testTransitionConnectionCascadingPersistSucceeded() {
		Connection connection = createConnection();
		Transition transition = createTransition();
		transition.setConnection(connection);
		SequenceButtonPress sequenceButtonPress = new SequenceButtonPress("sequencebuttonpressid", transition, "scope", new ArrayList<String>());
		transition.getSequenceButtonPresses().put("scope", sequenceButtonPress);
		KeyboardInput keyboardInput = new KeyboardInput("keyboardinputid", transition, "scope", new ArrayList<String>());
		transition.getKeyboardInputs().put("scope", keyboardInput);
		entityManager.persist(transition);
		entityManager.flush();
		transition = entityManager.find(Transition.class, transition.getTransitionId());
		Assertions.assertNotEquals(transition.getConnection(), null);
		Assertions.assertEquals(transition.getSequenceButtonPresses().size(), 1);
		Assertions.assertEquals(transition.getKeyboardInputs().size(), 1);
	}
	
	@Test
	@Transactional
	public void testTransitionConnectionCascadingMergeSucceeded() {
		Connection connection = createConnection();
		Transition transition = createTransition();
		transition.setConnection(connection);
		SequenceButtonPress sequenceButtonPress = new SequenceButtonPress("sequencebuttonpressid", transition, "scope", new ArrayList<String>());
		transition.getSequenceButtonPresses().put("scope", sequenceButtonPress);
		KeyboardInput keyboardInput = new KeyboardInput("keyboardinputid", transition, "scope", new ArrayList<String>());
		transition.getKeyboardInputs().put("scope", keyboardInput);
		entityManager.merge(transition);
		entityManager.flush();
		transition = entityManager.find(Transition.class, transition.getTransitionId());
		Assertions.assertNotEquals(transition.getConnection(), null);
		Assertions.assertEquals(transition.getSequenceButtonPresses().size(), 1);
		Assertions.assertEquals(transition.getKeyboardInputs().size(), 1);
	}
	
	@Test
	@Transactional
	public void testTransitionConnectionRemoveAndOrphanRemovalSucceeded() {
		testTransitionConnectionCascadingPersistSucceeded();
		Transition transition = entityManager.find(Transition.class, "transitionid");
		entityManager.remove(transition);
		entityManager.flush();
		Assertions.assertEquals(entityManager.createQuery("SELECT e FROM Connection e", Connection.class).getResultList().size(), 0);
		Assertions.assertEquals(entityManager.createQuery("SELECT e FROM SequenceButtonPress e", SequenceButtonPress.class).getResultList().size(), 0);
		Assertions.assertEquals(entityManager.createQuery("SELECT e FROM KeyboardInput e", KeyboardInput.class).getResultList().size(), 0);
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
