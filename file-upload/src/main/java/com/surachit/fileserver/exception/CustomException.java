package com.surachit.fileserver.exception;

import org.springframework.http.HttpStatus;

public class CustomException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private HttpStatus httpStatus;
	public CustomException(HttpStatus httpStatus, String msg) {
		super(msg);
		this.setHttpStatus(httpStatus);
	}
	public HttpStatus getHttpStatus() {
		return httpStatus;
	}
	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}

}
