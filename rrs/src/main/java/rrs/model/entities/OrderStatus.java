package rrs.model.entities;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder.ObtainVia;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "ORDER_STATUS")
public class OrderStatus implements Serializable {
	
	private static final long serialVersionUID = 5686806031673720756L;
	// @formatter:off

	@Id private Integer order_id;
	private String account_id;
	@ObtainVia private Integer status = 0;
	private String descript;
	
	@JoinColumn(name = "order_id", referencedColumnName = "id")
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	private Order order;
	
	// @formatter:on

}
