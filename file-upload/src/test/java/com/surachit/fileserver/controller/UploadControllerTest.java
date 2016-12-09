package com.surachit.fileserver.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Date;
import java.time.LocalDate;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.surachit.fileserver.AbstractTest;
import com.surachit.fileserver.dto.AccountDto;
import com.surachit.fileserver.dto.UploadDto;
import com.surachit.fileserver.entity.AccountEntity;
import com.surachit.fileserver.entity.BranchEntity;
import com.surachit.fileserver.entity.RoleEntity;
import com.surachit.fileserver.repository.AccountRepository;
import com.surachit.fileserver.repository.UploadRepository;
import com.surachit.fileserver.util.Constants;
import com.surachit.fileserver.util.Roles;

@WebIntegrationTest("server.port:8080")
public class UploadControllerTest extends AbstractTest {
	
	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private UploadRepository uploadRepository;
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	
	private String baseUrl = "http://localhost:8080/";
	
	private static final String USERNAME = "userTest";
	private static final String FILENAME = "หลักสูตรทดสอบ";
	private static final String DESCRIPTION = "รายหลักสูตรทดสอบ";
	private static final String PATH = "curriculum";
	private static final String USERNAMEADMIN = "userAdminTest";
	
	LocalDate today = LocalDate.now();
	Date sqlDate = Date.valueOf( today );
	private MockMvc mockMvc;
	
	private String tmpPath = System.getProperty("java.io.tmpdir");
	private String destination = tmpPath + "/test/";

	@Before
	public void initTest() throws NoSuchAlgorithmException, InvalidKeySpecException {
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		
	}
	
	@After
	public void tearDown() {
		AccountEntity argAdmin = accountRepository.findOne(USERNAMEADMIN);
		if (argAdmin != null) {
			accountRepository.delete(argAdmin);
		}
	}

}
