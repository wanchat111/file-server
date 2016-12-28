package com.surachit.fileserver.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.surachit.fileserver.AbstractTest;
import com.surachit.fileserver.entity.FileEntity;
import com.surachit.fileserver.entity.FolderEntity;


public class FileRepositoryTest extends AbstractTest {
	@Autowired
	private FileRepository fileRepo;

	@Test
	public void can_crud_file() {
		
		// create file
		FolderEntity folder = new FolderEntity("curriculum", "หลักสูตร");
		String fileName = "test";
		FileEntity file = new FileEntity(fileName, folder);
		
		fileRepo.save(file);
		int i = file.getFileId();
		FileEntity testCreate = fileRepo.findOne(i);
		assertEquals("test", testCreate.getFileName());
		
		//delete file
		fileRepo.delete(testCreate);
		FileEntity testDelete = fileRepo.findOne(i);
		assertNull(testDelete);		

	}

}
