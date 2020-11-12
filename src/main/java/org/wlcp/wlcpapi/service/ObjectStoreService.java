package org.wlcp.wlcpapi.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;
import org.wlcp.wlcpapi.datamodel.master.ObjectStore;

public interface ObjectStoreService {

	public ObjectStore store(MultipartFile file) throws IOException;

	public ObjectStore getFile(String id);

}
