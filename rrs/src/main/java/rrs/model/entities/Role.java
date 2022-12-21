package rrs.model.entities;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "ROLES")
public class Role {

	// @formatter:off
	
	@Id private String id;
	private String name;
	
	// @formatter:on

}
