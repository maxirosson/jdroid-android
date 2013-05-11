package com.jdroid.android.facebook;

import org.slf4j.Logger;
import com.facebook.android.FacebookError;
import com.jdroid.java.utils.LoggerUtils;

// TODO FB
public enum FacebookErrorType {
	
	OAUTH_ERROR("OAuthException");
	
	private final static Logger LOGGER = LoggerUtils.getLogger(FacebookErrorType.class);
	private String errorType;
	
	private FacebookErrorType(String errorType) {
		this.errorType = errorType;
	}
	
	public static FacebookErrorType find(FacebookError e) {
		for (FacebookErrorType each : values()) {
			if (each.errorType.equalsIgnoreCase(e.getErrorType())) {
				return each;
			}
		}
		LOGGER.warn("The Facebook error type [" + e.getErrorType() + "] is unknown");
		return null;
	}
	
}
