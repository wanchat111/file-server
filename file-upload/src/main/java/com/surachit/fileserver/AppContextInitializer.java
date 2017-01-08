package com.surachit.fileserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

public class AppContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

	private static final Logger logger = LoggerFactory.getLogger(ApplicationContextInitializer.class);

	public void initialize(ConfigurableApplicationContext arg0) {
		logger.debug("Initializing application context");
	}
}