package com.wiredbrain.order.integration;

import com.wiredbrain.order.model.message.OrderMessage;

public class KitchenManagementService {

	public static void sendOrder(OrderMessage orderMessage) throws KMSUnavailableException {
		throw new KMSUnavailableException("WMS is currently down for unknown reason");
	}
}
