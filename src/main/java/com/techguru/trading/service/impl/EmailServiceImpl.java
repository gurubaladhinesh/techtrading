package com.techguru.trading.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.techguru.trading.service.EmailService;

@Service
public class EmailServiceImpl implements EmailService {

	private JavaMailSender emailSender;

	@Autowired
	public EmailServiceImpl(JavaMailSender emailSender) {
		this.emailSender = emailSender;
	}

	@Override
	public void sendSimpleMessage(String[] to, String subject, String text) {
		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setTo(to);
			message.setSubject(subject);
			message.setText(text);

			emailSender.send(message);
		} catch (MailException e) {
			e.printStackTrace();
		}
	}
}
