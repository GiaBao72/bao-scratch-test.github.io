package rrs.model.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;

import lombok.AllArgsConstructor;
import lombok.Builder.ObtainVia;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(Comment.class)
@Entity(name = "COMMENTS")
public class Comment implements Serializable {
	
	private static final long serialVersionUID = 1985192875367924598L;
	
	@Id private Integer product_id;
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss.SSS")
	@ObtainVia @Column(name = "regtime")
	@Id private Date regTime = new Date();
	private String descript;
	private Integer evaluate;
	
	@JoinColumn(name = "account_id", referencedColumnName = "username", insertable = false, updatable = false)
	@ManyToOne @JsonIncludeProperties({"username", "email", "name"})
	@Id private Account account;
	
	public Comment(String account_id, Integer product_id) {
		this.product_id = product_id;
	}
	
}
