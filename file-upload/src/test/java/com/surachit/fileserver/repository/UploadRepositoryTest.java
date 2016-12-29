package com.surachit.fileserver.repository;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.sql.Date;
import java.time.LocalDate;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.surachit.fileserver.AbstractTest;
import com.surachit.fileserver.entity.AccountEntity;
import com.surachit.fileserver.entity.BranchEntity;
import com.surachit.fileserver.entity.FileEntity;
import com.surachit.fileserver.entity.FolderEntity;
import com.surachit.fileserver.entity.RoleEntity;
import com.surachit.fileserver.entity.UploadEntity;
import com.surachit.fileserver.util.Folders;


public class UploadRepositoryTest extends AbstractTest {

	@Autowired
	private UploadRepository uploadRepo;
	
	@Autowired
	private FileRepository fileRepo;
	
	@Autowired
	private AccountRepository accountRepo;
	
	
	LocalDate today = LocalDate.now();
	Date sqlDate = Date.valueOf( today ) ;

	@Test
	public void can_crud_upload() {
		// create user
		FolderEntity folder = new FolderEntity(Folders.FOLDER_COSTS);
		FileEntity file = new FileEntity("test2", folder);
		String username = "newnew";
		String password = "new";
		RoleEntity role = new RoleEntity(3, "user");
		BranchEntity branch = new BranchEntity(1, "ส่วนกลาง");
		
		AccountEntity account = new AccountEntity(username, password, role, branch);
		account.setEmail("new@test.com");
		account.setName("Nisarat");
		account.setSurname("Kongcheep");
		accountRepo.save(account);
		
		
		UploadEntity upload = new UploadEntity(file, username); 
		upload.setCreateBy(username);
		upload.setCreateDate(sqlDate);
		upload.setDateModify(null);
		upload.setDescription("i have a pen");
		uploadRepo.save(upload);
		
		//Check upload exist
		int i = upload.getUploadId();
		UploadEntity testCreate = uploadRepo.findOne(i); 
		assertEquals("i have a pen", testCreate.getDescription());
		
		//account exist
		String usernameCheck = upload.getUsername();
		AccountEntity accountCheck = accountRepo.findOne(usernameCheck);
		assertEquals("new@test.com", accountCheck.getEmail());
		
		//file exist
		int fileIdCheck = upload.getFile().getFileId();
		FileEntity fileCheck = fileRepo.findOne(fileIdCheck);
		assertEquals("test2", fileCheck.getFileName());
		
		//delete upload
		uploadRepo.delete(testCreate);
		
		accountRepo.delete(accountCheck);
		
		//upload delete
		UploadEntity testDelete = uploadRepo.findOne(i);
		assertNull(testDelete);		
		
		//check account delete
		AccountEntity accountCheckDelete = accountRepo.findOne(usernameCheck);
		assertNull(accountCheckDelete);	
		
		//check file delete
		FileEntity fileCheckDelete = fileRepo.findOne(fileIdCheck);
		assertNull(fileCheckDelete);	
		
		
	}
}
