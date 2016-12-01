package com.surachit.fileserver.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "file_upload")

public class UploadEntity {
	@Id
	@Column(name = "upload_id", nullable = false)
	private int uploadId;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "file_id", nullable = false)
	private FileEntity file;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "username", nullable = false)
	private AccountEntity username;
	
	@Column(name = "create_by")
	private String createBy;
	
	@Column(name = "create_date")
	private Date createDate;
	
	@Column(name = "date_modify")
	private Date dateModify;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "last_modify")
	private String lastModify;
	
	protected UploadEntity() {

	}
	
	public UploadEntity(int uploadId, FileEntity file, AccountEntity username ) {
		super();
		this.file = file;
		this.username = username;
	}

	public int getUploadId() {
		return uploadId;
	}

	public void setUploadId(int uploadId) {
		this.uploadId = uploadId;
	}

	public FileEntity getFile() {
		return file;
	}

	public void setFile(FileEntity file) {
		this.file = file;
	}

	public AccountEntity getUsername() {
		return username;
	}

	public void setUsername(AccountEntity username) {
		this.username = username;
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

	public Date getDateModify() {
		return dateModify;
	}

	public void setDateModify(Date dateModify) {
		this.dateModify = dateModify;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLastModify() {
		return lastModify;
	}

	public void setLastModify(String lastModify) {
		this.lastModify = lastModify;
	}
	
}
