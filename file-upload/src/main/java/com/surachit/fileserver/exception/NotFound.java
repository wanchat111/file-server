package com.surachit.fileserver.exception;

import org.springframework.http.HttpStatus;

public class NotFound extends CustomException{
	private static final long serialVersionUID = 1L;
	
	public NotFound(String msg) {
		super(HttpStatus.NOT_FOUND, msg);
	}
}