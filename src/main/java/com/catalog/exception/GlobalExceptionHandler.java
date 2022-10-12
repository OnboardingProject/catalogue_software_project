package com.catalog.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Configuration
@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(HierrarchyNotFoundException.class)
	public ResponseEntity<String> handleException(HierrarchyNotFoundException hierarchyNotFoundException) {
		return new ResponseEntity<String>(hierarchyNotFoundException.getErrorMessage(),
				hierarchyNotFoundException.getHttpStatus());
	}

	@ExceptionHandler(ListEmptyException.class)
	public ResponseEntity<String> listEmptyException(ListEmptyException listEmptyException) {
		return new ResponseEntity<String>(listEmptyException.getMsg(), listEmptyException.getHttpStatus());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class) 
	public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(
			MethodArgumentNotValidException ex) {
		Map<String, String> response = new HashMap<String, String>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String message = error.getDefaultMessage();
			response.put(fieldName, message);
		});
		return new ResponseEntity<Map<String, String>>(response, HttpStatus.BAD_REQUEST);
	}
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> exception(Exception exception) {
		return new ResponseEntity<String>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}