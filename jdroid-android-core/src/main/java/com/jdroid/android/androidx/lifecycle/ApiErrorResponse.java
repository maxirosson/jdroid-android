package com.jdroid.android.androidx.lifecycle;

public class ApiErrorResponse<T> extends ApiResponse<T> {

	private Exception exception;

	public ApiErrorResponse(Exception exception) {
		this.exception = exception;
	}

	public Exception getException() {
		return exception;
	}
}