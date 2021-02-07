package com.ikea.productservice.exception;

import com.ikea.productservice.constants.ProductExceptionCode;

public class ProductControllerException extends ProductApiException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ProductControllerException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ProductControllerException(ProductExceptionCode errorCode, String errorMessage) {
		super(errorCode, errorMessage);
		// TODO Auto-generated constructor stub
	}

	public ProductControllerException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public ProductControllerException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public ProductControllerException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public ProductControllerException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
	
	

}
