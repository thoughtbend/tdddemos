package com.bigbox.b2csite.order.service;

public class ServiceException extends Exception {

	private static final long serialVersionUID = 4548098263384359763L;

	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public ServiceException(String message) {
		super(message);
	}

}
