package com.surachit.fileserver.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.web.WebAppConfiguration;

import com.surachit.fileserver.AbstractTest;
import com.surachit.fileserver.dto.AccountDto;
import com.surachit.fileserver.entity.AccountEntity;
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
		if (arg0 != null) {
			accountRepository.delete(arg0);
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

}
