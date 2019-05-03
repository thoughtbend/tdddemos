package com.wiredbrain.order.dao;

import java.util.List;

import com.wiredbrain.common.DataAccessException;
import com.wiredbrain.order.model.entity.OrderEntity;

public interface OrderDao {

	// The four basic CRUD operations
	OrderEntity findById(long orderId) throws DataAccessException;
	OrderEntity insert(OrderEntity order) throws DataAccessException;
	OrderEntity update(OrderEntity order) throws DataAccessException;
	void remove(OrderEntity order) throws DataAccessException;
	
	// Other finder operations
	List<OrderEntity> findByCustomerId(long customerId) throws DataAccessException;
	List<OrderEntity> findByOrderSource(String orderSourceCode) throws DataAccessException;
}