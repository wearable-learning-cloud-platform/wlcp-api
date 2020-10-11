package org.wlcp.wlcpapi.response;

public class SubErrorResponse {
	
	private String message;
	
	public SubErrorResponse(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
