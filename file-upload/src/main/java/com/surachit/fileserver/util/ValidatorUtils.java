package com.surachit.fileserver.util;

public class ValidatorUtils {
	public static boolean isNullOrEmpty(String str) {
		return (str == null || str.isEmpty());
	}
	
	public static boolean isNullOrEmpty(String str1, String str2) {
		return isNullOrEmpty(str1) || isNullOrEmpty(str2);
	}

}