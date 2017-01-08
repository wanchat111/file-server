package com.surachit.fileserver.exception;

import org.springframework.http.HttpStatus;

public class ConflictingData extends CustomException {
	private static final long serialVersionUID = 1L;
	
	public ConflictingData(String msg) {
		super(HttpStatus.CONFLICT, msg);
	}

}
