package com.surachit.fileserver.controller;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.FileInputStream;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Date;
import java.time.LocalDate;

import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.surachit.fileserver.AbstractTest;
import com.surachit.fileserver.dto.AccountDto;
import com.surachit.fileserver.dto.UploadDto;
import com.surachit.fileserver.entity.AccountEntity;
import com.surachit.fileserver.entity.BranchEntity;
import com.surachit.fileserver.entity.FileEntity;
import com.surachit.fileserver.entity.RoleEntity;
import com.surachit.fileserver.entity.UploadEntity;
import com.surachit.fileserver.exception.ConflictingData;
import com.surachit.fileserver.repository.AccountRepository;
import com.surachit.fileserver.repository.FileRepository;
import com.surachit.fileserver.repository.UploadRepository;
import com.surachit.fileserver.service.AccountService;
import com.surachit.fileserver.util.Constants;
import com.surachit.fileserver.util.Roles;

@WebIntegrationTest("server.port:8080")
@RunWith(SpringJUnit4ClassRunner.class)
public class UploadControllerTest extends AbstractTest {
	
	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private FileRepository fileRepository;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private UploadRepository uploadRepo;
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	
	private static final String USERNAME = "userTest";
	private static final String FILENAME = "หลักสูตรทดสอบ";
	private static final String DESCRIPTION = "รายหลักสูตรทดสอบ";
	private static final String PATH = "curriculum";
	private static final String USERNAMEADMIN = "userAdminTest";
	
	private static final String PASSWORD = "1234";
	private static final String EMAIL = "test@test.com";
	private static final String NAME = "test";
	private static final String SURNAME = "test";
	private static final int BRANCHID = 1;
	
	
	LocalDate today = LocalDate.now();
	Date sqlDate = Date.valueOf( today );
	private MockMvc mockMvc;
	
	

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
	
	@Test
	public void can_run_upload_controller() throws Exception {
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
		RoleEntity role = new RoleEntity(Roles.SYS_ACCOUNT_ADMIN.getRoleId(), Roles.SYS_ACCOUNT_ADMIN.getRoleName());
		BranchEntity branch = new BranchEntity(1);
		AccountEntity activeAccount = testHelper.i_have_active_account(USERNAMEADMIN, role, branch);
		File file = new File("src/test/resources/test.pdf");
		FileInputStream input = new FileInputStream(file);
		UploadDto uploadDto = new UploadDto();
		uploadDto.setUserName(USERNAME);
		uploadDto.setFileName(FILENAME);
		uploadDto.setCreateBy(USERNAME);
		uploadDto.setCreateDate(sqlDate);
		uploadDto.setDescription(DESCRIPTION);
		uploadDto.setFilePath(PATH);
		
		byte[] requestBody = printJson(uploadDto);
	    MockMultipartFile multipartFile = new MockMultipartFile("file", file.getName(), "application/pdf", input);
	    MockMultipartFile jsonFile = new MockMultipartFile("uploadDto", "", "application/json", requestBody);
	
	    
	    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.fileUpload(Constants.URL_UPLOAD).file(multipartFile)
	    		.file(jsonFile)
	    		.header("Authentication", activeAccount.getUsername() + ";" + activeAccount.getSessionId()))
	    		.andDo(print())
	    		.andExpect(status().isOk()).andReturn();
	    
	    String body = mvcResult.getResponse().getContentAsString();
		assertNotNull(body);
		JSONObject myjson = new JSONObject(body);
		int uploadId = Integer.parseInt(myjson.get("data").toString());
		System.out.println(uploadId);
		UploadEntity uploadEntity = uploadRepo.findOne(uploadId);
		int fileId = uploadEntity.getFile().getFileId();
		FileEntity fileEntity = fileRepository.findOne(fileId);
		fileRepository.delete(fileEntity);
		AccountEntity account = accountRepository.findOne(USERNAME);
		if (account != null) {
			accountRepository.delete(account);
		}
	}

}
