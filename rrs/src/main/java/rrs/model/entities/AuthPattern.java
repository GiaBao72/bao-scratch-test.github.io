package rrs.model.entities;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rrs.util.Random;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthPattern {
	
	private String id;
	private String name;
	private String email;
	private String flatform;
	
	public AuthPattern(OAuth2AuthenticationToken token) {
		OAuth2User auth = token.getPrincipal();
		id = auth.getName();
		name = auth.getAttribute("name");
		email = auth.getAttribute("email");
		flatform = token.getAuthorizedClientRegistrationId().toUpperCase();
	}

	public Account toAccount() {
		return new Account(id,Random.NumUppLow("RRS", 9),email,name,flatform,"BUYER");
	}
}
