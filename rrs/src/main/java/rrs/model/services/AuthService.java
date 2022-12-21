package rrs.model.services;

import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import rrs.model.entities.Account;
import rrs.model.utils.InterDAO;

@Service
public class AuthService implements UserDetailsService {
	
	@Autowired private BCryptPasswordEncoder encoder;
	@Autowired private InterDAO<Account, String> dao;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Account> optional = dao.getOptional(username);
		try {
			Account e = new Account();
			if (optional.isPresent()) e = optional.get();

			Set<String> rs = e.getRoles();
			return User.withUsername(e.getUsername())
			.password(encoder.encode(e.getPassword()))
			.roles(rs.toArray(new String[rs.size()])).build();
		} catch (UsernameNotFoundException e) {throw e;}
	}

}
