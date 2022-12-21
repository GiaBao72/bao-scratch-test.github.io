package rrs.control.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import rrs.model.entities.Category;
import rrs.model.services.CategoryService;

@RestController
@CrossOrigin("*")
@RequestMapping({"/rest/categories"})
public class RestCategory extends AbstractRESTful<Category, String>{
	
	@PostMapping({"/random"}) // Post method to create entity
	public ResponseEntity<Category> save(@RequestBody Category entity) throws IllegalArgumentException {
		int i = req.getRequestURI().lastIndexOf("/random");
		return i>-1 
			? ResponseEntity.ok(((CategoryService) super.dao).save(entity, true)) 
			: super.save(entity);
	}
}
