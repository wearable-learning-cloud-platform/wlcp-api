package org.wlcp.wlcpapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wlcp.wlcpapi.dto.GenericResponse;
import org.wlcp.wlcpapi.dto.CopyRenameDeleteGameDto;
import org.wlcp.wlcpapi.service.impl.CopyRenameDeleteGameServiceImpl;

@Controller
@RequestMapping("/gameController")
public class CopyRenameDeleteGameController {
	
	@Autowired
	private CopyRenameDeleteGameServiceImpl copyRenameDeleteGameService;
	
	@PostMapping(value="/copyGame")
	@ResponseBody
	public ResponseEntity<GenericResponse> copyGame(@RequestBody CopyRenameDeleteGameDto copyRenameDeleteGameDto) {
		String message = copyRenameDeleteGameService.copyGame(copyRenameDeleteGameDto);
		if(message.equals("")) {
			return new ResponseEntity<GenericResponse>(new GenericResponse(message, new String()), HttpStatus.OK);
		} else {
			return new ResponseEntity<GenericResponse>(new GenericResponse(message, new String()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	@PostMapping(value="/renameGame")
	@ResponseBody
	public ResponseEntity<GenericResponse> renameGame(@RequestBody CopyRenameDeleteGameDto copyRenameDeleteGameDto) {
		copyRenameDeleteGameService.renameGame(copyRenameDeleteGameDto);
		return new ResponseEntity<GenericResponse>(new GenericResponse("Message", new String()), HttpStatus.OK);
	}
	
	@PostMapping(value="/deleteGame")
	@ResponseBody
	public ResponseEntity<GenericResponse> deleteGame(@RequestBody CopyRenameDeleteGameDto copyRenameDeleteGameDto) {
		String message = copyRenameDeleteGameService.deleteGame(copyRenameDeleteGameDto);
		if(message.equals("")) {
			return new ResponseEntity<GenericResponse>(new GenericResponse(message, new String()), HttpStatus.OK);
		} else {
			return new ResponseEntity<GenericResponse>(new GenericResponse(message, new String()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
