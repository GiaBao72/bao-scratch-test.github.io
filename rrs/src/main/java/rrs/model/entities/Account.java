package rrs.model.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

import lombok.AllArgsConstructor;
import lombok.Builder.ObtainVia;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "ACCOUNTS")
public class Account {
	
	@Id private String username;
	private String password;
	private String email;
	private String name;
	@ObtainVia
	private String flatform = "SYSTEM";
	
	@ElementCollection(fetch = FetchType.EAGER) @Column(name = "role_id") // phan quyen he thong
	@CollectionTable(name = "AUTHORITIES", joinColumns = { @JoinColumn(name = "account_id") })
	private Set<String> roles = new HashSet<>();
	
	public Account(String username) {
		this.username = username;
	}

	public Account(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public Account(String username, String password, String email, String name, String flatform, String...roles) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.name = name;
		this.flatform = flatform;
		for(String role : roles) try {this.roles.add(role);} catch (Exception e) {}
	}
	
	
}
