package rrs.control.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import rrs.model.entities.Product;
import rrs.model.services.ProductService;

@RestController
@CrossOrigin("*")
@RequestMapping({"/rest/products"})
public class RestProduct extends AbstractRESTful<Product, Integer> {
	
	@GetMapping("/account")
	public ResponseEntity<Object> getByAccount(@RequestParam(required = false) String id) {
		return ResponseEntity.ok(((ProductService) super.dao).byAccountId(id));
	}
	
	@GetMapping("/dis")
	public ResponseEntity<Object> getHOT(@RequestParam(required = false) Integer top) {
		return ResponseEntity.ok(((ProductService) super.dao).disData(top));
	}
	
	@GetMapping("/hot")
	public ResponseEntity<Object> getTOP(@RequestParam(required = false) Integer top) {
		return ResponseEntity.ok(((ProductService) super.dao).topData(top));
	}
	
}
