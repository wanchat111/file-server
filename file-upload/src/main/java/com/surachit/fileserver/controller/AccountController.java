package com.surachit.fileserver.controller;

import java.nio.charset.Charset;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.surachit.fileserver.dto.AccountDto;
import com.surachit.fileserver.entity.AccountEntity;
import com.surachit.fileserver.exception.BadRequest;
import com.surachit.fileserver.exception.NotFound;
import com.surachit.fileserver.exception.Unauthorized;
import com.surachit.fileserver.response.Response;
import com.surachit.fileserver.service.AccountService;
import com.surachit.fileserver.service.SessionService;
import com.surachit.fileserver.util.Constants;
import com.surachit.fileserver.util.Secured;
import com.surachit.fileserver.util.ValidatorUtils;

@RestController
@ConfigurationProperties(prefix = "surachit.account")
public class AccountController extends AbstractController{
	
	private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private AccountService accountService;

    private int minUserNameLenth;
    private Integer paginationStartPage;
    private Integer paginationLimitPerPage;
    
    @Autowired
	private SessionService sessionService;
    
    @RequestMapping(value = Constants.URL_SIGNIN, method = RequestMethod.GET)
	public Map<String, String> signIn(@RequestHeader(value = "Authorization") String authenStr,
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
		Map<String, String> token = new HashMap<String, String>();
		token.put(Constants.COOKIE_TKN, sessionId);
		return token;
    }
    
    @RequestMapping(value = Constants.URL_SIGNOUT + "/{username}/{sessionId}", method = RequestMethod.GET)
	public void signOut(@PathVariable(value="username") String userName,
			@PathVariable(value="sessionId") String sessionId) {

		logger.info("Got sign out request for user {}", userName);
		sessionService.validateSession(userName, sessionId);
		accountService.signOut(userName);
	}
    
    @Secured(permission = "admin")
    @RequestMapping(value = Constants.URL_ACCOUNT, method = RequestMethod.POST)
    public HttpEntity<Response<String>> createAccount(
            @RequestBody(required = true) AccountDto accountDto)
            throws NotFound {
        logger.debug("Create User Account {}", accountDto.getUserName());

        if (StringUtils.isEmpty(accountDto.getUserName())) {
            throw new BadRequest("userName cannot be null or empty");
        }
        
        if (!accountService.verifyProposedUsername(accountDto.getUserName())) {
            throw new BadRequest("userName must contain no white space, only "
                    + "alpha numeric characters allowed and must have at least "
                    + minUserNameLenth + " characters");
        }

        if (StringUtils.isEmpty(accountDto.getEmail())) {
            throw new BadRequest("Account Email cannot be null or empty");
        }

        if (StringUtils.isEmpty(accountDto.getRoleName())) {
            throw new BadRequest("Role Name cannot be null or empty");
        }

        if (StringUtils.isEmpty(accountDto.getBranchId())) {
            throw new BadRequest("Branch ID cannot be null or empty");
        }

        if (StringUtils.isEmpty(accountDto.getName())) {
            throw new BadRequest("Name cannot be null or empty");
        }

        if (StringUtils.isEmpty(accountDto.getSurname())) {
            throw new BadRequest("Surname cannot be null or empty");
        }

        accountService.createAccount(accountDto);
        return new ResponseEntity<>(returnWithSuccess("Create Success"), HttpStatus.CREATED);
    }
    
    @Secured(permission = "admin")
    @RequestMapping(value = Constants.URL_ACCOUNT + "/{userName}", method = RequestMethod.DELETE)
    public Response<Void> deleteAccount( @PathVariable(value = "userName") String userNameSelect) {
    	if(StringUtils.isEmpty(userNameSelect)) {
    		throw new BadRequest("Can not found userName");
    	}
    	accountService.deleteAccount(userNameSelect);
    	return returnWithSuccess();
    }
    
    @Secured(permission = "admin")
    @RequestMapping(value = Constants.URL_ACCOUNT + "/{userName}", method = RequestMethod.PUT)
    public Response<Void> updateAccount(
            @PathVariable(value = "userName") String userAccountName,
            @RequestBody(required = true) AccountDto accountDto)
            throws NotFound, BadRequest {

        if (StringUtils.isEmpty(userAccountName)) {
            throw new BadRequest("User Account is empty");
        }

        logger.debug("Update Account {}", userAccountName);
        accountService.updateAccount(userAccountName, accountDto);

        return returnWithSuccess();
    }
    
    @Secured
    @RequestMapping(value = Constants.URL_ACCOUNT + "/{userName}" , method = RequestMethod.GET)
    public Response<AccountDto> getAccount(
            @PathVariable("userName") String userAccountName)
            throws NotFound, BadRequest {

        if (StringUtils.isEmpty(userAccountName)) {
            throw new BadRequest("User Account is empty");
        }

        logger.debug("Got request account {}'s", userAccountName);
  

        return returnWithSuccess(accountService.getAccount(userAccountName));
    }
    
    @Secured(permission = "admin")
    @RequestMapping(value = Constants.URL_ACCOUNTS, method = RequestMethod.GET)
    public Response<List<AccountDto>> getListAccount(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "limit", required = false) Integer limit)
            throws NotFound {
        logger.debug("getAccount page {} , limit {} ", page, limit);
        
        if (page == null) {
            page = paginationStartPage;
        }

        if (limit == null) {
            limit = paginationLimitPerPage;
        }
        logger.debug("getAccount page {} , limit {} ", page, limit);
        
        Page<AccountEntity> accountDtoPage = accountService.getListAccount(page, limit);

        return returnSuccessWithPagination(accountDtoPage, source -> {
            AccountDto account = new AccountDto();
            account.setUserName(source.getUsername());
            account.setEmail(source.getEmail());
            account.setRoleName(source.getRole().getRoleName());
            account.setName(source.getName());
            account.setSurname(source.getSurname());
            account.setPassword(source.getPassword());
            account.setBranchId(source.getBranch().getBranchId());
            return account;
        }, Constants.URL_ACCOUNTS);
    }

	public int getMinUserNameLenth() {
		return minUserNameLenth;
	}

	public void setMinUserNameLenth(int minUserNameLenth) {
		this.minUserNameLenth = minUserNameLenth;
	}

	public Integer getPaginationStartPage() {
		return paginationStartPage;
	}

	public void setPaginationStartPage(Integer paginationStartPage) {
		this.paginationStartPage = paginationStartPage;
	}

	public Integer getPaginationLimitPerPage() {
		return paginationLimitPerPage;
	}

	public void setPaginationLimitPerPage(Integer paginationLimitPerPage) {
		this.paginationLimitPerPage = paginationLimitPerPage;
	}

}
