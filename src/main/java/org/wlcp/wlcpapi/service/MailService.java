package org.wlcp.wlcpapi.service;

public interface MailService {
	
	boolean handlePostMail(String name, String email, String subject, String message);

}
