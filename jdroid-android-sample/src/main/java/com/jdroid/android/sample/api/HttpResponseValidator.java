package com.jdroid.android.sample.api;

import com.jdroid.android.http.AbstractAndroidHttpResponseValidator;
import com.jdroid.android.sample.exception.AndroidErrorCode;
import com.jdroid.java.exception.ErrorCode;

public class HttpResponseValidator extends AbstractAndroidHttpResponseValidator {

	private static final HttpResponseValidator INSTANCE = new HttpResponseValidator();

	private HttpResponseValidator() {
		// nothing...
	}

	public static HttpResponseValidator get() {
		return INSTANCE;
	}

	@Override
	protected ErrorCode findByStatusCode(String statusCode) {
		return AndroidErrorCode.findByStatusCode(statusCode);
	}
}
