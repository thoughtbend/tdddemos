package com.wiredbrain.order.service;

import java.util.List;

import com.wiredbrain.common.ServiceException;
import com.wiredbrain.order.model.domain.OrderSummary;

public interface OrderService {

	List<OrderSummary> getOrderSummary(long customerId) throws ServiceException;
}
