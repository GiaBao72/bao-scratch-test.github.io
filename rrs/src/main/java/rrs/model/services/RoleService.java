package rrs.model.services; 

import org.springframework.stereotype.Service;

import rrs.model.entities.Role;
@Service
public class RoleService extends AbstractService<Role, String>{

	@Override
	protected String getId(Role entity) {
		return entity.getId();
	}

}
