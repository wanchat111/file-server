package com.surachit.fileserver.service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.UUID;

import javax.transaction.Transactional;

import com.surachit.fileserver.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.surachit.fileserver.dto.AccountDto;
import com.surachit.fileserver.entity.AccountEntity;
import com.surachit.fileserver.entity.BranchEntity;
import com.surachit.fileserver.entity.RoleEntity;
import com.surachit.fileserver.repository.AccountRepository;
import com.surachit.fileserver.util.Roles;

@Service
@ConfigurationProperties(prefix = "surachit.account")
public class AccountService {
	private Logger logger = LoggerFactory.getLogger(AccountService.class);

	@Autowired
	private AccountRepository accountRepo;

	private int minUserNameLenth;

	@Transactional
	public void createAccount(AccountDto accountDto) {
		logger.debug("Create User Account {}", accountDto.getUserName());
		RoleEntity role = null;
		BranchEntity branch = new BranchEntity(accountDto.getBranchId());

		if (accountDto.getRoleName().equals(Roles.SYS_ACCOUNT_ADMIN.getRoleName())) {
			role = new RoleEntity(Roles.SYS_ACCOUNT_ADMIN);
		} else if (accountDto.getRoleName().equals(Roles.SYS_ACCOUNT_USERADMIN.getRoleName())) {
			role = new RoleEntity(Roles.SYS_ACCOUNT_USERADMIN);
		} else if (accountDto.getRoleName().equals(Roles.USER.getRoleName())) {
			role = new RoleEntity(Roles.USER);
		}

		AccountEntity accountEntity = accountRepo.findOne(accountDto.getUserName());
		if (accountEntity == null) {
			accountEntity = new AccountEntity(accountDto.getUserName(), hashPassword(accountDto.getPassword()), role,
					branch);
			accountEntity.setEmail(accountDto.getEmail());
			accountEntity.setName(accountDto.getName());
			accountEntity.setSurname(accountDto.getSurname());
			accountRepo.save(accountEntity);

		} else {
			throw new ConflictingData("User is already taken");
		}
	}

	public boolean verifyProposedUsername(String userName) {
		String namingRules = "^\\w{" + minUserNameLenth + ",}$";
		return userName.matches(namingRules);
	}

	String hashPassword(String password) {
		try {
			MessageDigest message = MessageDigest.getInstance("MD5");
			message.reset();
			message.update(password.getBytes());
			byte[] digest = message.digest();
			BigInteger bigInt = new BigInteger(1, digest);
			String hashText = bigInt.toString();
			while (hashText.length() < 32) {
				hashText = "0" + hashText;
			}
			return hashText;
		} catch (Exception e) {
			logger.warn("Failed to hash password: {}", e.toString());
			throw new InternalError(e.toString());

		}
	}

	Roles determineRole() {
		return Roles.USER;
	}

	@Transactional
	public void deleteAccount(String userNameSelect) {
		logger.debug("Got account for delete userName: {}", userNameSelect);
		AccountEntity accountEntity = accountRepo.findOne(userNameSelect);
		if (accountEntity == null) {
			throw new NotFound("The username account: '" + userNameSelect + "' does not exist.");
		}
		try {
			accountRepo.delete(userNameSelect);
		} catch (Exception e) {
			throw new InternalError("Can't delete account.");
		}
	}

	@Transactional
	public void updateAccount(String userNameSelect, AccountDto accountDto) {
		logger.debug("Update role of Account {}", userNameSelect);
		AccountEntity accountEntity = accountRepo.findOne(userNameSelect);
		if (accountEntity == null) {
			throw new NotFound("No account found");
		}

		accountEntity.setName(accountDto.getName());
		accountEntity.setSurname(accountDto.getSurname());
		accountEntity.setPassword(hashPassword(accountDto.getPassword()));
		accountEntity.setEmail(accountDto.getEmail());
		accountRepo.save(accountEntity);
	}

	@Transactional
	public String signIn(String userName, String password) {
		logger.debug("signing in account {}", userName);
		AccountEntity accountEntity = accountRepo.findOne(userName);
		if (accountEntity == null) {
			logger.debug("No account {} found", userName);
			throw new NotFound("Not found account");
		} else {
			logger.debug("Account {} found", userName);
			if (hashPassword(password).equals(accountEntity.getPassword())) {
				String currentSession = accountEntity.getSessionId();
				boolean sessionActive = false;
				if (!StringUtils.isEmpty(currentSession)) {
					sessionActive = true;
				}
				if (sessionActive) {
					throw new Unauthorized("An active session for this user already exists");
				}
			} else {
				throw new Unauthorized("The password is wrong");
			}

		}
		return saveSession(accountEntity);
	}

	private String saveSession(AccountEntity accountEntity) {
		String sessionId = UUID.randomUUID().toString();
		accountEntity.setSessionId(sessionId);
		accountRepo.save(accountEntity);
		return sessionId;
	}

	@Transactional
	public void signOut(String userNameSelect) {
		AccountEntity accountEntity = accountRepo.findOne(userNameSelect);
		if (accountEntity == null) {
			throw new NotFound("No account " + userNameSelect + " found");
		} else {
			accountEntity.setSessionId(null);
			accountRepo.save(accountEntity);
		}
	}
}
