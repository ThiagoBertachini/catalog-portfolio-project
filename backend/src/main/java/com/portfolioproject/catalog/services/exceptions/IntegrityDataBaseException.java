package com.portfolioproject.catalog.services.exceptions;

public class IntegrityDataBaseException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public IntegrityDataBaseException(String msg) {
		super(msg);
	}

}
