package com.bigbox.b2csite.order.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.bigbox.b2csite.common.DataAccessException;
import com.bigbox.b2csite.order.dao.OrderDao;
import com.bigbox.b2csite.order.model.entity.OrderEntity;

public class OrderDaoJpaImpl implements OrderDao {

	@PersistenceContext(name="orderPersistenceUnit")
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	@Override
	public OrderEntity findById(long orderId) throws DataAccessException {
		return this.entityManager.find(OrderEntity.class, orderId);
	}

	@Override
	public OrderEntity insert(OrderEntity order) throws DataAccessException {
		this.entityManager.persist(order);
		return order;
	}

	@Override
	public OrderEntity update(OrderEntity order) throws DataAccessException {
		this.entityManager.persist(order);
		return order;
	}

	@Override
	public void remove(OrderEntity order) throws DataAccessException {
		this.entityManager.remove(order);
	}

	@Override
	public List<OrderEntity> findByCustomerId(long customerId)
			throws DataAccessException {
		
		String queryText = "from OrderEntity oe where oe.customerId = :p_customerId";
		TypedQuery<OrderEntity> query = this.entityManager.createQuery(queryText, OrderEntity.class);
		query.setParameter("p_customerId", customerId);
		
		return query.getResultList();
	}
	
	@Override
	public List<OrderEntity> findByOrderSource(String orderSourceCode)
			throws DataAccessException {
		
		String queryText = "from OrderEntity oe where oe.orderSourceEntity.code = :p_orderSourceCode";
		TypedQuery<OrderEntity> query = this.entityManager.createQuery(queryText, OrderEntity.class);
		query.setParameter("p_orderSourceCode", orderSourceCode);
		
		return query.getResultList();
	}
}
