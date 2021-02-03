package org.wlcp.wlcpapi.datamodel.master;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class TestObjectStoreJPA {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Test
	@Transactional
	public void testThatObjectStoreIdIsUUID() {
		ObjectStore objectStore = new ObjectStore();
		entityManager.persist(objectStore);
		objectStore = entityManager.createQuery("SELECT e FROM ObjectStore e", ObjectStore.class).getResultList().get(0);
		Assertions.assertEquals(objectStore.getId().matches("\\b[0-9a-f]{8}\\b-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-\\b[0-9a-f]{12}\\b"), true);
	}

}
