package rrs.model.repositories;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import rrs.model.entities.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
	
	@Modifying @Transactional // DELETE comments by product_id
	@Query(value = "DELETE FROM COMMENTS WHERE product_id=:id", nativeQuery = true)
	int deleteComments(Integer id);

	@Modifying @Transactional // DELETE product by id
	@Query(value = "DELETE FROM PRODUCTS WHERE id=:id", nativeQuery = true)
	void deleteId(Integer id);

	@Query(value = "SELECT * FROM PRODUCTS WHERE account_id=:username", nativeQuery = true)
	List<Product> findByAccountId(String username);
	
	@Query(value = "EXEC PROC_DIS_PRODUCT :top", nativeQuery = true)
	List<Product> disData(Integer top);

	@Query(value = "EXEC PROC_TOP_PRODUCT :top", nativeQuery = true)
	List<Product> topData(Integer top);
	
}
