package com.surachit.fileserver.repository;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.surachit.fileserver.AbstractTest;
import com.surachit.fileserver.entity.AccountEntity;
import com.surachit.fileserver.entity.BranchEntity;
import com.surachit.fileserver.entity.FileEntity;
import com.surachit.fileserver.entity.FolderEntity;
import com.surachit.fileserver.entity.RoleEntity;
import com.surachit.fileserver.entity.UploadEntity;

public class UploadRepositoryTest extends AbstractTest {
	@Autowired
	private UploadRepository uploadRepo;
	
	private LocalDate lDate = LocalDate.now();
	Instant instant = Instant.from(lDate.atStartOfDay(ZoneId.of("Asia/Bangkok")));
	Date date = Date.from(instant);
	
	@Test
	public void can_crud_upload() {
		FolderEntity folder = new FolderEntity("curriculum", "หลักสูตร");
		FileEntity file = new FileEntity("testUpload", folder);
		
		RoleEntity role = new RoleEntity(3, "user");
		BranchEntity branch = new BranchEntity(1, "ส่วนกลาง");
		AccountEntity username = new AccountEntity("testUpload", "test", role, branch);
		username.setName("user");
		username.setSurname("user");
		username.setEmail("test@test.com");
		UploadEntity uploadEntity = new UploadEntity(file, username);
		uploadEntity.setCreateBy("user");
		uploadEntity.setCreateDate(date);
		uploadEntity.setDescription("test upload");
		uploadRepo.save(uploadEntity);
		int i = uploadEntity.getUploadId();
		
		UploadEntity uploadDelete = uploadRepo.findOne(i);
		Assert.assertEquals("testUpload", uploadDelete.getUsername().getUsername());
		uploadRepo.delete(uploadDelete);
		UploadEntity checkDelete = uploadRepo.findOne(i);
		Assert.assertNull(checkDelete);
		
		
	}
}
