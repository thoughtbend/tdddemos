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

		int itemCount = orderEntity.getOrderItemList().stream()
									.mapToInt(OrderItemEntity::getQuantity)
									.sum();

		BigDecimal totalAmount = orderEntity.getOrderItemList().stream().map(orderItem -> {

			BigDecimal quantityBD = new BigDecimal(orderItem.getQuantity());
			BigDecimal itemTotal = orderItem.getSellingPrice().multiply(quantityBD);
			return itemTotal;
		}).reduce(new BigDecimal("0.00"), (left, right) -> {

			return left.add(right);
		});

		orderSummaryResult.setItemCount(itemCount);
		orderSummaryResult.setTotalAmount(totalAmount);

		return orderSummaryResult;
	}
}
