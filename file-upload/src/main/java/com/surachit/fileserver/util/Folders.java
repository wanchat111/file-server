package com.surachit.fileserver.util;

public enum Folders {
	FOLDER_COSTS("costs", "ค่าใช้จ่าย"),
	FOLDER_CURRICULUM("curriculum", "หลักสูตร"),
	FOLDER_INFORMATION("information", "รายละเอียดโรงเรียน"),
	FOLDER_SCHEDULE("schedule", "ตารางสอน");
	private final String path;
	private final String name;
	
	Folders(String path, String name) {
		this.path = path;
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public String getName() {
		return name;
	}

}
