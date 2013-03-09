package com.jdroid.android.facebook;

import android.util.Log;
import com.facebook.android.FacebookError;

// TODO FB
public enum FacebookErrorType {
	
	OAUTH_ERROR("OAuthException");
	
	private static final String TAG = FacebookErrorType.class.getSimpleName();
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
		Log.w(TAG, "The Facebook error type [" + e.getErrorType() + "] is unknown");
		return null;
	}
	
}
