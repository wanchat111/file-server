package com.surachit.fileserver.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.surachit.fileserver.exception.BadRequest;
import com.surachit.fileserver.service.SessionService;
import com.surachit.fileserver.util.Secured;

public class SecuredInterceptor implements HandlerInterceptor {
	private static final Logger logger = LoggerFactory.getLogger(SecuredInterceptor.class);

	@Autowired
	private SessionService sessionService;

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.servlet.HandlerInterceptor#preHandle(javax.servlet.http.
	 *      HttpServletRequest, javax.servlet.http.HttpServletResponse,
	 *      java.lang.Object)
	 * 
	 *      Below snippet code for fix CORS issue but it not quite good solution
	 *      if you have to implement Swagger UI in central architecture you have
	 *      serious consider this or figure out the better solution than it.
	 * 
	 *      How to use the snippet code: Just place the snippet code in the
	 *      first line body of this method.
	 * 
	 *      if("OPTIONS".equalsIgnoreCase(request.getMethod())){
	 *      response.setHeader("Allow", "GET, OPTIONS"); return false; }
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		HandlerMethod handlerMethod = (HandlerMethod) handler;
		Secured authentication = handlerMethod.getMethod().getAnnotation(Secured.class);

		if (authentication == null) {
			return true;
		}

		logger.info("Secured Interceptor.");

		if (request.getHeader("Authentication") == null) {
			logger.info("Request header authentication is null.");
			throw new BadRequest("Invalid userName / sessionId.");
		}

		logger.info("Authentication: {}", request.getHeader("Authentication"));

		String[] headerAuthentication = request.getHeader("Authentication").split(";");

		logger.info("header authentication length: {}", headerAuthentication.length);
		if (headerAuthentication.length < 2) {
			logger.info("Request header username is null or token is null.");
			throw new BadRequest("Invalid userName / sessionId.");
		}

		String username = headerAuthentication[0]; // headerAuthentication[0]:
													// user name
		String token = headerAuthentication[1]; // headerAuthentication[0]:
												// token

		logger.info("username: {}, token: {}", username, token);

		if (username == null || token == null) {
			throw new BadRequest("Invalid userName / sessionId.");
		}

		String permissions = authentication.permission();
		if (permissions.equals("")) {
			logger.info("Authentication Session Service without role permissions userName : {}", username);
			sessionService.validateSession(username, token);
		} else {
			logger.info("Authentication Session Service with role permissions userName : {}, permissions : {}",
					username, permissions);
			sessionService.validateSession(username, token, permissions);
		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.servlet.HandlerInterceptor#postHandle(javax.
	 * servlet.http. HttpServletRequest, javax.servlet.http.HttpServletResponse,
	 * java.lang.Object, org.springframework.web.servlet.ModelAndView)
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.web.servlet.HandlerInterceptor#afterCompletion(javax.
	 * servlet.http. HttpServletRequest, javax.servlet.http.HttpServletResponse,
	 * java.lang.Object, java.lang.Exception)
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}

}
