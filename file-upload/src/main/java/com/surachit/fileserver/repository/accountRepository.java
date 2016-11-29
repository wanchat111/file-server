package com.surachit.fileserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.surachit.fileserver.entity.AccountEntity;

public interface accountRepository extends JpaRepository<AccountEntity, String>{

}
