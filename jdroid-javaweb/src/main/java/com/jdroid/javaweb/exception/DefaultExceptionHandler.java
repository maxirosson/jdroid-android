package com.jdroid.javaweb.exception;

import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

import java.lang.Thread.UncaughtExceptionHandler;

public class DefaultExceptionHandler implements UncaughtExceptionHandler {
	
	private static final Logger LOGGER = LoggerUtils.getLogger(DefaultExceptionHandler.class);
	
	private UncaughtExceptionHandler wrappedExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();

	@Override
	public void uncaughtException(Thread thread, Throwable throwable) {
		LOGGER.error("Uncaught Exception", throwable);
		wrappedExceptionHandler.uncaughtException(thread, throwable);
	}
}
