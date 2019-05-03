package com.wiredbrain.order.integration;

import com.wiredbrain.order.model.message.OrderMessage;

public class WarehouseManagementService {

	public static void sendOrder(OrderMessage orderMessage) throws WMSUnavailableException {
		throw new WMSUnavailableException("WMS is currently down for unknown reason");
	}
}
