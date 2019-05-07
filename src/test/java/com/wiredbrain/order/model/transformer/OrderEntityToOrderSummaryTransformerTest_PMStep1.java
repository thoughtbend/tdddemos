package com.wiredbrain.order.model.transformer;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.api.mockito.expectation.WithOrWithoutExpectedArguments;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.reflect.Whitebox;

import com.wiredbrain.order.model.domain.OrderSummary;
import com.wiredbrain.order.model.entity.OrderEntity;
import com.wiredbrain.order.model.entity.OrderItemEntity;

@ExtendWith(MockitoExtension.class)
@PrepareForTest(value= {OrderEntityToOrderSummaryTransformer.class})
public class OrderEntityToOrderSummaryTransformerTest_PMStep1 {
	
	private OrderEntityToOrderSummaryTransformer target = null;
	
	@BeforeEach
	public void setupBeforeEachTest() {
		this.target = PowerMockito.spy(new OrderEntityToOrderSummaryTransformer());
	}

	@Test
	public void test_transform_success() throws Exception {
		
		// Setup
		OrderEntity orderFixture = new OrderEntity();
		orderFixture.setOrderNumber("12343");
		orderFixture.setOrderItemList(new ArrayList<>());
		
		OrderItemEntity firstOrderItemFixture = new OrderItemEntity();
		firstOrderItemFixture.setQuantity(2);
		firstOrderItemFixture.setSellingPrice(new BigDecimal("5.35"));
		orderFixture.getOrderItemList().add(firstOrderItemFixture);
		
		OrderItemEntity secondOrderItemFixture = new OrderItemEntity();
		secondOrderItemFixture.setQuantity(1);
		secondOrderItemFixture.setSellingPrice(new BigDecimal(".99"));
		orderFixture.getOrderItemList().add(secondOrderItemFixture);
		
		// Execute
		OrderSummary result = this.target.transform(orderFixture);
		
		// Verify
		assertNotNull(result, "result should not be null");
		
		assertAll("",
			() -> assertEquals("12343", result.getOrderNumber()),
			() -> assertEquals(3, result.getItemCount()),
			() -> assertEquals(new BigDecimal("11.69"), result.getTotalAmount())
		);
	}
	
	@Test
	public void test_transform_orderEntityInputIsNull() throws Exception {
		
		IllegalArgumentException result = assertThrows(IllegalArgumentException.class, () -> {
			this.target.transform(null);
		});
		
		assertEquals("orderEntity should not be null", result.getMessage());
	}
	
	@Test
	public void test_transform_noItemsInOrder() throws Exception {
		
		// Setup
		OrderEntity orderFixture = new OrderEntity();
		orderFixture.setOrderNumber("12343");
		orderFixture.setOrderItemList(new ArrayList<>());
		
		// Execute
		OrderSummary result = this.target.transform(orderFixture);
		
		// Verify
		assertNotNull(result, "result should not be null");
		
		assertAll("",
			() -> assertEquals("12343", result.getOrderNumber()),
			() -> assertEquals(0, result.getItemCount()),
			() -> assertEquals(new BigDecimal("0.00"), result.getTotalAmount())
		);
	}
	
	@Test
	public void test_transform_mockCalculateTotal() throws Exception {
		
		// Setup
		OrderEntity orderFixture = new OrderEntity();
		orderFixture.setOrderNumber("12343");
		orderFixture.setOrderItemList(new ArrayList<>());
		
		OrderItemEntity firstOrderItemFixture = new OrderItemEntity();
		firstOrderItemFixture.setQuantity(2);
		firstOrderItemFixture.setSellingPrice(new BigDecimal("5.35"));
		orderFixture.getOrderItemList().add(firstOrderItemFixture);
		
		OrderItemEntity secondOrderItemFixture = new OrderItemEntity();
		secondOrderItemFixture.setQuantity(1);
		secondOrderItemFixture.setSellingPrice(new BigDecimal(".99"));
		orderFixture.getOrderItemList().add(secondOrderItemFixture);
		
		
		Method privateMethod = Whitebox.getMethod(OrderEntityToOrderSummaryTransformer.class, "calculateTotal", java.util.List.class);

		WithOrWithoutExpectedArguments wow = PowerMockito.when(this.target, privateMethod);
		wow.withArguments(orderFixture.getOrderItemList()).thenReturn(new BigDecimal("0.01"));
		
		// Execute
		OrderSummary result = this.target.transform(orderFixture);
		
		// Verify
		assertNotNull(result, "result should not be null");
		
		assertAll("",
			() -> assertEquals("12343", result.getOrderNumber()),
			() -> assertEquals(3, result.getItemCount()),
			() -> assertEquals(new BigDecimal("11.69"), result.getTotalAmount())
		);
	}
}
