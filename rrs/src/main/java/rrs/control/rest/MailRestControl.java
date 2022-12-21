package rrs.control.rest;

import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import rrs.model.utils.SendMail;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/mail")
public class MailRestControl {

	// @formatter:off
	@Autowired private SendMail mail;
	
	@RequestMapping("/send")
	public ResponseEntity<String> sendMail (
			@RequestParam(required = false) String subject,
			@RequestParam(required = false) String text,
			@RequestParam(required = false) MultipartFile[] files,
			@RequestParam(required = false) RecipientType type,
			@RequestParam(required = false) String addresses
		) {
		
		try {
			addresses = addresses.trim().replaceAll("\n", ",");
			mail.sendMimeMessage(subject, text, files, type, addresses.split(","));
		} catch (MessagingException e) {
			e.printStackTrace();
			System.err.println("Mail sending failed!");
			return ResponseEntity.ok("<h1 style='color:red; text-align:center;'>Mail sending failed⚠<h1>");
		}
		System.out.println("Mail sent successfully.");
		return ResponseEntity.ok("<h1 style='color:green; text-align:center;'>Mail sent successfully✅<h1>");
	}
	
	// @formatter:on
}
