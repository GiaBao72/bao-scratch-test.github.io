package rrs.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rrs.model.entities.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {

}
