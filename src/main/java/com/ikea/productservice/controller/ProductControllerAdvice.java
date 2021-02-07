package com.ikea.productservice.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ikea.productservice.constants.ProductExceptionCode;
import com.ikea.productservice.exception.ProductApiException;
import com.ikea.productservice.vo.ProductErrorResponse;

/**
 * 
 * Exception handler for all the exceptions being thrown during the service lifecycle
 * 
 * @author Dharmvir Tiwari
 *
 */
@RestControllerAdvice
public class ProductControllerAdvice {
	private static final Logger LOG=LoggerFactory.getLogger(ProductControllerAdvice.class);

	@ExceptionHandler(value = ProductApiException.class)
	public ResponseEntity<ProductErrorResponse> exceptionHandler(ProductApiException ex) {
		LOG.error(ex.getErrorMessage(),ex);
		ProductErrorResponse per = new ProductErrorResponse(fetchHttpStatus(ex.getErrorCode()),
				ex.getErrorCode().value(), List.of(ex.getErrorMessage()));
		return new ResponseEntity<>(per, fetchHttpStatus(ex.getErrorCode()));
	}

	private HttpStatus fetchHttpStatus(ProductExceptionCode errorCode) {
		switch (errorCode.value()) {
		case "400":
			return HttpStatus.BAD_REQUEST;
		case "401":
			return HttpStatus.UNAUTHORIZED;
		default:
			return HttpStatus.INTERNAL_SERVER_ERROR;
		}
	}

}
