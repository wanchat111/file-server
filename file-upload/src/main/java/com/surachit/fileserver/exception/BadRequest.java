package com.surachit.fileserver.exception;

import org.springframework.http.HttpStatus;

public class BadRequest extends CustomException {
	private static final long serialVersionUID = 1L;
	
	public BadRequest(String msg) {
		super(HttpStatus.BAD_REQUEST, msg);
	}
}
