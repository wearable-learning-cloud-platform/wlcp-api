package org.wlcp.wlcpapi.datamodel.master.state;

import java.nio.charset.Charset;

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
public class TestOutputStateJPA {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Test
	@Transactional
	public void testWhenCreateNullOutputStateExceptionThrown() {
		Assertions.assertThrows(PersistenceException.class, () -> {
			OutputState outputState = new OutputState();
			entityManager.persist(outputState);
			entityManager.flush();
		});
	}
	
	@Test
	@Transactional
	public void testStateTypeIsStartStateOnCreation() {
		OutputState outputState = createOutputState();
		entityManager.persist(outputState);
		entityManager.flush();
		outputState = entityManager.find(OutputState.class, outputState.getStateId());
		Assertions.assertEquals(outputState.getStateType(), StateType.OUTPUT_STATE);
	}
	
	@Test
	@Transactional
	public void testWhenMaxDisplayTextLengthExceededExceptionThrown() {
		Assertions.assertThrows(PersistenceException.class, () -> {
			OutputState outputState = createOutputState();
			outputState.getDisplayText().put("scope", new String(new byte[2050], Charset.forName("UTF-8")));
			entityManager.persist(outputState);
			entityManager.flush();
		});
	}
	
	private OutputState createOutputState() {
		OutputState outputState = new OutputState();
		outputState.setStateId("outputstateid");
		return outputState;
	}
}
