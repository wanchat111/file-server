package com.surachit.fileserver.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "file")
public class FileEntity {
	
	@Id
	@GeneratedValue
	@Column(name = "file_id", nullable = false)
	private int fileId;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "folder_path", nullable = false)
	private FolderEntity folder;

	@Column(name = "file_name")
	private String fileName;
	
	protected FileEntity() {

	}
	
	public FileEntity(String fileName, FolderEntity folder) {
		super();
		this.fileName = fileName;
		this.folder = folder;
	}
	
	public int getFileId() {
		return fileId;
	}

	public void setFileId(int fileId) {
		this.fileId = fileId;
	}

	public FolderEntity getFolder() {
		return folder;
	}

	public void setFolder(FolderEntity folder) {
		this.folder = folder;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
}
