package rrs.control.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import rrs.model.entities.Order;
import rrs.model.services.OrderService;

@RestController
@CrossOrigin("*")
@RequestMapping({"/rest/orders"})
public class RestOrder extends AbstractRESTful<Order, Integer> {
	
	@GetMapping("/account")
	public ResponseEntity<Object> getByAccount(@RequestParam(required = false) String id) {
		return ResponseEntity.ok(((OrderService) super.dao).byAccountId(id));
	}
	
	@GetMapping("/buyer")
	public ResponseEntity<Object> getByBuyer(@RequestParam(required = false) String id) {
		return ResponseEntity.ok(((OrderService) super.dao).byBuyerId(id));
	}
	
	@GetMapping("/product")
	public ResponseEntity<Object> getByAccount(@RequestParam(required = false) Integer id) {
		return ResponseEntity.ok(((OrderService) super.dao).byProductId(id));
	}
	
	@GetMapping("/status")
	public ResponseEntity<Object> getNoStatus(@RequestParam(required = false) Integer s) {
		return ResponseEntity.ok(((OrderService) super.dao).byStatus(s));
	}
	
}