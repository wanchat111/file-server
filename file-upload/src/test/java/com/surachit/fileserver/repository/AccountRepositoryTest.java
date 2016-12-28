package com.surachit.fileserver.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.surachit.fileserver.AbstractTest;
import com.surachit.fileserver.entity.AccountEntity;
import com.surachit.fileserver.entity.BranchEntity;
import com.surachit.fileserver.entity.RoleEntity;


public class AccountRepositoryTest extends AbstractTest {
	
	@Autowired
	private AccountRepository accountRepo;
	
	@Test
	public void can_crud_account() {
		//create user
		String username = "test";
		String password = "test";
		RoleEntity role = new RoleEntity(3, "user");
		BranchEntity branch = new BranchEntity(1, "ส่วนกลาง");
		
		AccountEntity account = new AccountEntity(username, password, role, branch);
		account.setEmail("test@test.com");
		account.setName(username);
		account.setSurname(username);
		accountRepo.save(account);
		
		AccountEntity testCreate = accountRepo.findOne("test");
		assertEquals("test", testCreate.getName());
		assertEquals("test@test.com", testCreate.getEmail());
		
		//edit user
		AccountEntity editAccount = accountRepo.findOne("test");
		editAccount.setName("new");
		accountRepo.save(editAccount);
		AccountEntity testEdit = accountRepo.findOne("test");
		assertEquals("new", testEdit.getName());
		
		//delete user
		AccountEntity deleteAccount = accountRepo.findOne("test");
		accountRepo.delete(deleteAccount);
		AccountEntity testDelete = accountRepo.findOne("test");
		assertNull(testDelete);
		
		
	}
	
}
