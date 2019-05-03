package com.wiredbrain.order.model.entity;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.TableGenerator;

@Entity
public class OrderEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@ManyToOne
	private OrderSourceEntity orderSourceEntity;

	@Column(nullable=false)
	private String orderNumber;

	@Column
	private String orderLabel;
	
	@Column
	private Long customerId;

	@Column
	private Date completionDate;
	
	@Column 
	private Date fulfillmentDate;
	
	@OneToMany
	private List<OrderItemEntity> orderItemList = new LinkedList<>();

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getOrderLabel() {
		return orderLabel;
	}

	public void setOrderLabel(String orderLabel) {
		this.orderLabel = orderLabel;
	}

	public Date getCompletionDate() {
		return completionDate;
	}

	public void setCompletionDate(Date completionDate) {
		this.completionDate = completionDate;
	}

	public List<OrderItemEntity> getOrderItemList() {
		return orderItemList;
	}

	public void setOrderItemList(List<OrderItemEntity> orderItemList) {
		this.orderItemList = orderItemList;
	}

	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public OrderSourceEntity getOrderSourceEntity() {
		return orderSourceEntity;
	}

	public void setOrderSourceEntity(OrderSourceEntity orderSource) {
		this.orderSourceEntity = orderSource;
	}

	public Date getFulfillmentDate() {
		return fulfillmentDate;
	}

	public void setFulfillmentDate(Date fulfillmentDate) {
		this.fulfillmentDate = fulfillmentDate;
	}
}
