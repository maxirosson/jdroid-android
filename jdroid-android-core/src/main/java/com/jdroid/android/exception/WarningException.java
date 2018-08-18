package com.jdroid.android.exception;

import com.jdroid.java.exception.AbstractException;

public class WarningException extends AbstractException {

	private static final long serialVersionUID = -3119943371589459664L;

	public WarningException(String message, Throwable cause) {
		super(message, cause);
		setPriorityLevel(AbstractException.LOW_PRIORITY);
	}

	public WarningException(String message) {
		super(message);
		setPriorityLevel(AbstractException.LOW_PRIORITY);
	}

	public WarningException(String message, boolean ignoreStackTrace) {
		super(message);
		setIgnoreStackTrace(ignoreStackTrace);
		setPriorityLevel(AbstractException.LOW_PRIORITY);
	}
}
