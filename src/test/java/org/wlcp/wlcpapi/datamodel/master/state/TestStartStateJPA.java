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

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class TestStartStateJPA {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Test
	@Transactional
	public void testWhenCreateNullStartStateExceptionThrown() {
		Assertions.assertThrows(PersistenceException.class, () -> {
			StartState startState = new StartState();
			entityManager.persist(startState);
			entityManager.flush();
		});
	}
	
	@Test
	public void testStateTypeIsStartStateOnCreation() {
		StartState startState = createStartState();
		entityManager.persist(startState);
		entityManager.flush();
		startState = entityManager.find(StartState.class, startState.getStateId());
		Assertions.assertEquals(startState.getStateType(), StateType.START_STATE);
	}
	
	private StartState createStartState() {
		StartState startState = new StartState();
		startState.setStateId("startstateid");
		return startState;
	}

}
