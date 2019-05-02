package com.bigbox.b2csite.order.service.impl;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bigbox.b2csite.common.DataAccessException;
import com.bigbox.b2csite.common.ServiceException;
import com.bigbox.b2csite.order.dao.OrderDao;
import com.bigbox.b2csite.order.integration.WMSUnavailableException;
import com.bigbox.b2csite.order.integration.WarehouseManagementService;
import com.bigbox.b2csite.order.model.domain.OrderCompletionAudit;
import com.bigbox.b2csite.order.model.domain.OrderSummary;
import com.bigbox.b2csite.order.model.entity.OrderEntity;
import com.bigbox.b2csite.order.model.message.ItemMessage;
import com.bigbox.b2csite.order.model.message.OrderMessage;
import com.bigbox.b2csite.order.model.transformer.OrderEntityToOrderSummaryTransformer;
import com.bigbox.b2csite.order.service.OrderService;

public class OrderServiceImpl implements OrderService {

	public final static int MAX_INSERT_ATTEMPT = 2;
	private final static Logger AUDIT_LOGGER = LoggerFactory.getLogger("AUDIT");
	
	private OrderDao orderDao = null;
	private OrderEntityToOrderSummaryTransformer transformer = null;
	
	public void setOrderDao(final OrderDao orderDao) {
		this.orderDao = orderDao;
	}
	
	public void setTransformer(final OrderEntityToOrderSummaryTransformer transformer) {
		this.transformer = transformer;
	}
	
	@Override
	public List<OrderSummary> getOrderSummary(long customerId)
			throws ServiceException {
		
		// Goal - interact with the dao to gather entities and 
		// create summary domain objects
		
		List<OrderSummary> resultList = new LinkedList<>();
		
		try {
			List<OrderEntity> orderEntityList = this.orderDao.findByCustomerId(customerId);
			
			resultList = orderEntityList
							.stream()
							.map(orderEntity -> {
								return this.transformer.transform(orderEntity);
							})
							.collect(Collectors.toList());
			
		} catch (DataAccessException e) {
			// You should log the error
			throw new ServiceException("Data access error occurred", e);
		}
		
		return resultList;
	}
	
	public String openNewOrder(long customerId) throws ServiceException {
		
		OrderEntity newOrderEntity = new OrderEntity();
		newOrderEntity.setCustomerId(customerId);
		newOrderEntity.setOrderNumber(UUID.randomUUID().toString());
		
		boolean insertSuccessful = false;
		int insertAttempt = 1;
		while (!insertSuccessful && insertAttempt <= MAX_INSERT_ATTEMPT) {
			
			try {
				OrderEntity receivedOrderEntity = orderDao.insert(newOrderEntity);
				if (receivedOrderEntity != null) {
					insertSuccessful = true;
				}
				else {

					++insertAttempt;
				}
			} catch (DataAccessException e) {
				// Log error
				++insertAttempt;
			}
		}
		
		if (!insertSuccessful) {
			throw new ServiceException("Data access error prevented creation of order");
		}
		
		return newOrderEntity.getOrderNumber();
	}
	
	public void completeOrder(long orderId) throws ServiceException {
		
		try {
			OrderEntity orderEntity = orderDao.findById(orderId);
			
			OrderMessage orderMessage = new OrderMessage();
			orderMessage.setOrderNumber(orderEntity.getOrderNumber());
			
			List<ItemMessage> itemMessageList = 
					orderEntity.getOrderItemList().stream()
						.map(itemEntity -> {
							ItemMessage itemMessage = new ItemMessage();
							itemMessage.setItemNumber(itemEntity.getSku());
							itemMessage.setQuantity(itemEntity.getQuantity());
							
							return itemMessage;
						})
						.collect(Collectors.toList());
			
			orderMessage.setItems(itemMessageList);
			
			WarehouseManagementService.sendOrder(orderMessage);
			
			Date completionDate = new Date();
			OrderCompletionAudit auditRecord = new OrderCompletionAudit();
			auditRecord.setOrderNumber(orderEntity.getOrderNumber());
			auditRecord.setCompletionDate(completionDate);
			
			AUDIT_LOGGER.info(String.format("Order completed - %1$s", auditRecord));
			
		} catch (DataAccessException e) {
			// Log error
			throw new ServiceException("Data access error while completing order", e);
		} catch (WMSUnavailableException e) {
			// Log error
			throw new ServiceException("WMS was unavailable when sending the order", e);
		}
	}

}
