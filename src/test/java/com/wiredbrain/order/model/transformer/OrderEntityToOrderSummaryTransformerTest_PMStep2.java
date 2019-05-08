package com.wiredbrain.order.model.transformer;

import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.api.mockito.expectation.WithOrWithoutExpectedArguments;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import com.wiredbrain.order.model.domain.OrderSummary;
import com.wiredbrain.order.model.entity.OrderEntity;
import com.wiredbrain.order.model.entity.OrderItemEntity;

@PrepareForTest(value= {OrderEntityToOrderSummaryTransformer.class})
@RunWith(PowerMockRunner.class)
public class OrderEntityToOrderSummaryTransformerTest_PMStep2 {
	
	private OrderEntityToOrderSummaryTransformer target = null;
	
	@Before
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
		assertNotNull("result should not be null", result);
		
		Assert.assertEquals("12343", result.getOrderNumber());
		Assert.assertEquals(3, result.getItemCount());
		Assert.assertEquals(new BigDecimal("11.69"), result.getTotalAmount());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void test_transform_orderEntityInputIsNull() throws Exception {
		
		this.target.transform(null);
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
		assertNotNull("result should not be null", result);
		
		Assert.assertEquals("12343", result.getOrderNumber());
		Assert.assertEquals(0, result.getItemCount());
		Assert.assertEquals(new BigDecimal("0.00"), result.getTotalAmount());
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

		WithOrWithoutExpectedArguments<BigDecimal> wow = PowerMockito.when(this.target, privateMethod);
		wow.withArguments(orderFixture.getOrderItemList()).thenReturn(new BigDecimal("0.01"));
		
		// Execute
		OrderSummary result = this.target.transform(orderFixture);
		
		// Verify
		assertNotNull("result should not be null", result);
		
		// !! Important to verify the private too
		PowerMockito.verifyPrivate(this.target).invoke(privateMethod).withArguments(orderFixture.getOrderItemList());
		
		Assert.assertEquals("12343", result.getOrderNumber());
		Assert.assertEquals(3, result.getItemCount());
		Assert.assertEquals(new BigDecimal("0.01"), result.getTotalAmount());
		
	}
	
	@Test
	public void test_calculateTotal_success() throws Exception {
		
		// Here we want to avoid the spy as we have nothing to mock, and are testing a private
		this.target = new OrderEntityToOrderSummaryTransformer();
		
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
		Method privateMethod = Whitebox.getMethod(OrderEntityToOrderSummaryTransformer.class, "calculateTotal", java.util.List.class);
		BigDecimal result = (BigDecimal) privateMethod.invoke(this.target, orderFixture.getOrderItemList());
		
		// Verify
		Assert.assertNotNull("result should not be null", result);
		
		// We aren't mocking, so don't verify the mock
	}
}
