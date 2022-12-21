package rrs.model.services; 

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import rrs.model.entities.Account;
import rrs.model.entities.Comment;
import rrs.model.repositories.CommentRepository;
import rrs.model.utils.InterDAO;
@Service
public class CommentService extends AbstractService<Comment, Comment>{

	@Override
	protected Comment getId(Comment entity) {
		return entity;
	}

	public Optional<Comment> getOptional(String a, Integer p) throws IllegalArgumentException {
		if(a == null) throw new IllegalArgumentException("account_id could be not null!");
		if(a.isEmpty()) throw new IllegalArgumentException("account_id is empty!");
		if(p == null) throw new IllegalArgumentException("product_id could be not null!");
		
		return Optional.of(((CommentRepository) super.rep).getById(a,p));
	}

	public List<Comment> findById(String a) throws IllegalArgumentException {
		return ((CommentRepository) rep).getListById(a);
	}
	
	public List<Comment> findById(Integer p) throws IllegalArgumentException {
		return ((CommentRepository) rep).getListById(p);
	}
	
	@Override
	public <S extends Comment> S save(S entity) throws IllegalArgumentException {
		String id = super.getUser(InterDAO.D_USER);
		entity.setAccount(new Account(id));
		return super.save(entity);
	}

	public void delete(String a, Integer p, String t) {
		try {
			((CommentRepository) rep).deleteById(a, p, t);
		} catch (Exception e) {
		}
	}

	public void delete(String a, Integer p) {
		((CommentRepository) rep).deleteById(a, p);
	}
}
