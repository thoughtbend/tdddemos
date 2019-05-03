package com.wiredbrain.order.model.transformer;

import java.math.BigDecimal;

import com.wiredbrain.order.model.domain.OrderSummary;
import com.wiredbrain.order.model.entity.OrderEntity;
import com.wiredbrain.order.model.entity.OrderItemEntity;



public class OrderEntityToOrderSummaryTransformer {

	public OrderSummary transform(OrderEntity orderEntity) {
		
		if (orderEntity == null) {
			throw new IllegalArgumentException("orderEntity should not be null");
		}
		
		OrderSummary orderSummaryResult = new OrderSummary();
		
		orderSummaryResult.setOrderNumber(orderEntity.getOrderNumber());
		
		int itemCount = 0;
		BigDecimal totalAmount = new BigDecimal("0.00");
		
		for (OrderItemEntity currentItem : orderEntity.getOrderItemList()) {
			
			itemCount += currentItem.getQuantity();
			
			BigDecimal quantityBD = new BigDecimal(currentItem.getQuantity());
			BigDecimal itemTotal = currentItem.getSellingPrice().multiply(quantityBD);
			totalAmount = totalAmount.add(itemTotal);
		}
		
		orderSummaryResult.setItemCount(itemCount);
		orderSummaryResult.setTotalAmount(totalAmount);
		
		return orderSummaryResult;
	}
}
