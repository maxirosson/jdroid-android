package com.jdroid.android.exception;

import com.jdroid.java.utils.LoggerUtils.ExceptionLogger;

import java.lang.Thread.UncaughtExceptionHandler;

public interface ExceptionHandler extends UncaughtExceptionHandler, ExceptionLogger {
	
	public void setDefaultExceptionHandler(UncaughtExceptionHandler uncaughtExceptionHandler);
	
	public void logHandledException(String errorMessage, Throwable throwable);

	public void logHandledException(String errorMessage);

	public void logWarningException(String errorMessage, Throwable throwable);
	
	public void logWarningException(String errorMessage);

	public void logIgnoreStackTraceWarningException(String errorMessage);
}
