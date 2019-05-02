package com.bigbox.b2csite.order.model.message;

import java.io.Serializable;

public class ItemMessage implements Serializable {

	private static final long serialVersionUID = 1514851877383883309L;

	private String itemNumber;
	private int quantity;

	public String getItemNumber() {
		return itemNumber;
	}

	public void setItemNumber(String itemNumber) {
		this.itemNumber = itemNumber;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

}
