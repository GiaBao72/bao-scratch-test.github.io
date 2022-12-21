package rrs.model.services;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import rrs.model.utils.InterDAO;

public abstract class AbstractService<E, K> implements InterDAO<E, K> {

	@Autowired protected JpaRepository<E, K> rep;
	@Autowired protected HttpServletRequest req;
	protected abstract K getId(E entity);
	
	@Override
	public List<E> getList() {
		return rep.findAll();
	}

	@Override
	public List<E> getList(Sort sort) {
		return rep.findAll(sort);
	}

	@Override
	public Page<E> getPage(Pageable pageable) {
		return rep.findAll(pageable);
	}

	@Override
	public Optional<E> getOptional(K id) throws IllegalArgumentException {
		return rep.findById(id);
	}

	@Override
	public <S extends E> S save(S entity) throws IllegalArgumentException {
		K id = this.getId(entity);
		Optional<E> optional = rep.findById(id);
		if(optional.isEmpty()) {
			return rep.save(entity);
		} else throw new IllegalArgumentException(id+" đã tồn tại, không thể thêm mới.");
	}

	@Override
	public <S extends E> S update(S entity) throws IllegalArgumentException {
		K id = this.getId(entity);
		Optional<E> optional = rep.findById(id);
		if(optional.isPresent()) {
			return rep.save(entity);
		} else throw new IllegalArgumentException(id+" không tồn tại, không thể cập nhật.");
	}

	@Override
	public void remove(K id) throws IllegalArgumentException {
		rep.deleteById(id);
	}

	protected String getUser(String defaultUser) {
		Principal p = req.getUserPrincipal();
		return p == null ? defaultUser : p.getName();
	}
}
