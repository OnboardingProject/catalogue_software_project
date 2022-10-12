package com.catalog.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;



@Getter
public class HierrarchyNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	private final String errorMessage;
	private final HttpStatus httpStatus;
	public HierrarchyNotFoundException(String errorMessage, HttpStatus httpStatus) {
		super();
		this.errorMessage = errorMessage;
		this.httpStatus = httpStatus;
	}


	
}
