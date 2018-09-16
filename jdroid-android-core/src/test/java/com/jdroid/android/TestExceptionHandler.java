package com.jdroid.android;


import com.jdroid.android.exception.DefaultExceptionHandler;

public class TestExceptionHandler extends DefaultExceptionHandler {

	@Override
	public void uncaughtException(Thread thread, Throwable throwable) {
		throwable.printStackTrace();
	}

	@Override
	public void logHandledException(String errorMessage, Throwable throwable) {
		throwable.printStackTrace();
	}

	@Override
	public void logHandledException(Throwable throwable) {
		throwable.printStackTrace();
	}

	@Override
	public void logWarningException(String errorMessage) {
		System.out.println(errorMessage);
	}

	@Override
	public void logWarningException(String errorMessage, Throwable throwable) {
		throwable.printStackTrace();
	}

	@Override
	public void logWarningException(Throwable throwable) {
		throwable.printStackTrace();
	}
}
