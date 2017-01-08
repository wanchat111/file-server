package com.surachit.fileserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.surachit.fileserver.entity.UploadEntity;

public interface UploadRepository extends JpaRepository<UploadEntity, Integer> {

}