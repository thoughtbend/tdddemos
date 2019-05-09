package com.wiredbrain.order.service.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.wiredbrain.order.dao.OrderDao;
import com.wiredbrain.order.model.domain.OrderSummary;
import com.wiredbrain.order.model.entity.OrderEntity;
import com.wiredbrain.order.model.transformer.OrderEntityToOrderSummaryTransformer;

public class OrderServiceImplTest_M4_Step1a {

	@Test
	public void test_getOrderSummary_success() throws Exception {
		
		OrderServiceImpl target = new OrderServiceImpl();
		
		// Setup
		OrderDao mockOrderDao = mock(OrderDao.class);
		OrderEntityToOrderSummaryTransformer mockTransformer = 
				mock(OrderEntityToOrderSummaryTransformer.class);
		
		target.setOrderDao(mockOrderDao);
		target.setTransformer(mockTransformer);
		
		OrderEntity orderEntityFixture = new OrderEntity();
		List<OrderEntity> orderEntityListFixture = new ArrayList<>();
		orderEntityListFixture.add(orderEntityFixture);
		
		when(mockOrderDao.findByCustomerId(1L)).thenReturn(orderEntityListFixture);
		
		OrderSummary orderSummaryFixture = new OrderSummary();
		
		when(mockTransformer.transform(orderEntityFixture)).thenReturn(orderSummaryFixture);
		
		// Execute
		List<OrderSummary> result = target.getOrderSummary(1L);
		
		// Verify
		assertNotNull("result should not be null", result);
		
		assertAll("",
			() -> assertEquals(1, result.size()),
			() -> assertSame(orderSummaryFixture, result.get(0))
		);
	}
}
