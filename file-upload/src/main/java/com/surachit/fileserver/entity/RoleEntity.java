package com.surachit.fileserver.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.surachit.fileserver.util.Roles;

@Entity
@Table(name = "role")
public class RoleEntity {
	@Id
	@GeneratedValue
	@Column(name = "role_id", nullable = false)
	private int roleId;
	
	@Column(name = "role_name")
	private String roleName;
	
	protected RoleEntity() {
		
	}

	public RoleEntity(int roleId, String roleName) {
		super();
		this.roleId = roleId;
		this.roleName = roleName;
	}
	
	public RoleEntity(Roles roleEnum) {
		this.roleId = roleEnum.getRoleId();
		this.roleName = roleEnum.getRoleName();
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

}