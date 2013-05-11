package com.jdroid.android.twitter;

import org.slf4j.Logger;
import twitter4j.TwitterException;
import com.jdroid.java.utils.LoggerUtils;

/**
 * 
 * @author Maxi Rosson
 */
public enum TwitterErrorType {
	
	OAUTH_ERROR(401);
	
	private final static Logger LOGGER = LoggerUtils.getLogger(TwitterErrorType.class);
	
	private Integer errorCode;
	
	private TwitterErrorType(Integer errorCode) {
		this.errorCode = errorCode;
	}
	
	public static TwitterErrorType find(TwitterException twitterException) {
		for (TwitterErrorType each : values()) {
			if (each.errorCode.equals(twitterException.getStatusCode())) {
				return each;
			}
		}
		LOGGER.warn("The Twitter status code [" + twitterException.getStatusCode() + "] is unknown");
		return null;
	}
	
}
