package rrs.control.rest;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import rrs.model.entities.Comment;
import rrs.model.services.CommentService;

@RestController
@CrossOrigin("*")
@RequestMapping("/rest/comments")
public class RestComment extends AbstractRESTful<Comment, Comment>{
	
	@GetMapping("/id") // reading method to get data
	public ResponseEntity<Object> getById(
			@RequestParam(required = false) String a,
			@RequestParam(required = false) Integer p
	) {
		CommentService dao2 = ((CommentService)super.dao);
		if(p == null) return ResponseEntity.ok(dao2.findById(a));
		if(a == null) return ResponseEntity.ok(dao2.findById(p));
		
		Optional<Comment> optional = dao2.getOptional(a, p);
		return optional.isPresent() 
				? ResponseEntity.ok(optional.get()) 
				: ResponseEntity.noContent().build();
	}
	
	@PostMapping("/post")
	public ResponseEntity<Comment> post(@RequestBody Comment entity) {
		return ResponseEntity.ok(dao.save(entity));
	}



	@DeleteMapping("/id") // Delete method to remove entity
	public ResponseEntity<Void> delete(
			@RequestParam(required = false) String a,
			@RequestParam(required = false) Integer p,
			@RequestParam(required = false) String t
	) throws IllegalArgumentException {
		if(a != null && p != null && t != null) {
			((CommentService) dao).delete(a,p,t);
			return ResponseEntity.ok().build();
		} else if(a != null && p != null) {
			((CommentService) dao).delete(a,p);
			return ResponseEntity.ok().build();
		} else return ResponseEntity.noContent().build();
	}
	
}
