package com.wiredbrain.order.dao;

import java.util.List;
import java.util.Optional;

import com.wiredbrain.common.DataAccessException;
import com.wiredbrain.order.model.entity.OrderEntity;

public interface OrderDao {

	// The four basic CRUD operations
	OrderEntity findById(long orderId) throws DataAccessException;
	Optional<Long> insert(OrderEntity order) throws DataAccessException;
	void update(OrderEntity order) throws DataAccessException;
	void remove(OrderEntity order) throws DataAccessException;
	
	// Other finder operations
	List<OrderEntity> findByCustomerId(long customerId) throws DataAccessException;
	List<OrderEntity> findByOrderSource(String orderSourceCode) throws DataAccessException;
}