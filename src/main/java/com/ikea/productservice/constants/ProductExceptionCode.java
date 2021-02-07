package com.ikea.productservice.constants;

/**
 * Exception code for custom exceptions
 * @author Dharmvir Tiwari
 *
 */
public enum ProductExceptionCode {

	UNAUTHORIZED("401"),BAD_REQUEST("400"),INTERNAL_SERVER_ERROR("500");
	private String errorCode;
	ProductExceptionCode(String errorCode){
		this.errorCode=errorCode;
	}
	
	public String value() {
		return this.errorCode;
	}
	
}
