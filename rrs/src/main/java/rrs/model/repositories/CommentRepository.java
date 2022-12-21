package rrs.model.repositories;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import rrs.model.entities.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Comment> {

	@Query(value = "SELECT * FROM COMMENTS WHERE account_id=:a", nativeQuery = true)
	public List<Comment> getListById(String a);

	@Query(value = "SELECT * FROM COMMENTS WHERE product_id=:p", nativeQuery = true)
	public List<Comment> getListById(Integer p);
	
	@Query(value = "SELECT * FROM COMMENTS WHERE account_id=:a AND product_id=:p", nativeQuery = true)
	public Comment getById(String a, Integer p);

	@Modifying @Transactional
	@Query(value = "DELETE FROM COMMENTS WHERE account_id=:a AND product_id=:p AND regTime=CAST(:t AS datetime)", nativeQuery = true)
	public void deleteById(String a, Integer p, String t);

	@Modifying @Transactional
	@Query(value = "DELETE FROM COMMENTS WHERE account_id=:a AND product_id=:p", nativeQuery = true)
	public void deleteById(String a, Integer p);
	
}