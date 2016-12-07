package com.surachit.fileserver.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import com.surachit.fileserver.AbstractTest;
import com.surachit.fileserver.dto.AccountDto;
import com.surachit.fileserver.entity.AccountEntity;
import com.surachit.fileserver.repository.AccountRepository;
import com.surachit.fileserver.service.AccountService;
import com.surachit.fileserver.service.SessionService;
import com.surachit.fileserver.util.Constants;
import com.surachit.fileserver.util.Roles;

@WebIntegrationTest("server.port:8080")
public class AccountControllerTest extends AbstractTest {

	@Autowired
	@InjectMocks
	private AccountController accountController;
	
	@Mock
	private AccountService accountServiceMock;
	
	@Autowired
	private AccountRepository accountRepository;
	
	@Mock
	private SessionService sessionServiceMock;
	
	@Autowired
    private WebApplicationContext webApplicationContext;
	
	private String baseUrl = "http://localhost:8080/";
	private String plainCredendtial = "TestUser:Password123";
	private String randomToken = "someRandomlyGeneratedToken";
	private RestTemplate rtp = new RestTemplate();

	private static final String USERNAME = "userTest";
	private static final String PASSWORD = "1234";
	private static final String EMAIL = "test@test.com";
	private static final String NAME = "test";
	private static final String SURNAME = "test";
	private static final int BRANCHID = 1;
	private MockMvc mockMvc;
	
	@Before
	public void initTest() 
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		MockitoAnnotations.initMocks(this);
		
		AccountDto accountDto = new AccountDto();
		accountDto.setUserName(USERNAME);
		accountDto.setPassword(PASSWORD);
		accountDto.setEmail(EMAIL);
		accountDto.setBranchId(BRANCHID);
		accountDto.setName(NAME);
		accountDto.setRoleName(Roles.USER.getRoleName());
		accountDto.setSurname(SURNAME);
		Mockito.when(accountServiceMock.signIn(Mockito.any(), Mockito.any()))
				.thenReturn(randomToken);
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}
	
	@After
	public void tearDown() {
		AccountEntity arg0 = accountRepository.findOne(USERNAME);
		if (arg0 != null) {
			accountRepository.delete(arg0);
		}

	}
	
	@Test
    public void can_signin() {
        
        byte[] plainCredsBytes = plainCredendtial.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + base64Creds);

        HttpEntity<String> req = new HttpEntity<>(headers);
        ResponseEntity<String> resp = rtp.exchange(baseUrl + Constants.URL_SIGNIN, HttpMethod.GET,
                req, String.class);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
		HttpHeaders respHeaders = resp.getHeaders();
		List<String> cookies = respHeaders.get("Set-Cookie");
		boolean isFound = false;
		for (String cookie: cookies) {
			if (cookie.equals("token=" + randomToken)) {
				isFound = true;
			}
		}
		assertTrue(isFound);
    }
}
