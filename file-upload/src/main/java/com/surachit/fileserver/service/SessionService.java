package com.surachit.fileserver.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.surachit.fileserver.entity.AccountEntity;
import com.surachit.fileserver.exception.BadRequest;
import com.surachit.fileserver.exception.Forbidden;
import com.surachit.fileserver.exception.Unauthorized;
import com.surachit.fileserver.repository.AccountRepository;

@Service
@ConfigurationProperties(prefix = "surachit.security")
public class SessionService {

    private Logger logger = LoggerFactory.getLogger(SessionService.class);

    @Autowired
    private AccountRepository acr;

    private int tokenValiditySeconds;

    /**
     * Validate the given <code>userName</code> and <code>sessionId</code>. The validation will
     * succeed if and only if they are valid and still has not timed out, in which case the last
     * activity time is renewed. Otherwise proper exception is thrown.
     * 
     * @param userName
     * @param sessionId
     */
    public void validateSession(String userName, String sessionId) {
        validateSession(userName, sessionId, null); // validate for any role
    }

    /**
     * Validate the given <code>userName</code> and <code>sessionId</code>. The validation will
     * succeed if and only if they are valid, still has not timed out and has permission for the
     * given <code>action</code>, in which case the last activity time is renewed. Otherwise proper
     * exception is thrown.
     * 
     * @param userName
     * @param sessionId
     * @param action null to validate for any action
     */
    @Transactional
    public void validateSession(String userName, String sessionId, String action) {
        logger.debug("Validating session for account {}", userName);
        if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(sessionId)) {
            throw new BadRequest("Invalid userName / sessionId");
        }

        AccountEntity ace = acr.findOne(userName);
        if ((ace == null) || // no such account
                StringUtils.isEmpty(ace.getSessionId()) || // ace is not signed in
                !ace.getSessionId().equals(sessionId)) { // sessionId is not the same
            throw new Unauthorized("Invalid userName / sessionId");
        }

                acr.save(ace);

        if (action != null) {
            boolean isAuthorized = false;
            String permissions = ace.getRole().getRoleName();
            
                if (permissions == action) {
                    isAuthorized = true;
                }
            

            if (!isAuthorized) {
                throw new Forbidden("Insufficient authorization for the requested action");
            }
        }

        logger.debug("Session for account {} validated and renewed", userName);
    }

    /**
     * @return the tokenValiditySeconds
     */
    public int getTokenValiditySeconds() {
        return tokenValiditySeconds;
    }

    /**
     * @param tokenValiditySeconds the tokenValiditySeconds to set
     */
    public void setTokenValiditySeconds(int tokenValiditySeconds) {
        this.tokenValiditySeconds = tokenValiditySeconds;
    }	
}