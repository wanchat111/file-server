package com.surachit.fileserver.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import com.surachit.fileserver.AbstractTest;
import com.surachit.fileserver.dto.AccountDto;
import com.surachit.fileserver.entity.AccountEntity;
import com.surachit.fileserver.entity.BranchEntity;
import com.surachit.fileserver.entity.RoleEntity;
import com.surachit.fileserver.repository.AccountRepository;
import com.surachit.fileserver.service.AccountService;
import com.surachit.fileserver.util.Constants;
import com.surachit.fileserver.util.Roles;

@WebIntegrationTest("server.port:8080")
public class AccountControllerTest extends AbstractTest {

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private AccountService accountService;

	@Autowired
	private WebApplicationContext webApplicationContext;

	private String baseUrl = "http://localhost:8080/";
	private String plainCredendtial = "userTest:1234";

	private RestTemplate rtp = new RestTemplate();

	private static final String USERNAME = "userTest";
	private static final String USERNAMEADMIN = "userAdminTest";
	private static final String PASSWORD = "1234";
	private static final String EMAIL = "test@test.com";
	private static final String NAME = "test";
	private static final String SURNAME = "test";
	private static final int BRANCHID = 1;
	private MockMvc mockMvc;

	@Before
	public void initTest() throws NoSuchAlgorithmException, InvalidKeySpecException {
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		testHelper.i_have_active_account(USERNAME);
	}

	@After
	public void tearDown() {
		AccountEntity arg0 = accountRepository.findOne(USERNAME);
		AccountEntity arg1 = accountRepository.findOne(USERNAME + 1);
		AccountEntity arg2 = accountRepository.findOne(USERNAME + 2);
		if (arg0 != null) {
			accountRepository.delete(arg0);
		}
		if (arg1 != null) {
			accountRepository.delete(arg1);
		}
		if (arg2 != null) {
			accountRepository.delete(arg2);
		}

	}

	@Test
	public void can_signin() {
		RoleEntity role = new RoleEntity(Roles.SYS_ACCOUNT_ADMIN.getRoleId(), Roles.SYS_ACCOUNT_ADMIN.getRoleName());
		BranchEntity branch = new BranchEntity(1);
		AccountEntity accountEntity = new AccountEntity(USERNAME, accountService.hashPassword(PASSWORD), role, branch);
		accountEntity.setEmail(EMAIL);
		accountEntity.setName(NAME);
		accountEntity.setSurname(SURNAME);
		accountRepository.save(accountEntity);
		testHelper.i_have_inactive_account(USERNAME);
		byte[] plainCredsBytes = plainCredendtial.getBytes();
		byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
		String base64Creds = new String(base64CredsBytes);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Basic " + base64Creds);

		HttpEntity<String> req = new HttpEntity<>(headers);
		ResponseEntity<String> resp = rtp.exchange(baseUrl + Constants.URL_SIGNIN, HttpMethod.GET, req, String.class);

		assertEquals(HttpStatus.OK, resp.getStatusCode());
		assertNotNull(resp.getBody());
	}

	@Test
	public void can_sign_out() {
		HttpHeaders header = new HttpHeaders();
		AccountEntity accountEntity = accountRepository.findOne(USERNAME);
		header.add(HttpHeaders.COOKIE, Constants.COOKIE_USR + "=" + USERNAME);
		header.add(HttpHeaders.COOKIE, Constants.COOKIE_TKN + "=" + accountEntity.getSessionId());
		HttpEntity<String> req = new HttpEntity<>(header);
		ResponseEntity<String> resp = rtp.exchange(baseUrl + Constants.URL_SIGNOUT, HttpMethod.POST, req, String.class);

		assertEquals(HttpStatus.OK, resp.getStatusCode());
	}

	@Test
	public void can_create_user_account() throws Exception {
		AccountEntity firstAccount = accountRepository.findOne(USERNAME);
		if (firstAccount != null) {
			accountRepository.delete(firstAccount);
		}

		AccountDto accountDto = new AccountDto();
		accountDto.setUserName(USERNAME);
		accountDto.setPassword(PASSWORD);
		accountDto.setEmail(EMAIL);
		accountDto.setName(NAME);
		accountDto.setSurname(SURNAME);
		accountDto.setBranchId(BRANCHID);
		accountDto.setRoleName(Roles.USER.getRoleName());
		RoleEntity role = new RoleEntity(Roles.SYS_ACCOUNT_ADMIN.getRoleId(), Roles.SYS_ACCOUNT_ADMIN.getRoleName());
		BranchEntity branch = new BranchEntity(1);
		AccountEntity activeAccount = testHelper.i_have_active_account(USERNAMEADMIN, role, branch);
		byte[] requestBody = printJson(accountDto);
		mockMvc.perform(post(baseUrl + Constants.URL_ACCOUNT)
				.header("Authentication", activeAccount.getUsername() + ";" + activeAccount.getSessionId())
				.contentType(MediaType.APPLICATION_JSON).content(requestBody)).andDo(print())
				.andExpect(status().isCreated());
		accountRepository.delete(activeAccount);
	}

