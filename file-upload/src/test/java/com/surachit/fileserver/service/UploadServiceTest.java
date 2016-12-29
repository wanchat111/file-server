package com.surachit.fileserver.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;

import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.multipart.MultipartFile;

import com.surachit.fileserver.AbstractTest;
import com.surachit.fileserver.dto.AccountDto;
import com.surachit.fileserver.dto.UploadDto;
import com.surachit.fileserver.entity.AccountEntity;
import com.surachit.fileserver.exception.ConflictingData;
import com.surachit.fileserver.repository.AccountRepository;
import com.surachit.fileserver.repository.UploadRepository;
import com.surachit.fileserver.util.Roles;

@WebAppConfiguration
public class UploadServiceTest extends AbstractTest {
	@Autowired
	private UploadService uploadService;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private UploadRepository uploadRepository;
	
	@Autowired
	private AccountRepository accountRepository;
	
	private static final String USERNAME = "userTest";
	private static final String PASSWORD = "1234";
	private static final String EMAIL = "test@test.com";
	private static final String NAME = "test";
	private static final String SURNAME = "test";
	private static final int BRANCHID = 1;
	private static final String FILENAME = "หลักสูตรทดสอบ";
	private static final String DESCRIPTION = "รายหลักสูตรทดสอบ";
	private static final String PATH = "curriculum";
	
	LocalDate today = LocalDate.now();
	Date sqlDate = Date.valueOf( today );
	
	@After
	public void tearDown() {
		
	}
	
	@Test
	public void can_create_upload() throws IOException {
		
		
		File file = new File("src/test/resources/test.pdf");
	    FileInputStream input = new FileInputStream(file);
	    MultipartFile multipartFile = new MockMultipartFile("filepppp", file.getName(), "application/pdf", IOUtils.toByteArray(input));
		AccountDto accountDto = new AccountDto();
		accountDto.setUserName(USERNAME);
		accountDto.setPassword(PASSWORD);
		accountDto.setEmail(EMAIL);
		accountDto.setBranchId(BRANCHID);
		accountDto.setName(NAME);
		accountDto.setRoleName(Roles.USER.getRoleName());
		accountDto.setSurname(SURNAME);

		try {
			accountService.createAccount(accountDto);
		} catch (ConflictingData e) {
			e.printStackTrace();
		}
		UploadDto uploadDto = new UploadDto();
		uploadDto.setUserName(USERNAME);
		uploadDto.setFileName(FILENAME);
		uploadDto.setCreateBy(USERNAME);
		uploadDto.setCreateDate(sqlDate);
		uploadDto.setDescription(DESCRIPTION);
		uploadDto.setFilePath(PATH);
		int i = 0;
		try {
			i = uploadService.uploadFile(uploadDto, multipartFile);
			//i = uploadService.createFileUpload(uploadDto);
		} catch (ConflictingData e) {
			e.printStackTrace();
		}
		
		uploadRepository.delete(i);
		AccountEntity accountEntity = accountRepository.findOne(USERNAME);
		accountRepository.delete(accountEntity);
	}
}
