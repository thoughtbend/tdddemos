package com.bigbox.b2csite.order.model.domain;

import java.io.Serializable;
import java.util.Date;

public class OrderCompletionAudit implements Serializable {

	private static final long serialVersionUID = 530955681732188492L;

	private String orderNumber;
	private Date completionDate;

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public Date getCompletionDate() {
		return completionDate;
	}

	public void setCompletionDate(Date completionDate) {
		this.completionDate = completionDate;
	}
}
