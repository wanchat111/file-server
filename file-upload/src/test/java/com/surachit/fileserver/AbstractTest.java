package com.surachit.fileserver;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TestApplication.class)
@ActiveProfiles("development, local")
public abstract class AbstractTest {
	@Autowired
	protected IntegrationTestHelper testHelper;
	
	public byte[] printJson(Object obj) throws JsonProcessingException {
		ObjectMapper om = new ObjectMapper();
		byte[] JsonString = om.writeValueAsBytes(obj);
		return JsonString;
	}

}
