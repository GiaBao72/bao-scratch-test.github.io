package rrs.model.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import rrs.model.entities.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

	@Query("SELECT o FROM ORDERS o WHERE account_id=:id AND id NOT IN (SELECT order_id FROM ORDER_STATUS)")
	List<Order> findByBuyertId(String id);
	
	@Query(value = "EXEC PROC_ORDER_ORDERS :status", nativeQuery = true)
	List<Order> findByStatus(Integer status);
	
	@Query(value = "SELECT * FROM ORDERS WHERE account_id=:id", nativeQuery = true)
	List<Order> findByAccountId(String id);

	@Query(value = "SELECT * FROM ORDERS WHERE product_id=:id", nativeQuery = true)
	List<Order> findByProductId(Integer id);

	
	
}
