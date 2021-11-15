package org.wlcp.wlcpapi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.wlcp.wlcpapi.service.MailService;

@Service
@Profile("ecs")
public class MailServiceImpl implements MailService {
	
	private final String from = "Wearable Learning Cloud Platform Contact Form <wearablelearning@gmail.com>";
	private final String toEmailAddress = "wearablelearning@groups.umass.edu";

	@Autowired
	private JavaMailSender emailSender;

	@Override
	public boolean handlePostMail(String name, String email, String subject, String message) {
		return sendMail(from, toEmailAddress, subject + " FROM <" + email + ">", message)
				&& sendConfirmationMail(email);
	}

	private boolean sendConfirmationMail(String to) {
		return sendMail(from, to,
				"Wearable Learning Contact Form Confirmation", "Thank you for reaching out! We have received your message!");
	}

	private boolean sendMail(String from, String to, String subject, String message) {
		SimpleMailMessage m = new SimpleMailMessage();
		m.setFrom(from);
		m.setTo(to);
		m.setSubject(subject);
		m.setText(message);
		try {
			emailSender.send(m);
			return true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}

}
