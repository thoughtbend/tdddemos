package com.bigbox.b2csite.order.model.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class OrderItemEntity {

	@Id
	private long id;

	@Column
	private String sku;

	@Column
	private int quantity;

	@Column
	private BigDecimal sellingPrice;

	@Column
	private Date addedToOrderDateTime;

	@ManyToOne
	private OrderEntity owningOrder;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getSellingPrice() {
		return sellingPrice;
	}

	public void setSellingPrice(BigDecimal sellingPrice) {
		this.sellingPrice = sellingPrice;
	}

	public Date getAddedToOrderDateTime() {
		return addedToOrderDateTime;
	}

	public void setAddedToOrderDateTime(Date addedToOrderDateTime) {
		this.addedToOrderDateTime = addedToOrderDateTime;
	}

	public OrderEntity getOwningOrder() {
		return owningOrder;
	}

	public void setOwningOrder(OrderEntity owningOrder) {
		this.owningOrder = owningOrder;
	}
}
