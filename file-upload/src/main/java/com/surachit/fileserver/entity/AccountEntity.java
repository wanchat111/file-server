package com.surachit.fileserver.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class AccountEntity {

	@Id
	@Column(name = "username", nullable = false)
	private String username;

	@ManyToOne (fetch = FetchType.EAGER)
	@JoinColumn(name = "role_id", nullable = false)
	private RoleEntity role;

	@ManyToOne (fetch = FetchType.EAGER)
	@JoinColumn(name = "branch_id", nullable = false)
	private BranchEntity branch;

	@Column(name = "session_id")
	private String sessionId;

	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "surname", nullable = false)
	private String surname;

	@Column(name = "email")
	private String email;

	protected AccountEntity() {

	}

	public AccountEntity(String username, String password, RoleEntity role, BranchEntity branch) {
		this.username = username;
		this.password = password;
		this.role = role;
		this.branch = branch;
	}
	
	public AccountEntity(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setRole(RoleEntity role) {
		this.role = role;
	}

	public BranchEntity getBranch() {
		return branch;
	}

	public void setBranch(BranchEntity branch) {
		this.branch = branch;
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

	public void setUsername(String username) {
		this.username = username;
	}

	public RoleEntity getRole() {
		return role;
	}

}
