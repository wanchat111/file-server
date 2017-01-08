package com.surachit.fileserver.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "branch")
public class BranchEntity {

	@Id
	@Column(name = "branch_id", nullable = false)
	private int branchId;

	@Column(name = "branch_name")
	private String branchName;

	protected BranchEntity() {

	}

	public BranchEntity(int branchId, String branchName) {
		this.branchId = branchId;
		this.branchName = branchName;
	}
	
	public BranchEntity(int branchId) {
		this.branchId = branchId;

	}

	public int getBranchId() {
		return branchId;
	}

	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

}