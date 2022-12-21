package rrs.model.services; 

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rrs.model.entities.Order;
import rrs.model.entities.OrderDetail;
import rrs.model.entities.OrderStatus;
import rrs.model.repositories.OrderDetailRepository;
import rrs.model.repositories.OrderStatusRepository;
import rrs.model.utils.InterDAO;

@Service
public class StatusService extends AbstractService<OrderStatus, Integer>{

	@Autowired private OrderDetailRepository service;
	
	@Override
	protected Integer getId(OrderStatus entity) {
		return entity.getOrder_id();
	}

	public List<OrderStatus> byAccountId(String id) {
		id = getUser(id==null?InterDAO.D_USER:id);
		return ((OrderStatusRepository) super.rep).findByAccountId(id);
	}
	
	public List<OrderStatus> byShipperId(String id) {
		id = getUser(id==null?InterDAO.D_USER:id);
		return ((OrderStatusRepository) super.rep).findByShipperId(id);
	}

	public List<OrderStatus> byProductId(Integer id) {
		return ((OrderStatusRepository) super.rep).findByProductId(id);
	}
	
	@Override
	public <S extends OrderStatus> S update(S entity) throws IllegalArgumentException {
		Order order = entity.getOrder();
		if(order!=null) for(OrderDetail o : order.getOrder_details())
			service.updateQuantity(o);
		return super.update(entity);
	}

	public OrderStatus updateStatus(OrderStatus o, Integer status) {
		((OrderStatusRepository) super.rep).updateStatus(o, status);
		Optional<OrderStatus> optional = rep.findById(o.getOrder_id());
		if(optional.isPresent()) return optional.get();
		throw new IllegalArgumentException(o.getOrder_id()+" không tồn tại!");
	}
	
}
