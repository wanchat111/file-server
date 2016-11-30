package com.surachit.fileserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.surachit.fileserver.entity.AccountEntity;

public interface AccountRepository extends JpaRepository<AccountEntity, String>{

}
