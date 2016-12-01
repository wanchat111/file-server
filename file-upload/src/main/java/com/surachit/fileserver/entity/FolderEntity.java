package com.surachit.fileserver.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "folder")
public class FolderEntity {

	@Id
	@Column(name = "folder_path")
	private String folderPath;

	@Column(name = "folder_name")
	private String folderName;

	protected FolderEntity() {

	}

	public FolderEntity(String folderPath, String folderName) {
		this.folderName = folderName;
		this.folderPath = folderPath;
	}

	public String getFolderName() {
		return folderName;
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}

	public String getFolderPath() {
		return folderPath;
	}

	public void setFolderPath(String folderPath) {
		this.folderPath = folderPath;
	}

}
