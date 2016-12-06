package com.surachit.fileserver.exception;

import org.springframework.http.HttpStatus;

public class Forbidden extends CustomException {
	private static final long serialVersionUID = 1L;
	
	public Forbidden(String msg) {
		super(HttpStatus.FORBIDDEN, msg);
	}
}
