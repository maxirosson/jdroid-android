package com.jdroid.android.exception;

import java.lang.Thread.UncaughtExceptionHandler;
import org.slf4j.Logger;
import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import com.crittercism.app.Crittercism;
import com.jdroid.android.AbstractApplication;
import com.jdroid.android.R;
import com.jdroid.android.analytics.AnalyticsSender;
import com.jdroid.android.context.DefaultApplicationContext;
import com.jdroid.android.utils.AndroidUtils;
import com.jdroid.android.utils.LocalizationUtils;
import com.jdroid.android.utils.ToastUtils;
import com.jdroid.java.exception.AbstractException;
import com.jdroid.java.exception.ApplicationException;
import com.jdroid.java.exception.BusinessException;
import com.jdroid.java.exception.ConnectionException;
import com.jdroid.java.utils.LoggerUtils;

/**
 * 
 * @author Maxi Rosson
 */
public class DefaultExceptionHandler implements ExceptionHandler {
	
	private final static Logger LOGGER = LoggerUtils.getLogger(DefaultExceptionHandler.class);
	
	private static final String MAIN_THREAD_NAME = "main";
	
	private static final String GO_BACK_KEY = "goBack";
	
	private UncaughtExceptionHandler defaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
	
	/**
	 * @see java.lang.Thread.UncaughtExceptionHandler#uncaughtException(java.lang.Thread, java.lang.Throwable)
	 */
	@Override
	public void uncaughtException(Thread thread, Throwable throwable) {
		
		Boolean mainThread = thread.getName().equals(MAIN_THREAD_NAME);
		
		if (mainThread) {
			handleMainThreadException(thread, throwable);
		} else if (throwable instanceof BusinessException) {
			handleException(thread, (BusinessException)throwable);
		} else if (throwable instanceof ConnectionException) {
			handleException(thread, (ConnectionException)throwable);
		} else if (throwable instanceof ApplicationException) {
			handleException(thread, (ApplicationException)throwable);
		} else if (!doUncaughtException(thread, throwable)) {
			handleException(thread, throwable);
		}
	}
	
	protected Boolean doUncaughtException(Thread thread, Throwable throwable) {
		return false;
	}
	
	/**
	 * @see com.jdroid.android.exception.ExceptionHandler#handleMainThreadException(java.lang.Thread,
	 *      java.lang.Throwable)
	 */
	@Override
	public void handleMainThreadException(Thread thread, Throwable throwable) {
		defaultExceptionHandler.uncaughtException(thread, throwable);
	}
	
	/**
	 * @see com.jdroid.android.exception.ExceptionHandler#handleException(com.jdroid.java.exception.BusinessException)
	 */
	@Override
	public void handleException(BusinessException businessException) {
		handleException(Thread.currentThread(), businessException);
	}
	
	/**
	 * @see com.jdroid.android.exception.ExceptionHandler#handleException(java.lang.Thread,
	 *      com.jdroid.java.exception.BusinessException)
	 */
	@Override
	public void handleException(Thread thread, BusinessException businessException) {
		String message = businessException.getMessage();
		if (businessException.getErrorCode() != null) {
			message = LocalizationUtils.getMessageFor(businessException.getErrorCode(),
				businessException.getErrorCodeParameters());
		}
		ToastUtils.showToastOnUIThread(message);
	}
	
	/**
	 * @see com.jdroid.android.exception.ExceptionHandler#handleException(java.lang.Thread,
	 *      com.jdroid.java.exception.ConnectionException)
	 */
	@Override
	public void handleException(Thread thread, ConnectionException connectionException) {
		logHandledException("Connection error", connectionException);
		displayError(LocalizationUtils.getString(R.string.connectionErrorTitle),
			LocalizationUtils.getString(R.string.connectionError), connectionException);
	}
	
	/**
	 * @see com.jdroid.android.exception.ExceptionHandler#handleException(java.lang.Thread,
	 *      com.jdroid.java.exception.ApplicationException)
	 */
	@Override
	public void handleException(Thread thread, ApplicationException applicationException) {
		String message = LocalizationUtils.getMessageFor(applicationException.getErrorCode());
		logHandledException(message, applicationException);
		displayError(
			LocalizationUtils.getString(R.string.exceptionReportDialogTitle, AndroidUtils.getApplicationName()),
			message, applicationException);
	}
	
	/**
	 * @see com.jdroid.android.exception.ExceptionHandler#handleException(java.lang.Thread, java.lang.Throwable)
	 */
	@Override
	public void handleException(Thread thread, Throwable throwable) {
		logHandledException("Unexepected error", throwable);
		displayError(
			LocalizationUtils.getString(R.string.exceptionReportDialogTitle, AndroidUtils.getApplicationName()),
			LocalizationUtils.getString(R.string.serverError), throwable);
	}
	
	/**
	 * @see com.jdroid.android.exception.ExceptionHandler#logHandledException(java.lang.Throwable)
	 */
	@Override
	public void logHandledException(Throwable throwable) {
		logHandledException("Handled Exception", throwable);
	}
	
	/**
	 * @see com.jdroid.android.exception.ExceptionHandler#logHandledException(java.lang.String, java.lang.Throwable)
	 */
	@Override
	public void logHandledException(String message, Throwable throwable) {
		if (throwable instanceof ConnectionException) {
			LOGGER.warn(message, throwable);
			AnalyticsSender.get().trackConnectionException((ConnectionException)throwable);
		} else {
			LOGGER.error(message, throwable);
			DefaultApplicationContext appContext = AbstractApplication.get().getAndroidApplicationContext();
			if (appContext.isCrittercismEnabled()) {
				if (appContext.isCrittercismPremium()) {
					Crittercism.logHandledException(throwable);
				} else if (!appContext.isProductionEnvironment()) {
					ExceptionReportActivity.reportException(throwable);
				}
			}
		}
	}
	
	protected void displayError(String title, String message, Throwable throwable) {
		Activity activity = AbstractApplication.get().getCurrentActivity();
		if (activity != null) {
			ErrorDialogFragment.show((FragmentActivity)activity, title, message, goBackOnError(throwable));
		}
	}
	
	private Boolean goBackOnError(Throwable throwable) {
		if (throwable instanceof AbstractException) {
			AbstractException abstractException = (AbstractException)throwable;
			return abstractException.hasParameter(GO_BACK_KEY) ? abstractException.<Boolean>getParameter(GO_BACK_KEY)
					: goBackOnErrorByDefault(abstractException);
		} else {
			return goBackOnErrorByDefault(throwable);
		}
	}
	
	protected Boolean goBackOnErrorByDefault(Throwable throwable) {
		return true;
	}
	
	public static void markAsGoBackOnError(RuntimeException runtimeException) {
		// FIXME The RuntimeException is not flagged. Possible fixes: wrap the RuntimeException or move the goBack logic
		// to the use case listener
		if (runtimeException instanceof AbstractException) {
			((AbstractException)runtimeException).addParameter(GO_BACK_KEY, true);
		}
	}
	
	public static void markAsNotGoBackOnError(RuntimeException runtimeException) {
		// FIXME The RuntimeException is not flagged. Possible fixes: wrap the RuntimeException or move the goBack logic
		// to the use case listener
		if (runtimeException instanceof AbstractException) {
			((AbstractException)runtimeException).addParameter(GO_BACK_KEY, false);
		}
	}
}
