package com.surachit.fileserver.repository;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.surachit.fileserver.AbstractTest;
import com.surachit.fileserver.entity.AccountEntity;
import com.surachit.fileserver.entity.RoleEntity;

public class AccountRepositoryTest extends AbstractTest {
	
	@Autowired
	private AccountRepository accountRepo;
	
	@Test
	public void can_crud_account() {
		String username = "test";
		String password = "test";
		RoleEntity role = new RoleEntity(3, "user");
		int branchId = 1;
		AccountEntity account = new AccountEntity(username, password, role);
		account.setBranchId(branchId);
		account.setEmail("test@test.com");
		account.setRole(role);
		account.setName(username);
		account.setSurname(username);
		accountRepo.save(account);
	}
	
}
