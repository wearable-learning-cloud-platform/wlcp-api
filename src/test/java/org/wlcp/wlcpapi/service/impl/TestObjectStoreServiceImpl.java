package org.wlcp.wlcpapi.service.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.wlcp.wlcpapi.datamodel.master.ObjectStore;
import org.wlcp.wlcpapi.repository.ObjectStoreRepository;


@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TestObjectStoreServiceImpl {
	
	@InjectMocks
	private ObjectStoreServiceImpl objectStoreServiceImpl;
	
	@Mock
	private ObjectStoreRepository objectStoreRepository;
	
	@Test
	public void testStoreObject() throws IOException {
		when(objectStoreRepository.save(any(ObjectStore.class))).thenReturn(new ObjectStore());
		ObjectStore objectStore = objectStoreServiceImpl.store(new MockMultipartFile("file", new byte[1024]));
		Assertions.assertNotNull(objectStore);
	}
	
	@Test
	public void testGetFile() {
		when(objectStoreRepository.findById(any(String.class))).thenReturn(Optional.of(new ObjectStore()));
		ObjectStore objectStore = objectStoreServiceImpl.getFile("");
		Assertions.assertNotNull(objectStore);
	}

}
