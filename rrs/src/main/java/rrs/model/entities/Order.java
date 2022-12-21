package rrs.model.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder.ObtainVia;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "ORDERS")
public class Order {
	
	// @formatter:off
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String address;
	@ObtainVia @Column(name = "regtime")
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss.SSS")
	private Date regTime = new Date();
	private String account_id;
	
	@JoinColumn(name = "id", referencedColumnName = "order_id")
	@JsonIgnore @OneToOne(cascade = CascadeType.ALL)
	private OrderStatus status;
	
	@JoinColumn(name = "order_id", insertable = false, updatable = false) @OneToMany
	private List<OrderDetail> order_details;

	public Order(String account_id) {
		this.account_id = account_id;
	}

	public Order(String address, Date regTime, String account_id) {
		this.address = address;
		this.regTime = regTime;
		this.account_id = account_id;
	}
	
	
	
	// @formatter:on

}

