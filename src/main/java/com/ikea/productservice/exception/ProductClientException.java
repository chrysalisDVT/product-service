package com.ikea.productservice.exception;

import com.ikea.productservice.constants.ProductExceptionCode;

public class ProductClientException extends ProductApiException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ProductClientException(ProductExceptionCode errorCode, String errorMessage) {
		super(errorCode, errorMessage);
		// TODO Auto-generated constructor stub
	}

	public ProductClientException() {
		// TODO Auto-generated constructor stub
	}

	public ProductClientException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public ProductClientException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public ProductClientException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public ProductClientException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
