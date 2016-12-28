package com.surachit.fileserver.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.MultipartProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.surachit.fileserver.dto.UploadDto;
import com.surachit.fileserver.entity.AccountEntity;
import com.surachit.fileserver.entity.FileEntity;
import com.surachit.fileserver.entity.FolderEntity;
import com.surachit.fileserver.entity.UploadEntity;
import com.surachit.fileserver.exception.BadRequest;
import com.surachit.fileserver.repository.AccountRepository;
import com.surachit.fileserver.repository.UploadRepository;
import com.surachit.fileserver.util.Folders;

@Service
@ConfigurationProperties(prefix = "surachit.upload")
public class UploadService {
	private String path;

	private static final Logger logger = LoggerFactory.getLogger(UploadService.class);
	@Autowired
	private MultipartProperties multipartProperties;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private UploadRepository uploadRepository;

	public int uploadFile(UploadDto uploadDto, MultipartFile file) {
		logger.debug("Upload file {}", file.getName());
		Long filesize = file.getSize();
		Long maxSize = this.getMaxFileSizeUpload();
		String directory = path + uploadDto.getFilePath();
		String filepath = Paths.get(directory, file.getName()).toString();

		if (filesize > this.getMaxFileSizeUpload()) {
			logger.debug("Check file size was exceed: input size is {} limit size is {}", filesize, maxSize);
		}

		if (file.isEmpty()) {
			logger.debug("The file upload was empty {}", file.getOriginalFilename());
			throw new BadRequest("The Upload is failed because file is empty");
		}
		try {
			BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filepath)));
			stream.write(file.getBytes());
			stream.close();
		} catch (IOException e) {
			throw new BadRequest("Upload fail");
		}
		int i = createFileUpload(uploadDto);
		return i;
	}

	private Long getMaxFileSizeUpload() {
		String maxSize = multipartProperties.getMaxFileSize().replace("Mb", "");
		Long maxSizeUpload = Long.valueOf(maxSize) * 1024 * 1024;
		return maxSizeUpload;
	}

	@Transactional
	public int createFileUpload(UploadDto uploadDto) {
		logger.debug("Create file upload {}", uploadDto);
		FolderEntity folder = null;
		if (uploadDto.getFilePath().equals(Folders.FOLDER_COSTS.getPath())) {
			folder = new FolderEntity(Folders.FOLDER_COSTS);
		} else if (uploadDto.getFilePath().equals(Folders.FOLDER_CURRICULUM.getPath())) {
			folder = new FolderEntity(Folders.FOLDER_CURRICULUM);
		} else if (uploadDto.getFilePath().equals(Folders.FOLDER_INFORMATION.getPath())) {
			folder = new FolderEntity(Folders.FOLDER_INFORMATION);
		} else if (uploadDto.getFilePath().equals(Folders.FOLDER_SCHEDULE.getPath())) {
			folder = new FolderEntity(Folders.FOLDER_SCHEDULE);
		}

		FileEntity file = new FileEntity(uploadDto.getFileName(), folder);
		AccountEntity account = accountRepository.findOne(uploadDto.getUserName());
		UploadEntity uploadEntity = new UploadEntity(file, account);
		uploadEntity.setCreateBy(uploadDto.getCreateBy());
		uploadEntity.setCreateDate(uploadDto.getCreateDate());
		uploadEntity.setDateModify(uploadDto.getDateModify());
		uploadEntity.setDescription(uploadDto.getDescription());
		uploadEntity.setLastModify(uploadDto.getLastModify());
		uploadRepository.save(uploadEntity);
		return uploadEntity.getUploadId();

	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
