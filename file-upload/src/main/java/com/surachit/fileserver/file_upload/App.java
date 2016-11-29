package com.surachit.fileserver.file_upload;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@ConfigurationProperties
@EnableJpaRepositories
public class App 
{
    public static void main( String[] args )
    {
    	new SpringApplicationBuilder(App.class).initializers(new AppContextInitializer()).run(args);
    }
}
