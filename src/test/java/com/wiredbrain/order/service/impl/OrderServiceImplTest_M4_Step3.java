package com.wiredbrain.order.service.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import com.wiredbrain.common.DataAccessException;
import com.wiredbrain.common.ServiceException;
import com.wiredbrain.order.dao.OrderDao;
import com.wiredbrain.order.model.domain.OrderSummary;
import com.wiredbrain.order.model.entity.OrderEntity;
import com.wiredbrain.order.model.transformer.OrderEntityToOrderSummaryTransformer;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest_M4_Step3 {
	
	@InjectMocks
	private OrderServiceImpl target = new OrderServiceImpl();
	
	@Mock
	private OrderDao mockOrderDao;
	
	@Mock
	private OrderEntityToOrderSummaryTransformer mockTransformer;

	@Test
	public void test_getOrderSummary_success() throws Exception {
		
		// Setup
		OrderEntity orderEntityFixture = new OrderEntity();
		List<OrderEntity> orderEntityListFixture = new ArrayList<>();
		orderEntityListFixture.add(orderEntityFixture);
		
		when(mockOrderDao.findByCustomerId(1L)).thenReturn(orderEntityListFixture);
		
		OrderSummary orderSummaryFixture = new OrderSummary();
		
		when(mockTransformer.transform(orderEntityFixture)).thenReturn(orderSummaryFixture);
		
		// Execute
		List<OrderSummary> result = this.target.getOrderSummary(1L);
		
		// Verify
		assertNotNull("result should not be null", result);
		
		assertAll("",
			() -> verify(mockOrderDao).findByCustomerId(1L),
			() -> verify(mockTransformer).transform(orderEntityFixture),
			() -> assertEquals(1, result.size()),
			() -> assertSame(orderSummaryFixture, result.get(0))
		);
	}
	
	@Test
	public void test_getOrderSummary_daoThrowsException() throws Exception {
		// Setup
		when(mockOrderDao.findByCustomerId(1L)).thenThrow(new DataAccessException("A testing exception"));
		
		// Verify
		ServiceException result = assertThrows(ServiceException.class, () -> {
			
			// Execute
			this.target.getOrderSummary(1L);
		});
		
		assertEquals("Data access error occurred", result.getMessage());
	}
}
