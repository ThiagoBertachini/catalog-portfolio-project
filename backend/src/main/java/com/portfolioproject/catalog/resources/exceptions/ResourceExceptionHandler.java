package com.portfolioproject.catalog.resources.exceptions;

import java.time.Instant;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fasterxml.jackson.databind.deser.impl.ExternalTypeHandler.Builder;
import com.portfolioproject.catalog.services.exceptions.IntegrityDataBaseException;
import com.portfolioproject.catalog.services.exceptions.ObjtNotFoundException;

@ControllerAdvice
public class ResourceExceptionHandler {

	@ExceptionHandler(ObjtNotFoundException.class)
	ResponseEntity<StandardError> entityNotFoundFromRepository(ObjtNotFoundException exception,
			HttpServletRequest request){
		int status = HttpStatus.NOT_FOUND.value();

		StandardError err = new StandardError();
		err.setTimestamp(Instant.now());
		err.setStatus(status);
		err.setError("Resource not found");
		err.setMessage(exception.getMessage());
		err.setPath(request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}
	
	@ExceptionHandler(IntegrityDataBaseException.class)
	ResponseEntity<StandardError> integrityDataBaseFromRepository(IntegrityDataBaseException exception,
			HttpServletRequest request){
		int status = HttpStatus.BAD_REQUEST.value();
		StandardError err = new StandardError();
		err.setTimestamp(Instant.now());
		err.setStatus(status);
		err.setError("Must not delete category with related product");
		err.setMessage(exception.getMessage());
		err.setPath(request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}
}
