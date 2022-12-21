package rrs.model.services;

import java.util.Date;

import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import rrs.model.utils.SendMail;

@Service
public class MailService implements SendMail {
	
	@Autowired private JavaMailSender mailSender;

	// Send mail no attachment and text/plain
	public void sendSimpleMail(String subject, String text, RecipientType type ,String...addresses) {
		SimpleMailMessage sm = new SimpleMailMessage();
		sm.setSubject(subject);
		sm.setText(text);
		if(type == RecipientType.CC) sm.setCc(addresses);
		else if(type == RecipientType.BCC) sm.setBcc(addresses);
		else sm.setTo(addresses);

		System.out.println("RRs > sending by sendSimpleMail...");
		sm.setSentDate(new Date());
		mailSender.send(sm);
	}
	
	// Send mail with attachment and text/html
	public void sendMimeMessage(String subject, String text,  MultipartFile[] files, RecipientType type, String...addresses) throws MessagingException {
		if(files == null && !text.matches(HTML_REGEX)) this.sendSimpleMail(subject, text, type, addresses);
		else {
			MimeMessageHelper mm = new MimeMessageHelper(mailSender.createMimeMessage(), true, "UTF-8");
			mm.setSubject(subject);
			mm.setText(text, true);
			if(files != null) for(MultipartFile file: files) mm.addAttachment(file.getOriginalFilename(), file);
			
			if(type == RecipientType.CC) mm.setCc(addresses);
			else if(type == RecipientType.BCC) mm.setBcc(addresses);
			else mm.setTo(addresses);
			
			System.out.println("RRs > sending by sendMimeMessage...");
			mm.setSentDate(new Date());
			mailSender.send(mm.getMimeMessage());
		}

	}
	
}
