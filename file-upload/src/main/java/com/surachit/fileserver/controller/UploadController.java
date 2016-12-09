package com.surachit.fileserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.surachit.fileserver.dto.UploadDto;
import com.surachit.fileserver.response.Response;
import com.surachit.fileserver.service.UploadService;
import com.surachit.fileserver.util.Constants;
import com.surachit.fileserver.util.Secured;

@RestController
public class UploadController extends AbstractController {
    @Autowired
    private UploadService uploadService;
	
	 @Secured
	    @RequestMapping(value = Constants.URL_UPLOAD, method = RequestMethod.POST)
	    public HttpEntity<Response<Integer>> handleFileUpload(
	    		@RequestBody(required = true) UploadDto uploadDto,
	    		@RequestParam(value = "file", required = true) MultipartFile file) {
	    	int idSuccess = uploadService.uploadFile(uploadDto, file);
	    	return new ResponseEntity<>(returnWithSuccess(idSuccess), HttpStatus.OK);
	    }
}
