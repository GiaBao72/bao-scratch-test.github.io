package rrs.model.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder.ObtainVia;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(OrderDetail.class)
@Entity(name = "ORDER_DETAILS")
public class OrderDetail implements Serializable {

	private static final long serialVersionUID = -3076955176935224191L;

	@Id private Integer order_id;
	@Id private Integer product_id;
	
	// @formatter:off
	@ObtainVia @Column(name = "oldprice")
	private Float oldPrice = 0F;
	@ObtainVia
	private Integer quantity = 1;

	@JoinColumn(name = "product_id", referencedColumnName = "id", insertable = false, updatable = false)
	@JsonIgnoreProperties({"category", "account_id", "account"})
	@ManyToOne private Product product;
	// @formatter:on

}
