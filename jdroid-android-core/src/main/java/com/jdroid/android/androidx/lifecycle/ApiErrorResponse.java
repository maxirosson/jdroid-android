package com.jdroid.android.androidx.lifecycle;

import com.jdroid.java.exception.AbstractException;

public class ApiErrorResponse<T> extends ApiResponse<T> {

	private AbstractException exception;

	public ApiErrorResponse(AbstractException exception) {
		this.exception = exception;
	}

	public AbstractException getException() {
		return exception;
	}
}