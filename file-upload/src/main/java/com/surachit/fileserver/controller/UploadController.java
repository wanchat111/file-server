package com.surachit.fileserver.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
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
			@RequestPart(value = "uploadDto", required = true) UploadDto uploadDto,
			@RequestPart(value = "file", required = true) MultipartFile file) throws IOException {

		int idSuccess = uploadService.uploadFile(uploadDto, file);
		return new ResponseEntity<>(returnWithSuccess(idSuccess), HttpStatus.OK);
	}
}
