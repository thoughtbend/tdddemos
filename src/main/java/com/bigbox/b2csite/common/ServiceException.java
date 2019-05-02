package com.bigbox.b2csite.common;

public class ServiceException extends Exception {

	private static final long serialVersionUID = 1110517910654342737L;

	public ServiceException(String message) {
		super(message);
	}

	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}

}
