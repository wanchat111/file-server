package com.surachit.fileserver.dto;

import java.util.Date;

import com.surachit.fileserver.entity.UploadEntity;

public class UploadDto {
	private int uploadId;
	private String userName;
	private int fileId;
	private String description;
	private String createBy;
	private Date createDate;
	private String lastModify;
	private Date dateModify;
	private String filePath;
	private String fileName;
	
	public UploadDto() {
		
	}
	
	public UploadDto(UploadEntity uploadEntity) {
		userName = uploadEntity.getUsername();
		fileId = uploadEntity.getFile().getFileId();
		description = uploadEntity.getDescription();
		createBy = uploadEntity.getCreateBy();
		createDate = uploadEntity.getCreateDate();
		lastModify = uploadEntity.getLastModify();
		dateModify = uploadEntity.getDateModify();
		filePath = uploadEntity.getFile().getFolder().getFolderPath();
		fileName = uploadEntity.getFile().getFileName();
	}

	public int getUploadId() {
		return uploadId;
	}

	public void setUploadId(int uploadId) {
		this.uploadId = uploadId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getFileId() {
		return fileId;
	}

	public void setFileId(int fileId) {
		this.fileId = fileId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getLastModify() {
		return lastModify;
	}

	public void setLastModify(String lastModify) {
		this.lastModify = lastModify;
	}

	public Date getDateModify() {
		return dateModify;
	}

	public void setDateModify(Date dateModify) {
		this.dateModify = dateModify;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}