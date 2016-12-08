package com.surachit.fileserver;

import java.util.UUID;

import javax.servlet.http.Cookie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.surachit.fileserver.entity.AccountEntity;
import com.surachit.fileserver.entity.BranchEntity;
import com.surachit.fileserver.entity.RoleEntity;
import com.surachit.fileserver.repository.AccountRepository;

@Component
public class IntegrationTestHelper {
	@Autowired
	private AccountRepository accountRepository;

	public AccountEntity i_have_active_account(String username, RoleEntity role, BranchEntity branch) {
		AccountEntity accountEntity = new AccountEntity(username, username , role , branch);
		accountEntity.setName("TestUser1");
		accountEntity.setSurname("LastnameTest");
		accountEntity.setEmail("test@testest.com");
		accountEntity.setSessionId(UUID.randomUUID().toString());
		
		accountRepository.save(accountEntity);

		return accountEntity;
	}

	public AccountEntity i_have_active_null_account(String username, RoleEntity role , BranchEntity branch) {
		AccountEntity accountEntity = new AccountEntity("", "", role, branch);
		accountEntity.setName(null);
		accountEntity.setSurname(null);
		accountEntity.setSurname(null);
		accountEntity.setEmail(null);
		accountEntity.setSessionId(UUID.randomUUID().toString());
		
		accountRepository.save(accountEntity);

		return accountEntity;
	}

	public String i_have_active_account(String username) {
		RoleEntity role = new RoleEntity(3, "USER");
		BranchEntity branch = new BranchEntity(1);
		AccountEntity activeAccount = i_have_active_account(username, role, branch);
		String auth_token = activeAccount.getUsername() + ";" + activeAccount.getSessionId();
		return auth_token;
	}

	// Especially for SecuredInterceptor and SessionService test
	public AccountEntity i_have_active_account_intercepter(String username) {
		RoleEntity role = new RoleEntity(3, "USER");
		BranchEntity branch = new BranchEntity(1);
		return i_have_active_account(username, role, branch);
	}

	public void i_have_inactive_account(String username) {
		AccountEntity accountEntity = accountRepository.findOne(username);
		if (accountEntity != null && accountEntity.getSessionId() != null) {
			accountEntity.setSessionId(null);
			accountRepository.save(accountEntity);
		}
	}

	public Cookie[] i_hava_active_session_cookie(String username, RoleEntity role, BranchEntity branch) {
		AccountEntity accountEntity = i_have_active_account(username, role, branch);

		Cookie user = new Cookie("username", username);
		Cookie token = new Cookie("token", accountEntity.getSessionId());

		return new Cookie[] { user, token };
	}

	public Cookie[] i_hava_active_session_cookie(String username) {
		RoleEntity role = new RoleEntity(3, "USER");
		BranchEntity branch = new BranchEntity(1);
		return i_hava_active_session_cookie(username, role, branch);
	}
}
