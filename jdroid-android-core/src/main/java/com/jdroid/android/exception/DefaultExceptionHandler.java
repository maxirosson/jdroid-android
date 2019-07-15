package com.jdroid.android.exception;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.context.UsageStats;
import com.jdroid.android.utils.LocalizationUtils;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.exception.AbstractException;
import com.jdroid.java.exception.ErrorCode;
import com.jdroid.java.exception.ErrorCodeException;
import com.jdroid.java.exception.UnexpectedException;
import com.jdroid.java.http.exception.ConnectionException;
import com.jdroid.java.utils.LoggerUtils;
import com.jdroid.java.utils.ReflectionUtils;
import com.jdroid.java.utils.StringUtils;

import org.slf4j.Logger;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.List;
import java.util.Locale;

public class DefaultExceptionHandler implements ExceptionHandler {

	private final static Logger LOGGER = LoggerUtils.getLogger(DefaultExceptionHandler.class);

	private static final String MAIN_THREAD_NAME = "main";

	private UncaughtExceptionHandler wrappedExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
	private UncaughtExceptionHandler defaultExceptionHandler;

	@Override
	public void uncaughtException(Thread thread, Throwable throwable) {
		Boolean mainThread = thread.getName().equals(MAIN_THREAD_NAME);
		if (mainThread) {
			handleMainThreadException(thread, throwable);
		} else {
			handleWorkerThreadException(throwable);
		}
	}

	protected void handleMainThreadException(Thread thread, Throwable throwable) {
		try {
			try {
				UsageStats.setLastCrashTimestamp();
			} catch (Exception e) {
				wrappedExceptionHandler.uncaughtException(thread, e);
			}
			wrappedExceptionHandler.uncaughtException(thread, throwable);
		} catch (Exception e) {
			LOGGER.error("Error when trying to handle an exception", e);
			defaultExceptionHandler.uncaughtException(thread, throwable);
		}
	}

	protected void handleWorkerThreadException(Throwable throwable) {
		try {
			logHandledException(throwable);
		} catch (Exception e) {
			logHandledExceptionSafe("Error when trying to handle an exception", e);
		}
	}

	public void logHandledException(String errorMessage) {
		logHandledException(new UnexpectedException(errorMessage));
	}

	@Override
	public void logHandledException(Throwable throwable) {
		logHandledException(null, throwable);
	}

	@Override
	public void logHandledException(String errorMessage, Throwable throwable) {

		if (throwable instanceof ConnectionException) {
			if (errorMessage == null) {
				errorMessage = "Connection error";
			}
			LOGGER.warn(errorMessage, throwable);
		} else {
			Boolean trackable = true;
			Throwable throwableToLog = throwable;
			int priorityLevel = AbstractException.NORMAL_PRIORITY;

			if (throwable instanceof AbstractException) {
				AbstractException abstractException = (AbstractException)throwable;
				trackable = abstractException.isTrackable();
				throwableToLog = abstractException.getThrowableToLog();
				priorityLevel = abstractException.getPriorityLevel();
			}

			if (errorMessage == null) {
				errorMessage = throwableToLog.getMessage();
				if (errorMessage == null) {
					errorMessage = "Error";
				}
			}

			if (trackable) {
				LOGGER.error(errorMessage, throwableToLog);
				List<String> tags = getThrowableTags(throwable, priorityLevel);
				AbstractApplication.get().getCoreAnalyticsSender().trackHandledException(throwableToLog, tags);
			} else {
				LOGGER.warn(errorMessage);
			}
		}
	}

	private void logHandledExceptionSafe(String errorMessage, Throwable throwable) {
		LOGGER.error(errorMessage, throwable);
		AbstractApplication.get().getCoreAnalyticsSender().trackHandledException(throwable, getDefaultThrowableTags());
	}

	protected List<String> getDefaultThrowableTags() {
		return Lists.newArrayList();
	}

	public static Boolean matchAnyErrorCode(Throwable throwable, ErrorCode... errorCodes) {
		List<ErrorCode> errorCodesList = Lists.newArrayList(errorCodes);
		if (throwable instanceof ErrorCodeException) {
			ErrorCodeException errorCodeException = (ErrorCodeException)throwable;
			return errorCodesList.contains(errorCodeException.getErrorCode());
		}
		return false;
	}

	protected List<String> getThrowableTags(Throwable throwable, Integer priorityLevel) {
		List<String> tags = Lists.newArrayList();
		if (priorityLevel != null) {
			tags.add("level" + String.format(Locale.getDefault(), "%02d", priorityLevel));
		}
		return tags;
	}

	public static void addTags(Throwable throwable, List<String> tags) {
		if (!Lists.isNullOrEmpty(tags)) {
			StringBuilder builder = new StringBuilder();
			builder.append(StringUtils.join(tags, " "));

			if (throwable.getMessage() != null) {
				builder.append(" ");
				builder.append(throwable.getMessage());
			}

			try {
				ReflectionUtils.set(throwable, "detailMessage", builder.toString());
			} catch (Exception e) {
				// do nothing
			}
		}
	}

	@Override
	public void logWarningException(String errorMessage, Throwable throwable) {
		if (throwable instanceof ConnectionException) {
			logHandledException(throwable);
		} else {
			logHandledException(new WarningException(errorMessage, throwable));
		}
	}

	@Override
	public void logWarningException(String errorMessage) {
		logHandledException(new WarningException(errorMessage));
	}

	@Override
	public void logWarningException(Throwable throwable) {
		logHandledException(throwable.getMessage(), throwable);
	}

	@Override
	public void logIgnoreStackTraceWarningException(String errorMessage) {
		logHandledException(new WarningException(errorMessage, true));
	}

	@Override
	public void setDefaultExceptionHandler(UncaughtExceptionHandler uncaughtExceptionHandler) {
		defaultExceptionHandler = uncaughtExceptionHandler;
	}

	public static void setMessageOnConnectionTimeout(RuntimeException runtimeException, int resId) {
		setMessageOnConnectionTimeout(runtimeException, LocalizationUtils.INSTANCE.getString(resId));
	}

	public static void setMessageOnConnectionTimeout(RuntimeException runtimeException, String text) {
		if (runtimeException instanceof ConnectionException) {
			ConnectionException connectionException = (ConnectionException)runtimeException;
			if (connectionException.isReadTimeout()) {
				connectionException.setDescription(text);
			}
		}
	}
}
