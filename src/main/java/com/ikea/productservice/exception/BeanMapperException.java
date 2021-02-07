package com.ikea.productservice.exception;

import com.ikea.productservice.constants.ProductExceptionCode;

public class BeanMapperException extends ProductApiException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	BeanMapperException(ProductExceptionCode errorCode, String errorMessage) {
		super(errorCode, errorMessage);
	}

	public BeanMapperException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BeanMapperException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public BeanMapperException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public BeanMapperException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public BeanMapperException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
	
	

}
