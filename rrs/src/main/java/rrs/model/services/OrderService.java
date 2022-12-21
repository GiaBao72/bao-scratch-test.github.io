package rrs.model.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rrs.model.entities.Order;
import rrs.model.entities.OrderDetail;
import rrs.model.repositories.OrderDetailRepository;
import rrs.model.repositories.OrderRepository;
import rrs.model.utils.InterDAO;

@Service
public class OrderService extends AbstractService<Order, Integer> {

	@Autowired
	private OrderDetailRepository service;

	@Override
	protected Integer getId(Order entity) {
		return entity.getId();
	}
	
	public List<Order> byStatus(Integer status) {
		return ((OrderRepository) super.rep).findByStatus(status);
	}

	public List<Order> byAccountId(String id) {
		id = getUser(id==null?InterDAO.D_USER:id);
		return ((OrderRepository) super.rep).findByAccountId(id);
	}

	public List<Order> byBuyerId(String id) {
		id = getUser(id==null?InterDAO.D_USER:id);
		return ((OrderRepository) super.rep).findByBuyertId(id);
	}

	public List<Order> byProductId(Integer id) {
		return ((OrderRepository) super.rep).findByProductId(id);
	}

	@Override
	public <S extends Order> S save(S e) throws IllegalArgumentException {
		e.setRegTime(new Date());
		e.setAccount_id(super.getUser(null));
		Integer id = rep.save(new Order(e.getAddress(), e.getRegTime(), e.getAccount_id())).getId();
		e.setId(id);
		e.getOrder_details().forEach(x -> x.setOrder_id(id));
		return bothSave(e);
	}

	@Override
	public <S extends Order> S update(S entity) throws IllegalArgumentException {
		System.out.println(entity);
		return this.bothSave(entity);
	}
	
	private <S extends Order> S bothSave(S e) throws IllegalArgumentException {
		// update children
		service.deleteByOrderId(e.getId());
		List<OrderDetail> ods = e.getOrder_details();

		// update
		e.setOrder_details(new ArrayList<>());
		e = super.update(e);
		
		ods.forEach(x -> service.saveOrigin(x));
		e.setOrder_details(ods);
		
		return e;
	}

}
