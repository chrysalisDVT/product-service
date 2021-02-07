package com.ikea.productservice.exception;

import com.ikea.productservice.constants.ProductExceptionCode;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public  class ProductApiException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ProductExceptionCode errorCode;
	private String errorMessage;
	
	ProductApiException(ProductExceptionCode errorCode,String errorMessage){
		this.errorCode=errorCode;
		this.errorMessage=errorMessage;
	}

	public ProductApiException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ProductApiException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public ProductApiException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public ProductApiException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public ProductApiException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
	

}
