package rrs.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rrs.model.entities.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {

}
