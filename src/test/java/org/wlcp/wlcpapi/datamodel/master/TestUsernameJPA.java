package org.wlcp.wlcpapi.datamodel.master;

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
public class TestUsernameJPA {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Test
	@Transactional
	public void testWhenCreateNullUsernameExceptionThrown() {
		Assertions.assertThrows(PersistenceException.class, () -> {
			Username username = new Username();
			entityManager.persist(username);
			entityManager.flush();
		});
	}
	
	@Test
	@Transactional
	public void testWhenMaxUsernameIdLengthExceededExceptionThrown() {
		Assertions.assertThrows(PersistenceException.class, () -> {
			Username username = new Username(new String(new byte[41], Charset.forName("UTF-8")),"password", "firstname", "lastname", "email");
			entityManager.persist(username);
			entityManager.flush();
		});
	}
	
	@Test
	@Transactional
	public void testWhenMaxPasswordLengthExceededExceptionThrown() {
		Assertions.assertThrows(PersistenceException.class, () -> {
			Username username = new Username("usernameid", new String(new byte[61], Charset.forName("UTF-8")), "firstname", "lastname", "email");
			entityManager.persist(username);
			entityManager.flush();
		});
	}
	
	@Test
	@Transactional
	public void testWhenMaxFirstNameLengthExceededExceptionThrown() {
		Assertions.assertThrows(PersistenceException.class, () -> {
			Username username = new Username("usernameid","password", new String(new byte[41], Charset.forName("UTF-8")), "lastname", "email");
			entityManager.persist(username);
			entityManager.flush();
		});
	}
	
	@Test
	@Transactional
	public void testWhenMaxLastNameLengthExceededExceptionThrown() {
		Assertions.assertThrows(PersistenceException.class, () -> {
			Username username = new Username("usernameid","password", "firstname", new String(new byte[41], Charset.forName("UTF-8")), "email");
			entityManager.persist(username);
			entityManager.flush();
		});
	}
	
	@Test
	@Transactional
	public void testWhenMaxEmailLengthExceededExceptionThrown() {
		Assertions.assertThrows(PersistenceException.class, () -> {
			Username username = new Username("usernameid","password", "firstname", "lastname", new String(new byte[41], Charset.forName("UTF-8")));
			entityManager.persist(username);
			entityManager.flush();
		});
	}

}
