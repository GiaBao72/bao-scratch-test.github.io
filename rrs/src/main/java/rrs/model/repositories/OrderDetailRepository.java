package rrs.model.repositories;

import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import rrs.model.entities.OrderDetail;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, OrderDetail> {
	
	@Modifying @Transactional
	@Query(value = "UPDATE ORDER_DETAILS SET quantity=:#{#o.quantity} WHERE "
	+ "order_id=:#{#o.order_id} AND product_id=:#{#o.product.id}", nativeQuery = true)
	int updateQuantity(OrderDetail o);
	
	@Modifying @Transactional
	@Query("DELETE FROM ORDER_DETAILS WHERE order_id=:order_id")
	int deleteByOrderId(Integer order_id);
	
	
	@Modifying @Transactional 
	@Query(value = "INSERT INTO ORDER_DETAILS VALUES "
			+ "(:#{#e.order_id},:#{#e.product_id},:#{#e.oldPrice},:#{#e.quantity})",
			nativeQuery = true)
	int saveOrigin(OrderDetail e);
}
