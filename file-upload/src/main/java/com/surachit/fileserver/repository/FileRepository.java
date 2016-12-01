package com.surachit.fileserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.surachit.fileserver.entity.FileEntity;

public interface FileRepository extends JpaRepository<FileEntity, Integer> {

}
