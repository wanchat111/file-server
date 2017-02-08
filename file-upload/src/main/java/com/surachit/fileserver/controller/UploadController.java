package com.surachit.fileserver.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.surachit.fileserver.dto.UploadDto;
import com.surachit.fileserver.entity.UploadEntity;
import com.surachit.fileserver.response.Response;
import com.surachit.fileserver.service.UploadService;
import com.surachit.fileserver.util.Constants;
import com.surachit.fileserver.util.Secured;

@RestController
public class UploadController extends AbstractController {

	private static final Logger logger = LoggerFactory.getLogger(UploadController.class);
	@Autowired
	private UploadService uploadService;

	@Secured
	@RequestMapping(value = Constants.URL_UPLOAD, method = RequestMethod.POST)
	public HttpEntity<Response<Integer>> handleFileUpload(
			@RequestPart(value = "uploadDto", required = true) UploadDto uploadDto,
			@RequestParam(value = "file", required = true) MultipartFile file) throws IOException {

		int idSuccess = uploadService.uploadFile(uploadDto, file);
		return new ResponseEntity<>(returnWithSuccess(idSuccess), HttpStatus.OK);
	}

	@Secured
	@RequestMapping(value = Constants.URL_DOWNLOAD + "/{fileId}", method = RequestMethod.GET)
	public void downloadFile(@PathVariable (value = "fileId") int fileId, HttpServletResponse response) throws IOException {
		logger.info("fileId : {}", fileId);
		try {
			String fullPath = uploadService.getPathFile(fileId);
			File file = new File(fullPath);
			InputStream inputStream = new FileInputStream(file);
			response.setContentType("application/pdf");
			response.setHeader("Content-Disposition", "attachment; filename=" + fullPath);
			IOUtils.copy(inputStream, response.getOutputStream());
			response.flushBuffer();
			inputStream.close();
		} catch (Exception e) {
			logger.debug("Request could not be completed at this moment. Please try again.");
			e.printStackTrace();
		}

	}

	@Secured
	@RequestMapping(value = Constants.URL_UPLOADLIST, method = RequestMethod.GET)
	public List<UploadEntity> getUpload() {
		
		logger.info("getUpload");
		
		List<UploadEntity> upload = uploadService.getFileAll();
		
		logger.info("upload : {}", upload);
		
		return upload;
	}
}
