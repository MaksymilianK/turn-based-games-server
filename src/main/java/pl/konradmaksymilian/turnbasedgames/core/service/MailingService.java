package pl.konradmaksymilian.turnbasedgames.core.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailingService {
		
	private JavaMailSender mailSender;

	public MailingService(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}
	
	public SimpleMailMessageBuilder prepare() {
		return new SimpleMailMessageBuilder();
	}
			
	public class SimpleMailMessageBuilder {
		
		private final SimpleMailMessage message = new SimpleMailMessage();
		
		private SimpleMailMessageBuilder() {}

		public SimpleMailMessageBuilder from(String from) {
			message.setFrom(from);
			return this;
		}

		public SimpleMailMessageBuilder to(String to) {
			message.setTo(to);
			return this;
		}

		public SimpleMailMessageBuilder subject(String subject) {
			message.setSubject(subject);
			return this;
		}

		public SimpleMailMessageBuilder text(String text) {
			message.setText(text);
			return this;
		}
		
		public void send() {
			sendMessage(message);
		}
	}
	
	private void sendMessage(SimpleMailMessage message) {
		//todo
		return;
	}
}
