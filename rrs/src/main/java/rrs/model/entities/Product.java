package rrs.model.entities;

import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "PRODUCTS")
public class Product {
	 
	// @formatter:off
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Integer id;
	@Column(name = "cosprice") private float cosPrice;
	@Column(name = "proprice") private float proPrice;
	private String name;
	private String descript;
	
	@ManyToOne
	private Category category;

	@ManyToOne @JoinColumn(name="account_id")
	@JsonIncludeProperties({"username","name","email"})
	private Account account;

	@ElementCollection @Column(name = "image")
	@CollectionTable (
		name = "PRODUCT_IMAGES",
		joinColumns = @JoinColumn(name = "product_id")
	) private List<String> images;

	public Product(Integer id) {
		this.id = id;
	}
	// @formatter:on

	
}
