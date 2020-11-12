package org.wlcp.wlcpapi.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.wlcp.wlcpapi.datamodel.master.ObjectStore;
import org.wlcp.wlcpapi.service.ObjectStoreService;

@Controller
@RequestMapping("/objectStoreController")
public class ObjectStoreController {

	@Autowired
	private ObjectStoreService objectStoreService;

	@GetMapping("/files/{id}")
	public ResponseEntity<byte[]> getFile(@PathVariable String id) {
		ObjectStore objectStore = objectStoreService.getFile(id);

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + objectStore.getName() + "\"").contentType(MediaType.valueOf(objectStore.getType()))
				.body(objectStore.getData());
	}

	@PostMapping("/uploadFile")
	public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
		return new ResponseEntity<String>(objectStoreService.store(file).getId(), HttpStatus.OK);
	}

}
