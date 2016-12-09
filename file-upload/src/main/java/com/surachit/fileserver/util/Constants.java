package com.surachit.fileserver.util;

public class Constants {
	// DB constants
		public static final String TABLE_ACCOUNTS = "user";
		public static final String TABLE_ROLES = "role";
		public static final String TABLE_FILE = "file";
		public static final String TABLE_FOLDER = "folder";
		public static final String TABLE_BRANCH = "branch";
		public static final String TABLE_FILE_UPLOAD = "file_upload";
		
		// URL constants
		public static final String BASE_URL = "/surachit/fileserver";
		
		public static final String URL_SIGNUP = BASE_URL + "/signup";
		public static final String URL_SIGNIN = BASE_URL + "/signin";
		public static final String URL_SIGNOUT = BASE_URL + "/signout";
		public static final String URL_ACCOUNT = BASE_URL + "/account";
		public static final String URL_ACCOUNTS = BASE_URL + "/accounts";
		public static final String URL_UPLOAD = BASE_URL + "/upload";
		
		// Cookies
		public static final String COOKIE_USR = "username";
		public static final String COOKIE_TKN = "token";
		
}
