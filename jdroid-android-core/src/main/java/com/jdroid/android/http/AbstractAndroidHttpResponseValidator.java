package com.jdroid.android.http;

import com.jdroid.android.exception.CommonErrorCode;
import com.jdroid.java.exception.ErrorCode;
import com.jdroid.java.http.AbstractHttpResponseValidator;

public abstract class AbstractAndroidHttpResponseValidator extends AbstractHttpResponseValidator {
	
	@Override
	protected ErrorCode findByCommonStatusCode(String statusCode) {
		return CommonErrorCode.findByStatusCode(statusCode);
	}
}