package com.surachit.fileserver.controller;

import java.nio.charset.Charset;
import java.util.Base64;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.surachit.fileserver.exception.Unauthorized;
import com.surachit.fileserver.service.AccountService;
import com.surachit.fileserver.service.SessionService;
import com.surachit.fileserver.util.Constants;
import com.surachit.fileserver.util.ValidatorUtils;

@RestController
@ConfigurationProperties(prefix = "pushad.account")
public class AccountController {
	
	private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private AccountService accountService;

    private int minUserNameLenth;
    
    @Autowired
	private SessionService sessionService;
    
    @RequestMapping(value = Constants.URL_SIGNIN, method = RequestMethod.GET)
	public void signIn(@RequestHeader(value = "Authorization") String authenStr,
			HttpServletResponse response) {

		if (ValidatorUtils.isNullOrEmpty(authenStr)
				|| !authenStr.startsWith("Basic")) {
			throw new Unauthorized("Basic HTTP Authentication required");
		}

		String base64Credentials = authenStr.substring("Basic".length()).trim();
		String credentials = new String(
				Base64.getDecoder().decode(base64Credentials),
				Charset.forName("UTF-8"));
		final String[] values = credentials.split(":", 2);
		String userName = values[0];
		String password = values[1];

		logger.debug("Got sign in request for account {}", userName);
		String sessionId = accountService.signIn(userName, password);
		response.addCookie(new Cookie(Constants.COOKIE_TKN, sessionId));
    }
    
    @RequestMapping(value = Constants.URL_SIGNOUT, method = RequestMethod.POST)
	public void signOut(
			@CookieValue(value = Constants.COOKIE_USR, defaultValue = "") String userName,
			@CookieValue(value = Constants.COOKIE_TKN, defaultValue = "") String sessionId) {

		logger.debug("Got sign out request for user {}", userName);
		sessionService.validateSession(userName, sessionId);
		accountService.signOut(userName);
	}

}
