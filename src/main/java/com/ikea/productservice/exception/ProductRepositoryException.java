package com.ikea.productservice.exception;

import com.ikea.productservice.constants.ProductExceptionCode;

public class ProductRepositoryException extends ProductApiException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	ProductRepositoryException(ProductExceptionCode errorCode, String errorMessage) {
		super(errorCode, errorMessage);
	}

}
