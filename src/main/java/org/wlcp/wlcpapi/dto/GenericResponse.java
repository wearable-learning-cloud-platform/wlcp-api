package org.wlcp.wlcpapi.dto;

public class GenericResponse {
	
	public String message;
	public Object object;
	
	public GenericResponse(String message, Object object) {
		this.message = message;
		this.object = object;
	}

}
