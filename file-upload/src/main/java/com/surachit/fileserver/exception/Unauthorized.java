package com.surachit.fileserver.exception;

import org.springframework.http.HttpStatus;

public class Unauthorized extends CustomException {
	private static final long serialVersionUID = 1L;
	
	public Unauthorized(String msg) {
		super(HttpStatus.UNAUTHORIZED, msg);
	}
}
