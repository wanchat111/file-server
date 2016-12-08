package com.surachit.fileserver.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.expression.ParseException;
import org.springframework.test.context.web.WebAppConfiguration;

import com.surachit.fileserver.AbstractTest;
import com.surachit.fileserver.dto.AccountDto;
import com.surachit.fileserver.entity.AccountEntity;
import com.surachit.fileserver.entity.BranchEntity;
import com.surachit.fileserver.entity.RoleEntity;
import com.surachit.fileserver.exception.ConflictingData;
import com.surachit.fileserver.repository.AccountRepository;
import com.surachit.fileserver.util.Roles;

@WebAppConfiguration
public class AccountServiceTest extends AbstractTest {

	@Autowired
	private AccountService accountService;

	@Autowired
	private AccountRepository accountRepository;

	private static final String USERNAME = "userTest";
	private static final String PASSWORD = "1234";
	private static final String EMAIL = "test@test.com";
	private static final String NAME = "test";
	private static final String SURNAME = "test";
	private static final int BRANCHID = 1;
	private static final String EMAILUPDATE = "testupodate@test.com";
	private static final String NAMEUPDATE = "testupdate";
	private static final String SURNAMEUPDATE = "testupdate";

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
	public void can_create_account() {
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

	}

	@Test
	public void can_delete_account() {
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
		accountService.deleteAccount(USERNAME);
		AccountEntity deleteAccount = accountRepository.findOne(USERNAME);
		assertNull(deleteAccount);
	}

	@Test
	public void can_update_account() {
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

		// update
		accountDto.setEmail(EMAILUPDATE);
		accountDto.setName(NAMEUPDATE);
		accountDto.setSurname(SURNAMEUPDATE);
		accountService.updateAccount(USERNAME, accountDto);
		// check expect result
		AccountEntity accountUpdate = accountRepository.findOne(USERNAME);
		assertEquals(EMAILUPDATE, accountUpdate.getEmail());
		assertEquals(NAMEUPDATE, accountUpdate.getName());
		assertEquals(SURNAMEUPDATE, accountUpdate.getSurname());
	}
	
	@Test
	public void can_sign_in_account() {
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
		accountService.signIn(USERNAME, PASSWORD);
		
		AccountEntity signin = accountRepository.findOne(USERNAME);
		assertNotNull(signin.getSessionId());
	}
	
	@Test
	public void can_sign_out_account() {
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
		accountService.signIn(USERNAME, PASSWORD);
		AccountEntity signIn = accountRepository.findOne(USERNAME);
		assertNotNull(signIn.getSessionId());
		
		accountService.signOut(USERNAME);
		AccountEntity signOut = accountRepository.findOne(USERNAME);
		assertNull(signOut.getSessionId());
	}
	
	@Test
    public void can_get_account_list_success() throws ParseException {
        simulateDataForTest();
        int pageNumber = 1;
        int limit = 5;
        Page<AccountEntity> page = accountService.getListAccount(pageNumber, limit);
        assertNotNull(page);
        assertNotNull(page.getContent());

    }
	
	@Test
	public void can_get_account_success() {
		
	}

    private void simulateDataForTest() throws ParseException {
        // Set up data for test 2 row
        RoleEntity role = new RoleEntity(Roles.USER.getRoleId(), Roles.USER.getRoleName());
        BranchEntity branch = new BranchEntity(1);
        for (int i = 1; i <= 2; i++) {

            AccountEntity account = new AccountEntity(USERNAME + i, PASSWORD, role, branch);
            account.setName(NAME + i);
            account.setSurname(SURNAME);
            account.setEmail("test" + i + "@test.com");
            accountRepository.save(account);
        }
    }

}