	@Test
	public void delete_account_success() throws Exception {
		AccountDto accountDto = new AccountDto();
		accountDto.setUserName(USERNAME);
		accountDto.setPassword(PASSWORD);
		accountDto.setEmail(EMAIL);
		accountDto.setName(NAME);
		accountDto.setSurname(SURNAME);
		accountDto.setBranchId(BRANCHID);
		accountDto.setRoleName(Roles.USER.getRoleName());
		RoleEntity role = new RoleEntity(Roles.SYS_ACCOUNT_ADMIN.getRoleId(), Roles.SYS_ACCOUNT_ADMIN.getRoleName());
		BranchEntity branch = new BranchEntity(1);
		AccountEntity activeAccount = testHelper.i_have_active_account(USERNAMEADMIN, role, branch);
		mockMvc.perform(delete(Constants.URL_ACCOUNT + "/" + USERNAME).header("Authentication",
				activeAccount.getUsername() + ";" + activeAccount.getSessionId())).andDo(print())
				.andExpect(status().isOk());

		AccountEntity deletedAccount = accountRepository.findOne(USERNAME);
		// check expected result
		assertNull(deletedAccount);
		accountRepository.delete(activeAccount);
	}
	
	@Test
    public void update_account_success() throws Exception {
        AccountEntity account = accountRepository.findOne(USERNAME);
        account.setEmail("update@test.com");
        account.setName("update");
        account.setSurname("update");
        byte[] requestBody = printJson(account);
        RoleEntity role = new RoleEntity(Roles.SYS_ACCOUNT_ADMIN.getRoleId(), Roles.SYS_ACCOUNT_ADMIN.getRoleName());
		BranchEntity branch = new BranchEntity(1);
		AccountEntity activeAccount = testHelper.i_have_active_account(USERNAMEADMIN, role, branch);

        mockMvc
                .perform(put(Constants.URL_ACCOUNT + "/" + USERNAME)
                        .header("Authentication",
                                activeAccount.getUsername() + ";" + activeAccount.getSessionId())
                        .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andDo(print()).andExpect(status().is2xxSuccessful());

        // Assert Value
        AccountEntity updatedAccount = accountRepository.findOne(USERNAME);

        assertEquals("update", updatedAccount.getName());
        assertEquals("update", updatedAccount.getSurname());
        assertEquals("update@test.com", updatedAccount.getEmail());
        accountRepository.delete(activeAccount);
    }
	
	@Test
    public void can_get_account_list_setup_with_null_page_and_limit() throws Exception {
        simulateDataForTest();

        RoleEntity role = new RoleEntity(Roles.SYS_ACCOUNT_ADMIN.getRoleId(),
                Roles.SYS_ACCOUNT_ADMIN.getRoleName());
        BranchEntity branch = new BranchEntity(1);
        AccountEntity activeAccount = testHelper.i_have_active_account(USERNAMEADMIN, role, branch);
        mockMvc
                .perform(get(Constants.URL_ACCOUNTS)
                        .header("Authentication",
                                activeAccount.getUsername() + ";"
                                        + activeAccount.getSessionId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().is2xxSuccessful());
    }
	
	@Test
	public void can_get_account() throws Exception {
		AccountEntity accountEntity = accountRepository.findOne(USERNAME);
		byte[] requestBody = printJson(accountEntity);
		RoleEntity role = new RoleEntity(Roles.SYS_ACCOUNT_ADMIN.getRoleId(),
                Roles.SYS_ACCOUNT_ADMIN.getRoleName());
        BranchEntity branch = new BranchEntity(1);
		AccountEntity activeAccount = testHelper.i_have_active_account(USERNAMEADMIN, role, branch);
		mockMvc
        .perform(get(Constants.URL_ACCOUNT + "/" + USERNAME)
                .header("Authentication",
                        activeAccount.getUsername() + ";"
                                + activeAccount.getSessionId())
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
        .andDo(print()).andExpect(status().is2xxSuccessful());
	}
	
	private void simulateDataForTest() {
        // Set up data for test 20 row
        RoleEntity role = new RoleEntity(Roles.USER.getRoleId(), Roles.USER.getRoleName());
        BranchEntity branch = new BranchEntity(1);
        for (int i = 1; i <= 2; i++) {

            AccountEntity account = new AccountEntity(USERNAME + i, PASSWORD, role, branch);
            account.setName(NAME + i);
            account.setSurname(SURNAME);
            account.setEmail(EMAIL);

            accountRepository.save(account);
        }

        
    }
}
