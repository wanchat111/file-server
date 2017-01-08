package com.surachit.fileserver.util;

public enum Roles {
	SYS_ACCOUNT_ADMIN(1, "admin"),
	SYS_ACCOUNT_USERADMIN(2, "useradmin"),
	USER(3, "user");
	private final int roleId;
	private final String roleName;
	
	Roles(int roleId, String roleName) {
		this.roleId = roleId;
		this.roleName = roleName;
	}

	public int getRoleId() {
		return roleId;
	}

	public String getRoleName() {
		return roleName;
	}

}