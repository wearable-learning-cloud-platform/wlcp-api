package org.wlcp.wlcpapi.service.impl;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.wlcp.wlcpapi.datamodel.master.ObjectStore;
import org.wlcp.wlcpapi.repository.ObjectStoreRepository;
import org.wlcp.wlcpapi.service.ObjectStoreService;

@Service
public class ObjectStoreServiceImpl implements ObjectStoreService {

	@Autowired
	private ObjectStoreRepository objectStoreRepository;

	public ObjectStore store(MultipartFile file) throws IOException {
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		ObjectStore objecStore = new ObjectStore(fileName, file.getContentType(), file.getBytes());

		return objectStoreRepository.save(objecStore);
	}

	public ObjectStore getFile(String id) {
		return objectStoreRepository.findById(id).get();
	}

}
