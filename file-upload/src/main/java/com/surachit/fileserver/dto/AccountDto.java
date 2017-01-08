package com.surachit.fileserver.dto;

import com.surachit.fileserver.entity.AccountEntity;

public class AccountDto {
	private String userName;
	private String roleName;
	private int branchId;
	private String sessionId;
	private String password;
	private String name;
	private String surname;
	private String email;
	
	public AccountDto() {
		
	}
	
	public AccountDto(AccountEntity accountEntity) {
		userName = accountEntity.getUsername();
		roleName = accountEntity.getRole().getRoleName();
		branchId = accountEntity.getBranch().getBranchId();
		sessionId = accountEntity.getSessionId();
		password = accountEntity.getPassword();
		name = accountEntity.getName();
		surname = accountEntity.getSurname();
		email = accountEntity.getEmail();
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public int getBranchId() {
		return branchId;
	}

	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}
	
}